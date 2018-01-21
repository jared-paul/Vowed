package net.vowed.core.storage;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

/**
 * Store meta-data in an ItemStack as attributes.
 *
 * @author Kristian
 */
public class AttributeStorage
{
    public static final UUID storageUUID = UUID.fromString("92bd6fd9-0b5c-4bd1-988c-8afa6a063822");
    private ItemStack target;
    private final UUID uniqueKey;

    private AttributeStorage(ItemStack target, UUID uniqueKey)
    {
        this.target = Preconditions.checkNotNull(target, "target cannot be NULL");
        this.uniqueKey = Preconditions.checkNotNull(uniqueKey, "uniqueKey cannot be NULL");
    }

    /**
     * Construct a new attribute storage system.
     * <p/>
     * The key must be the same in order to retrieve the same data.
     *
     * @param target    - the getItem stack where the data will be stored.
     * @param uniqueKey - the unique key used to retrieve the correct data.
     */
    public static AttributeStorage newTarget(ItemStack target, UUID uniqueKey)
    {
        return new AttributeStorage(target, uniqueKey);
    }

    /**
     * Retrieve the data stored in the getItem's attribute.
     *
     * @param returnValue - the default value to return if no data can be found.
     * @return The stored data, or defaultValue if not found.
     */
    public String getData(String returnValue)
    {
        Attributes.Attribute current = getAttribute(new Attributes(target), uniqueKey);
        if (current != null)
        {
            return current.getName();
        }
        else
        {
            return returnValue;
        }
    }

    /**
     * Determine if we are storing any data.
     *
     * @return TRUE if we are, FALSE otherwise.
     */
    public boolean hasData()
    {
        return getAttribute(new Attributes(target), uniqueKey) != null;
    }

    /**
     * Set the data stored in the attributes.
     *
     * @param data - the data.
     */
    public void setData(String data)
    {
        Attributes attributes = new Attributes(target);
        Attributes.Attribute current = getAttribute(attributes, uniqueKey);

        if (current == null)
        {
            attributes.add(
                    Attributes.Attribute.newBuilder().
                            name(data).
                            amount(getBaseDamage(target)).
                            uuid(uniqueKey).
                            operation(Attributes.Operation.ADD_NUMBER).
                            type(Attributes.AttributeType.GENERIC_ATTACK_DAMAGE).
                            build()
            );
        } else
        {
            current.setName(data);
        }

        ItemStack item = attributes.getStack();
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);

        this.target = item;
    }

    /**
     * Retrieve the base damage of the given getItem.
     *
     * @param stack - the stack.
     * @return The base damage.
     */
    private int getBaseDamage(ItemStack stack)
    {
        // Yes - we have to hard code these values. Cannot use Operation.ADD_PERCENTAGE either.
        switch (stack.getType())
        {
            case WOOD_SWORD:
                return 4;
            case GOLD_SWORD:
                return 4;
            case STONE_SWORD:
                return 5;
            case IRON_SWORD:
                return 6;
            case DIAMOND_SWORD:
                return 7;

            case WOOD_AXE:
                return 3;
            case GOLD_AXE:
                return 3;
            case STONE_AXE:
                return 4;
            case IRON_AXE:
                return 5;
            case DIAMOND_AXE:
                return 6;
            default:
                return 0;
        }
    }

    /**
     * Retrieve the target stack. May have been changed.
     *
     * @return The target stack.
     */
    public ItemStack getTarget()
    {
        return target;
    }

    /**
     * Retrieve an attribute by UUID.
     *
     * @param attributes - the attribute.
     * @param id         - the UUID to search for.
     * @return The first attribute associated with this UUID, or NULL.
     */
    private Attributes.Attribute getAttribute(Attributes attributes, UUID id)
    {
        for (Attributes.Attribute attribute : attributes.values())
        {
            if (Objects.equal(attribute.getUUID(), id))
            {
                return attribute;
            }
        }
        return null;
    }
}

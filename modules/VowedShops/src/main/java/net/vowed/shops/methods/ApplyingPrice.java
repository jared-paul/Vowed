package net.vowed.shops.methods;

import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.shops.IChestShop;
import net.vowed.core.util.strings.Strings;
import net.vowed.shops.ChestShop;
import net.vowed.shops.ShopItem;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

/**
 * Created by JPaul on 6/28/2016.
 */
public class ApplyingPrice extends AbstractMethod
{
    public ApplyingPrice(IVowedPlayer vowedPlayer, IChestShop shop, ShopItem shopItem)
    {
        super(vowedPlayer, shop, shopItem);
    }

    @Override
    public ValidatingPrompt firstPrompt()
    {
        return new PricePrompt();
    }

    private class PricePrompt extends NumericPrompt
    {

        @Override
        protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number)
        {
            Vowed.LOG.debug(shop.getName());
            Vowed.LOG.debug(shopItem.getUUID().toString());
            Vowed.LOG.debug(number.intValue());

            shopItem.addPrice(number.intValue(), shop);
            shop.addItem(shopItem);
            vowedPlayer.getPlayer().getInventory().remove(shopItem.getItem());
            shop.getMenu(vowedPlayer).showToPlayer(vowedPlayer.getPlayer());

            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public String getPromptText(ConversationContext conversationContext)
        {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "Please enter the selling price";
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput)
        {
            return Strings.handleError("Please enter a valid integer for the selling price", "valid integer");
        }
    }
}

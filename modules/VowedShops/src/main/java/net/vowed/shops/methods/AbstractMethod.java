package net.vowed.shops.methods;

import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.shops.IChestShop;
import net.vowed.shops.ChestShop;
import net.vowed.shops.ShopItem;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ValidatingPrompt;

/**
 * Created by JPaul on 6/29/2016.
 */
public abstract class AbstractMethod implements ConversationAbandonedListener
{
    IVowedPlayer vowedPlayer;
    IChestShop shop;
    ShopItem shopItem;

    public AbstractMethod(IVowedPlayer vowedPlayer, IChestShop shop, ShopItem shopItem)
    {
        this.vowedPlayer = vowedPlayer;
        this.shop = shop;
        this.shopItem = shopItem;


        ConversationFactory conversationFactory = new ConversationFactory(Vowed.getPlugin())
                .withModality(true)
                .withFirstPrompt(firstPrompt())
                .withEscapeSequence("cancel")
                .withTimeout(10)
                .withLocalEcho(false)
                .addConversationAbandonedListener(this);

        conversationFactory.buildConversation(vowedPlayer.getPlayer()).begin();

    }

    public abstract ValidatingPrompt firstPrompt();

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent conversationAbandonedEvent)
    {

    }
}

package net.vowed.shops.methods;

import net.vowed.api.plugin.Vowed;
import net.vowed.api.shops.IChestShop;
import net.vowed.core.util.strings.Strings;
import net.vowed.shops.ChestShop;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 6/29/2016.
 */
public class NamingShopConversation implements ConversationAbandonedListener
{
    private IChestShop shop;

    public NamingShopConversation(Player player, IChestShop shop)
    {
        this.shop = shop;

        ConversationFactory conversationFactory = new ConversationFactory(Vowed.getPlugin())
                .withModality(true)
                .withFirstPrompt(new NamePrompt())
                .withEscapeSequence("cancel")
                .withTimeout(10)
                .withLocalEcho(false)
                .addConversationAbandonedListener(this);

        conversationFactory.buildConversation(player.getPlayer()).begin();
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent conversationAbandonedEvent)
    {

    }

    private class NamePrompt extends ValidatingPrompt
    {
        @Override
        protected boolean isInputValid(ConversationContext conversationContext, String input)
        {
            return input.length() <= 16;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext conversationContext, String input)
        {
            shop.changeName(input);

            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput)
        {
            return Strings.handleError("Please type in a valid 16 character name, or enter ' cancel '", "16 character");
        }

        @Override
        public String getPromptText(ConversationContext conversationContext)
        {
            return ChatColor.YELLOW + ChatColor.BOLD.toString() + "Please enter your shop's name";
        }
    }
}

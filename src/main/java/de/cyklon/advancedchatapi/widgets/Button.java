package de.cyklon.advancedchatapi.widgets;


import de.cyklon.advancedchatapi.AdvancedChatAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Button extends FunctionalWidget {

    String getText();

    public static Button createButton(final TextComponent text, final Executor executor) {
        final UUID uuid = UUID.randomUUID();
        final ComponentBuilder builder = new ComponentBuilder(text);
        AdvancedChatAPI.BaseCommand command = AdvancedChatAPI.getInstance().getBaseCommand();
        builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s %s %s", command.getCommand().getName(), command.getSubcommand(), uuid)));
        Button button =  new Button() {
            @Override
            public void trigger(Player player) {
                executor.onExecute(player, this);
            }

            @Override
            public String getText() {
                return text.getText();
            }

            @Override
            public UUID getID() {
                return uuid;
            }

            @Override
            public BaseComponent[] getComponents() {
                return builder.create();
            }
        };
        AdvancedChatAPI.getInstance().registerFunctionalWidget(button);
        return button;
    }

    public static interface Executor {
        void onExecute(Player executor, Button button);
    }

}

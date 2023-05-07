package de.cyklon.advancedchatapi.widgets;

import org.bukkit.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public interface Graph extends Widget {

    char character = '█';

    public static Graph createGraph(int[] values) {
        return createGraph(values, ChatColor.WHITE);
    }

    public static Graph createGraph(int[] values, ChatColor fg) {
        return createGraph(values, fg, ChatColor.BLACK);
    }

    public static Graph createGraph(int[] values, ChatColor fg, ChatColor bg) {
        final UUID id = UUID.randomUUID();
        int max = 0;
        for (int value : values) max = Math.max(max, value);
        ComponentBuilder builder = new ComponentBuilder("\n");
        for (int i = max; i > 0; i--) {
            StringBuilder sb = new StringBuilder(String.format("%" + (max+"").length() + "s", (i + "")).replace(' ', '0') + ". ");
            for (int value : values) {
                if (value >= i) sb.append(fg).append(character);
                else sb.append(bg).append(character);
            }
            builder.append(new TextComponent(sb + "\n"));
        }
        return new Graph() {
            @Override
            public UUID getID() {
                return id;
            }

            @Override
            public BaseComponent[] getComponents() {
                return builder.create();
            }
        };
    }

}

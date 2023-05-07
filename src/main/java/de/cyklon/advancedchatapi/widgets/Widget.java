package de.cyklon.advancedchatapi.widgets;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Widget {

    UUID getID();
    BaseComponent[] getComponents();

    default void send(Player player) {
        player.spigot().sendMessage(getComponents());
    }

}

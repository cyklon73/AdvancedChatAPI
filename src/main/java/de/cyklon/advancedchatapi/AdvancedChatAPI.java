package de.cyklon.advancedchatapi;

import de.cyklon.advancedchatapi.widgets.Button;
import de.cyklon.advancedchatapi.widgets.FunctionalWidget;
import de.cyklon.advancedchatapi.widgets.Graph;
import net.md_5.bungee.api.chat.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AdvancedChatAPI extends JavaPlugin {

    private static AdvancedChatAPI instance;
    private static final Map<UUID, FunctionalWidget> functionalWidgets = new HashMap<>();
    private static BaseCommand baseCommand;
    private static CommandExecutor externalBaseCommandExecutor = (sender, command, label, args) -> false;

    @Override
    public void onEnable() {
        instance = this;

        setBaseCommand(getCommand("base-cmd"), "control");
        Bukkit.getOnlinePlayers().forEach((p) -> {
            Button.createButton((TextComponent) new ComponentBuilder("[Button]").event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Color Button").color(net.md_5.bungee.api.ChatColor.GREEN).obfuscated(true).create())).underlined(true).color(ChatColor.LIGHT_PURPLE.asBungee()).italic(true).create()[0], (executor, button) -> executor.sendMessage("Executed Button: " + button.getID())).send(p);
            int[] array = generateIntArray(30, 100);
            p.sendMessage(Arrays.toString(array));
            Graph.createGraph(array, ChatColor.GOLD).send(p);
        });
    }

    private int[] generateIntArray(int maxSize, int maxValue) {
        int[] array = new int[(int) Math.round(Math.random()*maxSize)];
        for (int i = 0; i < array.length; i++) array[i] = (int) Math.round(Math.random()*maxValue);
        return array;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && matchArgs(baseCommand.subcommand, args)) {
            Player player = (Player) sender;
            System.out.println(Arrays.toString(args));
            args = stripArgs(getSubArgs(baseCommand.subcommand), args);
            System.out.println(Arrays.toString(args));
            try {
                UUID id = UUID.fromString(args[0]);
                FunctionalWidget fw = functionalWidgets.get(id);
                if (fw!=null) fw.trigger(player);
            } catch (Exception ignored) {}
        }
        return externalBaseCommandExecutor.onCommand(sender, command, label, args);
    }

    private String[] getSubArgs(String subCommand) {
        return StringUtils.strip(subCommand).split(" ");
    }

    private String[] stripArgs(String[] subArgs, String[] args) {
        return Arrays.copyOfRange(args, args.length-subArgs.length, args.length);
    }

    private boolean matchArgs(String subCommand, String[] args) {
        if (StringUtils.isBlank(subCommand)) return true;
        String[] args1 = getSubArgs(subCommand);
        if (args.length<args1.length) return false;
        for (int i = 0; i < args1.length; i++) if (!args[i].equals(args1[i])) return false;
        return true;
    }

    public static AdvancedChatAPI getInstance() {
        return instance;
    }

    public void registerFunctionalWidget(FunctionalWidget widget) {
        functionalWidgets.put(widget.getID(), widget);
    }

    public static void setBaseCommand(PluginCommand command) {
        setBaseCommand(command, "");
    }

    public static void setBaseCommand(PluginCommand command, String subcommand) {
        command.setExecutor(AdvancedChatAPI.getInstance());
        baseCommand = new BaseCommand(command, subcommand);
    }

    public static void setBaseCommandExecutor(CommandExecutor executor) {
        externalBaseCommandExecutor = executor;
    }

    public BaseCommand getBaseCommand() {
        return baseCommand;
    }


    public static class BaseCommand {
        private final PluginCommand command;
        private final String subcommand;

        public BaseCommand(PluginCommand command, String subcommand) {
            this.command = command;
            this.subcommand = subcommand;
        }

        public PluginCommand getCommand() {
            return command;
        }

        public String getSubcommand() {
            return subcommand;
        }
    }
}

package com.i0dev.plugin.technobladememorial.object;

import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import com.i0dev.plugin.technobladememorial.config.GeneralConfig;
import com.i0dev.plugin.technobladememorial.config.MessageConfig;
import com.i0dev.plugin.technobladememorial.template.AbstractCommand;
import com.i0dev.plugin.technobladememorial.template.AbstractConfiguration;
import com.i0dev.plugin.technobladememorial.template.AbstractHook;
import com.i0dev.plugin.technobladememorial.template.AbstractManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class CorePlugin extends JavaPlugin {

    Set<AbstractConfiguration> configs = new HashSet<>();
    Set<AbstractManager> managers = new HashSet<>();
    Set<AbstractCommand> commands = new HashSet<>();
    Set<AbstractHook> hooks = new HashSet<>();
    @Getter
    public List<String> commandsForHelp = new ArrayList<>();

    @Override
    public void onEnable() {
        startup();
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        System.out.println("\u001B[32m" + getDescription().getName() + " by: " + getDescription().getAuthors().get(0) + " has been enabled.");
    }

    public abstract void startup();

    protected void setCommandsForHelp(String... commands) {
        commandsForHelp.addAll(Arrays.asList(commands));
    }

    @Override
    public void onDisable() {
        shutdown();
        HandlerList.unregisterAll(TechnoBladeMemorial.getPlugin());
        Bukkit.getScheduler().cancelTasks(TechnoBladeMemorial.getPlugin());
        commands.forEach(AbstractCommand::deinitialize);
        hooks.forEach(AbstractHook::deinitialize);
        managers.forEach(AbstractManager::deinitialize);
        System.out.println("\u001B[31m" + getDescription().getName() + " by: " + getDescription().getAuthors().get(0) + " has been disabled.");
    }

    public void shutdown() {

    }

    public void registerManager(AbstractManager manager) {
        if (manager.isLoaded()) manager.deinitialize();
        manager.initialize();
        manager.setLoaded(true);
        if (manager.isListener())
            getServer().getPluginManager().registerEvents(manager, TechnoBladeMemorial.getPlugin());
    }


    public void registerHook(AbstractHook hook, String hookName) {
        hook.setName(hookName);
        if (hook.isLoaded()) hook.deinitialize();
        hook.initialize();
        hook.setLoaded(true);
        hooks.add(hook);
        System.out.println("\u001B[32mRegistered Hook: " + hookName);
    }

    public void registerCommand(AbstractCommand command, String commandName) {
        command.setCommandName(commandName);
        command.setPlugin(TechnoBladeMemorial.getPlugin());
        if (command.isLoaded()) command.deinitialize();
        command.initialize();
        command.setLoaded(true);

        getCommand(command.getCommandName()).setExecutor(command);
        getCommand(command.getCommandName()).setTabCompleter(command);

        commands.add(command);
    }

    @SneakyThrows
    public void registerConfig(AbstractConfiguration config) {
        config.getConfig().save();
        configs.add(config);
    }

    @SneakyThrows
    public void registerConfig(String path, AbstractConfiguration config) {
        config.getConfig().save();
        configs.add(config);
    }

    public void reloadConfig() {
        configs.forEach(cnf -> cnf.getConfig().reloadConfig());
    }

    public SimpleConfig getConfig(Class<? extends AbstractConfiguration> clazz) {
        AbstractConfiguration c = configs.stream().filter(config -> config.getClass().equals(clazz)).findFirst().orElse(null);
        if (c == null) return null;
        return c.getConfig();
    }

    public <T> T getHook(Class<T> clazz) {
        return (T) hooks.stream().filter(hook -> hook.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public boolean isHookEnabled(String hookName) {
        return hooks.stream().anyMatch(hook -> hook.getName().equalsIgnoreCase(hookName));
    }

    public boolean isPluginEnabled(String name) {
        return getServer().getPluginManager().isPluginEnabled(name);
    }

    public SimpleConfig cnf() {
        return getConfig(GeneralConfig.class);
    }

    public SimpleConfig msg() {
        return getConfig(MessageConfig.class);
    }

}

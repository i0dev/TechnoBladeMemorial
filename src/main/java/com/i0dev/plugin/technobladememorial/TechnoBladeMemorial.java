package com.i0dev.plugin.technobladememorial;

import com.i0dev.plugin.technobladememorial.command.*;
import com.i0dev.plugin.technobladememorial.config.GeneralConfig;
import com.i0dev.plugin.technobladememorial.config.MessageConfig;
import com.i0dev.plugin.technobladememorial.hook.PlaceholderAPIHook;
import com.i0dev.plugin.technobladememorial.manager.*;
import com.i0dev.plugin.technobladememorial.object.CorePlugin;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TechnoBladeMemorial extends CorePlugin {
    @Getter
    private static TechnoBladeMemorial plugin;
    public static final String PERMISSION_PREFIX = "technobladememorial";

    @SneakyThrows
    @Override
    public void startup() {
        plugin = this;

        this.getDataFolder().mkdir();

        // Managers
        registerManager(RestrictionManager.getInstance());
        registerManager(StorageManager.getInstance());
        registerManager(PigManager.getInstance());

        // Hooks
        if (isPluginEnabled("PlaceholderAPI"))
            registerHook(new PlaceholderAPIHook(), "papi");

        registerConfig(new GeneralConfig("config.yml"));
        registerConfig(new MessageConfig("messages.yml"));

        // Commands
        registerCommand(CmdTechno.getInstance(), "techno");
        registerCommand(CmdClaimPig.getInstance(), "claimPig");
        registerCommand(CmdMessage.getInstance(), "message");


        setCommandsForHelp(
                "techno help",
                "techno version",
                "techno reload",
                "claimPig",
                "message <content>",
                "techno addpigspawn"
        );
    }

    /**
     * Gets a message directly from the messaging config
     *
     * @param path The path of the message.
     * @return The message that corresponds to the specified path.
     */
    public static String getMsg(String path) {
        return TechnoBladeMemorial.getPlugin().msg().getString(path);
    }

}

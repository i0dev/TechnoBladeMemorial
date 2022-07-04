package com.i0dev.plugin.technobladememorial.config;

import com.i0dev.plugin.technobladememorial.template.AbstractConfiguration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageConfig extends AbstractConfiguration {

    public MessageConfig(String path) {
        super(path);
    }

    protected void setValues() {
        config.set("reloadedConfig", "&7You have&a reloaded&7 the configuration.");
        config.set("noPermission", "&cYou don not have permission to run that command.");
        config.set("cantFindPlayer", "&cThe player: &f{player}&c cannot be found!");
        config.set("invalidNumber", "&cThe number &f{num} &cis invalid! Try again.");
        config.set("cantRunAsConsole", "&cYou cannot run this command from console.");

        config.set("helpPageTitle", "&8_______&r&8[&r &c&lTechno Help &8]_______");
        config.set("helpPageFormat", " &c* &7/{cmd}");

        config.set("cantDoThat", "&cYou cannot do that.");

        config.set("nothingToClaim", "&cYou do not have any pigs to claim. If you recently purchased one, please wait a little while.");
        config.set("receivedPig", "&7You have received a new pig spawn egg! Claim with &c/claimPig");
        config.set("claimedAllPigs", "&7You have claimed all your pending pigs");
        config.set("notPigSpawnEgg","&7Hold a pig spawn egg while running this command.");
        config.set("badMessage","&7Your message contains banned words. Please use friendly and positive language.");
    }
}
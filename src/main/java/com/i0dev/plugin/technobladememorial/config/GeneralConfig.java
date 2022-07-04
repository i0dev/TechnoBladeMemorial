package com.i0dev.plugin.technobladememorial.config;


import com.i0dev.plugin.technobladememorial.object.config.ConfigItemStack;
import com.i0dev.plugin.technobladememorial.template.AbstractConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter
public class GeneralConfig extends AbstractConfiguration {
    public GeneralConfig(String path) {
        super(path);
    }

    protected void setValues() {
        config.set("pigSpawnItemStackTiers.IRON",
                new ConfigItemStack(
                        Material.MONSTER_EGG,
                        "&7&lIron Tier Pig",
                        Arrays.asList(
                                "",
                                "&7This spawn egg is the official token of your donation",
                                "&7towards the fight for Sarcoma cancer, and to show your",
                                "&7support for TechnoBlade and his family.",
                                "",
                                "&4&lPig Stats:",
                                " &c* &7Donor: &f{donor}",
                                " &c* &7Tier: &f{tier}",
                                " &c* &7Amount: &f${amount}",
                                " &c* &7Date: &f{date}",
                                "",
                                "&4&lCustom Message:",
                                "&7{message}",
                                "",
                                "&8Donation ID: {id}"
                        ),
                        90,
                        true

                ).serialize());

        config.set("pigSpawnItemStackTiers.GOLD",
                new ConfigItemStack(
                        Material.MONSTER_EGG,
                        "&6&lGold Tier Pig",
                        Arrays.asList(
                                "",
                                "&7This spawn egg is the official token of your donation",
                                "&7towards the fight for Sarcoma cancer, and to show your",
                                "&7support for TechnoBlade and his family.",
                                "",
                                "&4&lPig Stats:",
                                " &c* &7Donor: &f{donor}",
                                " &c* &7Tier: &f{tier}",
                                " &c* &7Amount: &f${amount}",
                                " &c* &7Date: &f{date}",
                                "",
                                "&4&lCustom Message:",
                                "&7{message}",
                                "",
                                "&8Donation ID: {id}"
                        ),
                        90,
                        true

                ).serialize());
        config.set("pigSpawnItemStackTiers.DIAMOND",
                new ConfigItemStack(
                        Material.MONSTER_EGG,
                        "&b&lDiamond Tier Pig",
                        Arrays.asList(
                                "",
                                "&7This spawn egg is the official token of your donation",
                                "&7towards the fight for Sarcoma cancer, and to show your",
                                "&7support for TechnoBlade and his family.",
                                "",
                                "&4&lPig Stats:",
                                " &c* &7Donor: &f{donor}",
                                " &c* &7Tier: &f{tier}",
                                " &c* &7Amount: &f${amount}",
                                " &c* &7Date: &f{date}",
                                "",
                                "&4&lCustom Message:",
                                "&7{message}",
                                "",
                                "&8Donation ID: {id}"
                        ),
                        90,
                        true

                ).serialize());
        config.set("pigSpawnItemStackTiers.CUSTOM",
                new ConfigItemStack(
                        Material.MONSTER_EGG,
                        "&c&lCustom Amount Pig",
                        Arrays.asList(
                                "",
                                "&7This spawn egg is the official token of your donation",
                                "&7towards the fight for Sarcoma cancer, and to show your",
                                "&7support for TechnoBlade and his family.",
                                "",
                                "&4&lPig Stats:",
                                " &c* &7Donor: &f{donor}",
                                " &c* &7Tier: &f{tier}",
                                " &c* &7Amount: &f${amount}",
                                " &c* &7Date: &f{date}",
                                "",
                                "&4&lCustom Message:",
                                "&7{message}",
                                "",
                                "&8Donation ID: {id}"
                        ),
                        90,
                        true

                ).serialize());

        config.set("messageBlacklistedWords", Arrays.asList(
                "fuck",
                "cunt",
                "shit",
                "bitch",
                "n1gga",
                "nigger",
                "nigga"
        ));
    }
}

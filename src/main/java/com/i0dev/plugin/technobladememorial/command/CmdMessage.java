package com.i0dev.plugin.technobladememorial.command;

import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import com.i0dev.plugin.technobladememorial.manager.PigManager;
import com.i0dev.plugin.technobladememorial.object.DonationTier;
import com.i0dev.plugin.technobladememorial.template.AbstractCommand;
import com.i0dev.plugin.technobladememorial.utility.MsgUtil;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CmdMessage extends AbstractCommand {

    @Getter
    public static final CmdMessage instance = new CmdMessage();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "addpigspawn")) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("noPermission"));
            return;
        }
        if (!(sender instanceof Player)) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("cantRunAsConsole"));
            return;
        }

        ItemStack hand = ((Player) sender).getItemInHand();
        if (hand == null || hand.getType().equals(Material.AIR)) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("notPigSpawnEgg"));
            return;
        }

        NBTItem nbtHand = new NBTItem(hand);
        if (!nbtHand.hasKey("tier")) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("notPigSpawnEgg"));
            return;
        }

        if (args.length == 1) {
            MsgUtil.msg(sender, "&cFormat: &7/message <message>");
            return;
        }

        List<String> words = new ArrayList<>(Arrays.asList(args));

        boolean badWord = false;
        for (String word : words) {
            if (!isGoodMessage(word)) badWord = true;
        }

        if (badWord) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("badMessage"));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String word : words) {
            message.append(word).append(" ");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));

        ((Player) sender).setItemInHand(new ItemStack(Material.AIR));
        PigManager.getInstance().givePig(((Player) sender), DonationTier.valueOf(nbtHand.getString("tier")), nbtHand.getDouble("amountUSD"), UUID.fromString(nbtHand.getString("donationUUID")), nbtHand.getLong("timestamp"), message.toString());

        MsgUtil.msg(sender, "&7You set the message to: &f" + message);
    }


    public boolean isGoodMessage(String word) {
        for (String blacklisted : TechnoBladeMemorial.getPlugin().cnf().getStringList("messageBlacklistedWords")) {
            if (word.contains(blacklisted)) return false;
        }
        return true;
    }

}

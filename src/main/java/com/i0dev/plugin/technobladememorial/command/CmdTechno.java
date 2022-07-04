package com.i0dev.plugin.technobladememorial.command;

import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import com.i0dev.plugin.technobladememorial.manager.StorageManager;
import com.i0dev.plugin.technobladememorial.object.DonationTier;
import com.i0dev.plugin.technobladememorial.object.QueuedDonation;
import com.i0dev.plugin.technobladememorial.template.AbstractCommand;
import com.i0dev.plugin.technobladememorial.utility.MsgUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdTechno extends AbstractCommand {

    @Getter
    public static final CmdTechno instance = new CmdTechno();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            help(sender, args);
        } else {
            switch (args[0].toLowerCase()) {
                case "reload":
                    reload(sender, args);
                    break;
                case "version":
                case "ver":
                    version(sender, args);
                    break;
                case "give":
                    give(sender, args);
                    break;
                case "addpigspawn":
                    addpigspawn(sender, args);
                    break;
                default:
                    help(sender, args);
            }
        }
    }

    private void addpigspawn(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "addpigspawn")) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("noPermission"));
            return;
        }
        if (!(sender instanceof Player)) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("cantRunAsConsole"));
            return;
        }

        StorageManager.getInstance().addSpawnLocation(((Player) sender).getLocation());

        MsgUtil.msg(sender, "&aAdded pig spawn location.");
    }

    private void give(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "give")) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("noPermission"));
            return;
        }

        // /  -1    0    1          2       3
        // /techno add {PLAYER} {TIER} {AMOUNT}
        if (args.length != 4) {
            MsgUtil.msg(sender, "&cFormat: &7/techno give <player> <tier> <amount>");
            return;
        }


        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        if (offlinePlayer == null) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("cantFindPlayer"));
            return;
        }

        QueuedDonation queuedDonation = new QueuedDonation();
        queuedDonation.setTier(DonationTier.valueOf(args[2].toUpperCase()));
        queuedDonation.setDonorUUID(offlinePlayer.getUniqueId());
        queuedDonation.setTimestamp(System.currentTimeMillis());
        queuedDonation.setAmountUSD(Double.parseDouble(args[3]));
        queuedDonation.setDonationUUID(UUID.randomUUID());


        StorageManager.getInstance().addQueuedDonation(queuedDonation);

        MsgUtil.msg(sender, "&aAdded successfully");

        Player player = offlinePlayer.getPlayer();
        if (player == null) return;
        MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("receivedPig"));
    }
}

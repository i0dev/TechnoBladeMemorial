package com.i0dev.plugin.technobladememorial.command;

import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import com.i0dev.plugin.technobladememorial.manager.PigManager;
import com.i0dev.plugin.technobladememorial.manager.StorageManager;
import com.i0dev.plugin.technobladememorial.object.DonationTier;
import com.i0dev.plugin.technobladememorial.object.QueuedDonation;
import com.i0dev.plugin.technobladememorial.template.AbstractCommand;
import com.i0dev.plugin.technobladememorial.utility.MsgUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CmdClaimPig extends AbstractCommand {
    @Getter
    public static final CmdClaimPig instance = new CmdClaimPig();

    @Override
    @SneakyThrows
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "claim")) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("noPermission"));
            return;
        }
        if (!(sender instanceof Player)) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("cantRunAsConsole"));
            return;
        }
        Player player = ((Player) sender);

        ResultSet results = StorageManager.getInstance().executeQuery("SELECT * FROM queued_donations WHERE donorUUID = '" + player.getUniqueId().toString() + "'");

        Set<QueuedDonation> queuedDonations = new HashSet<>();
        while (results.next()) {
            QueuedDonation donation = new QueuedDonation();
            donation.setAmountUSD(Double.parseDouble(results.getString("amountUSD")));
            donation.setDonorUUID(player.getUniqueId());
            donation.setTimestamp(results.getLong("timestamp"));
            donation.setTier(DonationTier.valueOf(results.getString("tier")));
            donation.setDonationUUID(UUID.fromString(results.getString("donationUUID")));
            queuedDonations.add(donation);
        }

        if (queuedDonations.size() == 0) {
            MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("nothingToClaim"));
            return;
        }

        for (QueuedDonation donation : queuedDonations) {
            PigManager.getInstance().givePig(player, donation.getTier(), donation.getAmountUSD(), donation.getDonationUUID(), donation.getTimestamp());
            StorageManager.getInstance().removeQueuedDonation(donation.getDonationUUID());
        }

        MsgUtil.msg(sender, TechnoBladeMemorial.getMsg("claimedAllPigs"));


    }
}

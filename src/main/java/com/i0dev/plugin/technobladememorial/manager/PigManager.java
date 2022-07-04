package com.i0dev.plugin.technobladememorial.manager;

import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import com.i0dev.plugin.technobladememorial.object.DonationTier;
import com.i0dev.plugin.technobladememorial.object.FinalDonation;
import com.i0dev.plugin.technobladememorial.object.config.ConfigItemStack;
import com.i0dev.plugin.technobladememorial.template.AbstractManager;
import com.i0dev.plugin.technobladememorial.utility.APIUtil;
import com.i0dev.plugin.technobladememorial.utility.MsgUtil;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class PigManager extends AbstractManager {

    @Getter
    public final static PigManager instance = new PigManager();

    BukkitTask taskSpawnPigs;

    @Override
    public void initialize() {
        setListener(true);
        taskSpawnPigs = Bukkit.getScheduler().runTaskTimerAsynchronously(TechnoBladeMemorial.getPlugin(), assurePigsAreSpawnedIn, 20 * 10, 20 * 60 * 30);
    }

    @Override
    public void deinitialize() {
        if (taskSpawnPigs != null) taskSpawnPigs.cancel();
    }

    //  Entity UUID , Donation UUID
    Map<UUID, UUID> entityDonationMap = new HashMap<>();


    public void givePig(Player player, DonationTier tier, double amount, UUID donationUUID, long timestamp) {
        player.getInventory().addItem(getPigItem(player, tier, amount, donationUUID, timestamp, null));
    }

    public void givePig(Player player, DonationTier tier, double amount, UUID donationUUID, long timestamp, String message) {
        player.getInventory().addItem(getPigItem(player, tier, amount, donationUUID, timestamp, message));
    }

    public void clearPigs() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity livingEntity : world.getLivingEntities()) {
                if (!(livingEntity instanceof Pig)) continue;
                if (entityDonationMap.containsKey(livingEntity.getUniqueId())) continue;
                livingEntity.remove();
            }
        }
    }

    public ItemStack getPigItem(Player player, DonationTier tier, double amount, UUID donationUUID, long timestamp, String message) {
        ItemStack itemStack = new ConfigItemStack(TechnoBladeMemorial.getPlugin().cnf().getConfigurationSection("pigSpawnItemStackTiers." + tier.toString()).getValues(true)).toItemStack();
        String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(Date.from(Instant.ofEpochMilli(timestamp)));


        List<String> newLore = new ArrayList<>();
        for (String s : itemStack.getItemMeta().getLore()) {
            newLore.add(s
                    .replace("{donor}", player.getDisplayName())
                    .replace("{tier}", tier.toString())
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{id}", donationUUID.toString())
                    .replace("{date}", formattedDate)
                    .replace("{message}", message == null ? "No message set yet! &7set with &c/message <message> &7while holding the spawn egg." : message)
            );
        }
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(MsgUtil.color(newLore));
        itemStack.setItemMeta(meta);


        NBTItem item = new NBTItem(itemStack);
        item.setString("donorUUID", player.getUniqueId().toString());
        item.setString("tier", tier.toString());
        item.setDouble("amountUSD", amount);
        item.setLong("timestamp", timestamp);
        item.setString("donationUUID", donationUUID.toString());
        item.setString("message", message);

        return item.getItem();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack hand = e.getPlayer().getItemInHand();
        if (hand == null || hand.getType().equals(Material.AIR)) return;

        NBTItem nbtHand = new NBTItem(hand);

        if (nbtHand.hasKey("tier")) {
            e.setCancelled(true);
            spawnPig(e.getPlayer().getLocation(), DonationTier.valueOf(nbtHand.getString("tier")), UUID.fromString(nbtHand.getString("donationUUID")), e.getPlayer().getUniqueId());
            e.getPlayer().setItemInHand(new ItemStack(Material.AIR));

            FinalDonation finalDonation = new FinalDonation();
            finalDonation.setTier(DonationTier.valueOf(nbtHand.getString("tier")));
            finalDonation.setDonorUUID(e.getPlayer().getUniqueId());
            finalDonation.setTimestamp(nbtHand.getLong("timestamp"));
            finalDonation.setAmountUSD(nbtHand.getDouble("amount"));
            finalDonation.setDonationUUID(UUID.fromString(nbtHand.getString("donationUUID")));
            finalDonation.setMessage(nbtHand.getString("message") == null ? "" : nbtHand.getString("message"));

            StorageManager.getInstance().addFinalDonation(finalDonation);
        }
    }

    @SneakyThrows
    @EventHandler
    public void onClickPig(PlayerInteractEntityEvent e) {
        UUID donationUUID = entityDonationMap.getOrDefault(e.getRightClicked().getUniqueId(), null);
        if (donationUUID == null) return;

        ResultSet result = StorageManager.getInstance().executeQuery("SELECT * from final_donations WHERE donationUUID = '" + donationUUID + "'");
        if (!result.next()) {
            result.close();
            return;
        }

        String message = result.getString("message");
        String donorUUID = result.getString("donorUUID");
        result.close();

        if (e.getPlayer().isSneaking()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(message.equals("") ? "No message" : message);
            return;
        }

        if (!donorUUID.equals(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("You can only ride your own piggy, (shift + right click to get the pigs information)");
        }
    }

    public Pig spawnPig(Location location, DonationTier tier, UUID donationUUID, UUID donorUUID) {
        Pig pig = (Pig) location.getWorld().spawnEntity(location, EntityType.PIG);
        pig.setSaddle(true);
        pig.setCustomNameVisible(true);

        String name = "";
        String playerName = APIUtil.getIGNFromUUID(donorUUID.toString());

        switch (tier) {
            case IRON:
                name = "&7" + playerName + "'s pig";
                break;
            case GOLD:
                name = "&6" + playerName + "'s pig";
                break;
            case DIAMOND:
                name = "&b" + playerName + "'s pig";
                break;
            case CUSTOM:
                name = "&c" + playerName + "'s pig";
                break;
        }

        pig.setCustomName(MsgUtil.color(name));
        entityDonationMap.put(pig.getUniqueId(), donationUUID);
        return pig;
    }


    public Runnable assurePigsAreSpawnedIn = () -> {
        try {
            clearPigs();

            ResultSet finalDonations = StorageManager.getInstance().executeQuery("SELECT * FROM final_donations");
            ResultSet spawnLocations = StorageManager.getInstance().executeQuery("SELECT * FROM spawn_locations");
            Set<Location> locations = new HashSet<>();
            while (spawnLocations.next()) {
                locations.add(new Location(
                        Bukkit.getWorld(spawnLocations.getString("world")),
                        spawnLocations.getInt("x"),
                        spawnLocations.getInt("y"),
                        spawnLocations.getInt("z")
                ));
            }
            spawnLocations.close();

            while (finalDonations.next()) {
                UUID donationUUID = UUID.fromString(finalDonations.getString("donationUUID"));

                if (entityDonationMap.containsKey(donationUUID)) continue;
                Location location = locations.stream().skip(new Random().nextInt(locations.size())).findFirst().orElse(Bukkit.getOnlinePlayers().stream().findFirst().get().getLocation());

                DonationTier tier = DonationTier.valueOf(finalDonations.getString("tier"));
                UUID donorUUID = UUID.fromString(finalDonations.getString("donorUUID"));

                Bukkit.getScheduler().scheduleSyncDelayedTask(TechnoBladeMemorial.getPlugin(), () -> spawnPig(location, tier, donationUUID, donorUUID));
            }
            finalDonations.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

}

package com.i0dev.plugin.technobladememorial.manager;

import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import com.i0dev.plugin.technobladememorial.template.AbstractCommand;
import com.i0dev.plugin.technobladememorial.template.AbstractManager;
import com.i0dev.plugin.technobladememorial.utility.MsgUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class RestrictionManager extends AbstractManager {

    @Getter
    private static final RestrictionManager instance = new RestrictionManager();

    @Override
    public void initialize() {
        setListener(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!hasPermission(e.getPlayer(), "admin")) {
            MsgUtil.msg(e.getPlayer(), TechnoBladeMemorial.getMsg("cantDoThat"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!hasPermission(e.getPlayer(), "admin")) {
            MsgUtil.msg(e.getPlayer(), TechnoBladeMemorial.getMsg("cantDoThat"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPlayedBefore()) return;
        e.getPlayer().getInventory().addItem(new ItemStack(Material.CARROT_STICK));
    }

}

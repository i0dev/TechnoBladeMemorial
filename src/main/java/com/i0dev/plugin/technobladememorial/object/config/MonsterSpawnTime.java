package com.i0dev.plugin.technobladememorial.object.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonsterSpawnTime implements SerializableConfig {

    Map<Integer, Integer> times;
    String customName;
    EntityType entityType;
    int health;
    List<PotionEffect> potionEffects;
    ConfigEnchantedItemStack helmet;
    ConfigEnchantedItemStack chestplate;
    ConfigEnchantedItemStack leggings;
    ConfigEnchantedItemStack boots;
    ConfigEnchantedItemStack sword;


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("times", times);
        data.put("customName", customName);
        data.put("entityType", entityType.toString());
        data.put("health", health);
        List<Map<String, Object>> potEff = new ArrayList<>();
        potionEffects.forEach(configPotionEffect -> potEff.add(configPotionEffect.serialize()));
        data.put("potionEffects", potEff);
        data.put("helmet", helmet.serialize());
        data.put("chestplate", chestplate.serialize());
        data.put("leggings", leggings.serialize());
        data.put("boots", boots.serialize());
        data.put("sword", sword.serialize());
        return data;
    }

    public MonsterSpawnTime(Map<String, Object> map) {
        times = new LinkedHashMap<>();
        for (Map.Entry<Number, Integer> num : ((Map<Number, Integer>) map.get("times")).entrySet()) {
            times.put(Integer.parseInt(num.getKey() + ""), num.getValue());
        }
        this.customName = (String) map.get("customName");
        this.entityType = EntityType.valueOf((String) map.get("entityType"));
        this.health = (int) map.get("health");

        potionEffects = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : ((List<Map<String, Object>>) map.get("potionEffects"))) {
            potionEffects.add(new PotionEffect(stringObjectMap));
        }

        this.helmet = new ConfigEnchantedItemStack((Map<String, Object>) map.get("helmet"));
        this.chestplate = new ConfigEnchantedItemStack((Map<String, Object>) map.get("chestplate"));
        this.leggings = new ConfigEnchantedItemStack((Map<String, Object>) map.get("leggings"));
        this.boots = new ConfigEnchantedItemStack((Map<String, Object>) map.get("boots"));
        this.sword = new ConfigEnchantedItemStack((Map<String, Object>) map.get("sword"));
    }

}

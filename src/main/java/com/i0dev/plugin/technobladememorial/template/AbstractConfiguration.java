package com.i0dev.plugin.technobladememorial.template;

import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import com.i0dev.plugin.technobladememorial.manager.ConfigManager;
import com.i0dev.plugin.technobladememorial.object.SimpleConfig;
import com.i0dev.plugin.technobladememorial.object.SimpleConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
public abstract class AbstractConfiguration {

    protected SimpleConfig config;
    String path;

    @SneakyThrows
    public AbstractConfiguration(String path) {
        this.path = path;
        this.config = ConfigManager.getInstance().getNewConfig(path, new String[]{"Plugin made by " + TechnoBladeMemorial.getPlugin().getDescription().getAuthors().toString().substring(1, TechnoBladeMemorial.getPlugin().getDescription().getAuthors().toString().length() - 1)});
        this.setValues();
    }

    protected abstract void setValues();
}

package com.i0dev.plugin.technobladememorial.template;


import com.i0dev.plugin.technobladememorial.TechnoBladeMemorial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;

@Getter
@Setter
public abstract class AbstractManager implements Listener {

    public boolean loaded = false;
    public boolean listener = false;

    /**
     * This method is called when the manager gets registered in the main class.
     */
    public void initialize() {

    }

    /**
     * This method gets called during shutdown of the plugin.
     */
    public void deinitialize() {

    }


    protected boolean hasPermission(Permissible entity, String permission) {
        return entity.hasPermission(TechnoBladeMemorial.PERMISSION_PREFIX + "." + permission);
    }

}

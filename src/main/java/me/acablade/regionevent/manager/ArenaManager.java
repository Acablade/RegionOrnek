package me.acablade.regionevent.manager;

import me.acablade.regionevent.RegionEventPlugin;
import me.acablade.regionevent.events.RegionEnterEvent;
import me.acablade.regionevent.events.RegionLeaveEvent;
import me.acablade.regionevent.objects.Region;
import me.acablade.regionevent.utils.SerializationUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;

public class ArenaManager {

    private final RegionEventPlugin plugin;
    private final Region region;
    private final String[] inventory;

    public ArenaManager(RegionEventPlugin plugin, String[] inventory,Location min, Location max){
        this.plugin = plugin;
        this.inventory = inventory;
        this.region = new Region(
                min,
                max,
                plugin);


        region.addListener(RegionEnterEvent.class, event -> {
            try {
                equipPlayer(event.getPlayer());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        region.addListener(RegionLeaveEvent.class, event -> {
            try {
                unequipPlayer(event.getPlayer());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        region.addListener(EntityDamageEvent.class, event -> {
            event.setCancelled(!region.isInside(event.getEntity()));
        });

    }

    public ArenaManager(RegionEventPlugin plugin, PlayerInventory inventory, Location min, Location max){
        this(plugin, SerializationUtils.playerInventoryToBase64(inventory),min,max);
    }



    public void equipPlayer(Player player) throws IOException {
        plugin.getRollbackManager().save(player);
        player.getInventory().clear();
        player.updateInventory();
        player.getInventory().setContents(SerializationUtils.itemStackArrayFromBase64(inventory[0]));
        player.getInventory().setArmorContents(SerializationUtils.itemStackArrayFromBase64(inventory[1]));

        player.setGameMode(GameMode.SURVIVAL);

        player.setHealth(20);
        player.setFoodLevel(20);

    }

    public void unequipPlayer(Player player) throws IOException {
        plugin.getRollbackManager().rollback(player);
    }



}

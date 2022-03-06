package me.acablade.regionevent.events;

import me.acablade.regionevent.objects.Region;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RegionLeaveEvent extends PlayerMoveEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Region region;

    public RegionLeaveEvent(Player player, Location from, Location to, Region region) {
        super(player, from, to);
        this.region = region;
    }


    public Region getRegion() {
        return region;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
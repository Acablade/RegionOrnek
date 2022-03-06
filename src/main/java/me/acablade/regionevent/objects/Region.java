package me.acablade.regionevent.objects;

import me.acablade.regionevent.events.RegionEnterEvent;
import me.acablade.regionevent.events.RegionLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class Region{

    private static final Listener LISTENER = new Listener(){};

    private final Location min;
    private final Location max;

    private final JavaPlugin plugin;


    public Region(Location min, Location max, JavaPlugin plugin){

        this.min = min;
        this.max = max;
        this.plugin = plugin;

        addListener(PlayerMoveEvent.class, event -> {
            Player player = event.getPlayer();
            if(isInside(event.getTo())&&!isInside(event.getFrom())){
                Bukkit.getPluginManager().callEvent(new RegionEnterEvent(player,event.getFrom(),event.getTo(),this));
            }else if(!isInside(player)&&isInside(event.getFrom())){
                Bukkit.getPluginManager().callEvent(new RegionLeaveEvent(player,event.getFrom(),event.getTo(),this));
            }
        },false);

    }

    public <T extends Event> void addListener(Class<T> eventClass, Consumer<T> eventConsumer, boolean checkInside){

        EventExecutor eventExecutor = (ignored, event) -> {

            if(event instanceof PlayerEvent){
                if(checkInside){
                    if(isInside(((PlayerEvent) event).getPlayer())) eventConsumer.accept((T) event);
                    return;
                }
                eventConsumer.accept((T) event);
                return;
            }else if(event instanceof EntityEvent){
                if(checkInside){
                    if(isInside(((EntityEvent) event).getEntity())) eventConsumer.accept((T) event);
                    return;
                }
                eventConsumer.accept((T) event);
                return;
            }
            eventConsumer.accept((T) event);

        };

        Bukkit.getPluginManager().registerEvent(eventClass,LISTENER, EventPriority.NORMAL,eventExecutor,plugin);


    }

    public <T extends Event> void addListener(Class<T> eventClass, Consumer<T> eventConsumer){

        EventExecutor eventExecutor = (ignored, event) -> {
            eventConsumer.accept((T) event);
        };

        Bukkit.getPluginManager().registerEvent(eventClass,LISTENER, EventPriority.NORMAL,eventExecutor,plugin);


    }


    @Override
    public String toString() {
        return "Region{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return min.equals(region.min) && max.equals(region.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    public boolean isInside(Entity entity){

        Location playerLocation = entity.getLocation();

        return isInside(playerLocation);

    }

    public boolean isInside(Location location){
        if(location.getWorld()!=min.getWorld()) return false;

        return location.toVector().isInAABB(min.toVector(),max.toVector());
    }



}

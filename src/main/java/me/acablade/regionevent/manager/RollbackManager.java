package me.acablade.regionevent.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RollbackManager {


    private final HashMap<UUID, String[]> inventoryMap;
    private final HashMap<UUID, Double> healthMap;
    private final HashMap<UUID, Integer> foodMap;
    private final HashMap<UUID, GameMode> gameModeMap;


    public RollbackManager(){
        inventoryMap = new HashMap<>();
        healthMap = new HashMap<>();
        foodMap = new HashMap<>();
        gameModeMap = new HashMap<>();
    }



    public boolean rollback(Player player) throws IOException {
        // CHECK
        if(!inventoryMap.containsKey(player.getUniqueId())) return false;
        if(!healthMap.containsKey(player.getUniqueId())) return false;
        if(!foodMap.containsKey(player.getUniqueId())) return false;
        if(!gameModeMap.containsKey(player.getUniqueId())) return false;

        UUID uuid = player.getUniqueId();

        //INVENTORY
        String[] inv = inventoryMap.get(uuid);
        player.getInventory().setContents(itemStackArrayFromBase64(inv[0]));
        player.getInventory().setArmorContents(itemStackArrayFromBase64(inv[1]));

        player.setHealth(healthMap.get(uuid));

        player.setFoodLevel(foodMap.get(uuid));

        player.setGameMode(gameModeMap.get(uuid));


        return true;
    }

    public void save(Player player){
        UUID uuid = player.getUniqueId();
        inventoryMap.put(uuid,playerInventoryToBase64(player.getInventory()));
        healthMap.put(uuid,player.getHealth());
        foodMap.put(uuid,player.getFoodLevel());
        gameModeMap.put(uuid,player.getGameMode());
    }


    public String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        //get the main content part, this doesn't return the armor
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[] { content, armor };
    }

    public String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }


    public String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }


    public Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }


    public ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }




}

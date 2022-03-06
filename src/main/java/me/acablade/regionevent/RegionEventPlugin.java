package me.acablade.regionevent;

import me.acablade.regionevent.manager.ArenaManager;
import me.acablade.regionevent.manager.RollbackManager;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class RegionEventPlugin extends JavaPlugin {

    private RollbackManager rollbackManager;
    private ArenaManager arenaManager;


    @Override
    public void onEnable() {
        // Plugin startup login

        rollbackManager = new RollbackManager();

        String contents = "rO0ABXcEAAAAKXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw" +
                "dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi" +
                "bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl" +
                "Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA" +
                "AAN0AAI9PXQAAXZ0AAR0eXBldXEAfgAGAAAAA3QAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1T" +
                "dGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5n" +
                "Lk51bWJlcoaslR0LlOCLAgAAeHAAAAqqdAALRElBTU9ORF9BWEVzcQB+AABzcQB+AAN1cQB+AAYA" +
                "AAADcQB+AAhxAH4ACXEAfgAKdXEAfgAGAAAAA3EAfgAMc3EAfgANAAAKqnQADURJQU1PTkRfU1dP" +
                "UkRzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAKdXEAfgAGAAAAA3EAfgAMc3EA" +
                "fgANAAAKqnQAA0JPV3BwcHBwc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnQA" +
                "BmFtb3VudHVxAH4ABgAAAARxAH4ADHNxAH4ADQAACqp0AAVBUlJPV3NxAH4ADQAAAAhwcHBwcHBw" +
                "cHBwcHBwcHBwcHBwcHBwcHBwcHBzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAK" +
                "dXEAfgAGAAAAA3EAfgAMc3EAfgANAAAKqnQADURJQU1PTkRfQk9PVFNzcQB+AABzcQB+AAN1cQB+" +
                "AAYAAAADcQB+AAhxAH4ACXEAfgAKdXEAfgAGAAAAA3EAfgAMc3EAfgANAAAKqnQAEERJQU1PTkRf" +
                "TEVHR0lOR1NzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAKdXEAfgAGAAAAA3EA" +
                "fgAMc3EAfgANAAAKqnQAEkRJQU1PTkRfQ0hFU1RQTEFURXNxAH4AAHNxAH4AA3VxAH4ABgAAAANx" +
                "AH4ACHEAfgAJcQB+AAp1cQB+AAYAAAADcQB+AAxzcQB+AA0AAAqqdAAORElBTU9ORF9IRUxNRVRz" +
                "cQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAKdXEAfgAGAAAAA3EAfgAMc3EAfgAN" +
                "AAAKqnQABlNISUVMRA==";

        String armor = "rO0ABXcEAAAABHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw" +
                "dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi" +
                "bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl" +
                "Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA" +
                "AAN0AAI9PXQAAXZ0AAR0eXBldXEAfgAGAAAAA3QAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1T" +
                "dGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5n" +
                "Lk51bWJlcoaslR0LlOCLAgAAeHAAAAqqdAANRElBTU9ORF9CT09UU3NxAH4AAHNxAH4AA3VxAH4A" +
                "BgAAAANxAH4ACHEAfgAJcQB+AAp1cQB+AAYAAAADcQB+AAxzcQB+AA0AAAqqdAAQRElBTU9ORF9M" +
                "RUdHSU5HU3NxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+AAp1cQB+AAYAAAADcQB+" +
                "AAxzcQB+AA0AAAqqdAASRElBTU9ORF9DSEVTVFBMQVRFc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EA" +
                "fgAIcQB+AAlxAH4ACnVxAH4ABgAAAANxAH4ADHNxAH4ADQAACqp0AA5ESUFNT05EX0hFTE1FVA==";


        arenaManager = new ArenaManager(
                this,
                new String[]{contents,armor},
                new Location(Bukkit.getWorlds().get(0),-90,68,0),
                new Location(Bukkit.getWorlds().get(0),-80,75,10)
        );


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public RollbackManager getRollbackManager() {
        return rollbackManager;
    }
}

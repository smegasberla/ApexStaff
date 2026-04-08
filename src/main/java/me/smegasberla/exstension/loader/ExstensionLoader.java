package me.smegasberla.exstension.loader;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.exstension.Exstension;
import me.smegasberla.exstension.ExstensionManager;

public class ExstensionLoader {

    private static ExstensionManager instance;

    public static void init(ApexStaff plugin) {
        instance = new ExstensionManager(plugin);
    }

    public static void load(ApexStaff plugin) {
        if (instance == null) {
            init(plugin);
        }
        instance.loadExstensions();
    }

    public static void enableAll(ApexStaff plugin) {
        if (instance == null) {
            init(plugin);
        }
        instance.enableExstensions();
    }

    public static void disableAll() {
        if (instance != null) {
            instance.disableExstensions();
        }
    }

    public static ExstensionManager getInstance() {
        return instance;
    }

    public static ExstensionManager getManager() {
        return instance;
    }
}

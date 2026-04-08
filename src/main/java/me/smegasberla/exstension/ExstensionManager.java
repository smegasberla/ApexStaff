package me.smegasberla.exstension;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.exstension.api.ApexStaffExstension;
import me.smegasberla.exstension.loader.ExstensionLoader;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ExstensionManager {

    private final ApexStaff plugin;
    private final File exstensionsFolder;
    private final List<ExstensionWrapper> loadedExstensions;

    public ExstensionManager(ApexStaff plugin) {
        this.plugin = plugin;
        this.exstensionsFolder = new File(plugin.getDataFolder(), "exstensions");
        this.loadedExstensions = new ArrayList<>();

        if (!exstensionsFolder.exists()) {
            exstensionsFolder.mkdirs();
        }
    }

    public void loadExstensions() {
        File[] files = exstensionsFolder.listFiles((dir, name) -> name.endsWith(".jar"));

        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            try {
                ExstensionWrapper wrapper = loadExstension(file);
                if (wrapper != null) {
                    loadedExstensions.add(wrapper);
                    Bukkit.getLogger().info("[Exstension] Loaded: " + wrapper.getName() + " v" + wrapper.getVersion());
                }
            } catch (Exception e) {
                Bukkit.getLogger().severe("[Exstension] Failed to load " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    public void enableExstensions() {
        for (ExstensionWrapper wrapper : loadedExstensions) {
            try {
                wrapper.onEnable();
                Bukkit.getLogger().info("[Exstension] Enabled: " + wrapper.getName());
            } catch (Exception e) {
                Bukkit.getLogger().severe("[Exstension] Error enabling " + wrapper.getName() + ": " + e.getMessage());
            }
        }
    }

    public void disableExstensions() {
        for (ExstensionWrapper wrapper : loadedExstensions) {
            try {
                wrapper.onDisable();
                Bukkit.getLogger().info("[Exstension] Disabled: " + wrapper.getName());
            } catch (Exception e) {
                Bukkit.getLogger().severe("[Exstension] Error disabling " + wrapper.getName() + ": " + e.getMessage());
            }
        }
        loadedExstensions.clear();
    }

    private ExstensionWrapper loadExstension(File file) throws Exception {
        if (!file.exists()) {
            return null;
        }

        String mainClassPath = ExstensionWrapper.getMainClassFromJar(file);
        if (mainClassPath == null) {
            throw new Exception("No Main-Class found in manifest");
        }

        Class<?> mainClass = loadClass(file, mainClassPath);
        if (!ApexStaffExstension.class.isAssignableFrom(mainClass)) {
            throw new Exception("Main class does not implement ApexStaffExstension");
        }

        Constructor<?> constructor = mainClass.getDeclaredConstructor();
        ApexStaffExstension instance = (ApexStaffExstension) constructor.newInstance();

        String name = instance.getName();
        String version = instance.getVersion();

        ExstensionWrapper wrapper = new ExstensionWrapper(file, instance, name, version, mainClassPath, plugin);

        if (instance instanceof Exstension) {
            File extensionDataFolder = new File(exstensionsFolder, name);
            if (!extensionDataFolder.exists()) {
                extensionDataFolder.mkdirs();
            }
            ((Exstension) instance).init(plugin, extensionDataFolder, file);
        }

        return wrapper;
    }

    private Class<?> loadClass(File file, String className) throws Exception {
        URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
        return loader.loadClass(className);
    }

    public List<ExstensionWrapper> getLoadedExstensions() {
        return loadedExstensions;
    }

    public ExstensionWrapper getExstensionByName(String name) {
        for (ExstensionWrapper wrapper : loadedExstensions) {
            if (wrapper.getName().equals(name)) {
                return wrapper;
            }
        }
        return null;
    }

    public boolean isExstensionLoaded(String name) {
        return getExstensionByName(name) != null;
    }

    public File getExstensionsFolder() {
        return exstensionsFolder;
    }
}

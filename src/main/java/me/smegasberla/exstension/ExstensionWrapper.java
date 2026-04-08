package me.smegasberla.exstension;

import me.smegasberla.exstension.api.ApexStaffExstension;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ExstensionWrapper {

    private final File file;
    private final ApexStaffExstension instance;
    private final String name;
    private final String version;
    private final String mainClass;
    private final JavaPlugin plugin;

    public ExstensionWrapper(File file, ApexStaffExstension instance, String name, String version, String mainClass, JavaPlugin plugin) {
        this.file = file;
        this.instance = instance;
        this.name = name;
        this.version = version;
        this.mainClass = mainClass;
        this.plugin = plugin;
    }

    public File getFile() {
        return file;
    }

    public ApexStaffExstension getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMainClass() {
        return mainClass;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void onEnable() {
        instance.onEnable();
    }

    public void onDisable() {
        instance.onDisable();
    }

    public static String getMainClassFromJar(File jarFile) throws Exception {
        try (JarFile jar = new JarFile(jarFile)) {
            Manifest manifest = jar.getManifest();
            if (manifest != null) {
                return manifest.getMainAttributes().getValue("Main-Class");
            }
        }
        return null;
    }
}

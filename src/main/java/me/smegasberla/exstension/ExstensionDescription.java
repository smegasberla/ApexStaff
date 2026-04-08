package me.smegasberla.exstension;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class ExstensionDescription {

    private final String name;
    private final String version;
    private final String main;
    private final String description;
    private final String author;
    private final List<String> depend;
    private final List<String> softDepend;

    public ExstensionDescription(String name, String version, String main, String description, String author, List<String> depend, List<String> softDepend) {
        this.name = name;
        this.version = version;
        this.main = main;
        this.description = description;
        this.author = author;
        this.depend = depend;
        this.softDepend = softDepend;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getDepend() {
        return depend;
    }

    public List<String> getSoftDepend() {
        return softDepend;
    }

    public static ExstensionDescription load(File file) {
        return null;
    }
}

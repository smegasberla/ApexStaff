package me.smegasberla.exstension;

import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExstensionLogger {

    private final String prefix;
    private final Logger logger;

    public ExstensionLogger(String pluginName) {
        this.prefix = "[" + pluginName + "]";
        this.logger = Bukkit.getLogger();
    }

    public void info(String message) {
        logger.info(prefix + " " + message);
    }

    public void warning(String message) {
        logger.warning(prefix + " " + message);
    }

    public void severe(String message) {
        logger.severe(prefix + " " + message);
    }

    public void debug(String message) {
        logger.info(prefix + " [DEBUG] " + message);
    }
}

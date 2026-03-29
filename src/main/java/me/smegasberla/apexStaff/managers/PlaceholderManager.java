package me.smegasberla.apexStaff.managers;

import me.smegasberla.apexStaff.ApexStaff;
import org.bukkit.Bukkit;

public class PlaceholderManager {

    public String getOnlineStaffCount() {
        long count = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("apexstaff.admin"))
                .count();
        return String.valueOf(count);
    }

}

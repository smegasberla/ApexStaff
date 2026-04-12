package me.smegasberla.apexStaff.managers;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.utils.MessageUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static net.kyori.adventure.text.event.ClickEvent.clickEvent;

public class XRayCheckManager {

  private XRayCheckManager manager;

  private final ApexStaff plugin;

  public HashMap<UUID, Integer> totalBlocks = new HashMap<>();
  public HashMap<UUID, Integer> totalOres = new HashMap<>();

  public XRayCheckManager(ApexStaff plugin) {
    this.plugin = plugin;
  }

  public double calcutaleXRayPercentage(Player p) {

    FileConfiguration config = plugin.getConfig();

    UUID uuid = p.getUniqueId();

    int totalBlockNumber = totalBlocks.getOrDefault(uuid, 0);
    int totalOreNumber = totalOres.getOrDefault(uuid, 0);

    if (totalBlockNumber == 0) {
      return 0.0;
    }

    double percentage = ((double) totalOreNumber / totalBlockNumber) * 100;

    return percentage;
  }

  public XRayCheckManager getManager() {
    return manager;
  }

  public List<String> getMaterialList() {

    List<String> materialListCheck = plugin.getConfig().getStringList("xray.xray-blocks");

    return materialListCheck;

  }

  public boolean isSuspect(Player p) {
    UUID uuid = p.getUniqueId();
    int totalMined = totalBlocks.getOrDefault(uuid, 0);
    int minBlocks = plugin.getConfig().getInt("xray.amount-of-blocks");
    double percentage = calcutaleXRayPercentage(p);
    double threshold = plugin.getConfig().getDouble("xray.alert-threshold");

    return totalMined >= minBlocks && percentage >= threshold;
  }

  public void sendStaffAlert(Player target, Player p) {

    double ratio = calcutaleXRayPercentage(target);

    String suspicionLevel = null;

    if (isSuspect(target)) {

      suspicionLevel = plugin.getConfig().getString("xray-suspect.suspect");

    } else {

      suspicionLevel = plugin.getConfig().getString("xray-suspect.clear");

    }

    String riskLevel = null;

    double threshold = (double) plugin.getConfig().getDouble("xray.alert-threshold");
    double highRiskDouble = plugin.getConfig().getDouble("xray.high-risk");

    if (ratio < threshold) {

      riskLevel = plugin.getConfig().getString("xray-risk-level.low");

    } else if (ratio >= threshold && ratio < highRiskDouble) {

      riskLevel = plugin.getConfig().getString("xray-risk-level.moderate");

    } else {

      riskLevel = plugin.getConfig().getString("xray-risk-level.high");

    }

    String formattedRatio = String.format("%.2f%%", ratio);

    String message = MessageUtils.getMessage(plugin, "alert-message",
        "{target}", target.getName(),
        "{percentage_of_xray}", formattedRatio,
        "{suspect_status}", String.valueOf(suspicionLevel),
        "{risk_of_xray}", String.valueOf(riskLevel)

    );

    String tpButtonName = MessageUtils.getMessage(plugin, "xray.tp-button-text");

    if(tpButtonName == null) tpButtonName = "[CLICK TO TP]";

    Component text = Component.text(message);

    Component button = Component.text(tpButtonName)
                    .clickEvent(ClickEvent.runCommand("/tp " + target.getName()));

    Component finalMessage = text.append(button);

    p.sendMessage(finalMessage);

  }


}

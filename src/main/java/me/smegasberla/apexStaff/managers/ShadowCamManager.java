package me.smegasberla.apexStaff.managers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateViewPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class ShadowCamManager {

    public HashMap<UUID, Integer> cameraIDHash = new HashMap<>();
    public HashMap<UUID, Location> originalLocationMap = new HashMap<>();
    public HashSet<UUID> isASpectator = new HashSet<>();
    public HashMap<UUID, UUID> targetSpectator = new HashMap<>();


    public void startShadowCam(UUID spectatorUUID, UUID targetUUID) {

        Player spectator = Bukkit.getPlayer(spectatorUUID);
        Player target = Bukkit.getPlayer(targetUUID);

        User spectatorUser = PacketEvents.getAPI().getPlayerManager().getUser(spectator);

        originalLocationMap.put(spectatorUUID, spectator.getLocation());
        isASpectator.add(spectatorUUID);


        int targetCameraID = cameraIDHash.getOrDefault(targetUUID, target.getEntityId());


        int chunkX = target.getLocation().getBlockX() >> 4;
        int chunkZ = target.getLocation().getBlockZ() >> 4;


        WrapperPlayServerUpdateViewPosition position = new WrapperPlayServerUpdateViewPosition(chunkX, chunkZ);

        WrapperPlayServerCamera camera = new WrapperPlayServerCamera(targetCameraID);

        spectatorUser.sendPacket(position);
        spectatorUser.sendPacket(camera);

        targetSpectator.put(targetUUID, spectatorUUID);
    }

    public void stopShadowCam(UUID spectatorUUID) {

        Player spectator = Bukkit.getPlayer(spectatorUUID);

        User spectatorUser = PacketEvents.getAPI().getPlayerManager().getUser(spectator);

        isASpectator.remove(spectatorUUID);

        UUID targetUUID = null;

        for (Map.Entry<UUID, UUID> entry : targetSpectator.entrySet()) {
            if (entry.getValue().equals(spectatorUUID)) {
                targetUUID = entry.getKey();
                break;
            }
        }

        int spectatorEntityId = spectator.getEntityId();
        WrapperPlayServerCamera resetCamera = new WrapperPlayServerCamera(spectatorEntityId);

        spectatorUser.sendPacket(resetCamera);


        if (originalLocationMap.containsKey(spectatorUUID)) {
            Location originalLoc = originalLocationMap.remove(spectatorUUID);
            spectator.teleport(originalLoc);

        }


        cameraIDHash.remove(spectatorUUID);
        if(targetUUID == null) return;
        targetSpectator.remove(targetUUID);

    }

    public UUID getSpectator(UUID targetUUID) {

        return targetSpectator.get(targetUUID);

    }

}

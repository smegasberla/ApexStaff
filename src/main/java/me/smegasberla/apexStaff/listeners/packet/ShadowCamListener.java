package me.smegasberla.apexStaff.listeners.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAnimation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.ShadowCamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ShadowCamListener implements PacketListener {

    private final ApexStaff plugin;
    private final ShadowCamManager manager;

    public ShadowCamListener(ApexStaff plugin, ShadowCamManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {

        if (event.getPacketType() == PacketType.Play.Server.CAMERA) {
            WrapperPlayServerCamera cameraPacket = new WrapperPlayServerCamera(event);


            int cameraID = cameraPacket.getCameraId();

            Player p = event.getPlayer();

            UUID uuid = p.getUniqueId();

            manager.cameraIDHash.put(uuid, cameraID);

        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ANIMATION) {
            Player targetPlayer = event.getPlayer();
            UUID targetUUID = targetPlayer.getUniqueId();

            UUID spectatorUUID = manager.getSpectator(targetUUID);

            if (spectatorUUID != null) {
                Player spectator = Bukkit.getPlayer(spectatorUUID);

                if (spectator != null && spectator.isOnline()) {
                    User spectatorUser = PacketEvents.getAPI().getPlayerManager().getUser(spectator);

                    WrapperPlayClientAnimation swingPacket = new WrapperPlayClientAnimation(
                            com.github.retrooper.packetevents.protocol.player.InteractionHand.MAIN_HAND
                    );

                    spectatorUser.receivePacket(swingPacket);

                    WrapperPlayServerEntityAnimation serverSwing = new WrapperPlayServerEntityAnimation(
                            spectator.getEntityId(),
                            WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM
                    );
                    spectatorUser.sendPacket(serverSwing);
                }
            }
        }
    }
}


package me.smegasberla.apexStaff.listeners.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.ShadowCamManager;
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
}


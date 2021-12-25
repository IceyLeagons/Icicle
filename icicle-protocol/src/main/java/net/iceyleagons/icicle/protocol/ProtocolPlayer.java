package net.iceyleagons.icicle.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.iceyleagons.icicle.nms.player.WrappedCraftPlayer;
import net.iceyleagons.icicle.protocol.event.PacketContainer;
import net.iceyleagons.icicle.protocol.event.impl.OutBoundPacketEvent;
import net.iceyleagons.icicle.protocol.handler.InBoundPacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
@EqualsAndHashCode
public class ProtocolPlayer {

    private static final Map<Player, Channel> channels = new ConcurrentHashMap<>();

    private final Player player;
    private final WrappedCraftPlayer craftPlayer;
    private boolean injected;

    public ProtocolPlayer(Player player) {
        this.player = player;
        this.craftPlayer = new WrappedCraftPlayer(player);
        this.injected = false;
    }

    public void sendPacket(Object packet) {
        PacketContainer packetContainer = new PacketContainer(packet);
        OutBoundPacketEvent event = new OutBoundPacketEvent(this, packetContainer, true);

        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            craftPlayer.getHandle().getPlayerConnection().sendPacket(packet);
        }
    }

    public void inject() {
        if (injected) return;

        this.injected = true;
        Channel channel = craftPlayer.getHandle().getNetworkManager().getChannel();
        ChannelPipeline pipeline = channel.pipeline();

        channels.put(this.player, channel);
        pipeline.addBefore("decoder", "Icicle_InBoundPacketHandler", new InBoundPacketHandler(this));
    }

    public void uninject() {
        this.injected = false;

        channels.get(player).pipeline().remove("Icicle_InBoundPacketHandler");
        channels.remove(this.player);
    }
}

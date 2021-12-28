/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.iceyleagons.icicle.nms.NMSHandler;
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

    public ProtocolPlayer(Player player, NMSHandler handler) {
        this.player = player;
        this.craftPlayer = handler.wrap(player, WrappedCraftPlayer.class);
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
        Channel channel = craftPlayer.getHandle().getPlayerConnection().getNetworkManager().getChannel();
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

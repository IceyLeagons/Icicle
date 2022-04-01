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

import io.netty.channel.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.nms.NMSHandler;
import net.iceyleagons.icicle.nms.wrap.player.WrappedCraftPlayer;
import net.iceyleagons.icicle.protocol.events.PacketInEvent;
import net.iceyleagons.icicle.protocol.events.PacketOutEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
@EqualsAndHashCode
public class ProtocolPlayer {

    private final Player player;
    private final WrappedCraftPlayer craftPlayer;

    public ProtocolPlayer(Player player, NMSHandler handler) {
        this.player = player;
        this.craftPlayer = handler.wrapFromOrigin(player, WrappedCraftPlayer.class);
        injectPlayer();
    }

    private void injectPlayer() {
        ProtocolPlayer pp = this;
        if (!getPipeline().names().contains("icicle-handler"))
            getPipeline().addBefore("packet_handler", "icicle-handler", new ChannelDuplexHandler() {
                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    // event??
                    PacketOutEvent event = new PacketOutEvent(pp, msg);
                    if (msg != null)
                        if (!event.isCancelled())
                            super.write(ctx, msg, promise);
                }

                @Override
                public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
                    PacketInEvent event = new PacketInEvent(pp, msg);
                    // event?
                    super.channelRead(ctx, msg);
                }
            });
    }

    public void sendPacket(Object packet) {
        // probably works??
        // FIXME: tesztelj?
        getChannel().write(packet);
    }

    public Channel getChannel() {
        return this.craftPlayer.getHandle().getPlayerConnection().getNetworkManager().getChannel();
    }

    public ChannelPipeline getPipeline() {
        return this.getChannel().pipeline();
    }
}

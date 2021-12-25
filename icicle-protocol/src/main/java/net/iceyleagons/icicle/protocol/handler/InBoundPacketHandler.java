package net.iceyleagons.icicle.protocol.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.protocol.ProtocolPlayer;
import net.iceyleagons.icicle.protocol.event.PacketContainer;
import net.iceyleagons.icicle.protocol.event.impl.InBoundPacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@RequiredArgsConstructor
public class InBoundPacketHandler extends ChannelInboundHandlerAdapter {

    private final ProtocolPlayer player;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (canProceed(ctx, msg)) {
            ctx.fireChannelRead(msg);
        }
    }
    private boolean canProceed(ChannelHandlerContext ctx, Object msg) {
        if(!ByteBuf.class.isAssignableFrom(msg.getClass())) return true;
        ByteBuf data = ((ByteBuf) msg).copy();

        PacketContainer packetContainer = new PacketContainer(msg);
        InBoundPacketEvent event = new InBoundPacketEvent(this.player, packetContainer);
        Bukkit.getPluginManager().callEvent(event);

        return !event.isCancelled();
    }
}

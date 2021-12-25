package net.iceyleagons.icicle.protocol.event.impl;

import net.iceyleagons.icicle.protocol.ProtocolPlayer;
import net.iceyleagons.icicle.protocol.event.PacketContainer;
import net.iceyleagons.icicle.protocol.event.PacketEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
public class InBoundPacketEvent extends PacketEvent {

    private static final HandlerList handlerList = new HandlerList();

    public InBoundPacketEvent(ProtocolPlayer player, PacketContainer packetContainer) {
        super(player, packetContainer);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

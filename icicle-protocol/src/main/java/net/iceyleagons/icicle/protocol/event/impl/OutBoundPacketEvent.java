package net.iceyleagons.icicle.protocol.event.impl;

import lombok.Getter;
import net.iceyleagons.icicle.protocol.ProtocolPlayer;
import net.iceyleagons.icicle.protocol.event.PacketContainer;
import net.iceyleagons.icicle.protocol.event.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
public class OutBoundPacketEvent extends PacketEvent {

    private static final HandlerList handlerList = new HandlerList();
    private final boolean customPacket;

    public OutBoundPacketEvent(ProtocolPlayer player, PacketContainer packetContainer, boolean custom) {
        super(player, packetContainer);
        this.customPacket = custom;
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

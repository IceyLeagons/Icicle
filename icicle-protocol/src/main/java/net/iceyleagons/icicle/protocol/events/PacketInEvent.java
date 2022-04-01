package net.iceyleagons.icicle.protocol.events;

import net.iceyleagons.icicle.protocol.ProtocolPlayer;
import org.bukkit.entity.Player;

public class PacketInEvent extends PacketEvent {
    public PacketInEvent(ProtocolPlayer player, Object packet) {
        super(Direction.IN, player, packet);
    }
}

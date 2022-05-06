package net.iceyleagons.icicle.protocol.events;

import net.iceyleagons.icicle.protocol.ProtocolPlayer;

public class PacketInEvent extends PacketEvent {
    public PacketInEvent(ProtocolPlayer player, Object packet) {
        super(Direction.IN, player, packet);
    }
}

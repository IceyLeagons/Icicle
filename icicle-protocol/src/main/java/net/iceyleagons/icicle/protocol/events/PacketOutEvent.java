package net.iceyleagons.icicle.protocol.events;

import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.protocol.ProtocolPlayer;

@Getter
public class PacketOutEvent extends PacketEvent {
    @Setter
    private boolean cancelled = false;

    public PacketOutEvent(ProtocolPlayer player, Object packet) {
        super(Direction.OUT, player, packet);
    }
}

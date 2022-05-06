package net.iceyleagons.icicle.protocol.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.iceyleagons.icicle.protocol.ProtocolPlayer;

@Getter
@AllArgsConstructor
public class PacketEvent {
    private Direction direction;
    private ProtocolPlayer player;
    private Object packet;

    public enum Direction {
        IN, OUT
    }
}

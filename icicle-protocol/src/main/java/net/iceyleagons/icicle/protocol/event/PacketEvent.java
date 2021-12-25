package net.iceyleagons.icicle.protocol.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.protocol.ProtocolPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Data
@RequiredArgsConstructor
public abstract class PacketEvent extends Event implements Cancellable {

    private final ProtocolPlayer player;
    private final PacketContainer packetContainer;
    private boolean cancelled;

}

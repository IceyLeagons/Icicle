package net.iceyleagons.icicle.protocol.event;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Data
@RequiredArgsConstructor
public class PacketContainer {

    private final Object rawPacket;


}

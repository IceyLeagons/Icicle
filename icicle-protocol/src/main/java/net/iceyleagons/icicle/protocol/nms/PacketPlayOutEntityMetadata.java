package net.iceyleagons.icicle.protocol.nms;

import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.OriginGetter;
import net.iceyleagons.icicle.nms.annotations.constructor.Constructor;

@NMSWrap("network.protocol.game.PacketPlayOutEntityMetadata")
@Constructor(id = 0, paramTypeClasses = {"int", "{nms}network.syncher.DataWatcher", "boolean"})
public interface PacketPlayOutEntityMetadata {
    @OriginGetter
    Object getOrigin();
}

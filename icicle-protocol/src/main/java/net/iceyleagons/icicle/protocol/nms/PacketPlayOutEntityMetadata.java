package net.iceyleagons.icicle.protocol.nms;

import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.OriginGetter;
import net.iceyleagons.icicle.nms.annotations.constructor.Constructor;

@NMSWrap("network.protocol.game.PacketPlayOutEntityMetadata")
@Constructor(id = 0, paramTypeClasses = {"java.lang.Integer", "{nms}network.syncher.DataWatcher", "java.lang.Boolean"})
public interface PacketPlayOutEntityMetadata {
    @OriginGetter
    Object getOrigin();
}

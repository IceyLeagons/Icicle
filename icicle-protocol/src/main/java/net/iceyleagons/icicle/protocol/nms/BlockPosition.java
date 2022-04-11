package net.iceyleagons.icicle.protocol.nms;

import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.OriginGetter;
import net.iceyleagons.icicle.nms.annotations.constructor.Constructor;

@NMSWrap("core.BlockPosition")
@Constructor(id = 0, paramTypeClasses = {"int", "int", "int"})
public interface BlockPosition {
    @OriginGetter
    Object getOrigin();
}

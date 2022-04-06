package net.iceyleagons.icicle.protocol.nms;

import net.iceyleagons.icicle.nms.annotations.FieldWrapping;
import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.constructor.Constructor;

@NMSWrap("world.level.border.WorldBorder")
@Constructor(id = 0, paramTypeClasses = {})
public interface WorldBorder {

    @FieldWrapping(value = "world", isSetter = true)
    void setWorld(Object world);

}

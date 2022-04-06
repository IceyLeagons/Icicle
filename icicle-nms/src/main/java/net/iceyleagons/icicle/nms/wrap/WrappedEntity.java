package net.iceyleagons.icicle.nms.wrap;

import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.Wrapping;
import net.iceyleagons.icicle.nms.wrap.server.WrappedDataWatcher;

@NMSWrap("world.Entity")
public interface WrappedEntity {
    @Wrapping(value = "ai")
    WrappedDataWatcher getDataWatcher();

    @Wrapping(value = "j", paramTypes = {"java.lang.Integer"})
    void setFreezeTicks(int value);
}

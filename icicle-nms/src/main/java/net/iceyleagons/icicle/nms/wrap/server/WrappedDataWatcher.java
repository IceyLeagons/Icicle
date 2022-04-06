package net.iceyleagons.icicle.nms.wrap.server;

import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.OriginGetter;

@NMSWrap("network.syncher.DataWatcher")
public interface WrappedDataWatcher {
    @OriginGetter
    Object getOrigin();
}

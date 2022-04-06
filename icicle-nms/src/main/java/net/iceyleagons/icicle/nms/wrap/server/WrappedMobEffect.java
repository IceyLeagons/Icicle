package net.iceyleagons.icicle.nms.wrap.server;

import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.OriginGetter;

@NMSWrap("world.effect.MobEffect")
public interface WrappedMobEffect {
    @OriginGetter
    Object getOrigin();
}

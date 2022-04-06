package net.iceyleagons.icicle.nms.wrap.player;

import net.iceyleagons.icicle.nms.NMSHandler;
import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.OriginGetter;
import net.iceyleagons.icicle.nms.annotations.Wrapping;
import net.iceyleagons.icicle.nms.wrap.WrappedEntity;
import net.iceyleagons.icicle.nms.wrap.server.WrappedMobEffect;

@NMSWrap("world.entity.EntityLiving")
public interface WrappedEntityLiving {
    @Wrapping(value = "a", paramTypes = {"{nms}world.effect.MobEffect", "{nms}world.entity.Entity"})
    void addEffect(WrappedMobEffect effect, Object entity);

    default WrappedEntity getHandle(NMSHandler handler) {
        return handler.wrapFromOrigin(getOrigin(), WrappedEntity.class);
    }

    @OriginGetter
    Object getOrigin();
}

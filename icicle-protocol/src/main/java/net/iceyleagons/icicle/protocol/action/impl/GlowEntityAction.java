package net.iceyleagons.icicle.protocol.action.impl;

import lombok.SneakyThrows;
import lombok.val;
import net.iceyleagons.icicle.nms.wrap.player.WrappedEntityLiving;
import net.iceyleagons.icicle.nms.wrap.server.WrappedMobEffect;
import net.iceyleagons.icicle.protocol.action.Action;
import net.iceyleagons.icicle.protocol.action.Settings;
import net.iceyleagons.icicle.protocol.nms.PacketPlayOutEntityMetadata;
import net.iceyleagons.icicle.protocol.service.ProtocolService;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlowEntityAction extends Action {

    public GlowEntityAction(boolean async, Settings settings) {
        super(async, settings.getSettings());
    }

    public GlowEntityAction(Settings settings) {
        super(settings);
    }

    @SneakyThrows
    @Override
    protected void onExecute(ProtocolService protocolService) {
        Entity glow = super.getSettingAs(Settings.ENTITY, Entity.class);

        val craftClass = Class.forName("org.bukkit.craftbukkit.v1_18_R1.entity");
        val handle = craftClass.getDeclaredMethod("getHandle");

        val entityClass = Class.forName("net.minecraft.world.entity.EntityLiving");

        val utilClass = Class.forName("org.bukkit.craftbukkit.v1_18_R1.potion.CraftPotionUtil");
        val utilMethod = utilClass.getDeclaredMethod("fromBukkit", PotionEffect.class);
        val effect = protocolService.getNMSHandler().wrapFromOrigin(utilMethod.invoke(null, new PotionEffect(PotionEffectType.GLOWING, 20000, 1)), WrappedMobEffect.class);

        val entityLiving = protocolService.getNMSHandler().wrapFromOrigin(entityClass.cast(handle.invoke(craftClass.cast(glow))), WrappedEntityLiving.class);

        entityLiving.addEffect(effect, null);
        val entity = entityLiving.getHandle(protocolService.getNMSHandler());

        super.sendPacketToTarget(protocolService, protocolService.getNMSHandler().wrap(PacketPlayOutEntityMetadata.class, 0,
                glow.getEntityId(), entity.getDataWatcher().getOrigin(), true).getOrigin());
    }

    @Override
    protected void validateSettings() {
        assertTarget();
        assertEntity();
    }
}


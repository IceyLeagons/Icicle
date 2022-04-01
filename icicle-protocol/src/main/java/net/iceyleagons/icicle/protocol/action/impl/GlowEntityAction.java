package net.iceyleagons.icicle.protocol.action.impl;

import net.iceyleagons.icicle.protocol.action.Action;
import net.iceyleagons.icicle.protocol.action.Settings;
import net.iceyleagons.icicle.protocol.service.ProtocolService;
import org.bukkit.entity.Entity;

public class GlowEntityAction extends Action {

    public GlowEntityAction(boolean async, Settings settings) {
        super(async, settings.getSettings());
    }

    public GlowEntityAction(Settings settings) {
        super(settings);
    }

    @Override
    protected void onExecute(ProtocolService protocolService) {
        Entity glow = super.getSettingAs(Settings.ENTITY, Entity.class);
        super.sendPacketToTarget(protocolService, null);
    }

    @Override
    protected void validateSettings() {
        assertTarget();
        assertEntity();
    }
}


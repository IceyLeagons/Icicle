package net.iceyleagons.icicle.protocol.action.impl;

import net.iceyleagons.icicle.protocol.action.Action;
import net.iceyleagons.icicle.protocol.action.Settings;
import net.iceyleagons.icicle.protocol.service.ProtocolService;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;

public class WorldBorderAction extends Action {

    public WorldBorderAction(boolean async, Settings settings) {
        super(async, settings.getSettings());
    }

    public WorldBorderAction(Settings settings) {
        super(settings);
    }

    @Override
    protected void onExecute(ProtocolService protocolService) {
        Tuple<Integer, Integer> size = super.getSettingAs(Settings.SIZE, Tuple.class);
        Tuple<Integer, Integer> center = super.getSettingAs(Settings.CENTER, Tuple.class);
        super.sendPacketToTarget(protocolService, null);
    }

    @Override
    protected void validateSettings() {
        assertTarget();

        assertDefined(Settings.SIZE, "Size");
        assertOfType(Settings.SIZE, "Size", Tuple.class);
        assertDefined(Settings.CENTER, "Center");
        assertOfType(Settings.CENTER, "Center", Tuple.class);
    }
}


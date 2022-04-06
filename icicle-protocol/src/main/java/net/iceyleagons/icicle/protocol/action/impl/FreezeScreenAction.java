package net.iceyleagons.icicle.protocol.action.impl;

import lombok.SneakyThrows;
import lombok.val;
import net.iceyleagons.icicle.nms.wrap.player.WrappedCraftPlayer;
import net.iceyleagons.icicle.protocol.action.Action;
import net.iceyleagons.icicle.protocol.action.Settings;
import net.iceyleagons.icicle.protocol.nms.PacketPlayOutEntityMetadata;
import net.iceyleagons.icicle.protocol.service.ProtocolService;
import org.bukkit.entity.Player;

public class FreezeScreenAction extends Action {

    public FreezeScreenAction(boolean async, Settings settings) {
        super(async, settings.getSettings());
    }

    public FreezeScreenAction(Settings settings) {
        super(settings);
    }

    @SneakyThrows
    @Override
    protected void onExecute(ProtocolService protocolService) {
        Player target = super.getProtocolPlayerFromSettings(protocolService).getPlayer();
        val length = super.getSettingAs(Settings.LENGTH, Integer.class);
        val entity = protocolService.getNMSHandler().wrapFromOrigin(target, WrappedCraftPlayer.class).getHandle().getHandle(protocolService.getNMSHandler());

        if (length < 10)
            entity.setFreezeTicks(length);
        else entity.setFreezeTicks(139 + length);

        super.sendPacketToTarget(protocolService, protocolService.getNMSHandler().wrap(PacketPlayOutEntityMetadata.class,
                0, target.getEntityId(), entity.getDataWatcher().getOrigin(), true).getOrigin());
    }

    @Override
    protected void validateSettings() {
        assertDefined(Settings.LENGTH, "Length");
        assertOfType(Settings.LENGTH, "Length", Integer.class);
        assertTarget();
    }
}

package net.iceyleagons.icicle.protocol.nms;

import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.constructor.Constructor;
import net.iceyleagons.icicle.nms.annotations.version.alt.Alternative;

@NMSWrap("network.protocol.game.ClientboundInitializeBorderPacket")
@Constructor(id = 0, paramTypeClasses = {"{nms}world.level.border.WorldBorder"})
// @Alternative(version = "<1.17", value = "network.protocol.game.PacketPlayOutWorldBorder")
public interface PacketPlayOutWorldBorder {
}

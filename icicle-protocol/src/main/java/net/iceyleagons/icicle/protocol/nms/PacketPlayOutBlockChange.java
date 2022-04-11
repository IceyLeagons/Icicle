package net.iceyleagons.icicle.protocol.nms;

import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.OriginGetter;
import net.iceyleagons.icicle.nms.annotations.constructor.Constructor;

@NMSWrap("network.protocol.game.PacketPlayOutBlockChange")
@Constructor(id = 0, paramTypeClasses = {"{nms}core.BlockPosition", "{nms}world.level.block.state.IBlockData"})
public interface PacketPlayOutBlockChange {

    @OriginGetter
    Object getOrigin();

}

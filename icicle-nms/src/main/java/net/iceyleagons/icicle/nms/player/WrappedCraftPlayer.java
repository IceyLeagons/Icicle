package net.iceyleagons.icicle.nms.player;

import net.iceyleagons.icicle.nms.WrapType;
import net.iceyleagons.icicle.nms.WrapperClass;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
public class WrappedCraftPlayer extends WrapperClass {

    public WrappedCraftPlayer(Player origin) {
        super("entity.CraftPlayer", WrapType.CRAFT_BUKKIT, origin);
    }

    public Integer getProtocolVersion() {
        return super.executeMethod("getProtocolVersion", Integer.class);
    }

    public WrappedEntityPlayer getHandle() {
        return new WrappedEntityPlayer(super.executeMethod("getHandle", Object.class));
    }
}

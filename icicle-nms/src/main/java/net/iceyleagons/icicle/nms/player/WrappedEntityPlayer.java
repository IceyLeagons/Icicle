package net.iceyleagons.icicle.nms.player;

import net.iceyleagons.icicle.nms.WrapType;
import net.iceyleagons.icicle.nms.WrapperClass;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
public class WrappedEntityPlayer extends WrapperClass {

    public WrappedEntityPlayer(Object origin) {
        super("EntityPlayer", WrapType.NMS, origin);
    }

    public WrappedNetworkManager getNetworkManager() {
        return new WrappedNetworkManager(super.getFieldValue("networkManager", Object.class));
    }

    public Integer getPing() {
        return getFieldValue("ping", Integer.class);
    }

    public WrappedPlayerConnection getPlayerConnection() {
        return new WrappedPlayerConnection(getFieldValue("playerConnection", Object.class));
    }
}

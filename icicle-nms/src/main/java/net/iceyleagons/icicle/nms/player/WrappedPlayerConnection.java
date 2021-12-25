package net.iceyleagons.icicle.nms.player;

import net.iceyleagons.icicle.nms.utils.ClassHelper;
import net.iceyleagons.icicle.nms.WrapType;
import net.iceyleagons.icicle.nms.WrapperClass;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
public class WrappedPlayerConnection extends WrapperClass {

    public WrappedPlayerConnection(Object origin) {
        super("PlayerConnection", WrapType.NMS, origin);
        super.preDiscoverMethod("sendPacketA","sendPacket", ClassHelper.getNMSClass("Packet").getClazz()); // we specify here that we want to use the one with the param type of Packet
    }

    public Object getNetworkManager() {
        return super.getFieldValue("networkManager", Object.class);
    }

    public void sendPacket(Object packet) {
        super.executeMethod("sendPacketA", Void.class, packet);
    }
}

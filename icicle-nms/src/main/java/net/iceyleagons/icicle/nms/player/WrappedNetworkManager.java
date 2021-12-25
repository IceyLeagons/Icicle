package net.iceyleagons.icicle.nms.player;

import io.netty.channel.Channel;
import net.iceyleagons.icicle.nms.WrapType;
import net.iceyleagons.icicle.nms.WrapperClass;


/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
public class WrappedNetworkManager extends WrapperClass {

    public WrappedNetworkManager(Object origin) {
        super("NetworkManager", WrapType.NMS, origin);
    }

    public Channel getChannel() {
        return getFieldValue("channel", Channel.class);
    }
}

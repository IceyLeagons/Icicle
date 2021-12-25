package net.iceyleagons.icicle.protocol;

import net.iceyleagons.icicle.protocol.event.impl.InBoundPacketEvent;
import net.iceyleagons.icicle.protocol.event.impl.OutBoundPacketEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
public class Test implements Listener {

    @EventHandler
    public void onPacketIn(InBoundPacketEvent event) {
        ProtocolPlayer player = event.getPlayer();

        Object obj = event.getPacketContainer().getRawPacket();

        
    }
    
    @EventHandler
    public void onPacketOut(OutBoundPacketEvent event) {
        if (event.isCustomPacket()) {
            int ping = event.getPlayer().getCraftPlayer().getHandle().getPing();
            System.out.println("Just sent a custom packet to " + event.getPlayer().getPlayer().getName() + " their ping is: " + ping);
        }
    }
}

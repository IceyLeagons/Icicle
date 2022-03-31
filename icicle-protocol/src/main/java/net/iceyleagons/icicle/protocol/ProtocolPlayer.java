/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.nms.NMSHandler;
import net.iceyleagons.icicle.nms.wrap.player.WrappedCraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
@EqualsAndHashCode
public class ProtocolPlayer {

    private final Player player;
    private final WrappedCraftPlayer craftPlayer;

    @Setter
    private boolean injected = false;

    public ProtocolPlayer(Player player, NMSHandler handler) {
        this.player = player;
        this.craftPlayer = handler.wrapFromOrigin(player, WrappedCraftPlayer.class);
    }

    public void sendPacket(Object object) {
        // TODO
    }

    public Channel getChannel() {
        return this.craftPlayer.getHandle().getPlayerConnection().getNetworkManager().getChannel();
    }

    public ChannelPipeline getPipeline() {
        return this.getChannel().pipeline();
    }
}

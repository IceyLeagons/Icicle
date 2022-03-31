/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.protocol.service;

import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.service.Service;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import net.iceyleagons.icicle.nms.NMSHandler;
import net.iceyleagons.icicle.protocol.ProtocolPlayer;
import net.iceyleagons.icicle.protocol.action.Action;
import net.iceyleagons.icicle.protocol.action.Settings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 29, 2022
 */
@Service
public class ProtocolServiceImpl implements ProtocolService {

    private final Application application;
    private final JavaPlugin plugin;
    private final NMSHandler nms;

    private final Map<Player, ProtocolPlayer> protocolPlayers = new ConcurrentHashMap<>();

    public ProtocolServiceImpl(Application application, JavaPlugin plugin) {
        this.application = application;
        this.plugin = plugin;
        this.nms = NMSHandler.create(application);
    }

    public void executeAction(Action action) {
        action.execute(this);
    }

    @Override
    public ProtocolPlayer getProtocolPlayer(Player player) {
        return protocolPlayers.computeIfAbsent(player, p -> new ProtocolPlayer(p, this.nms));
    }

    @Override
    public ExecutionHandler getExecutionHandler() {
        return this.application.getExecutionHandler();
    }
}

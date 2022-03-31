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

package net.iceyleagons.icicle.protocol.action;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.protocol.ProtocolPlayer;
import net.iceyleagons.icicle.protocol.service.ProtocolService;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.lang.Internal;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 31, 2022
 */
@RequiredArgsConstructor
public abstract class Action {

    private final boolean async;
    private final Map<Integer, Object> settings;

    public Action(Settings settings) {
        this(false, settings.getSettings());
    }

    @Internal
    public void execute(ProtocolService protocolService) {
        validateSettings();

        if (async) {
            protocolService.getExecutionHandler().runAsync(() -> {
                onExecute(protocolService);
                return null;
            });
            return;
        }

        protocolService.getExecutionHandler().runSync(() -> {
            onExecute(protocolService);
            return null;
        });
    }

    protected abstract void onExecute(ProtocolService protocolService);
    protected abstract void validateSettings();

    protected ProtocolPlayer getProtocolPlayerFromSettings(ProtocolService service) {
        assertTarget();

        Object target = settings.get(Settings.TARGET);
        if (target instanceof Player) {
            return service.getProtocolPlayer((Player) target);
        }

        return (ProtocolPlayer) target;
    }

    protected <T> T getSettingAs(int id, Class<T> wantedType) {
        if (!settings.containsKey(id)) return null;

        T value = ReflectionUtils.castIfNecessary(wantedType, settings.get(id));
        if (value == null) {
            throw new IllegalArgumentException("Targeted setting (" + id + ") is not instance of " + wantedType);
        }

        return value;
    }

    protected void sendPacketToTarget(ProtocolService service, Object packet) {
        ProtocolPlayer player = getProtocolPlayerFromSettings(service);
        player.sendPacket(packet);
    }

    protected void assertTarget() {
        assertDefined(Settings.TARGET, "Target");
        assertOfType(Settings.TARGET, "Target", Player.class, ProtocolPlayer.class);
    }

    protected void assertDefined(int id, String name) {
        if (!settings.containsKey(id)) {
            throw new IllegalArgumentException(name + " must be defined!");
        }
    }

    protected void assertOfType(int id, String name, Class<?>... allowedTypes) {
        Object obj = settings.get(id);
        for (Class<?> allowedType : allowedTypes) {
            if (allowedType.isInstance(obj)) return;
        }

        throw new IllegalArgumentException(name + " must be an instance of: " + Arrays.toString(allowedTypes));
    }
}

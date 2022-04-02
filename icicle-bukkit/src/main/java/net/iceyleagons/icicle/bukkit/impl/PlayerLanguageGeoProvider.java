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

package net.iceyleagons.icicle.bukkit.impl;

import net.iceyleagons.icicle.core.translations.LanguageProvider;
import net.iceyleagons.icicle.core.translations.geo.IPDetailsGeoProvider;
import net.iceyleagons.icicle.core.translations.impl.ConstantLanguageProvider;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 16, 2022
 */
public class PlayerLanguageGeoProvider implements LanguageProvider {

    private final IPDetailsGeoProvider geoProvider = new IPDetailsGeoProvider();
    private final ConstantLanguageProvider fallBack;

    public PlayerLanguageGeoProvider() {
        this(new ConstantLanguageProvider("en"));
    }

    public PlayerLanguageGeoProvider(ConstantLanguageProvider fallBack) {
        this.fallBack = fallBack;
    }

    @Override
    public String getLanguage(Object player) {
        if (!(player instanceof Player)) {
            //throw new IllegalStateException("Parameter must be a type of org.bukkit.entity.Player!");
            return fallBack.getLanguage(player);
        }

        final InetSocketAddress addr = ((Player) player).getAddress();
        if (addr == null) return fallBack.getLanguage(null);

        final String ip = addr.getHostString();
        if (ip == null || ip.isEmpty()) { // Making sure, prob. unnecessary
            return fallBack.getLanguage(null);
        }

        final String result = geoProvider.getLanguage(ip);
        if (result == null || result.isEmpty()) {
            return fallBack.getLanguage(null);
        }

        return result;
    }
}

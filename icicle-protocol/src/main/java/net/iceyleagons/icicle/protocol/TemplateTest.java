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

package net.iceyleagons.icicle.protocol;

import net.iceyleagons.icicle.protocol.action.Settings;
import net.iceyleagons.icicle.protocol.action.impl.HideBlockAction;
import net.iceyleagons.icicle.protocol.service.ProtocolService;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 31, 2022
 */
public class TemplateTest {

    // This class is only here for us to test, whether the structure/flow feels nice

    public static void main(String[] args) {
        ProtocolService service = null;
        Location pos = null;
        Player player = null;

        HideBlockAction action = new HideBlockAction(true,
                Settings.create()
                        .with(Settings.TARGET, player)
                        .with(Settings.POSITION, pos));

        service.executeAction(action);
    }
}

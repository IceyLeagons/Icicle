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

package net.iceyleagons.icicle.protocol.action.impl;

import net.iceyleagons.icicle.protocol.action.Action;
import net.iceyleagons.icicle.protocol.action.Settings;
import net.iceyleagons.icicle.protocol.service.ProtocolService;
import org.bukkit.Location;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 31, 2022
 */
public class ReplaceBlockAction extends Action {

    public ReplaceBlockAction(boolean async, Settings settings) {
        super(async, settings.getSettings());
    }

    public ReplaceBlockAction(Settings settings) {
        super(settings);
    }

    @Override
    protected void onExecute(ProtocolService protocolService) {
        Location toHide = super.getSettingAs(Settings.POSITION, Location.class);
        super.sendPacketToTarget(protocolService, null);
    }

    @Override
    protected void validateSettings() {
        assertTarget();
        assertDefined(Settings.POSITION, "Position");
        assertOfType(Settings.POSITION, "Position", Location.class);
    }
}

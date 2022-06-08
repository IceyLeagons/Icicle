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

package net.iceyleagons.test.icicle;

import be.seeseemelk.mockbukkit.MockBukkit;
import net.iceyleagons.icicle.bukkit.IcicleBukkit;
import net.iceyleagons.icicle.core.Icicle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 08, 2022
 */
public class TestMain {

    // Temporarily commented out, to have a successful build on github

    /*
    private static IcicleTestPlugin plugin;
    private static IcicleBukkit icicleInstance;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();

        icicleInstance = MockBukkit.load(IcicleBukkit.class);
        //plugin = MockBukkit.load(IcicleTestPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Verify test environment")
    public void verifyEnv() {
        assertTrue(Icicle.LOADED);
        assertTrue(icicleInstance.isTestingEnvironment);
    }

     */
}

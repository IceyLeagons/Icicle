/*
 * MIT License
 *
 * Copyright (c) 2020 IceyLeagons (Tamás Tóth and Márton Kissik) and Contributors
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

package net.iceyleagons.icicle;

import net.iceyleagons.icicle.injection.InjectionHandlerRegistry;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class doesn't really have functions, only update checkers.
 * All methods/utils in Icicle should be used with static access or with the provided constructors!
 * Only purpose is version checking and providing methods in Classloader without shading.
 * Also contains metadata information!
 *
 * @author TOTHTOMI
 * @version 1.3.4
 * @since 1.0.0-SNAPSHOT
 */
public class Icicle extends JavaPlugin {

    @Override
    public void onLoad() {
        PreLoader.preload();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        new InjectionHandlerRegistry(this, "net.iceyleagons.icicle.testing").init();
    }

    /**
     * @return the version of the current icicle library. Our versions use the Semantic versioning.
     */
    public static String getVersion() {
        return "1.4.0-SNAPSHOT";
    }

    /**
     * Not yet implemented!
     *
     * @return will always return false atm
     */
    public static boolean checkForUpdates() {
        return false;
    }

    /**
     * This is optional to show us some love, by printing this out.
     * You'd ideally print this out to the console, or to the players.
     *
     * @return our copyright text
     */
    public static String getCopyrightText() {
        return "This project was built upon IceyLeagons' Icicle Library v" + getVersion() +
                " (Licensed under the terms of MIT License)";
    }

}

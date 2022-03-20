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

package net.iceyleagons.icicle.bukkit;

import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.annotations.meta.PlayerOnly;
import net.iceyleagons.icicle.commands.annotations.params.CommandSender;
import net.iceyleagons.icicle.commands.annotations.params.Optional;
import net.iceyleagons.icicle.commands.annotations.validators.Range;
import net.iceyleagons.icicle.core.annotations.execution.Async;
import net.iceyleagons.icicle.core.annotations.execution.Measure;
import net.iceyleagons.icicle.core.annotations.execution.Sync;
import net.iceyleagons.icicle.core.annotations.execution.extra.After;
import net.iceyleagons.icicle.core.annotations.execution.extra.Periodically;
import net.iceyleagons.icicle.utilities.Benchmark;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@CommandManager("test")
public class TestCommand {

    Plugin plugin = null;

    @PlayerOnly
    @Command(value = "four", returnsTranslationKey = false)
    public String asd2(@CommandSender Player player, String arg, @Optional Player argOpt, @Range(min = 18, value = 50 /* maximum */) int eletkor) {
        return "Executed2 sender: " + player.getName() + " | arg: " + arg + " opt: " + argOpt;
    }

    /*

    @Sync
    @After(delay = 500, unit = TimeUnit.MILLISECONDS) // 0.5 másodperces késleltetés
    @Periodically(period = 2, unit = TimeUnit.SECONDS) //Automatikus meghívás minden 2 másodpercben
    public void test() {
        System.out.println("Idegesítő 2 másodperces szöveg :)");
    }

    @Async
    public void testAsync() {
        System.out.println("Valami időigényes számítás kezdete");
        // [...]
    }

    @Async
    @After(delay = 2, unit = TimeUnit.SECONDS) //Metódus meghívás után 2 másodperccel fut csak le a kód
    public void testAsyncWithDelay() {
        System.out.println("Valami időigényes számítás kezdete 2 másodperc után");
        // [...]
    }

     */
}

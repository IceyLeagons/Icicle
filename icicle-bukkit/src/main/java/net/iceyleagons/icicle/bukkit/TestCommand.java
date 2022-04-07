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
import net.iceyleagons.icicle.commands.annotations.params.FlagOptional;
import net.iceyleagons.icicle.commands.annotations.validators.Range;
import net.iceyleagons.icicle.core.translations.TranslationService;
import net.iceyleagons.icicle.core.translations.impl.file.FileStringProvider;
import net.iceyleagons.icicle.core.translations.impl.file.separated.CSVLanguageFile;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@CommandManager(value = "test", printExceptionStackTrace = true)
public class TestCommand {

    public TestCommand(TranslationService translationService) {
        FileStringProvider provider = new FileStringProvider(
                new CSVLanguageFile(new File("strings-en.csv"), "en", true)
        );

        translationService.setTranslationStringProvider(provider);
    }

    @PlayerOnly
    @Command(value = "four")
    public String fourCommand(@CommandSender Player player, String asd, String[] rest) {
        return "Asd: " + asd + "| Rest: " + Arrays.toString(rest);
    }

    @PlayerOnly
    @Command(value = "range")
    public String forwardCommand(@Range(value = 50.0d, min = 10.0d) double ranged) {
        return "Érték: " + ranged;
    }

    @PlayerOnly
    @Command(value = "forward")
    public String forwardCommand(@CommandSender Player player, String required, @FlagOptional("s") String test) {
        return "Req: " + required + " | Test: " + test;
    }
}

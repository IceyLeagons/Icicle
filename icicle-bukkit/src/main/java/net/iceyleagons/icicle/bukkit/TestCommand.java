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
import net.iceyleagons.icicle.commands.annotations.validators.Range;
import net.iceyleagons.icicle.core.translations.TranslationService;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@CommandManager("test")
public class TestCommand {

    private final TranslationService translationService;

    public TestCommand(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PlayerOnly
    @Command(value = "four", returnsTranslationKey = false)
    public String fourCommand(@CommandSender Player player, int age) {
        return translationService.getTranslation(
                "test", "en", // ATM no translation provider so will use defaultValue
                "Your age is {age}, so you're {IF(GTEQ(age, 18), 'an adult', 'a child')}.",
                Map.of("age", String.valueOf(age)));
    }

    @PlayerOnly
    @Command(value = "range", returnsTranslationKey = false)
    public String forwardCommand(@Range(value = 50.0d, min = 10.0d) double ranged) {
        return "Value: " + ranged;
    }

    @PlayerOnly
    @Command(value = "forward", returnsTranslationKey = false)
    public void forwardCommand(@CommandSender Player player, Player forwardTo, String messageToForward) {
        forwardTo.sendMessage(
                translationService.getTranslation(
                        "test2", "en",
                        "{from} --> {to}: {msg}",
                        Map.of("from", player.getName(), "to", forwardTo.getName(), "msg", messageToForward)
                )
        );
    }
}

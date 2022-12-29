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

package net.iceyleagons.icicle.jda;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.iceyleagons.icicle.jda.commands.annotations.Command;
import net.iceyleagons.icicle.jda.commands.annotations.CommandContainer;
import net.iceyleagons.icicle.jda.commands.annotations.CommandParameter;
import net.iceyleagons.icicle.jda.commands.annotations.CommandSender;

import java.util.Optional;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 28, 2022
 */
@CommandContainer
public class TestCommand {

    @Command(
            name = "test",
            description = "This is to test the command system!"
    )
    public void test(
            @CommandParameter(name = "test", description = "This is only to test something") String param,
            @CommandParameter(name = "test2", description = "This is an optional integer") Optional<Integer> optInt,
            @CommandSender User user,
            SlashCommandInteractionEvent event) {
        event.deferReply(true).addContent("Got your message " + user.getName() + "! Param is: " + param + " | your optional value is " + (optInt.isEmpty() ? "not present" : optInt.get())).queue();
    }
}

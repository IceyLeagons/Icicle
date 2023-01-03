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

package net.iceyleagons.test.icicle.jda;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.CommandContainer;
import net.iceyleagons.icicle.commands.annotations.CommandParameter;
import net.iceyleagons.icicle.commands.annotations.CommandSender;
import net.iceyleagons.icicle.jda.InteractionUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 28, 2022
 */
@CommandContainer
public class TestCommand {

    @Command(
            name = "enumtest",
            description = "Testing enums"
    )
    public void enumTest(
            @CommandParameter(name = "enum", description = "test") Test test,
            @CommandParameter(name = "optional", description = "optional enum") Optional<Test> optTest,
            SlashCommandInteractionEvent event) {
        event.deferReply(true).addContent("You choose: " + test.name() + (optTest.isEmpty() ? " and u did not choose the optional one." : " and u choose opt: " + optTest.get().name())).queue();
    }

    @Command(
            name = "test2",
            description = "Testing for other parameters"
    )
    public void test(@CommandParameter(name = "member", description = "lol") Member member, SlashCommandInteractionEvent event) {
        event.deferReply(true).addContent("Your member of choice is: " + member.getId()).queue();
    }

    @Command(
            name = "modal",
            description = "Test"
    )
    public void modalTest(SlashCommandInteractionEvent event) {
        TextInput subject = TextInput.create("subject", "Text1", TextInputStyle.SHORT)
                .setPlaceholder("Subject of this ticket")
                .setMinLength(10)
                .setMaxLength(100) // or setRequiredRange(10, 100)
                .build();

        TextInput body = TextInput.create("body", "Reason", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Your concerns go here")
                .setMinLength(30)
                .setMaxLength(1000)
                .build();


        Modal modal = InteractionUtils.withCallback(Modal.create("modmail", "Modmail"), e -> {
                    e.deferReply(true).addContent("Got your answer! Subject: " + Objects.requireNonNull(e.getValue("subject")).getAsString() + " Body: " + Objects.requireNonNull(e.getValue("body")).getAsString()).queue();
                })
                .addActionRows(ActionRow.of(subject), ActionRow.of(body))
                .build();

        event.replyModal(modal).queue();
    }

    @Command(
            name = "selection",
            description = "Selection test"
    )
    public void testSelect(SlashCommandInteractionEvent event) {
        event.deferReply(true).setActionRow(
                InteractionUtils.withCallback(StringSelectMenu.create("my-id"), stringSelectInteractionEvent -> {
                            stringSelectInteractionEvent.deferReply(true).addContent("Got your reply: " + stringSelectInteractionEvent.getSelectedOptions().get(0).getValue()).queue();
                        })
                        .addOption("Option 1", "opt1")
                        .addOption("Option 2", "opt2")
                        .addOption("Option 3", "opt3")
                        .build()
        ).queue();
    }

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

    enum Test {
        OPTION1,
        SECOND,
        RANDOM
    }
}

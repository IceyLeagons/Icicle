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

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 29, 2022
 */
public class InteractionUtils {

    private static final Map<String, Consumer<StringSelectInteractionEvent>> selectMenus = new HashMap<>();
    private static final Map<String, Consumer<ModalInteractionEvent>> modals = new HashMap<>();
    private static final Map<String, Consumer<ButtonInteractionEvent>> buttons = new HashMap<>();

    public static Button withCallback(Button button, Consumer<ButtonInteractionEvent> consumer) {
        buttons.put(button.getId(), consumer);
        return button;
    }

    public static StringSelectMenu.Builder withCallback(StringSelectMenu.Builder builder, Consumer<StringSelectInteractionEvent> consumer) {
        selectMenus.put(builder.getId(), consumer);
        return builder;
    }

    public static Modal.Builder withCallback(Modal.Builder builder, Consumer<ModalInteractionEvent> consumer) {
        modals.put(builder.getId(), consumer);
        return builder;
    }


    public static ListenerAdapter getListener() {
        return new ListenerAdapter() {
            @Override
            public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
                if (selectMenus.containsKey(event.getSelectMenu().getId())) {
                    selectMenus.get(event.getSelectMenu().getId()).accept(event);
                }
            }

            @Override
            public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
                if (buttons.containsKey(event.getButton().getId())) {
                    buttons.get(event.getButton().getId()).accept(event);
                }
            }

            @Override
            public void onModalInteraction(@NotNull ModalInteractionEvent event) {
                if (modals.containsKey(event.getModalId())) {
                    modals.get(event.getModalId()).accept(event);
                }
            }
        };
    }
}

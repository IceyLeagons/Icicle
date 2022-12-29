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

package net.iceyleagons.icicle.jda.interactions;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 29, 2022
 */
public class SelectMenuUtils {

    private static final Map<String, Consumer<StringSelectInteractionEvent>> consumers = new HashMap<>();

    public static StringSelectMenu.Builder withCallback(StringSelectMenu.Builder builder, Consumer<StringSelectInteractionEvent> consumer) {
        consumers.put(builder.getId(), consumer);
        return builder;
    }

    public static ListenerAdapter getListener() {
        return new ListenerAdapter() {
            @Override
            public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
                if (consumers.containsKey(event.getSelectMenu().getId())) {
                    consumers.get(event.getSelectMenu().getId()).accept(event);
                }
            }
        };
    }
}

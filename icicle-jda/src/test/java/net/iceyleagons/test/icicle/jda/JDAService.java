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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.iceyleagons.icicle.core.annotations.bean.Bean;
import net.iceyleagons.icicle.core.annotations.config.Property;
import net.iceyleagons.icicle.core.annotations.service.Service;
import net.iceyleagons.icicle.jda.interactions.ModalUtils;
import net.iceyleagons.icicle.jda.interactions.SelectMenuUtils;
import net.iceyleagons.icicle.jda.interactions.commands.CommandListener;
import net.iceyleagons.icicle.jda.interactions.commands.CommandServiceImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 28, 2022
 */
@Service
public class JDAService {

    private final CommandServiceImpl commandService;
    private final String token;

    public JDAService(CommandServiceImpl commandService, @Property("bot.token") String token) {
        this.commandService = commandService;
        this.token = token;
    }

    @Bean
    public JDA jda() throws InterruptedException {
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new CommandListener(commandService), SelectMenuUtils.getListener(), ModalUtils.getListener())
                .build().awaitReady();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                commandService.registerToJda(jda);
            }
        }, 3000L);


        return jda;
    }

}

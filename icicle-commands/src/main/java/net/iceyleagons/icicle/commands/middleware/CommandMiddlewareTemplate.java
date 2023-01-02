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

package net.iceyleagons.icicle.commands.middleware;

import net.iceyleagons.icicle.commands.CommandManager;
import net.iceyleagons.icicle.core.translations.TranslationService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
public interface CommandMiddlewareTemplate {

    /**
     * @param commandManager      the {@link CommandManager}
     * @param commandManagerClass the commandManager's class
     * @param method              the method to-be-executed
     * @return true if the command can proceed, false if issues arise.
     * @throws Exception if anything bad happens
     */
    boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                      Method method, Object sender, TranslationService translationService) throws Exception;

    /**
     * @param commandManager      the {@link CommandManager}
     * @param commandManagerClass the commandManager's class
     * @param field               the field to-be-handled
     * @return true if the command can proceed, false if issues arise.
     * @throws Exception if anything bad happens
     */
    boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                      Field field, Object sender, TranslationService translationService) throws Exception;

}

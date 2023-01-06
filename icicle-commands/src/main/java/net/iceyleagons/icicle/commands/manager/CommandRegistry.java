/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.commands.manager;

import net.iceyleagons.icicle.commands.RegisteredCommand;
import net.iceyleagons.icicle.core.utils.Store;

/**
 * This class holds all the registered commands for the {@link CommandService}.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 * @see Store
 */
public class CommandRegistry extends Store<String, RegisteredCommand> {

    /**
     * Registers a new command into the registry.
     * If the command has registered aliases, then the store will contain them as a separate entry.
     *
     * @param registeredCommand the command to register
     */
    public void registerCommand(RegisteredCommand registeredCommand) {
        super.getElements().put(registeredCommand.getName(), registeredCommand);
        for (String alias : registeredCommand.getAliases()) {
            super.getElements().put(alias, registeredCommand);
        }
    }
}

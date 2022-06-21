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

package net.iceyleagons.icicle.commands;

import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import net.iceyleagons.icicle.commands.command.RegisteredCommand;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.lang.Internal;
import net.iceyleagons.icicle.utilities.lang.Utility;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 19, 2022
 */
@Utility
public final class Predictor {

    private Predictor() {
    }

    @Internal
    public static Optional<RegisteredCommand> predict(RegisteredCommandManager manager, String[] inputArgs) {
        final Double2ObjectRBTreeMap<RegisteredCommand> possibilities = new Double2ObjectRBTreeMap<>(); // Insertions > Searching

        for (Tuple<String, RegisteredCommand> command : manager.getCommandRegistry().getAllChildCommands(manager.getCommandManager().value())) {
            String cmd = command.getA();

            String[] args = new String[cmd.split(" ").length];
            System.arraycopy(inputArgs, 0, args, 0, Math.min(inputArgs.length, args.length));

            String cmdArgs = StringUtils.join(args, " ");
            possibilities.put((1D - (StringUtils.getLevenshteinDistance(cmdArgs, cmd) / (Math.max(cmdArgs.length(), cmd.length()) * 1D))) * 100D, command.getB());
        }

        return possibilities.size() > 0 ? Optional.ofNullable(possibilities.get(possibilities.lastDoubleKey())) : Optional.empty();
    }
}

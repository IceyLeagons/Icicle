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

package net.iceyleagons.icicle.commands.utils;

import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import net.iceyleagons.icicle.commands.Command;
import net.iceyleagons.icicle.commands.CommandManager;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.lang.Utility;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
@Utility
public class CommandPredictor {

    private CommandPredictor() {}

    public static Optional<Command> predictFromInput(CommandManager commandManager, String[] input) {
        final Double2ObjectRBTreeMap<Command> possibilities = new Double2ObjectRBTreeMap<>();
        final Set<Tuple<String, Command>> children = commandManager.getCommandRegistry().getAllChildCommands(commandManager.getRoot());

        for (final Tuple<String, Command> child : children) {
            final String name = child.getA();
            final Command cmd = child.getB();

            final String[] args = new String[name.split(" ").length];
            System.arraycopy(input, 0, args, 0, Math.min(input.length, args.length));

            possibilities.put(calculateDistance(args, name), cmd);
        }

        return possibilities.size() > 0 ? Optional.ofNullable(possibilities.get(possibilities.lastDoubleKey())) : Optional.empty();
    }

    private static double calculateDistance(String[] args, String expected) {
        final String joined = StringUtils.join(args, " ");
        final double levD = StringUtils.getLevenshteinDistance(joined, expected);
        final double max = Math.max(joined.length(), expected.length()) * 1D;

        return (1D - (levD / max)) * 100D;
    }

}

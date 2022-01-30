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

package net.iceyleagons.icicle.database;

import lombok.Getter;
import net.iceyleagons.icicle.database.filters.Filter;
import net.iceyleagons.icicle.utilities.generic.acessors.OneTypeAccessor;
import net.iceyleagons.icicle.utilities.lang.Experimental;

import java.util.Optional;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 26, 2022
 */
@Getter
@Experimental
public class Repository<T> extends OneTypeAccessor<T> {

    private final DatabaseConnector connector;
    private final Schema<T> schema;

    public Repository(DatabaseConnector connector) {
        this.schema = Schema.of(super.getATypeClass());
        this.connector = connector;
    }

    public void save(T toSave) {
    }

    public Set<T> findAll() {
        return null;
    }

    public Optional<T> findOne(Filter... filters) {
        return null;
    }

    public Set<T> findMany(Filter... filters) {
        if (filters.length == 0) return findAll();

        return null;
    }
}

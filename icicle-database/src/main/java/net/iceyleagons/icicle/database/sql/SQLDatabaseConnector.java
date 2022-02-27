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

package net.iceyleagons.icicle.database.sql;

import ca.krasnay.sqlbuilder.SelectBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.database.DatabaseConnector;
import net.iceyleagons.icicle.database.Schema;
import net.iceyleagons.icicle.database.filters.Filter;
import net.iceyleagons.icicle.database.filters.Filters;
import net.iceyleagons.icicle.oldserialization.serializers.impl.PreparedStatementFiller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 29, 2022
 */
@RequiredArgsConstructor
public abstract class SQLDatabaseConnector<T> implements DatabaseConnector<T> {

    private final Schema<T> schema;
    private final PreparedStatementFiller filler = new PreparedStatementFiller();

    @Override
    public boolean isSql() {
        return true;
    }

    @Override
    public void buildDatabase(Schema<T> schema) {

    }

    @Override
    @SneakyThrows
    public void save(T toSave) {
        if (!has(schema.getId(toSave))) {
            // INSERT INTO
        }

        // UPDATE
    }

    @Override
    public Set<T> findAll() {
        return null;
    }

    @Override
    @SneakyThrows
    public Optional<T> findOne(Filter... filters) {
        SelectBuilder sb = new SelectBuilder().column("*").from(schema.getRepoName());
        for (Filter filter : filters) {
            sb = sb.where(filter.sql(this.schema));
        }

        try (PreparedStatement ps = getConnection().prepareStatement(sb.toString())) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            //TODO indices
            return rs.first() ? Optional.ofNullable(filler.convertFromResultSet(rs, schema.getClazz(), null)) : Optional.empty();
        }
    }

    @Override
    public Set<T> findMany(Filter... filters) {
        return null;
    }

    public boolean has(Object id) throws SQLException {
        SelectBuilder sb = new SelectBuilder()
                .column(schema.getIdKey())
                .from(schema.getRepoName())
                .where(Filters.id(id).sql(this.schema));

        try (PreparedStatement ps = getConnection().prepareStatement(sb.toString() + " LIMIT 1")) {
            return ps.executeQuery().next();
        }
    }
}

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

package net.iceyleagons.icicle.serialization.serializers.impl;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.utilities.MapUtils;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 29, 2022
 */
public class PreparedStatementFiller extends JsonSerializer {

    public PreparedStatementFiller() {
        super(0);
    }

    @SneakyThrows
    public void fillPreparedStatement(PreparedStatement preparedStatement, Object object, Map<String, Integer> parameterIndices) {
        Map<String, Object> obj = super.toJsonObject(object).toMap(); // Very lazy way, could probably optimize this and leave out json

        for (Map.Entry<String, Object> stringObjectEntry : obj.entrySet()) {
            preparedStatement.setObject(parameterIndices.get(stringObjectEntry.getKey()), stringObjectEntry.getValue());
        }
    }

    @SneakyThrows
    public <T> T convertFromResultSet(ResultSet input, Class<T> wantedType, Map<String, Integer> parameterIndices) {
        JSONObject jsonObject = new JSONObject();
        Map<Integer, String> swapped = MapUtils.swapKeyValues(parameterIndices);

        for (int i = 0; i < swapped.size(); i++) {
            jsonObject.put(swapped.get(i), input.getObject(i));
        }

        return super.fromJsonObject(jsonObject, wantedType); // Very lazy way, could probably optimize this and leave out json
    }
}

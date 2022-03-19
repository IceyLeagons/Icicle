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
import net.iceyleagons.icicle.serialization.serializers.FileSerializer;
import org.bson.BsonBinaryReader;
import org.bson.BsonBinaryWriter;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.io.BasicOutputBuffer;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 14, 2022
 */
public class BsonSerializer implements FileSerializer {

    // TODO Native implementation and skip using JsonSerializer!
    //  Or probably make it so ObjectMapper can return a Map<String, Object> identical of what JSONObject would return -->
    //  no need to call JSONObject and anything could use it --> ease of use for us devs.
    //  (warning: identical message in YamlSerializer, remove it there as well, if this is implemented!)

    private final JsonSerializer serializer = new JsonSerializer();

    public Document serialize(Object object) {
        // IK This is jank, and should writer a proper serializer (and not use JsonSerializer),
        // but I got stuck with BsonArray, and am 2 lazy. Please don't hurt me - TOTHTOMI
        Document document = new Document();
        document.putAll(serializer.serialize(object).toMap());
        return document;
    }

    public <T> T deserialize(Document document, Class<T> type) {
        // IK This is jank, and should writer a proper deserializer (and not use JsonSerializer) - TOTHTOMI
        return serializer.deserializeFromString(document.toJson(), type);
    }

    public <T> T deserialize(BsonDocument document, Class<T> type) {
        // IK This is jank, and should writer a proper deserializer (and not use JsonSerializer) - TOTHTOMI
        return serializer.deserializeFromString(document.toJson(), type);
    }

    @Override
    @SneakyThrows
    public void serializeToPath(Object object, Path path) {
        try (BasicOutputBuffer out = new BasicOutputBuffer()) {
            try (BsonBinaryWriter writer = new BsonBinaryWriter(out)) {
                new BsonDocumentCodec().encode(writer, serialize(object).toBsonDocument(), EncoderContext.builder().isEncodingCollectibleDocument(true).build());
            }
            Files.write(path, out.toByteArray());
        }
    }

    @Override
    @SneakyThrows
    public <T> T deserializeFromPath(Path path, Class<T> type) {
        try (BsonBinaryReader reader = new BsonBinaryReader(ByteBuffer.wrap(Files.readAllBytes(path)))) {
            return deserialize(new BsonDocumentCodec().decode(reader, DecoderContext.builder().build()), type);
        }
    }
}

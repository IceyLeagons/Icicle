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

package net.iceyleagons.icicle.core.configuration.driver;

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Setter;
import net.iceyleagons.icicle.core.annotations.config.ConfigField;
import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Skeleton for all {@link net.iceyleagons.icicle.core.annotations.config.ConfigurationDriver}s, with some utility methods.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jul. 19, 2022
 */
@Setter
public abstract class ConfigDriver implements Configuration {

    protected AdvancedFile configFile;
    protected Object origin;
    protected Class<?> originType;

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> declaringType() {
        return this.originType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Map.Entry<String, Object>> getValues() {
        return getValues(getFields());
    }


    // TODO: This could be improved, see ConfigDelegator for information about it.

    /**
     * @return a fresh new instance of the driver.
     */
    protected abstract ConfigDriver newInstance();

    protected void reloadValues() {
        Map<String, Field> values = new ConcurrentHashMap<>();

        for (Field field : getFields()) {
            ConfigField configPath = field.getAnnotation(ConfigField.class);
            values.put(configPath.value(), field);
        }

        values.forEach((path, field) -> {
            if (get(path) != null) {
                ReflectionUtils.set(field, origin, get(path));
            }
        });
    }

    protected Set<Field> getFields() {
        return Arrays.stream(originType.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ConfigField.class)).collect(Collectors.toSet());
    }

    protected Set<Map.Entry<String, Object>> getValues(Set<Field> fields) {
        Map<String, Object> values = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

        for (Field field : fields) {
            ConfigField configPath = field.getAnnotation(ConfigField.class);
            values.put(configPath.value(), ReflectionUtils.get(field, origin, Object.class));
        }
        return values.entrySet();
    }
}

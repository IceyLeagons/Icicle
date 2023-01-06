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

package net.iceyleagons.icicle.core.configuration.driver.impl;

import lombok.Setter;
import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.annotations.config.ConfigComment;
import net.iceyleagons.icicle.core.annotations.config.ConfigField;
import net.iceyleagons.icicle.core.annotations.config.ConfigurationDriver;
import net.iceyleagons.icicle.core.configuration.CommentPlacement;
import net.iceyleagons.icicle.core.configuration.driver.ConfigDriver;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import net.iceyleagons.icicle.utilities.file.FileUtils;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.comments.format.YamlHeaderFormatter;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * Configuration driver for the YAML file type.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jul. 10, 2022
 */
@ConfigurationDriver({"yml", "yaml"})
public class YamlConfigurationDriver extends ConfigDriver {

    @Setter
    private String header = null;

    private YamlFile file;

    @Override
    public void afterConstruct(Config annotation, Path configRootFolder) {
        setConfigFile(new AdvancedFile(configRootFolder.resolve(annotation.value())));
        Asserts.isTrue(!configFile.isDirectory(), "Config file must not be a folder!");

        if (annotation.headerLines().length != 0) {
            setHeader(String.join("\n", annotation.headerLines()));
        }

        try {
            this.file = new YamlFile(configFile.asFile());
            if (!file.exists()) {
                this.file.createNewFile(true);
                if (this.header != null) {
                    FileUtils.appendFile(configFile.getPath(), this.header.split("\n"));
                }

                this.file.loadWithComments();
            }

            applyHeaderOptions(annotation);
            loadDefaultValues();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load Configuration described by " + originType.getName(), e);
        }
    }

    /**
     * Applies the configuration options to the header formatter
     *
     * @param ann the annotation declaring the options
     */
    private void applyHeaderOptions(Config ann) {
        final YamlHeaderFormatter formatter = file.options().headerFormatter();

        if (!ann.headerPrefixFirst().isEmpty()) formatter.prefixFirst(ann.headerPrefixFirst());
        if (!ann.headerCommentPrefix().isEmpty()) formatter.commentPrefix(ann.headerCommentPrefix());
        if (!ann.headerCommentSuffix().isEmpty()) formatter.commentSuffix(ann.headerCommentSuffix());
        if (!ann.headerSuffixLast().isEmpty()) formatter.suffixLast(ann.headerSuffixLast());
    }

    @Override
    public void addDefault(String path, Object object) {
        if (this.file != null) {
            this.file.addDefault(path, object);
        }
    }

    @Override
    public void save() {
        try {
            this.file.save();
        } catch (IOException e) {
            throw new IllegalStateException("Could not save config described by: " + originType.getName(), e);
        }
    }

    @Override
    public void reload() {
        try {
            this.file.loadWithComments();
            super.reloadValues();
        } catch (IOException e) {
            throw new IllegalStateException("Could not reload config described by: " + originType.getName(), e);
        }
    }

    @Override
    public Object get(String path) {
        return this.file != null ? this.file.get(path) : null;
    }

    /**
     * Saves the default values to disk, then reloads the config.
     */
    private void loadDefaultValues() {
        Set<Field> fields = getFields();
        Set<Map.Entry<String, Object>> values = getValues(fields);

        if (header != null) {
            file.setHeader(header);
        }

        values.forEach((entry) -> {
            String path = entry.getKey();
            Object value = entry.getValue();

            if (!file.contains(path)) {
                file.set(path, value);
            }
        });

        file.setCommentFormat(YamlCommentFormat.PRETTY);
        fields.stream().filter(f -> f.isAnnotationPresent(ConfigComment.class)).forEach(f -> {
            String path = f.getAnnotation(ConfigField.class).value();
            ConfigComment comment = f.getAnnotation(ConfigComment.class);

            file.setComment(path, comment.value(), toSimpleYamlType(comment.type()));
        });

        save();
        reload();
    }

    @Override
    protected ConfigDriver newInstance() {
        return new YamlConfigurationDriver();
    }

    // We need to include these here (rather than in super class), because of byte buddy.
    @Override
    public void setConfigFile(AdvancedFile configFile) {
        super.setConfigFile(configFile);
    }

    @Override
    public void setOrigin(Object origin) {
        super.setOrigin(origin);
    }

    @Override
    public void setDeclaringType(Class<?> declaringType) {
        super.setDeclaringType(declaringType);
    }

    private static CommentType toSimpleYamlType(CommentPlacement placement) {
        if (placement == CommentPlacement.SIDE) {
            return CommentType.SIDE;
        }

        return CommentType.BLOCK;
    }
}
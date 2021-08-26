package net.iceyleagons.icicle.core.configuration;

import lombok.Setter;
import net.iceyleagons.icicle.core.annotations.Internal;
import net.iceyleagons.icicle.core.annotations.config.ConfigComment;
import net.iceyleagons.icicle.core.annotations.config.ConfigField;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractConfiguration implements Configuration {

    @Setter
    private AdvancedFile configFile;

    @Setter
    private Object origin;

    @Setter
    private Class<?> originType;

    @Setter
    private String header = null;

    private YamlFile file;

    @Internal
    public void afterConstruct() {
        Asserts.notNull(configFile, "Config file must not be null!");
        Asserts.isTrue(configFile.isFolder(), "Config file must not be a folder!");

        try {
            this.file = new YamlFile(configFile.getFile());
            if (!file.exists()) {
                this.file.createNewFile(true);
                this.file.loadWithComments();
            }

            loadDefaultValues();
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Could not load Configuration described by " + originType.getName(), e);
        }
    }

    @Override
    public void addDefault(String path, Object object) {
        if (this.file != null) {
            this.file.addDefault(path, object);
        }

        save();
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
            reloadFromConfig();
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Could not reload config described by: " + originType.getName(), e);
        }
    }

    @Override
    public Object get(String path) {
        return this.file != null ? this.file.get(path) : null;
    }

    @Override
    public Set<Map.Entry<String, Object>> getValues() {
        return getValues(getFields());
    }

    @Override
    public Class<?> declaringType() {
        return this.originType;
    }

    private void loadDefaultValues() {
        Set<Field> fields = getFields();
        Set<Map.Entry<String, Object>> values = getValues(fields);

        if (header != null) file.options().header(header);

        values.forEach((entry) -> {
            String path = entry.getKey();
            Object value = entry.getValue();

            if (!file.contains(path)) {
                System.out.println("Setting \"" + path + "\" to " + value);
                file.set(path, value);
            }
        });

        fields.stream().filter(f -> f.isAnnotationPresent(ConfigComment.class)).forEach(f -> {
            String path = f.getAnnotation(ConfigField.class).value();
            ConfigComment comment = f.getAnnotation(ConfigComment.class);

            file.setComment(path, comment.value(), comment.type());
        });

        save();
        reload();
    }

    private void reloadFromConfig() throws IOException, InvalidConfigurationException {
        this.file.loadWithComments();
        Map<String, Field> values = new ConcurrentHashMap<>();

        for (Field field : getFields()) {
            ConfigField configPath = field.getAnnotation(ConfigField.class);
            values.put(configPath.value(), field);
        }

        values.forEach((path, field) -> {
            if (file.get(path) != null) {
                try {
                    field.set(origin, file.get(path));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Set<Field> getFields() {
        return Arrays.stream(originType.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ConfigField.class)).collect(Collectors.toSet());
    }

    private Set<Map.Entry<String, Object>> getValues(Set<Field> fields) {
        Map<String, Object> values = new ConcurrentHashMap<>();

        for (Field field : fields) {
            ConfigField configPath = field.getAnnotation(ConfigField.class);
            try {
                values.put(configPath.value(), field.get(origin));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values.entrySet();
    }
}

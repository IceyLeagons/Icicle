package net.iceyleagons.icicle.core.configuration;

import lombok.Setter;
import net.iceyleagons.icicle.core.annotations.Internal;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.util.Map;
import java.util.Set;

public interface Configuration {

    void addDefault(String path, Object object);
    void save();
    void reload();

    Object get(String path);
    Set<Map.Entry<String, Object>> getValues();

    Class<?> declaringType();

    void setConfigFile(AdvancedFile configFile);
    void setOrigin(Object origin);
    void setOriginType(Class<?> originType);
    void setHeader(String header);

    @Internal
    void afterConstruct();

}

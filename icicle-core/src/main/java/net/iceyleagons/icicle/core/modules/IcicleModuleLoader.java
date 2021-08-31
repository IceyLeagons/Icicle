package net.iceyleagons.icicle.core.modules;

import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class IcicleModuleLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(IcicleModuleLoader.class);

    private final AdvancedFile modulesFolder;

    //This map keeps track of all loaded modules' classloaders, so when a plugin is dependent on the module
    //its reflections can use that ClassLoader as well
    private final Map<String, ClassLoader> moduleClassLoaders = new ConcurrentHashMap<>();

    public IcicleModuleLoader(AdvancedFile folder) {
        Asserts.state(folder.isFolder(), "Folder must be a folder!");

        this.modulesFolder = folder;
        loadModulesInFolder();
    }

    public void downloadAndLoadDependencies(String... dependencies) {
        for (String dependency : dependencies) {
            if (!moduleClassLoaders.containsKey(dependency)) {
                //The module has not been loaded --> we need to reach out to API, so we can confirm it's valid, then download and load it!
                //TODO
            }
        }
    }

    public void loadModulesInFolder() {
        for (File file : Objects.requireNonNull(modulesFolder.getFile().listFiles())) {
            try {
                loadModule(file);
            } catch (MalformedURLException e) {
                LOGGER.warn("Could not load module from file {} due to {}", file.getPath(), e);
            }
        }
    }

    public void loadModule(File file) throws MalformedURLException {
        Asserts.notNull(file, "Module file must not be null!");
        Asserts.state(file.exists(), "Module file does not exist!");

        ModuleMetadata moduleMetadata = ModuleMetadata.fromFile(file);
        String name = file.getName(); //maybe replace it with moduleData.getName()???

        //TODO do stuff with module metadata

        ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
        moduleClassLoaders.put(name, classLoader);
    }
}

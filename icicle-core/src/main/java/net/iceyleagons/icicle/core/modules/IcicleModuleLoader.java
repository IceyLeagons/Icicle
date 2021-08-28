package net.iceyleagons.icicle.core.modules;

import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IcicleModuleLoader {

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
        //TODO
    }
}

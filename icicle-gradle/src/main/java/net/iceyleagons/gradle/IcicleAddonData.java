package net.iceyleagons.gradle;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class IcicleAddonData {
    private final List<String> dependencies = new ArrayList<>(1);
    private final List<String> runtimeDownloads = new ArrayList<>(1);
    @Setter
    private String name;
    @Setter
    private String dependencyNotation = "undefined";
    @Setter
    private String description = "No description provided.";
    @Setter
    private String version = "0.1-SNAPSHOT";
    @Setter
    private List<String> developers = new ArrayList<>(1);

}

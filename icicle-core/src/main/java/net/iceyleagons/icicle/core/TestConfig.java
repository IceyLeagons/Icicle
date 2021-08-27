package net.iceyleagons.icicle.core;

import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.annotations.config.ConfigField;
import net.iceyleagons.icicle.core.configuration.AbstractConfiguration;

@Config("test.yaml")
public class TestConfig extends AbstractConfiguration {

    @ConfigField("asd.test")
    public String test = "hello";

    @ConfigField("asd.test2")
    public String test2 = "hello2";

}

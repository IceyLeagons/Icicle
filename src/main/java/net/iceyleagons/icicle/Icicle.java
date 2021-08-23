package net.iceyleagons.icicle;

import org.reflections.Reflections;

public class Icicle {

    public static Reflections ICICLE_INTERNAL_REFLECTIONS = new Reflections("net.iceyleagons.icicle", Icicle.class.getClassLoader());

}

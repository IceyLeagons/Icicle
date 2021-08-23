package net.iceyleagons.icicle.core.exceptions;

public class CircularDependencyException extends Exception {

    public CircularDependencyException(String msg) {
        super("Bean dependency tree forms a circle:\n" + msg);
    }
}

package net.iceyleagons.icicle.core.exceptions;

/**
 * This exception is used when the dependency tree forms a circle.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @see net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver
 * @see net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingDependencyTreeResolver
 * @since Aug. 23, 2021
 */
public class CircularDependencyException extends Exception {

    /**
     * @param msg the formatted circle to print out
     */
    public CircularDependencyException(String msg) {
        super("Bean dependency tree forms a circle:\n" + msg);
    }
}

package net.iceyleagons.icicle.database;

import java.sql.Connection;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 26, 2021
 */
public interface DatabaseDriver {

    Connection connect(final String url) throws UnsupportedOperationException;

    boolean supportsNativeRollback();
}

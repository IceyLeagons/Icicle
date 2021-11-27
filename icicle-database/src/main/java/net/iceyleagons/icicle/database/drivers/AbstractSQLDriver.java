package net.iceyleagons.icicle.database.drivers;

import net.iceyleagons.icicle.database.DatabaseDriver;

import java.sql.Connection;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 27, 2021
 */
public abstract class AbstractSQLDriver implements DatabaseDriver {

    @Override
    public Connection connect(String url) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public boolean supportsNativeRollback() {
        return false;
    }
}

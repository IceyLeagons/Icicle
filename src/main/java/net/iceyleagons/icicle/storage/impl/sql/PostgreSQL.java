/*
 * MIT License
 *
 * Copyright (c) 2020 IceyLeagons (Tamás Tóth and Márton Kissik) and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.storage.impl.sql;

import net.iceyleagons.icicle.storage.StorageException;
import net.iceyleagons.icicle.storage.StorageType;
import net.iceyleagons.icicle.storage.impl.SQLDatabase;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * MySQL implementation of {@link SQLDatabase}
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since 1.3.0-SNAPSHOT"
 */
public class PostgreSQL extends SQLDatabase {

    private final String host;
    private final String databaseName;
    private final String username;
    private final String password;


    /**
     * @param host         db host
     * @param databaseName db name
     * @param username     username used for authentication
     * @param password     password used for authentication
     * @param logger       logger to use
     */
    public PostgreSQL(String host, String databaseName, String username, String password, Logger logger) {
        super(databaseName, StorageType.POSTGRE_SQL, logger);
        this.host = host;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    @Override
    protected boolean init() throws StorageException {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (Exception e) {
            throw new StorageException("Could not find Driver for PostgreSQL (org.postgresql.Driver)!");
        }
        return true;
    }

    @Override
    protected boolean openConnection() throws StorageException {
        if (super.connection != null && !closeConnection()) throw new StorageException("Could not close connection!");

        try {
            super.connection = DriverManager.getConnection(
                    String.format("jdbc:postgresql://%s/%s", host, databaseName), username, password);
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }

}

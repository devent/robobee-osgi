package com.anrisoftware.sscontrol.database.external;

/**
 * User access to database.
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
public interface DatabaseAccess {

    /**
     * Returns the database name to access.
     */
    String getDatabase();

}

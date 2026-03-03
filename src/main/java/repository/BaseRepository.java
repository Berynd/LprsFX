package repository;

import database.Database;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseRepository {
    private Connection cnx;

    protected Connection getCnx() throws SQLException {
        if (cnx == null || cnx.isClosed()) {
            cnx = Database.getConnexion();
        }
        return cnx;
    }
}

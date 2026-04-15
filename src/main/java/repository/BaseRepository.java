package repository;

import database.Database;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe abstraite parente de tous les repositories.
 *
 * Elle gère une connexion JDBC partagée pour toute la durée de vie du repository.
 * La connexion est créée à la demande (lazy) et réutilisée tant qu'elle reste ouverte.
 * Chaque sous-classe hérite de getCnx() et n'a pas à gérer la connexion elle-même.
 */
public abstract class BaseRepository {

    /** Connexion JDBC réutilisée entre les appels. */
    private Connection cnx;

    /**
     * Retourne la connexion active, ou en ouvre une nouvelle si elle est null ou fermée.
     *
     * @return une connexion JDBC valide
     * @throws SQLException si l'ouverture de la connexion échoue
     */
    protected Connection getCnx() throws SQLException {
        if (cnx == null || cnx.isClosed()) {
            cnx = Database.getConnexion();
        }
        return cnx;
    }
}

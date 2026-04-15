package database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe utilitaire de connexion à la base de données MySQL.
 *
 * Les paramètres de connexion (serveur, nom BDD, utilisateur, mot de passe)
 * sont lus depuis le fichier "database.properties" présent dans les ressources.
 * Cela évite d'avoir des credentials en dur dans le code source.
 */
public class Database {

    /** Paramètres chargés une seule fois au démarrage de l'application. */
    private static final Properties props = chargerProperties();

    /**
     * Charge le fichier database.properties depuis le classpath.
     * Si le fichier est introuvable, des valeurs par défaut sont utilisées.
     */
    private static Properties chargerProperties() {
        Properties p = new Properties();
        try (InputStream in = Database.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (in != null) {
                p.load(in);
            } else {
                System.err.println("Fichier database.properties introuvable, utilisation des valeurs par défaut.");
                p.setProperty("db.serveur", "localhost");
                p.setProperty("db.nom", "lprsfx");
                p.setProperty("db.utilisateur", "root");
                p.setProperty("db.motdepasse", "");
            }
        } catch (IOException e) {
            System.err.println("Erreur lecture database.properties : " + e.getMessage());
        }
        return p;
    }

    /** Construit l'URL JDBC à partir des propriétés chargées. */
    private static String getUrl() {
        return "jdbc:mysql://" + props.getProperty("db.serveur") + "/"
                + props.getProperty("db.nom") + "?serverTimezone=UTC";
    }

    /**
     * Ouvre et retourne une nouvelle connexion à la base de données.
     * Chaque appel crée une connexion distincte ; c'est le BaseRepository
     * qui la met en cache tant qu'elle reste ouverte.
     *
     * @return une connexion JDBC, ou null si la connexion échoue
     */
    public static Connection getConnexion() {
        try {
            return DriverManager.getConnection(
                    getUrl(),
                    props.getProperty("db.utilisateur"),
                    props.getProperty("db.motdepasse"));
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return null;
        }
    }
}

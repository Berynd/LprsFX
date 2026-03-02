package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Database {
    private static final String SERVEUR = "localhost";
    private static final String NOM_BDD = "lprsfx";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";
    private static String getUrl() {
        return "jdbc:mysql://" + SERVEUR + "/" + NOM_BDD + "?serverTimezone=UTC";
    }

    public static void main(String[] args) {
        Connection cnx = getConnexion();
        if (cnx != null) {
            System.out.println("Connection successful !");
        } else {
            System.out.println("Connection failed !");
        }
    }

    public static Connection getConnexion() {
        Connection cnx = null;
        try {
            cnx = DriverManager.getConnection(getUrl(), UTILISATEUR, MOT_DE_PASSE);
            System.out.println("Connection successful !");
        } catch (SQLException e) {
            System.out.println("Connection error : " + e.getMessage());
        }
        return cnx;
    }
}


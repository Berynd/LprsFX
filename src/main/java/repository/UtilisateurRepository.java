package repository;
import appli.StartApplication;
import model.Utilisateur;
import database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UtilisateurRepository {
    private Connection cnx;

    public UtilisateurRepository() {
        this.cnx = Database.getConnexion();
        System.out.println("UtilisateurRepository initialisé - Connexion: " + (cnx != null ? "OK" : "NULL"));
    }
    
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        System.out.println(">>> ajouterUtilisateur appelé");
        System.out.println("    Utilisateur: " + utilisateur);
        
        String sql = "INSERT INTO utilisateur (nom, prenom, email, mdp, role) VALUES (?, ?, ?, ?, ?)";
        
        try {
            // Vérifier la connexion
            if (cnx == null || cnx.isClosed()) {
                System.out.println("    Reconnexion à la base de données...");
                cnx = Database.getConnexion();
            }
            
            System.out.println("    Préparation de la requête SQL...");
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getmdp());
            stmt.setString(5, utilisateur.getRole());
            
            System.out.println("    Requête SQL: " + sql);
            System.out.println("    Paramètres:");
            System.out.println("      1. nom = " + utilisateur.getNom());
            System.out.println("      2. prenom = " + utilisateur.getPrenom());
            System.out.println("      3. email = " + utilisateur.getEmail());
            System.out.println("      4. mdp (longueur) = " + utilisateur.getmdp().length() + " caractères");
            System.out.println("      5. role = " + utilisateur.getRole());
            
            System.out.println("    Exécution de la requête...");
            int rowsAffected = stmt.executeUpdate();
            
            System.out.println("    Lignes affectées: " + rowsAffected);
            
            if (rowsAffected > 0) {
                System.out.println("✓ Utilisateur ajouté avec succès !");
            } else {
                System.err.println("✗ Aucune ligne n'a été insérée !");
            }
            
        } catch (SQLException e) {
            System.err.println("✗✗✗ ERREUR SQL lors de l'ajout de l'utilisateur ✗✗✗");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Code erreur SQL: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        }
    }
    
    public Utilisateur getUtilisateurParEmail(String email) {
        System.out.println(">>> getUtilisateurParEmail: " + email);
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getString("role")
                );
                System.out.println("✓ Utilisateur trouvé: " + u);
                return u;
            } else {
                System.out.println("✓ Aucun utilisateur avec cet email");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Utilisateur> getTousLesUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getString("role")
                );
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    public boolean supprimerUtilisateurParEmail(String email) {
        String sql = "DELETE FROM utilisateur WHERE email = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.executeUpdate();
            
            //Vérification
            String verif = "SELECT * FROM utilisateur WHERE email = ?";
            PreparedStatement stmtverif = cnx.prepareStatement(verif);
            stmtverif.setString(1, email);
            ResultSet rs = stmtverif.executeQuery();
            if (rs.next()) {
                System.out.println("Désolé échec : " + email);
            }
            else{
                System.out.println("Utilisateur supprimé avec succès !");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    public void mettreAJourUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?,email = ? WHERE id_utilisateur = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setInt(4, utilisateur.getIdUtilisateur());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet email.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }
}

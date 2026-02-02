package repository;

import model.Fournisseur;
import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournisseurRepository {
    
    private Connection cnx;

    public FournisseurRepository() {
        this.cnx = Database.getConnexion();
    }

    /**
     * Ajouter un nouveau fournisseur
     */
    public boolean ajouterFournisseur(Fournisseur fournisseur) {
        String sql = "INSERT INTO fournisseur (nom, contact) VALUES (?, ?)";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getContact());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Fournisseur ajouté avec succès !");
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du fournisseur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupérer un fournisseur par son ID
     */
    public Fournisseur getFournisseurParId(int id) {
        String sql = "SELECT * FROM fournisseur WHERE id_fournisseur = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Fournisseur(
                    rs.getInt("id_fournisseur"),
                    rs.getString("nom"),
                    rs.getString("contact")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du fournisseur : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupérer tous les fournisseurs
     */
    public List<Fournisseur> getTousLesFournisseurs() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur ORDER BY nom";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur(
                    rs.getInt("id_fournisseur"),
                    rs.getString("nom"),
                    rs.getString("contact")
                );
                fournisseurs.add(fournisseur);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournisseurs : " + e.getMessage());
            e.printStackTrace();
        }
        return fournisseurs;
    }

    /**
     * Mettre à jour un fournisseur
     */
    public boolean mettreAJourFournisseur(Fournisseur fournisseur) {
        String sql = "UPDATE fournisseur SET nom = ?, contact = ? WHERE id_fournisseur = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getContact());
            stmt.setInt(3, fournisseur.getIdFournisseur());
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Fournisseur mis à jour avec succès !");
                return true;
            } else {
                System.out.println("Aucun fournisseur trouvé avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du fournisseur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprimer un fournisseur par son ID
     */
    public boolean supprimerFournisseur(int id) {
        String sql = "DELETE FROM fournisseur WHERE id_fournisseur = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("Fournisseur supprimé avec succès !");
                return true;
            } else {
                System.out.println("Aucun fournisseur trouvé avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du fournisseur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rechercher des fournisseurs par nom
     */
    public List<Fournisseur> rechercherFournisseurParNom(String nom) {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur WHERE nom LIKE ? ORDER BY nom";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur(
                    rs.getInt("id_fournisseur"),
                    rs.getString("nom"),
                    rs.getString("contact")
                );
                fournisseurs.add(fournisseur);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de fournisseurs : " + e.getMessage());
            e.printStackTrace();
        }
        return fournisseurs;
    }
}

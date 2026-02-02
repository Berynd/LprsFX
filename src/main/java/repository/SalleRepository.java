package repository;

import model.Salle;
import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalleRepository {
    
    private Connection cnx;

    public SalleRepository() {
        this.cnx = Database.getConnexion();
    }

    /**
     * Ajouter une nouvelle salle
     */
    public boolean ajouterSalle(Salle salle) {
        String sql = "INSERT INTO salle (nom) VALUES (?)";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, salle.getNom());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Salle ajoutée avec succès !");
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la salle : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupérer une salle par son ID
     */
    public Salle getSalleParId(int id) {
        String sql = "SELECT * FROM salle WHERE id_salle = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Salle(
                    rs.getInt("id_salle"),
                    rs.getString("nom")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la salle : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupérer une salle par son nom
     */
    public Salle getSalleParNom(String nom) {
        String sql = "SELECT * FROM salle WHERE nom = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Salle(
                    rs.getInt("id_salle"),
                    rs.getString("nom")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la salle : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupérer toutes les salles
     */
    public List<Salle> getToutesLesSalles() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle ORDER BY nom";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Salle salle = new Salle(
                    rs.getInt("id_salle"),
                    rs.getString("nom")
                );
                salles.add(salle);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des salles : " + e.getMessage());
            e.printStackTrace();
        }
        return salles;
    }

    /**
     * Mettre à jour une salle
     */
    public boolean mettreAJourSalle(Salle salle) {
        String sql = "UPDATE salle SET nom = ? WHERE id_salle = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, salle.getNom());
            stmt.setInt(2, salle.getIdSalle());
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Salle mise à jour avec succès !");
                return true;
            } else {
                System.out.println("Aucune salle trouvée avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la salle : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprimer une salle par son ID
     */
    public boolean supprimerSalle(int id) {
        String sql = "DELETE FROM salle WHERE id_salle = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("Salle supprimée avec succès !");
                return true;
            } else {
                System.out.println("Aucune salle trouvée avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la salle : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rechercher des salles par nom (recherche partielle)
     */
    public List<Salle> rechercherSalleParNom(String nom) {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle WHERE nom LIKE ? ORDER BY nom";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Salle salle = new Salle(
                    rs.getInt("id_salle"),
                    rs.getString("nom")
                );
                salles.add(salle);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de salles : " + e.getMessage());
            e.printStackTrace();
        }
        return salles;
    }

    /**
     * Vérifier si une salle existe déjà (par nom)
     */
    public boolean salleExiste(String nom) {
        String sql = "SELECT COUNT(*) as count FROM salle WHERE nom = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence de la salle : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtenir le nombre total de salles
     */
    public int getNombreTotalSalles() {
        String sql = "SELECT COUNT(*) as count FROM salle";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des salles : " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}

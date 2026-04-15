package repository;

import model.Salle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository gérant la persistance des salles de l'établissement.
 * Les salles sont utilisées pour les rendez-vous (voir RdvRepository).
 */
public class SalleRepository extends BaseRepository {

    /** Insère une nouvelle salle. Retourne true si l'insertion a réussi. */
    public boolean ajouterSalle(Salle salle) {
        String sql = "INSERT INTO salle (nom) VALUES (?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, salle.getNom());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la salle : " + e.getMessage());
            return false;
        }
    }

    /** Retourne une salle par son id, ou null si introuvable. */
    public Salle getSalleParId(int id) {
        String sql = "SELECT * FROM salle WHERE id_salle = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return new Salle(rs.getInt("id_salle"), rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la salle : " + e.getMessage());
        }
        return null;
    }

    /** Retourne une salle par son nom exact, ou null si introuvable. */
    public Salle getSalleParNom(String nom) {
        String sql = "SELECT * FROM salle WHERE nom = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, nom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return new Salle(rs.getInt("id_salle"), rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la salle : " + e.getMessage());
        }
        return null;
    }

    /** Retourne toutes les salles triées par nom. */
    public List<Salle> getToutesLesSalles() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle ORDER BY nom";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                salles.add(new Salle(rs.getInt("id_salle"), rs.getString("nom")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des salles : " + e.getMessage());
        }
        return salles;
    }

    /** Renomme une salle existante. */
    public boolean mettreAJourSalle(Salle salle) {
        String sql = "UPDATE salle SET nom = ? WHERE id_salle = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, salle.getNom());
            stmt.setInt(2, salle.getIdSalle());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la salle : " + e.getMessage());
            return false;
        }
    }

    /** Supprime une salle par son id. */
    public boolean supprimerSalle(int id) {
        String sql = "DELETE FROM salle WHERE id_salle = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la salle : " + e.getMessage());
            return false;
        }
    }

    /** Recherche des salles dont le nom contient le terme donné. */
    public List<Salle> rechercherSalleParNom(String nom) {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle WHERE nom LIKE ? ORDER BY nom";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    salles.add(new Salle(rs.getInt("id_salle"), rs.getString("nom")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de salles : " + e.getMessage());
        }
        return salles;
    }

    /**
     * Vérifie si une salle avec ce nom existe déjà en base.
     * Utilisé pour éviter les doublons lors de la création.
     */
    public boolean salleExiste(String nom) {
        String sql = "SELECT COUNT(*) as count FROM salle WHERE nom = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, nom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence de la salle : " + e.getMessage());
        }
        return false;
    }

    /** Retourne le nombre total de salles en base. */
    public int getNombreTotalSalles() {
        String sql = "SELECT COUNT(*) as count FROM salle";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt("count");
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des salles : " + e.getMessage());
        }
        return 0;
    }
}

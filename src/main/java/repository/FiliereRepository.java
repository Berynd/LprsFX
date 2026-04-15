package repository;

import model.Filiere;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository gérant la persistance des filières de formation.
 * Les filières sont référencées par les dossiers d'inscription.
 */
public class FiliereRepository extends BaseRepository {

    /**
     * Insère une nouvelle filière et retourne l'id généré.
     *
     * @return l'id de la filière créée, ou -1 en cas d'erreur
     */
    public int ajouterFiliere(Filiere filiere) {
        String sql = "INSERT INTO filiere (nom) VALUES (?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, filiere.getNom());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout filière : " + e.getMessage());
        }
        return -1;
    }

    /** Retourne toutes les filières triées par nom. */
    public List<Filiere> getToutesLesFilieres() {
        List<Filiere> list = new ArrayList<>();
        String sql = "SELECT * FROM filiere ORDER BY nom";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Filiere f = new Filiere();
                f.setIdFiliere(rs.getInt("id_filiere"));
                f.setNom(rs.getString("nom"));
                list.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération filières : " + e.getMessage());
        }
        return list;
    }

    /** Retourne une filière par son id, ou null si introuvable. */
    public Filiere getFiliereById(int id) {
        String sql = "SELECT * FROM filiere WHERE id_filiere = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Filiere f = new Filiere();
                    f.setIdFiliere(rs.getInt("id_filiere"));
                    f.setNom(rs.getString("nom"));
                    return f;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération filière : " + e.getMessage());
        }
        return null;
    }

    /** Renomme une filière existante. */
    public boolean modifierFiliere(Filiere filiere) {
        String sql = "UPDATE filiere SET nom=? WHERE id_filiere=?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, filiere.getNom());
            stmt.setInt(2, filiere.getIdFiliere());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification filière : " + e.getMessage());
            return false;
        }
    }

    /** Supprime une filière par son id. */
    public boolean supprimerFiliere(int idFiliere) {
        String sql = "DELETE FROM filiere WHERE id_filiere = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idFiliere);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression filière : " + e.getMessage());
            return false;
        }
    }
}

package repository;

import model.Etudiant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository gérant la persistance des étudiants.
 *
 * Un étudiant est un candidat à l'inscription ; il n'est pas un utilisateur
 * de l'application (pas de compte, pas de mot de passe).
 */
public class EtudiantRepository extends BaseRepository {

    /**
     * Insère un nouvel étudiant et retourne l'id généré par la base.
     *
     * @return l'id du nouvel étudiant, ou -1 en cas d'erreur
     */
    public int ajouterEtudiant(Etudiant etudiant) {
        String sql = "INSERT INTO etudiant (nom, prenom, email, telephone, adresse, dernier_diplome) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getEmail());
            stmt.setString(4, etudiant.getTelephone());
            stmt.setString(5, etudiant.getAdresse());
            stmt.setString(6, etudiant.getDernierDiplome());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // Récupération de l'id auto-incrémenté généré par MySQL
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout étudiant : " + e.getMessage());
        }
        return -1;
    }

    /** Retourne tous les étudiants triés par nom puis prénom. */
    public List<Etudiant> getTousLesEtudiants() {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT * FROM etudiant ORDER BY nom, prenom";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération étudiants : " + e.getMessage());
        }
        return list;
    }

    /** Retourne un étudiant par son id, ou null si introuvable. */
    public Etudiant getEtudiantById(int id) {
        String sql = "SELECT * FROM etudiant WHERE id_etudiant = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération étudiant : " + e.getMessage());
        }
        return null;
    }

    /**
     * Recherche des étudiants par nom, prénom ou email (recherche partielle).
     * Utilisé par la barre de recherche de FicheEtudianteView.
     */
    public List<Etudiant> rechercherEtudiant(String terme) {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT * FROM etudiant WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? ORDER BY nom";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            String p = "%" + terme + "%";
            stmt.setString(1, p);
            stmt.setString(2, p);
            stmt.setString(3, p);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche étudiant : " + e.getMessage());
        }
        return list;
    }

    /** Met à jour toutes les informations d'un étudiant existant. */
    public boolean modifierEtudiant(Etudiant etudiant) {
        String sql = "UPDATE etudiant SET nom=?, prenom=?, email=?, telephone=?, adresse=?, dernier_diplome=? WHERE id_etudiant=?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getEmail());
            stmt.setString(4, etudiant.getTelephone());
            stmt.setString(5, etudiant.getAdresse());
            stmt.setString(6, etudiant.getDernierDiplome());
            stmt.setInt(7, etudiant.getIdEtudiant());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification étudiant : " + e.getMessage());
            return false;
        }
    }

    /** Supprime un étudiant par son id. */
    public boolean supprimerEtudiant(int idEtudiant) {
        String sql = "DELETE FROM etudiant WHERE id_etudiant = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression étudiant : " + e.getMessage());
            return false;
        }
    }

    /** Convertit une ligne ResultSet en objet Etudiant. */
    private Etudiant map(ResultSet rs) throws SQLException {
        return new Etudiant(
            rs.getInt("id_etudiant"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("email"),
            rs.getString("telephone"),
            rs.getString("adresse"),
            rs.getString("dernier_diplome")
        );
    }
}

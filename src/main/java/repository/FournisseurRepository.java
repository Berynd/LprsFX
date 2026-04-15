package repository;

import model.Fournisseur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository gérant la persistance des fournisseurs.
 * Un fournisseur est une entreprise auprès de laquelle on commande des fournitures.
 */
public class FournisseurRepository extends BaseRepository {

    /** Insère un nouveau fournisseur. Retourne true si l'insertion a réussi. */
    public boolean ajouterFournisseur(Fournisseur fournisseur) {
        String sql = "INSERT INTO fournisseur (nom, contact) VALUES (?, ?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getContact());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du fournisseur : " + e.getMessage());
            return false;
        }
    }

    /** Retourne un fournisseur par son id, ou null si introuvable. */
    public Fournisseur getFournisseurParId(int id) {
        String sql = "SELECT * FROM fournisseur WHERE id_fournisseur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Fournisseur(rs.getInt("id_fournisseur"),
                            rs.getString("nom"), rs.getString("contact"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du fournisseur : " + e.getMessage());
        }
        return null;
    }

    /** Retourne tous les fournisseurs triés par nom. */
    public List<Fournisseur> getTousLesFournisseurs() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur ORDER BY nom";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                fournisseurs.add(new Fournisseur(rs.getInt("id_fournisseur"),
                        rs.getString("nom"), rs.getString("contact")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournisseurs : " + e.getMessage());
        }
        return fournisseurs;
    }

    /** Met à jour le nom et le contact d'un fournisseur existant. */
    public boolean mettreAJourFournisseur(Fournisseur fournisseur) {
        String sql = "UPDATE fournisseur SET nom = ?, contact = ? WHERE id_fournisseur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getContact());
            stmt.setInt(3, fournisseur.getIdFournisseur());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du fournisseur : " + e.getMessage());
            return false;
        }
    }

    /** Supprime un fournisseur par son id. */
    public boolean supprimerFournisseur(int id) {
        String sql = "DELETE FROM fournisseur WHERE id_fournisseur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du fournisseur : " + e.getMessage());
            return false;
        }
    }

    /** Recherche des fournisseurs dont le nom contient le terme donné (recherche partielle). */
    public List<Fournisseur> rechercherFournisseurParNom(String nom) {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur WHERE nom LIKE ? ORDER BY nom";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fournisseurs.add(new Fournisseur(rs.getInt("id_fournisseur"),
                            rs.getString("nom"), rs.getString("contact")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de fournisseurs : " + e.getMessage());
        }
        return fournisseurs;
    }
}

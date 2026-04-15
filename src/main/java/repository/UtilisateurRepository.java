package repository;

import model.Utilisateur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository gérant la persistance des utilisateurs (personnel administratif).
 *
 * Les mots de passe sont toujours stockés hashés (BCrypt) ; ce repository
 * ne fait jamais de hachage lui-même, il délègue cette responsabilité aux controllers.
 */
public class UtilisateurRepository extends BaseRepository {

    /**
     * Insère un nouvel utilisateur en base.
     * Le mot de passe doit déjà être hashé avant d'appeler cette méthode.
     */
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, mdp, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMdp());
            stmt.setString(5, utilisateur.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    /**
     * Recherche un utilisateur par son adresse email.
     * Utilisé à la connexion pour récupérer l'objet complet (avec id et hash mdp).
     *
     * @return l'utilisateur trouvé, ou null si aucun compte n'existe avec cet email
     */
    public Utilisateur getUtilisateurParEmail(String email) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Important : on inclut l'id pour que SessionUtilisateur.getIdUtilisateur() fonctionne
                    return new Utilisateur(
                            rs.getInt("id_utilisateur"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("mdp"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    /** Retourne la liste complète des utilisateurs (pour l'administration). */
    public List<Utilisateur> getTousLesUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                utilisateurs.add(new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    /** Supprime un utilisateur par son email. Retourne true si la suppression a réussi. */
    public boolean supprimerUtilisateurParEmail(String email) {
        String sql = "DELETE FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    /** Recherche un utilisateur par son id. Retourne null si introuvable. */
    public Utilisateur getUtilisateurById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                            rs.getInt("id_utilisateur"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("mdp"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur par id : " + e.getMessage());
        }
        return null;
    }

    /** Met à jour le nom, prénom et email d'un utilisateur (sans toucher au mot de passe). */
    public void mettreAJourUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ? WHERE id_utilisateur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setInt(4, utilisateur.getIdUtilisateur());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    /**
     * Remplace le mot de passe d'un utilisateur.
     * Le nouveau mot de passe doit être hashé BCrypt avant d'appeler cette méthode.
     */
    public void changerMotDePasse(int idUtilisateur, String nouveauMdpHashe) {
        String sql = "UPDATE utilisateur SET mdp = ? WHERE id_utilisateur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, nouveauMdpHashe);
            stmt.setInt(2, idUtilisateur);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors du changement de mot de passe : " + e.getMessage());
        }
    }
}

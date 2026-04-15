package repository;

import model.Rdv;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository gérant la persistance des rendez-vous professeur/étudiant.
 *
 * Avant d'insérer un RDV, il faut toujours appeler isSalleDisponible()
 * pour s'assurer qu'aucun autre RDV n'occupe déjà ce créneau dans cette salle.
 */
public class RdvRepository extends BaseRepository {

    /**
     * Insère un nouveau rendez-vous et retourne l'id généré.
     *
     * @return l'id du nouveau RDV, ou -1 en cas d'erreur
     */
    public int ajouterRdv(Rdv rdv) {
        String sql = "INSERT INTO rdv (date, demi_journee, ref_etudiant, ref_professeur, ref_salle) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(rdv.getDate()));
            stmt.setString(2, rdv.getDemiJournee());
            stmt.setInt(3, rdv.getRefEtudiant());
            stmt.setInt(4, rdv.getRefProfesseur());
            stmt.setInt(5, rdv.getRefSalle());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout RDV : " + e.getMessage());
        }
        return -1;
    }

    /** Retourne tous les rendez-vous triés du plus récent au plus ancien. */
    public List<Rdv> getTousLesRdv() {
        List<Rdv> list = new ArrayList<>();
        String sql = "SELECT * FROM rdv ORDER BY date DESC";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Erreur récupération RDV : " + e.getMessage());
        }
        return list;
    }

    /** Retourne uniquement les RDV d'un professeur donné (pour la vue Professeur). */
    public List<Rdv> getRdvParProfesseur(int idProfesseur) {
        List<Rdv> list = new ArrayList<>();
        String sql = "SELECT * FROM rdv WHERE ref_professeur=? ORDER BY date DESC";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idProfesseur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération RDV professeur : " + e.getMessage());
        }
        return list;
    }

    /**
     * Vérifie qu'une salle est libre sur un créneau donné (date + demi-journée).
     * Une salle est disponible si aucun RDV existant ne correspond à ces trois critères.
     *
     * @return true si la salle est disponible, false si elle est déjà occupée
     */
    public boolean isSalleDisponible(int idSalle, LocalDate date, String demiJournee) {
        String sql = "SELECT COUNT(*) FROM rdv WHERE ref_salle=? AND date=? AND demi_journee=?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idSalle);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setString(3, demiJournee);
            try (ResultSet rs = stmt.executeQuery()) {
                // COUNT(*) == 0 → aucun conflit → salle disponible
                if (rs.next()) return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur vérification disponibilité salle : " + e.getMessage());
        }
        return false;
    }

    /** Met à jour toutes les informations d'un RDV existant. */
    public boolean modifierRdv(Rdv rdv) {
        String sql = "UPDATE rdv SET date=?, demi_journee=?, ref_etudiant=?, ref_professeur=?, ref_salle=? WHERE id_rdv=?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(rdv.getDate()));
            stmt.setString(2, rdv.getDemiJournee());
            stmt.setInt(3, rdv.getRefEtudiant());
            stmt.setInt(4, rdv.getRefProfesseur());
            stmt.setInt(5, rdv.getRefSalle());
            stmt.setInt(6, rdv.getIdRdv());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur modification RDV : " + e.getMessage());
            return false;
        }
    }

    /** Supprime un RDV par son id (annulation du rendez-vous). */
    public boolean supprimerRdv(int idRdv) {
        String sql = "DELETE FROM rdv WHERE id_rdv=?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idRdv);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression RDV : " + e.getMessage());
            return false;
        }
    }

    /** Convertit une ligne ResultSet en objet Rdv. */
    private Rdv map(ResultSet rs) throws SQLException {
        Rdv r = new Rdv();
        r.setIdRdv(rs.getInt("id_rdv"));
        Date date = rs.getDate("date");
        r.setDate(date != null ? date.toLocalDate() : null);
        r.setDemiJournee(rs.getString("demi_journee"));
        r.setRefEtudiant(rs.getInt("ref_etudiant"));
        r.setRefProfesseur(rs.getInt("ref_professeur"));
        r.setRefSalle(rs.getInt("ref_salle"));
        return r;
    }
}

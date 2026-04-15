package repository;

import model.Fourniture;
import model.FournitureDemandeFourniture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository gérant la table de liaison entre DemandeFourniture et Fourniture.
 *
 * Une demande peut contenir plusieurs fournitures avec des quantités différentes.
 * Ce repository gère cette relation N-N :
 *   demande_fourniture ←→ fournituredemandefourniture ←→ fourniture
 *
 * Utilisation typique :
 *   - Lors de la création d'une demande : ajouterFournitureADemande() pour chaque article
 *   - Lors de la validation : getFournituresDemande() pour connaître les articles à décroître
 *   - Lors de la suppression : supprimerToutesFournituresDemande() avant de supprimer la demande
 */
public class FournitureDemandeFournitureRepository extends BaseRepository {

    /** Repository utilisé pour enrichir les résultats avec les données de la fourniture. */
    private FournitureRepository fournitureRepo = new FournitureRepository();

    /**
     * Associe une fourniture à une demande avec sa quantité.
     * Appelé pour chaque article ajouté lors de la création d'une demande.
     */
    public boolean ajouterFournitureADemande(int refDemande, int refFourniture, int quantite) {
        String sql = "INSERT INTO fournituredemandefourniture (ref_demande_fourniture, ref_fourniture, quantite) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, refDemande);
            stmt.setInt(2, refFourniture);
            stmt.setInt(3, quantite);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la fourniture à la demande : " + e.getMessage());
            return false;
        }
    }

    /**
     * Retourne toutes les fournitures (avec quantités) d'une demande donnée.
     * Utilisé lors de la validation pour savoir quels stocks décrementer.
     */
    public List<FournitureDemandeFourniture> getFournituresDemande(int idDemande) {
        List<FournitureDemandeFourniture> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fournituredemandefourniture WHERE ref_demande_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idDemande);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int refFourniture = rs.getInt("ref_fourniture");
                    int quantite = rs.getInt("quantite");
                    // Chargement de l'objet Fourniture pour l'affichage (libellé, etc.)
                    Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                    resultats.add(new FournitureDemandeFourniture(idDemande, refFourniture, quantite, fourniture));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures de la demande : " + e.getMessage());
        }
        return resultats;
    }

    /** Retourne toutes les demandes qui concernent une fourniture donnée. */
    public List<FournitureDemandeFourniture> getDemandesPourFourniture(int idFourniture) {
        List<FournitureDemandeFourniture> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fournituredemandefourniture WHERE ref_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idFourniture);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int refDemande = rs.getInt("ref_demande_fourniture");
                    int quantite = rs.getInt("quantite");
                    Fourniture fourniture = fournitureRepo.getFournitureParId(idFourniture);
                    resultats.add(new FournitureDemandeFourniture(refDemande, idFourniture, quantite, fourniture));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des demandes : " + e.getMessage());
        }
        return resultats;
    }

    /** Retourne toutes les associations de la table de liaison. */
    public List<FournitureDemandeFourniture> getToutesLesAssociations() {
        List<FournitureDemandeFourniture> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fournituredemandefourniture";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int refDemande    = rs.getInt("ref_demande_fourniture");
                int refFourniture = rs.getInt("ref_fourniture");
                int quantite      = rs.getInt("quantite");
                Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                resultats.add(new FournitureDemandeFourniture(refDemande, refFourniture, quantite, fourniture));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des associations : " + e.getMessage());
        }
        return resultats;
    }

    /** Modifie la quantité d'une fourniture dans une demande. */
    public boolean mettreAJourQuantite(int refDemande, int refFourniture, int nouvelleQuantite) {
        String sql = "UPDATE fournituredemandefourniture SET quantite = ? WHERE ref_demande_fourniture = ? AND ref_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, refDemande);
            stmt.setInt(3, refFourniture);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la quantité : " + e.getMessage());
            return false;
        }
    }

    /** Retire une fourniture spécifique d'une demande. */
    public boolean supprimerFournitureDemande(int refDemande, int refFourniture) {
        String sql = "DELETE FROM fournituredemandefourniture WHERE ref_demande_fourniture = ? AND ref_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, refDemande);
            stmt.setInt(2, refFourniture);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime toutes les fournitures associées à une demande.
     * À appeler avant de supprimer la demande elle-même (contrainte de clé étrangère).
     */
    public boolean supprimerToutesFournituresDemande(int refDemande) {
        String sql = "DELETE FROM fournituredemandefourniture WHERE ref_demande_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, refDemande);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des fournitures : " + e.getMessage());
            return false;
        }
    }

    /** Retourne la quantité d'une fourniture dans une demande donnée. */
    public int getQuantite(int refDemande, int refFourniture) {
        String sql = "SELECT quantite FROM fournituredemandefourniture WHERE ref_demande_fourniture = ? AND ref_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, refDemande);
            stmt.setInt(2, refFourniture);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("quantite");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la quantité : " + e.getMessage());
        }
        return 0;
    }

    /** Retourne la quantité totale demandée pour une fourniture sur toutes les demandes. */
    public int getQuantiteTotaleDemandee(int idFourniture) {
        String sql = "SELECT SUM(quantite) as total FROM fournituredemandefourniture WHERE ref_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idFourniture);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul de la quantité totale : " + e.getMessage());
        }
        return 0;
    }
}

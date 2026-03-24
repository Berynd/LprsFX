package repository;

import model.Fourniture;
import model.FournitureDemandeFourniture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournitureDemandeFournitureRepository extends BaseRepository {

    private FournitureRepository fournitureRepo = new FournitureRepository();

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

    public List<FournitureDemandeFourniture> getFournituresDemande(int idDemande) {
        List<FournitureDemandeFourniture> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fournituredemandefourniture WHERE ref_demande_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idDemande);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int refFourniture = rs.getInt("ref_fourniture");
                    int quantite = rs.getInt("quantite");
                    Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                    resultats.add(new FournitureDemandeFourniture(idDemande, refFourniture, quantite, fourniture));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures de la demande : " + e.getMessage());
        }
        return resultats;
    }

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

    public List<FournitureDemandeFourniture> getToutesLesAssociations() {
        List<FournitureDemandeFourniture> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fournituredemandefourniture";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int refDemande = rs.getInt("ref_demande_fourniture");
                int refFourniture = rs.getInt("ref_fourniture");
                int quantite = rs.getInt("quantite");
                Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                resultats.add(new FournitureDemandeFourniture(refDemande, refFourniture, quantite, fourniture));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des associations : " + e.getMessage());
        }
        return resultats;
    }

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

    public int getQuantite(int refDemande, int refFourniture) {
        String sql = "SELECT quantite FROM fournituredemandefourniture WHERE ref_demande_fourniture = ? AND ref_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, refDemande);
            stmt.setInt(2, refFourniture);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantite");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la quantité : " + e.getMessage());
        }
        return 0;
    }

    public int getQuantiteTotaleDemandee(int idFourniture) {
        String sql = "SELECT SUM(quantite) as total FROM fournituredemandefourniture WHERE ref_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idFourniture);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul de la quantité totale : " + e.getMessage());
        }
        return 0;
    }
}

package repository;

import model.Fournisseur;
import model.Fourniture;
import model.FournitureFournisseur;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournitureFournisseurRepository extends BaseRepository {

    private FournitureRepository fournitureRepo = new FournitureRepository();
    private FournisseurRepository fournisseurRepo = new FournisseurRepository();

    public boolean ajouterAssociation(int refFourniture, int refFournisseur, BigDecimal prix) {
        String sql = "INSERT INTO fourniturefournisseur (ref_fourniture, ref_fournisseur, prix) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, refFourniture);
            stmt.setInt(2, refFournisseur);
            stmt.setBigDecimal(3, prix);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'association : " + e.getMessage());
            return false;
        }
    }

    public List<FournitureFournisseur> getFournisseursPourFourniture(int idFourniture) {
        List<FournitureFournisseur> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fourniturefournisseur WHERE ref_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idFourniture);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int refFournisseur = rs.getInt("ref_fournisseur");
                    BigDecimal prix = rs.getBigDecimal("prix");
                    Fourniture fourniture = fournitureRepo.getFournitureParId(idFourniture);
                    Fournisseur fournisseur = fournisseurRepo.getFournisseurParId(refFournisseur);
                    resultats.add(new FournitureFournisseur(idFourniture, refFournisseur, prix, fourniture, fournisseur));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournisseurs : " + e.getMessage());
        }
        return resultats;
    }

    public List<FournitureFournisseur> getFournituresPourFournisseur(int idFournisseur) {
        List<FournitureFournisseur> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fourniturefournisseur WHERE ref_fournisseur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idFournisseur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int refFourniture = rs.getInt("ref_fourniture");
                    BigDecimal prix = rs.getBigDecimal("prix");
                    Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                    Fournisseur fournisseur = fournisseurRepo.getFournisseurParId(idFournisseur);
                    resultats.add(new FournitureFournisseur(refFourniture, idFournisseur, prix, fourniture, fournisseur));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures : " + e.getMessage());
        }
        return resultats;
    }

    public List<FournitureFournisseur> getToutesLesAssociations() {
        List<FournitureFournisseur> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fourniturefournisseur";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int refFourniture = rs.getInt("ref_fourniture");
                int refFournisseur = rs.getInt("ref_fournisseur");
                BigDecimal prix = rs.getBigDecimal("prix");
                Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                Fournisseur fournisseur = fournisseurRepo.getFournisseurParId(refFournisseur);
                resultats.add(new FournitureFournisseur(refFourniture, refFournisseur, prix, fourniture, fournisseur));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des associations : " + e.getMessage());
        }
        return resultats;
    }

    public boolean mettreAJourPrix(int refFourniture, int refFournisseur, BigDecimal nouveauPrix) {
        String sql = "UPDATE fourniturefournisseur SET prix = ? WHERE ref_fourniture = ? AND ref_fournisseur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setBigDecimal(1, nouveauPrix);
            stmt.setInt(2, refFourniture);
            stmt.setInt(3, refFournisseur);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du prix : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerAssociation(int refFourniture, int refFournisseur) {
        String sql = "DELETE FROM fourniturefournisseur WHERE ref_fourniture = ? AND ref_fournisseur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, refFourniture);
            stmt.setInt(2, refFournisseur);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'association : " + e.getMessage());
            return false;
        }
    }

    public BigDecimal getPrix(int refFourniture, int refFournisseur) {
        String sql = "SELECT prix FROM fourniturefournisseur WHERE ref_fourniture = ? AND ref_fournisseur = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, refFourniture);
            stmt.setInt(2, refFournisseur);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("prix");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du prix : " + e.getMessage());
        }
        return null;
    }

    public FournitureFournisseur getMeilleurPrix(int idFourniture) {
        String sql = "SELECT * FROM fourniturefournisseur WHERE ref_fourniture = ? ORDER BY prix ASC LIMIT 1";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idFourniture);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int refFournisseur = rs.getInt("ref_fournisseur");
                    BigDecimal prix = rs.getBigDecimal("prix");
                    Fourniture fourniture = fournitureRepo.getFournitureParId(idFourniture);
                    Fournisseur fournisseur = fournisseurRepo.getFournisseurParId(refFournisseur);
                    return new FournitureFournisseur(idFourniture, refFournisseur, prix, fourniture, fournisseur);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du meilleur prix : " + e.getMessage());
        }
        return null;
    }
}

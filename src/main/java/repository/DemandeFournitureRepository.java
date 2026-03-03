package repository;

import database.Database;
import model.DemandeFourniture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeFournitureRepository {
    private Connection connection;

    public DemandeFournitureRepository() {
        connection = Database.getConnexion();
    }

    public int ajouterDemande(DemandeFourniture demande) {
        String sql = "INSERT INTO demande_fourniture (date, statut, raison, ref_professeur) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, demande.getDate());
            stmt.setString(2, demande.getStatut());
            stmt.setString(3, demande.getRaison());
            stmt.setInt(4, demande.getRefProfesseur());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur ajout demande : " + e.getMessage());
        }
        return -1;
    }

    public List<DemandeFourniture> getToutesLesDemandes() {
        List<DemandeFourniture> list = new ArrayList<>();
        String sql = "SELECT * FROM demande_fourniture ORDER BY date DESC";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Erreur récupération demandes : " + e.getMessage());
        }
        return list;
    }

    public List<DemandeFourniture> getDemandesParProfesseur(int idProfesseur) {
        List<DemandeFourniture> list = new ArrayList<>();
        String sql = "SELECT * FROM demande_fourniture WHERE ref_professeur=? ORDER BY date DESC";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idProfesseur);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Erreur récupération demandes professeur : " + e.getMessage());
        }
        return list;
    }

    public DemandeFourniture getDemandeById(int id) {
        String sql = "SELECT * FROM demande_fourniture WHERE id_demande_fourniture=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.out.println("Erreur récupération demande : " + e.getMessage());
        }
        return null;
    }

    public boolean validerDemande(int idDemande) {
        String sql = "UPDATE demande_fourniture SET statut='Validé' WHERE id_demande_fourniture=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idDemande);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur validation demande : " + e.getMessage());
            return false;
        }
    }

    public boolean refuserDemande(int idDemande, String justification) {
        String sql = "UPDATE demande_fourniture SET statut='Refusé', justification_refus=? WHERE id_demande_fourniture=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, justification);
            stmt.setInt(2, idDemande);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur refus demande : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerDemande(int idDemande) {
        String sql = "DELETE FROM demande_fourniture WHERE id_demande_fourniture=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idDemande);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression demande : " + e.getMessage());
            return false;
        }
    }

    public int countByStatut(String statut) {
        String sql = "SELECT COUNT(*) FROM demande_fourniture WHERE statut = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Erreur count demandes : " + e.getMessage());
        }
        return 0;
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM demande_fourniture";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Erreur count total demandes : " + e.getMessage());
        }
        return 0;
    }

    private DemandeFourniture map(ResultSet rs) throws SQLException {
        DemandeFourniture d = new DemandeFourniture();
        d.setIdDemandeFourniture(rs.getInt("id_demande_fourniture"));
        d.setDate(rs.getString("date"));
        d.setStatut(rs.getString("statut"));
        d.setRaison(rs.getString("raison"));
        d.setJustificationRefus(rs.getString("justification_refus"));
        d.setRefProfesseur(rs.getInt("ref_professeur"));
        return d;
    }
}

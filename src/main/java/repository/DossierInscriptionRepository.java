package repository;

import database.Database;
import model.DossierInscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DossierInscriptionRepository {
    private Connection connection;

    public DossierInscriptionRepository() {
        connection = Database.getConnexion();
    }

    public int ajouterDossierInscription(DossierInscription d) {
        String sql = "INSERT INTO dossier_inscription (date_creation, motivation, statut, ref_filiere, ref_etudiant, ref_secretaire) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, d.getDateCreation());
            stmt.setString(2, d.getMotivation());
            stmt.setString(3, d.getStatut());
            stmt.setInt(4, d.getRefFiliere());
            stmt.setInt(5, d.getRefEtudiant());
            stmt.setInt(6, d.getRefSecretaire());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur ajout dossier : " + e.getMessage());
        }
        return -1;
    }

    public List<DossierInscription> getTousDossiers() {
        List<DossierInscription> list = new ArrayList<>();
        String sql = "SELECT * FROM dossier_inscription ORDER BY date_creation DESC";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Erreur récupération dossiers : " + e.getMessage());
        }
        return list;
    }

    public List<DossierInscription> getDossiersParEtudiant(int idEtudiant) {
        List<DossierInscription> list = new ArrayList<>();
        String sql = "SELECT * FROM dossier_inscription WHERE ref_etudiant=? ORDER BY date_creation DESC";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Erreur récupération dossiers étudiant : " + e.getMessage());
        }
        return list;
    }

    public DossierInscription getDossierById(int id) {
        String sql = "SELECT * FROM dossier_inscription WHERE id_dossier_inscription=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.out.println("Erreur récupération dossier : " + e.getMessage());
        }
        return null;
    }

    public boolean changerStatut(int idDossier, String statut) {
        String sql = "UPDATE dossier_inscription SET statut=? WHERE id_dossier_inscription=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, statut);
            stmt.setInt(2, idDossier);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur changement statut dossier : " + e.getMessage());
            return false;
        }
    }

    public boolean modifierDossierInscription(DossierInscription d) {
        String sql = "UPDATE dossier_inscription SET date_creation=?, motivation=?, statut=?, ref_filiere=?, ref_etudiant=?, ref_secretaire=? WHERE id_dossier_inscription=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, d.getDateCreation());
            stmt.setString(2, d.getMotivation());
            stmt.setString(3, d.getStatut());
            stmt.setInt(4, d.getRefFiliere());
            stmt.setInt(5, d.getRefEtudiant());
            stmt.setInt(6, d.getRefSecretaire());
            stmt.setInt(7, d.getIdDossierInscription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur modification dossier : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerDossierInscription(int idDossier) {
        String sql = "DELETE FROM dossier_inscription WHERE id_dossier_inscription=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idDossier);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression dossier : " + e.getMessage());
            return false;
        }
    }

    private DossierInscription map(ResultSet rs) throws SQLException {
        DossierInscription d = new DossierInscription(rs.getInt("id_dossier_inscription"));
        d.setDateCreation(rs.getString("date_creation"));
        d.setMotivation(rs.getString("motivation"));
        d.setStatut(rs.getString("statut"));
        d.setRefFiliere(rs.getInt("ref_filiere"));
        d.setRefEtudiant(rs.getInt("ref_etudiant"));
        d.setRefSecretaire(rs.getInt("ref_secretaire"));
        return d;
    }
    public int countByStatut(String statut) {
        String sql = "SELECT COUNT(*) FROM dossier_inscription WHERE statut = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}


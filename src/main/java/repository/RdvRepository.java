package repository;

import database.Database;
import model.Rdv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RdvRepository {
    private Connection connection;

    public RdvRepository() {
        connection = Database.getConnexion();
    }

    public int ajouterRdv(Rdv rdv) {
        String sql = "INSERT INTO rdv (date, demi_journee, ref_etudiant, ref_professeur, ref_salle) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, rdv.getDate());
            stmt.setString(2, rdv.getDemiJournee());
            stmt.setInt(3, rdv.getRefEtudiant());
            stmt.setInt(4, rdv.getRefProfesseur());
            stmt.setInt(5, rdv.getRefSalle());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur ajout RDV : " + e.getMessage());
        }
        return -1;
    }

    public List<Rdv> getTousLesRdv() {
        List<Rdv> list = new ArrayList<>();
        String sql = "SELECT * FROM rdv ORDER BY date DESC";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Erreur récupération RDV : " + e.getMessage());
        }
        return list;
    }

    public List<Rdv> getRdvParProfesseur(int idProfesseur) {
        List<Rdv> list = new ArrayList<>();
        String sql = "SELECT * FROM rdv WHERE ref_professeur=? ORDER BY date DESC";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idProfesseur);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Erreur récupération RDV professeur : " + e.getMessage());
        }
        return list;
    }

    /**
     * Vérifie si une salle est disponible pour une date et demi-journée donnée.
     */
    public boolean isSalleDisponible(int idSalle, String date, String demiJournee) {
        String sql = "SELECT COUNT(*) FROM rdv WHERE ref_salle=? AND date=? AND demi_journee=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idSalle);
            stmt.setString(2, date);
            stmt.setString(3, demiJournee);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0;
        } catch (SQLException e) {
            System.out.println("Erreur vérification disponibilité salle : " + e.getMessage());
        }
        return false;
    }

    public boolean modifierRdv(Rdv rdv) {
        String sql = "UPDATE rdv SET date=?, demi_journee=?, ref_etudiant=?, ref_professeur=?, ref_salle=? WHERE id_rdv=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, rdv.getDate());
            stmt.setString(2, rdv.getDemiJournee());
            stmt.setInt(3, rdv.getRefEtudiant());
            stmt.setInt(4, rdv.getRefProfesseur());
            stmt.setInt(5, rdv.getRefSalle());
            stmt.setInt(6, rdv.getIdRdv());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur modification RDV : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerRdv(int idRdv) {
        String sql = "DELETE FROM rdv WHERE id_rdv=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idRdv);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression RDV : " + e.getMessage());
            return false;
        }
    }

    private Rdv map(ResultSet rs) throws SQLException {
        Rdv r = new Rdv();
        r.setIdRdv(rs.getInt("id_rdv"));
        r.setDate(rs.getString("date"));
        r.setDemiJournee(rs.getString("demi_journee"));
        r.setRefEtudiant(rs.getInt("ref_etudiant"));
        r.setRefProfesseur(rs.getInt("ref_professeur"));
        r.setRefSalle(rs.getInt("ref_salle"));
        return r;
    }
}

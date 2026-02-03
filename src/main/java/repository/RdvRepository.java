package repository;

import database.Database;
import model.Rdv;

import java.sql.*;

public class RdvRepository {
    private Connection connection;

    public RdvRepository() {
        connection = Database.getConnexion();
    }

    public int ajouterRdv(Rdv rdv) {
        String sql = "INSERT INTO Rdv (date, demi_journee, ref_etudiant, ref_professeur, ref_salle) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, rdv.getDate());
            stmt.setString(2, rdv.getDemiJournee());
            stmt.setInt(3, rdv.getRefEtudiant());
            stmt.setInt(4, rdv.getRefProfesseur());
            stmt.setInt(5, rdv.getRefSalle());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du rdv : " + e.getMessage());
        }
        return -1;
    }

    public boolean modifierRdv(Rdv rdv) {
        String sql = "UPDATE rdv SET date = ?, demi_journee = ?, ref_etudiant = ?, ref_professeur = ?, ref_salle = ? WHERE id_rdv = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, rdv.getDate());
            stmt.setString(2, rdv.getDemiJournee());
            stmt.setInt(3, rdv.getRefEtudiant());
            stmt.setInt(4, rdv.getRefProfesseur());
            stmt.setInt(5, rdv.getRefSalle());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'rdv : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerRdv(int idRdv) {
        String sql = "DELETE FROM rdv WHERE id_rdv = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idRdv);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la rdv : " + e.getMessage());
            return false;
        }
    }
}



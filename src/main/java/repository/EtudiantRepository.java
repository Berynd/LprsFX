package repository;

import database.Database;
import model.Etudiant;

import java.sql.*;

public class EtudiantRepository {
    private static Connection connection;

    public EtudiantRepository() {
        connection = Database.getConnexion();
    }

    public static boolean ajouterEtudiant(Etudiant etudiant) {
        String sql = "INSERT INTO etudiant (nom, prenom, email, telephone, adresse, dernier_diplome) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getEmail());
            stmt.setString(4, etudiant.getTelephone());
            stmt.setString(5, etudiant.getAdresse());
            stmt.setString(6, etudiant.getDernierDiplome());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'étudiant : " + e.getMessage());
        }
        return -1;
    }

    public boolean modifierEtudiant(Etudiant etudiant) {
        String sql = "UPDATE etudiant SET nom = ?, prenom = ?, email = ?, telephone = ?, adresse = ?, dernier_diplome = ? WHERE id_etudiant = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getEmail());
            stmt.setString(4, etudiant.getTelephone());
            stmt.setString(5, etudiant.getAdresse());
            stmt.setString(6, etudiant.getDernierDiplome());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'étudiant : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerEtudiant(int idEtudiant) {
        String sql = "DELETE FROM etudiant WHERE id_etudiant = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idEtudiant);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'étudiant : " + e.getMessage());
            return false;
        }
    }
}

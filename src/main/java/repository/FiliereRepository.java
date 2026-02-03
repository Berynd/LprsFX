package repository;

import database.Database;
import model.Filiere;

import java.sql.*;

public class FiliereRepository {
    private Connection connection;

    public FiliereRepository() {
        connection = Database.getConnexion();
    }

    public int ajouterFiliere(Filiere filiere) {
        String sql = "INSERT INTO filiere (nom) VALUES (?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, filiere.getNom());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'filiere : " + e.getMessage());
        }
        return -1;
    }

    public boolean modifierFiliere(Filiere filiere) {
        String sql = "UPDATE filiere SET nom = ? WHERE id_filiere = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, filiere.getNom());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'filiere : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerFiliere(int idFiliere) {
        String sql = "DELETE FROM filiere WHERE id_filiere = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idFiliere);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la filiere : " + e.getMessage());
            return false;
        }
    }
}


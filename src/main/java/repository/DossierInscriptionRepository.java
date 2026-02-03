package repository;

import database.Database;
import model.DossierInscription;

import java.sql.*;

public class DossierInscriptionRepository {
    private Connection connection;

    public DossierInscriptionRepository() {
        connection = Database.getConnexion();
    }

    public int ajouterDossierInscription(DossierInscription dossierInscription) {
        String sql = "INSERT INTO dossier_inscription (date_creation, motivation, statut, ref_filiere, ref_etudiant, ref_secretaire) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, dossierInscription.getDateCreation());
            stmt.setString(2, dossierInscription.getMotivation());
            stmt.setString(3, dossierInscription.getStatut());
            stmt.setInt(4, dossierInscription.getRefFiliere());
            stmt.setInt(5, dossierInscription.getRefEtudiant());
            stmt.setInt(6, dossierInscription.getRefSecretaire());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout d'un dossier d'inscription : " + e.getMessage());
        }
        return -1;
    }

    public boolean modifierDossierInscription(DossierInscription dossierInscription) {
        String sql = "UPDATE dossier_inscription SET date_creation = ?, motivation = ?, statut = ?, ref_filiere = ?, ref_etudiant = ?, ref_secretaire = ? WHERE id_dossier_inscription = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, dossierInscription.getDateCreation());
            stmt.setString(2, dossierInscription.getMotivation());
            stmt.setString(3, dossierInscription.getStatut());
            stmt.setInt(4, dossierInscription.getRefFiliere());
            stmt.setInt(5, dossierInscription.getRefEtudiant());
            stmt.setInt(6, dossierInscription.getRefSecretaire());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du dossier d'inscription : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerDossierInscription(int idDossierInscription) {
        String sql = "DELETE FROM dossier_inscription WHERE id_dossier_inscription = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idDossierInscription);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du dossier d'inscription : " + e.getMessage());
            return false;
        }
    }
}



package repository;

import database.Database;
import model.Etudiant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantRepository {
    private Connection connection;

    public EtudiantRepository() {
        connection = Database.getConnexion();
    }

    public int ajouterEtudiant(Etudiant etudiant) {
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
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur ajout étudiant : " + e.getMessage());
        }
        return -1;
    }

    public List<Etudiant> getTousLesEtudiants() {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT * FROM etudiant ORDER BY nom, prenom";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération étudiants : " + e.getMessage());
        }
        return list;
    }

    public Etudiant getEtudiantById(int id) {
        String sql = "SELECT * FROM etudiant WHERE id_etudiant = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.out.println("Erreur récupération étudiant : " + e.getMessage());
        }
        return null;
    }

    public List<Etudiant> rechercherEtudiant(String terme) {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT * FROM etudiant WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? ORDER BY nom";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            String p = "%" + terme + "%";
            stmt.setString(1, p); stmt.setString(2, p); stmt.setString(3, p);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Erreur recherche étudiant : " + e.getMessage());
        }
        return list;
    }

    public boolean modifierEtudiant(Etudiant etudiant) {
        String sql = "UPDATE etudiant SET nom=?, prenom=?, email=?, telephone=?, adresse=?, dernier_diplome=? WHERE id_etudiant=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getEmail());
            stmt.setString(4, etudiant.getTelephone());
            stmt.setString(5, etudiant.getAdresse());
            stmt.setString(6, etudiant.getDernierDiplome());
            stmt.setInt(7, etudiant.getIdEtudiant());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur modification étudiant : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerEtudiant(int idEtudiant) {
        String sql = "DELETE FROM etudiant WHERE id_etudiant = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idEtudiant);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression étudiant : " + e.getMessage());
            return false;
        }
    }

    private Etudiant map(ResultSet rs) throws SQLException {
        return new Etudiant(
            rs.getInt("id_etudiant"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("email"),
            rs.getString("telephone"),
            rs.getString("adresse"),
            rs.getString("dernier_diplome")
        );
    }
}

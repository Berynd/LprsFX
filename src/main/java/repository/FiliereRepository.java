package repository;

import database.Database;
import model.Filiere;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur ajout filière : " + e.getMessage());
        }
        return -1;
    }

    public List<Filiere> getToutesLesFilieres() {
        List<Filiere> list = new ArrayList<>();
        String sql = "SELECT * FROM filiere ORDER BY nom";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Filiere f = new Filiere();
                f.setIdFiliere(rs.getInt("id_filiere"));
                f.setNom(rs.getString("nom"));
                list.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération filières : " + e.getMessage());
        }
        return list;
    }

    public Filiere getFiliereById(int id) {
        String sql = "SELECT * FROM filiere WHERE id_filiere = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Filiere f = new Filiere();
                f.setIdFiliere(rs.getInt("id_filiere"));
                f.setNom(rs.getString("nom"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération filière : " + e.getMessage());
        }
        return null;
    }

    public boolean modifierFiliere(Filiere filiere) {
        String sql = "UPDATE filiere SET nom=? WHERE id_filiere=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, filiere.getNom());
            stmt.setInt(2, filiere.getIdFiliere());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur modification filière : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerFiliere(int idFiliere) {
        String sql = "DELETE FROM filiere WHERE id_filiere = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idFiliere);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression filière : " + e.getMessage());
            return false;
        }
    }
}

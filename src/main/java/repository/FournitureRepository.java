package repository;

import model.Fourniture;
import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournitureRepository {
    
    private Connection cnx;

    public FournitureRepository() {
        this.cnx = Database.getConnexion();
    }

    /**
     * Ajouter une nouvelle fourniture
     */
    public boolean ajouterFourniture(Fourniture fourniture) {
        String sql = "INSERT INTO fourniture (libelle, description, stock_actuel) VALUES (?, ?, ?)";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, fourniture.getLibelle());
            stmt.setString(2, fourniture.getDescription());
            stmt.setInt(3, fourniture.getStockActuel());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Fourniture ajoutée avec succès !");
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la fourniture : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupérer une fourniture par son ID
     */
    public Fourniture getFournitureParId(int id) {
        String sql = "SELECT * FROM fourniture WHERE id_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Fourniture(
                    rs.getInt("id_fourniture"),
                    rs.getString("libelle"),
                    rs.getString("description"),
                    rs.getInt("stock_actuel")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la fourniture : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupérer toutes les fournitures
     */
    public List<Fourniture> getToutesLesFournitures() {
        List<Fourniture> fournitures = new ArrayList<>();
        String sql = "SELECT * FROM fourniture ORDER BY libelle";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fourniture fourniture = new Fourniture(
                    rs.getInt("id_fourniture"),
                    rs.getString("libelle"),
                    rs.getString("description"),
                    rs.getInt("stock_actuel")
                );
                fournitures.add(fourniture);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures : " + e.getMessage());
            e.printStackTrace();
        }
        return fournitures;
    }

    /**
     * Mettre à jour une fourniture
     */
    public boolean mettreAJourFourniture(Fourniture fourniture) {
        String sql = "UPDATE fourniture SET libelle = ?, description = ?, stock_actuel = ? WHERE id_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, fourniture.getLibelle());
            stmt.setString(2, fourniture.getDescription());
            stmt.setInt(3, fourniture.getStockActuel());
            stmt.setInt(4, fourniture.getIdFourniture());
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Fourniture mise à jour avec succès !");
                return true;
            } else {
                System.out.println("Aucune fourniture trouvée avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la fourniture : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprimer une fourniture par son ID
     */
    public boolean supprimerFourniture(int id) {
        String sql = "DELETE FROM fourniture WHERE id_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("Fourniture supprimée avec succès !");
                return true;
            } else {
                System.out.println("Aucune fourniture trouvée avec cet ID.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la fourniture : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Rechercher des fournitures par libellé
     */
    public List<Fourniture> rechercherFournitureParLibelle(String libelle) {
        List<Fourniture> fournitures = new ArrayList<>();
        String sql = "SELECT * FROM fourniture WHERE libelle LIKE ? ORDER BY libelle";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, "%" + libelle + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fourniture fourniture = new Fourniture(
                    rs.getInt("id_fourniture"),
                    rs.getString("libelle"),
                    rs.getString("description"),
                    rs.getInt("stock_actuel")
                );
                fournitures.add(fourniture);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de fournitures : " + e.getMessage());
            e.printStackTrace();
        }
        return fournitures;
    }

    /**
     * Récupérer les fournitures en rupture de stock (stock <= seuil)
     */
    public List<Fourniture> getFournituresEnRupture(int seuil) {
        List<Fourniture> fournitures = new ArrayList<>();
        String sql = "SELECT * FROM fourniture WHERE stock_actuel <= ? ORDER BY stock_actuel";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, seuil);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Fourniture fourniture = new Fourniture(
                    rs.getInt("id_fourniture"),
                    rs.getString("libelle"),
                    rs.getString("description"),
                    rs.getInt("stock_actuel")
                );
                fournitures.add(fourniture);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures en rupture : " + e.getMessage());
            e.printStackTrace();
        }
        return fournitures;
    }

    /**
     * Mettre à jour le stock d'une fourniture
     */
    public boolean mettreAJourStock(int idFourniture, int nouvelleQuantite) {
        String sql = "UPDATE fourniture SET stock_actuel = ? WHERE id_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, idFourniture);
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Stock mis à jour avec succès !");
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du stock : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ajouter au stock d'une fourniture (augmente le stock)
     */
    public boolean ajouterAuStock(int idFourniture, int quantiteAAjouter) {
        String sql = "UPDATE fourniture SET stock_actuel = stock_actuel + ? WHERE id_fourniture = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, quantiteAAjouter);
            stmt.setInt(2, idFourniture);
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Stock augmenté de " + quantiteAAjouter + " unités !");
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout au stock : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retirer du stock d'une fourniture (diminue le stock)
     */
    public boolean retirerDuStock(int idFourniture, int quantiteARetirer) {
        String sql = "UPDATE fourniture SET stock_actuel = stock_actuel - ? WHERE id_fourniture = ? AND stock_actuel >= ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, quantiteARetirer);
            stmt.setInt(2, idFourniture);
            stmt.setInt(3, quantiteARetirer);
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Stock diminué de " + quantiteARetirer + " unités !");
                return true;
            } else {
                System.err.println("Stock insuffisant pour retirer " + quantiteARetirer + " unités !");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du retrait du stock : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

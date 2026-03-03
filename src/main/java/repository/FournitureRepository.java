package repository;

import model.Fourniture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournitureRepository extends BaseRepository {

    public boolean ajouterFourniture(Fourniture fourniture) {
        String sql = "INSERT INTO fourniture (libelle, description, stock_actuel) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, fourniture.getLibelle());
            stmt.setString(2, fourniture.getDescription());
            stmt.setInt(3, fourniture.getStockActuel());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la fourniture : " + e.getMessage());
            return false;
        }
    }

    public Fourniture getFournitureParId(int id) {
        String sql = "SELECT * FROM fourniture WHERE id_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Fourniture(rs.getInt("id_fourniture"), rs.getString("libelle"),
                            rs.getString("description"), rs.getInt("stock_actuel"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la fourniture : " + e.getMessage());
        }
        return null;
    }

    public List<Fourniture> getToutesLesFournitures() {
        List<Fourniture> fournitures = new ArrayList<>();
        String sql = "SELECT * FROM fourniture ORDER BY libelle";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                fournitures.add(new Fourniture(rs.getInt("id_fourniture"), rs.getString("libelle"),
                        rs.getString("description"), rs.getInt("stock_actuel")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures : " + e.getMessage());
        }
        return fournitures;
    }

    public boolean mettreAJourFourniture(Fourniture fourniture) {
        String sql = "UPDATE fourniture SET libelle = ?, description = ?, stock_actuel = ? WHERE id_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, fourniture.getLibelle());
            stmt.setString(2, fourniture.getDescription());
            stmt.setInt(3, fourniture.getStockActuel());
            stmt.setInt(4, fourniture.getIdFourniture());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la fourniture : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerFourniture(int id) {
        String sql = "DELETE FROM fourniture WHERE id_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la fourniture : " + e.getMessage());
            return false;
        }
    }

    public List<Fourniture> rechercherFournitureParLibelle(String libelle) {
        List<Fourniture> fournitures = new ArrayList<>();
        String sql = "SELECT * FROM fourniture WHERE libelle LIKE ? ORDER BY libelle";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, "%" + libelle + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fournitures.add(new Fourniture(rs.getInt("id_fourniture"), rs.getString("libelle"),
                            rs.getString("description"), rs.getInt("stock_actuel")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de fournitures : " + e.getMessage());
        }
        return fournitures;
    }

    public List<Fourniture> getFournituresEnRupture(int seuil) {
        List<Fourniture> fournitures = new ArrayList<>();
        String sql = "SELECT * FROM fourniture WHERE stock_actuel <= ? ORDER BY stock_actuel";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, seuil);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fournitures.add(new Fourniture(rs.getInt("id_fourniture"), rs.getString("libelle"),
                            rs.getString("description"), rs.getInt("stock_actuel")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures en rupture : " + e.getMessage());
        }
        return fournitures;
    }

    public boolean mettreAJourStock(int idFourniture, int nouvelleQuantite) {
        String sql = "UPDATE fourniture SET stock_actuel = ? WHERE id_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, nouvelleQuantite);
            stmt.setInt(2, idFourniture);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du stock : " + e.getMessage());
            return false;
        }
    }

    public boolean ajouterAuStock(int idFourniture, int quantiteAAjouter) {
        String sql = "UPDATE fourniture SET stock_actuel = stock_actuel + ? WHERE id_fourniture = ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, quantiteAAjouter);
            stmt.setInt(2, idFourniture);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout au stock : " + e.getMessage());
            return false;
        }
    }

    public boolean retirerDuStock(int idFourniture, int quantiteARetirer) {
        String sql = "UPDATE fourniture SET stock_actuel = stock_actuel - ? WHERE id_fourniture = ? AND stock_actuel >= ?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, quantiteARetirer);
            stmt.setInt(2, idFourniture);
            stmt.setInt(3, quantiteARetirer);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors du retrait du stock : " + e.getMessage());
            return false;
        }
    }
}

package repository;

import model.CommandeFourniture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeFournitureRepository extends BaseRepository {

    public int ajouterCommande(CommandeFourniture commande) {
        String sql = "INSERT INTO commande_fourniture (date, statut, ref_gestionnaire, ref_fournisseur) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, commande.getDate());
            stmt.setString(2, commande.getStatus());
            stmt.setInt(3, commande.getRefGestionnaire());
            stmt.setInt(4, commande.getRefFournisseur());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur ajout commande : " + e.getMessage());
        }
        return -1;
    }

    public List<CommandeFourniture> getToutesLesCommandes() {
        List<CommandeFourniture> list = new ArrayList<>();
        String sql = "SELECT * FROM commande_fourniture ORDER BY date DESC";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Erreur récupération commandes : " + e.getMessage());
        }
        return list;
    }

    public boolean changerStatut(int idCommande, String statut, String justification) {
        String sql = "UPDATE commande_fourniture SET statut=?, justification_refus=? WHERE id_commande_fourniture=?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setString(1, statut);
            stmt.setString(2, justification);
            stmt.setInt(3, idCommande);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur changement statut commande : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerCommande(int idCommande) {
        String sql = "DELETE FROM commande_fourniture WHERE id_commande_fourniture=?";
        try (PreparedStatement stmt = getCnx().prepareStatement(sql)) {
            stmt.setInt(1, idCommande);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression commande : " + e.getMessage());
            return false;
        }
    }

    private CommandeFourniture map(ResultSet rs) throws SQLException {
        CommandeFourniture c = new CommandeFourniture();
        c.setIdCommandeFourniture(rs.getInt("id_commande_fourniture"));
        c.setDate(rs.getString("date"));
        c.setStatus(rs.getString("statut"));
        c.setJustificationRefus(rs.getString("justification_refus"));
        c.setRefGestionnaire(rs.getInt("ref_gestionnaire"));
        c.setRefFournisseur(rs.getInt("ref_fournisseur"));
        return c;
    }
}

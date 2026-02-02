package repository;

import model.Fournisseur;
import model.Fourniture;
import model.FournitureFournisseur;
import database.Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FournitureFournisseurRepository {
    
    private Connection cnx;
    private FournitureRepository fournitureRepo;
    private FournisseurRepository fournisseurRepo;

    public FournitureFournisseurRepository() {
        this.cnx = Database.getConnexion();
        this.fournitureRepo = new FournitureRepository();
        this.fournisseurRepo = new FournisseurRepository();
    }

    /**
     * Associer une fourniture à un fournisseur avec un prix
     */
    public boolean ajouterAssociation(int refFourniture, int refFournisseur, BigDecimal prix) {
        String sql = "INSERT INTO fourniturefournisseur (ref_fourniture, ref_fournisseur, prix) VALUES (?, ?, ?)";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, refFourniture);
            stmt.setInt(2, refFournisseur);
            stmt.setBigDecimal(3, prix);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Association fourniture-fournisseur ajoutée avec succès !");
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'association : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupérer tous les fournisseurs pour une fourniture donnée
     */
    public List<FournitureFournisseur> getFournisseursPourFourniture(int idFourniture) {
        List<FournitureFournisseur> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fourniturefournisseur WHERE ref_fourniture = ?";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, idFourniture);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int refFournisseur = rs.getInt("ref_fournisseur");
                BigDecimal prix = rs.getBigDecimal("prix");
                
                // Récupérer les objets complets
                Fourniture fourniture = fournitureRepo.getFournitureParId(idFourniture);
                Fournisseur fournisseur = fournisseurRepo.getFournisseurParId(refFournisseur);
                
                FournitureFournisseur ff = new FournitureFournisseur(
                    idFourniture, 
                    refFournisseur, 
                    prix,
                    fourniture,
                    fournisseur
                );
                resultats.add(ff);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournisseurs : " + e.getMessage());
            e.printStackTrace();
        }
        return resultats;
    }

    /**
     * Récupérer toutes les fournitures d'un fournisseur donné
     */
    public List<FournitureFournisseur> getFournituresPourFournisseur(int idFournisseur) {
        List<FournitureFournisseur> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fourniturefournisseur WHERE ref_fournisseur = ?";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, idFournisseur);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int refFourniture = rs.getInt("ref_fourniture");
                BigDecimal prix = rs.getBigDecimal("prix");
                
                // Récupérer les objets complets
                Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                Fournisseur fournisseur = fournisseurRepo.getFournisseurParId(idFournisseur);
                
                FournitureFournisseur ff = new FournitureFournisseur(
                    refFourniture, 
                    idFournisseur, 
                    prix,
                    fourniture,
                    fournisseur
                );
                resultats.add(ff);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fournitures : " + e.getMessage());
            e.printStackTrace();
        }
        return resultats;
    }

    /**
     * Récupérer toutes les associations fourniture-fournisseur
     */
    public List<FournitureFournisseur> getToutesLesAssociations() {
        List<FournitureFournisseur> resultats = new ArrayList<>();
        String sql = "SELECT * FROM fourniturefournisseur";

        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int refFourniture = rs.getInt("ref_fourniture");
                int refFournisseur = rs.getInt("ref_fournisseur");
                BigDecimal prix = rs.getBigDecimal("prix");
                
                // Récupérer les objets complets
                Fourniture fourniture = fournitureRepo.getFournitureParId(refFourniture);
                Fournisseur fournisseur = fournisseurRepo.getFournisseurParId(refFournisseur);
                
                FournitureFournisseur ff = new FournitureFournisseur(
                    refFourniture, 
                    refFournisseur, 
                    prix,
                    fourniture,
                    fournisseur
                );
                resultats.add(ff);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des associations : " + e.getMessage());
            e.printStackTrace();
        }
        return resultats;
    }

    /**
     * Mettre à jour le prix d'une association
     */
    public boolean mettreAJourPrix(int refFourniture, int refFournisseur, BigDecimal nouveauPrix) {
        String sql = "UPDATE fourniturefournisseur SET prix = ? WHERE ref_fourniture = ? AND ref_fournisseur = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setBigDecimal(1, nouveauPrix);
            stmt.setInt(2, refFourniture);
            stmt.setInt(3, refFournisseur);
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Prix mis à jour avec succès !");
                return true;
            } else {
                System.out.println("Aucune association trouvée.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du prix : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprimer une association fourniture-fournisseur
     */
    public boolean supprimerAssociation(int refFourniture, int refFournisseur) {
        String sql = "DELETE FROM fourniturefournisseur WHERE ref_fourniture = ? AND ref_fournisseur = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, refFourniture);
            stmt.setInt(2, refFournisseur);
            
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("Association supprimée avec succès !");
                return true;
            } else {
                System.out.println("Aucune association trouvée.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'association : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtenir le prix d'une fourniture chez un fournisseur spécifique
     */
    public BigDecimal getPrix(int refFourniture, int refFournisseur) {
        String sql = "SELECT prix FROM fourniturefournisseur WHERE ref_fourniture = ? AND ref_fournisseur = ?";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, refFourniture);
            stmt.setInt(2, refFournisseur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("prix");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du prix : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trouver le meilleur prix pour une fourniture (le moins cher)
     */
    public FournitureFournisseur getMeilleurPrix(int idFourniture) {
        String sql = "SELECT * FROM fourniturefournisseur WHERE ref_fourniture = ? ORDER BY prix ASC LIMIT 1";
        
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = Database.getConnexion();
            }
            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setInt(1, idFourniture);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int refFournisseur = rs.getInt("ref_fournisseur");
                BigDecimal prix = rs.getBigDecimal("prix");
                
                Fourniture fourniture = fournitureRepo.getFournitureParId(idFourniture);
                Fournisseur fournisseur = fournisseurRepo.getFournisseurParId(refFournisseur);
                
                return new FournitureFournisseur(
                    idFourniture, 
                    refFournisseur, 
                    prix,
                    fourniture,
                    fournisseur
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du meilleur prix : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

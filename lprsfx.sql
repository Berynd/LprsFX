-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 24, 2026 at 07:58 AM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lprsfx`
--

-- --------------------------------------------------------

--
-- Table structure for table `commandefourniture`
--

DROP TABLE IF EXISTS `commandefourniture`;
CREATE TABLE IF NOT EXISTS `commandefourniture` (
  `ref_commande` int NOT NULL,
  `ref_fourniture` int NOT NULL,
  `quantite` int DEFAULT NULL,
  PRIMARY KEY (`ref_commande`,`ref_fourniture`),
  KEY `fk_commandefourniture_fourniture` (`ref_fourniture`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `commande_fourniture`
--

DROP TABLE IF EXISTS `commande_fourniture`;
CREATE TABLE IF NOT EXISTS `commande_fourniture` (
  `id_commande_fourniture` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `statut` varchar(50) COLLATE latin1_bin DEFAULT NULL,
  `justification_refus` text COLLATE latin1_bin,
  `ref_gestionnaire` int DEFAULT NULL,
  `ref_fournisseur` int DEFAULT NULL,
  PRIMARY KEY (`id_commande_fourniture`),
  KEY `fk_commande_gestionnaire` (`ref_gestionnaire`),
  KEY `fk_commande_fournisseur` (`ref_fournisseur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `demande_fourniture`
--

DROP TABLE IF EXISTS `demande_fourniture`;
CREATE TABLE IF NOT EXISTS `demande_fourniture` (
  `id_demande_fourniture` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `statut` varchar(50) COLLATE latin1_bin DEFAULT NULL,
  `raison` text COLLATE latin1_bin,
  `justification_refus` text COLLATE latin1_bin,
  `ref_professeur` int DEFAULT NULL,
  PRIMARY KEY (`id_demande_fourniture`),
  KEY `fk_demande_professeur` (`ref_professeur`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `demande_fourniture`
--

INSERT INTO `demande_fourniture` (`id_demande_fourniture`, `date`, `statut`, `raison`, `justification_refus`, `ref_professeur`) VALUES
(2, '2026-03-02', 'Validé', 'il me faut 3 stylo bleu', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `dossier_inscription`
--

DROP TABLE IF EXISTS `dossier_inscription`;
CREATE TABLE IF NOT EXISTS `dossier_inscription` (
  `id_dossier_inscription` int NOT NULL AUTO_INCREMENT,
  `date_creation` date DEFAULT NULL,
  `motivation` text COLLATE latin1_bin,
  `statut` varchar(50) COLLATE latin1_bin DEFAULT NULL,
  `ref_filiere` int DEFAULT NULL,
  `ref_etudiant` int DEFAULT NULL,
  `ref_secretaire` int DEFAULT NULL,
  PRIMARY KEY (`id_dossier_inscription`),
  KEY `fk_dossier_filiere` (`ref_filiere`),
  KEY `fk_dossier_etudiant` (`ref_etudiant`),
  KEY `fk_dossier_secretaire` (`ref_secretaire`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `dossier_inscription`
--

INSERT INTO `dossier_inscription` (`id_dossier_inscription`, `date_creation`, `motivation`, `statut`, `ref_filiere`, `ref_etudiant`, `ref_secretaire`) VALUES
(4, '2026-03-02', 'Veut devenir developpeur full stack', 'Validé', 1, 3, NULL),
(5, '2026-03-02', 'En avait marre de faire du réseaux', 'Refusé', 1, 5, NULL),
(6, '2026-03-02', 'En avait marre de certains prof', 'Validé', 1, 4, NULL),
(7, '2026-03-02', 'Il kiffe faire du réseaux', 'Refusé', 2, 6, NULL),
(12, '2026-03-17', 'Veut devenir comme Iron Man', 'En attente', 3, 8, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `etudiant`
--

DROP TABLE IF EXISTS `etudiant`;
CREATE TABLE IF NOT EXISTS `etudiant` (
  `id_etudiant` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `prenom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `email` varchar(150) COLLATE latin1_bin DEFAULT NULL,
  `telephone` varchar(20) COLLATE latin1_bin DEFAULT NULL,
  `adresse` varchar(255) COLLATE latin1_bin DEFAULT NULL,
  `dernier_diplome` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`id_etudiant`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `etudiant`
--

INSERT INTO `etudiant` (`id_etudiant`, `nom`, `prenom`, `email`, `telephone`, `adresse`, `dernier_diplome`) VALUES
(3, 'Passard', 'Ethan', 'ethanpassard@gmail.com', '07 87 90 12 73', '32 avenue du cailloux suprême', 'BAC'),
(4, 'Langui', 'Thomas', 'thomaslangui@gmail.com', '06 71 93 12 33', '67 rue du Maréchal Macron', 'BAC'),
(5, 'Berynde', 'Gabriel', 'gabrielberynde@gmail.com', '07 67 12 83 99', '9 route de romania', 'BREVET'),
(6, 'Oceane', 'Ossian', 'ossianoceane@gmail.com', '07 12 83 88 92', '9 bis rue de la rue dorée', 'BAC'),
(7, 'De lieu', 'Noah', 'noahdelieu', '07 87 56 11 23', '12 rue de la mongolefiere jaune', 'BAC'),
(8, 'Colin', 'Tom', 'tomcolin@gmail.com', '06 15 28 03 82', '77 rue du six seven', 'Brevet');

-- --------------------------------------------------------

--
-- Table structure for table `filiere`
--

DROP TABLE IF EXISTS `filiere`;
CREATE TABLE IF NOT EXISTS `filiere` (
  `id_filiere` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`id_filiere`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `filiere`
--

INSERT INTO `filiere` (`id_filiere`, `nom`) VALUES
(1, 'BTS SIO SLAM'),
(2, 'BTS SIO SISR'),
(3, 'BTS CPRP');

-- --------------------------------------------------------

--
-- Table structure for table `fournisseur`
--

DROP TABLE IF EXISTS `fournisseur`;
CREATE TABLE IF NOT EXISTS `fournisseur` (
  `id_fournisseur` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `contact` varchar(150) COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`id_fournisseur`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `fournisseur`
--

INSERT INTO `fournisseur` (`id_fournisseur`, `nom`, `contact`) VALUES
(3, 'BIC', '01 98 27 33 09'),
(4, 'UHU', '01 98 66 12 33'),
(5, 'DELL', '01 97 22 63 89'),
(6, 'HP', '01 45 72 83 22'),
(7, 'AMAZON', '01 92 83 62 84'),
(8, 'LEBONCOIN', '01 67 92 65 11'),
(9, 'TEMU', '01 27 18 93 00');

-- --------------------------------------------------------

--
-- Table structure for table `fourniture`
--

DROP TABLE IF EXISTS `fourniture`;
CREATE TABLE IF NOT EXISTS `fourniture` (
  `id_fourniture` int NOT NULL AUTO_INCREMENT,
  `libelle` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `description` text COLLATE latin1_bin,
  `stock_actuel` int DEFAULT '0',
  PRIMARY KEY (`id_fourniture`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `fourniture`
--

INSERT INTO `fourniture` (`id_fourniture`, `libelle`, `description`, `stock_actuel`) VALUES
(2, 'STYLO BLEU', 'Stylo bleu de la marque BIC', 38),
(3, 'STYLO NOIR', 'Stylo noir de la marque BIC', 50),
(4, 'STYLO VERT', 'Stylo vert de la marque bIC', 47),
(5, 'STYLO ROUGE', 'Stylo rouge de la marque BIC', 50),
(6, 'PC DELL', 'Laptop Dell i7 16GRAM 2TO', 15),
(7, 'FEUTRE NOIR', 'Feutre veleda noir de la marque BIC', 17);

-- --------------------------------------------------------

--
-- Table structure for table `fournituredemandefourniture`
--

DROP TABLE IF EXISTS `fournituredemandefourniture`;
CREATE TABLE IF NOT EXISTS `fournituredemandefourniture` (
  `ref_demande_fourniture` int NOT NULL,
  `ref_fourniture` int NOT NULL,
  `quantite` int DEFAULT NULL,
  PRIMARY KEY (`ref_demande_fourniture`,`ref_fourniture`),
  KEY `fk_fdf_fourniture` (`ref_fourniture`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `fournituredemandefourniture`
--

INSERT INTO `fournituredemandefourniture` (`ref_demande_fourniture`, `ref_fourniture`, `quantite`) VALUES
(2, 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `fourniturefournisseur`
--

DROP TABLE IF EXISTS `fourniturefournisseur`;
CREATE TABLE IF NOT EXISTS `fourniturefournisseur` (
  `ref_fourniture` int NOT NULL,
  `ref_fournisseur` int NOT NULL,
  `prix` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`ref_fourniture`,`ref_fournisseur`),
  KEY `fk_ff_fournisseur` (`ref_fournisseur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

-- --------------------------------------------------------

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
CREATE TABLE IF NOT EXISTS `log` (
  `id_log` int NOT NULL AUTO_INCREMENT,
  `date_log` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ref_utilisateur` int DEFAULT NULL,
  `nom_utilisateur` varchar(100) COLLATE latin1_bin NOT NULL,
  `message` text COLLATE latin1_bin NOT NULL,
  `action` varchar(50) COLLATE latin1_bin NOT NULL,
  `page` varchar(100) COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`id_log`),
  KEY `idx_ref_utilisateur` (`ref_utilisateur`),
  KEY `idx_date_log` (`date_log`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `log`
--

INSERT INTO `log` (`id_log`, `date_log`, `ref_utilisateur`, `nom_utilisateur`, `message`, `action`, `page`) VALUES
(1, '2026-03-17 10:51:45', NULL, 'admin admin', 'Connexion réussie', 'CONNEXION', 'Login'),
(2, '2026-03-17 11:08:32', NULL, 'admin admin', 'Connexion réussie', 'CONNEXION', 'Login'),
(3, '2026-03-17 11:09:03', NULL, 'admin admin', 'Salle ajoutée : test', 'AJOUTER', 'Salles'),
(4, '2026-03-17 11:09:10', NULL, 'admin admin', 'Salle modifiée : test test pouf', 'MODIFIER', 'Salles'),
(5, '2026-03-17 11:09:38', NULL, 'admin admin', 'Fournisseur ajouté : TEMU', 'AJOUTER', 'Fournisseurs'),
(6, '2026-03-17 11:10:01', NULL, 'admin admin', 'RDV créé le 2026-03-18 Après-midi salle Zeus', 'AJOUTER', 'RendezVous'),
(7, '2026-03-17 11:11:10', NULL, 'admin admin', 'Étudiant ajouté : Colin Tom', 'AJOUTER', 'FicheEtudiante'),
(8, '2026-03-17 11:11:38', NULL, 'admin admin', 'Dossier créé pour étudiant #8', 'AJOUTER', 'DossierInscription'),
(9, '2026-03-17 11:12:39', NULL, 'admin admin', 'Salle supprimée : test test pouf', 'SUPPRIMER', 'Salles'),
(10, '2026-03-17 11:13:02', NULL, 'prof prof', 'Connexion réussie', 'CONNEXION', 'Login'),
(11, '2026-03-17 11:13:07', NULL, 'admin admin', 'Connexion réussie', 'CONNEXION', 'Login'),
(12, '2026-03-17 11:18:27', NULL, 'Sebastien Lemoine', 'Connexion réussie', 'CONNEXION', 'Login'),
(13, '2026-03-17 11:18:34', NULL, 'admin admin', 'Connexion réussie', 'CONNEXION', 'Login');

-- --------------------------------------------------------

--
-- Table structure for table `rdv`
--

DROP TABLE IF EXISTS `rdv`;
CREATE TABLE IF NOT EXISTS `rdv` (
  `id_rdv` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `demi_journee` varchar(20) COLLATE latin1_bin DEFAULT NULL,
  `ref_etudiant` int DEFAULT NULL,
  `ref_professeur` int DEFAULT NULL,
  `ref_salle` int DEFAULT NULL,
  PRIMARY KEY (`id_rdv`),
  KEY `fk_rdv_etudiant` (`ref_etudiant`),
  KEY `fk_rdv_professeur` (`ref_professeur`),
  KEY `fk_rdv_salle` (`ref_salle`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `rdv`
--

INSERT INTO `rdv` (`id_rdv`, `date`, `demi_journee`, `ref_etudiant`, `ref_professeur`, `ref_salle`) VALUES
(3, '2026-03-18', 'Après-midi', 5, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `salle`
--

DROP TABLE IF EXISTS `salle`;
CREATE TABLE IF NOT EXISTS `salle` (
  `id_salle` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`id_salle`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `salle`
--

INSERT INTO `salle` (`id_salle`, `nom`) VALUES
(1, 'Zeus'),
(3, 'Poséidon'),
(4, 'Hades'),
(5, 'Athéna'),
(6, 'Apollon'),
(7, 'Aphrodite'),
(8, 'Héra'),
(9, 'Arès'),
(10, 'Artémis'),
(11, 'Hermès');

-- --------------------------------------------------------

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id_utilisateur` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `prenom` varchar(100) COLLATE latin1_bin DEFAULT NULL,
  `email` varchar(150) COLLATE latin1_bin DEFAULT NULL,
  `mdp` varchar(255) COLLATE latin1_bin DEFAULT NULL,
  `role` varchar(50) COLLATE latin1_bin DEFAULT NULL,
  `date_derniere_connexion` datetime DEFAULT NULL,
  `ref_filiere` int DEFAULT NULL,
  PRIMARY KEY (`id_utilisateur`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_utilisateur_filiere` (`ref_filiere`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1 COLLATE=latin1_bin;

--
-- Dumping data for table `utilisateur`
--

INSERT INTO `utilisateur` (`id_utilisateur`, `nom`, `prenom`, `email`, `mdp`, `role`, `date_derniere_connexion`, `ref_filiere`) VALUES
(6, 'secretaire', 'secretaire', 'secretaire', '$2a$10$sHzlG75sFbSxVQeTljcaV.TzsFT6Nx8lj0qsAg7Rrx3skfgMmfEEm', 'Secrétaire', NULL, NULL),
(7, 'prof', 'prof', 'prof', '$2a$10$6CMkaNoq87LrgLBzSsOD0O6vVNJyuHdl70tA4jXwSy3BKKX5dNPjW', 'Professeur', NULL, NULL),
(8, 'stock', 'stock', 'stock', '$2a$10$nbmuuOknUpdl4tQPyiD.Qu8xgMxwRDE1qSLyNjnGp.HWMZiU8KWHG', 'Gestionnaire de stock', NULL, NULL),
(10, 'test', 'test', 'test', '$2a$10$4zMbQX0b.TO7vA.zVbUY6eecsU8Ax0ws/zZt3hlIJpCwYMSNqDF4y', 'Professeur', NULL, NULL),
(11, 'admin', 'admin', 'admin', '$2a$10$/WoZ.csqqdp2zO8RMYOhGur/cgckpc.ctKj8u5.fdmj4342IU0iyi', 'Admin', NULL, NULL),
(12, 'Lemoine', 'Sebastien', 'sebastienlemoine@lprs.fr', '$2a$10$TSDYcT/K4FQFyUg.scLIyu5Fo0cMZjUpODlabx0w1kpCKpfQxNV7.', 'Professeur', NULL, NULL);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `commandefourniture`
--
ALTER TABLE `commandefourniture`
  ADD CONSTRAINT `fk_commandefourniture_commande` FOREIGN KEY (`ref_commande`) REFERENCES `commande_fourniture` (`id_commande_fourniture`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_commandefourniture_fourniture` FOREIGN KEY (`ref_fourniture`) REFERENCES `fourniture` (`id_fourniture`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `commande_fourniture`
--
ALTER TABLE `commande_fourniture`
  ADD CONSTRAINT `fk_commande_fournisseur` FOREIGN KEY (`ref_fournisseur`) REFERENCES `fournisseur` (`id_fournisseur`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_commande_gestionnaire` FOREIGN KEY (`ref_gestionnaire`) REFERENCES `utilisateur` (`id_utilisateur`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `demande_fourniture`
--
ALTER TABLE `demande_fourniture`
  ADD CONSTRAINT `fk_demande_professeur` FOREIGN KEY (`ref_professeur`) REFERENCES `utilisateur` (`id_utilisateur`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `dossier_inscription`
--
ALTER TABLE `dossier_inscription`
  ADD CONSTRAINT `fk_dossier_etudiant` FOREIGN KEY (`ref_etudiant`) REFERENCES `etudiant` (`id_etudiant`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_dossier_filiere` FOREIGN KEY (`ref_filiere`) REFERENCES `filiere` (`id_filiere`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_dossier_secretaire` FOREIGN KEY (`ref_secretaire`) REFERENCES `utilisateur` (`id_utilisateur`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `fournituredemandefourniture`
--
ALTER TABLE `fournituredemandefourniture`
  ADD CONSTRAINT `fk_fdf_demande` FOREIGN KEY (`ref_demande_fourniture`) REFERENCES `demande_fourniture` (`id_demande_fourniture`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_fdf_fourniture` FOREIGN KEY (`ref_fourniture`) REFERENCES `fourniture` (`id_fourniture`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `fourniturefournisseur`
--
ALTER TABLE `fourniturefournisseur`
  ADD CONSTRAINT `fk_ff_fournisseur` FOREIGN KEY (`ref_fournisseur`) REFERENCES `fournisseur` (`id_fournisseur`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_ff_fourniture` FOREIGN KEY (`ref_fourniture`) REFERENCES `fourniture` (`id_fourniture`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `log`
--
ALTER TABLE `log`
  ADD CONSTRAINT `fk_log_utilisateur` FOREIGN KEY (`ref_utilisateur`) REFERENCES `utilisateur` (`id_utilisateur`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `rdv`
--
ALTER TABLE `rdv`
  ADD CONSTRAINT `fk_rdv_etudiant` FOREIGN KEY (`ref_etudiant`) REFERENCES `etudiant` (`id_etudiant`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_rdv_professeur` FOREIGN KEY (`ref_professeur`) REFERENCES `utilisateur` (`id_utilisateur`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_rdv_salle` FOREIGN KEY (`ref_salle`) REFERENCES `salle` (`id_salle`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `fk_utilisateur_filiere` FOREIGN KEY (`ref_filiere`) REFERENCES `filiere` (`id_filiere`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


-- Host: 127.0.0.1:3306
-- Generation Time: Sep 06, 2019 at 02:45 PM
-- Server version: 5.7.26


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `imenik`
--

-- --------------------------------------------------------

--
-- Table structure for table `kontakt`
--

DROP TABLE IF EXISTS `kontakt`;
CREATE TABLE IF NOT EXISTS `kontakt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ime` varchar(50) DEFAULT NULL,
  `prezime` varchar(50) DEFAULT NULL,
  `broj` varchar(30) DEFAULT NULL,
  `adresa` varchar(150) DEFAULT NULL,
  `tip` varchar(10) NOT NULL DEFAULT 'Privatni',
  `id_korisnika` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_korisnika` (`id_korisnika`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf16;

--
-- Dumping data for table `kontakt`
--

INSERT INTO `kontakt` (`id`, `ime`, `prezime`, `broj`, `adresa`, `tip`, `id_korisnika`) VALUES
(2, 'Nenad', 'Jovanovic', '065 356 678', 'Mis. Irbijeva 65, Beograd', 'Privatni', 1),
(3, 'Ivan', 'Milenkovic', '062 252 565', 'Kraljice Katarine 15, Beograd', 'Privatni', 1),
(4, 'Irena', 'Petrovic', '065 885 885', 'Radanska 2, Beograd', 'Poslovni', 1),
(5, 'Ana', 'Tomasevic', '063 345 543', 'Dunavska 44, Beograd', 'Privatni', 1),
(6, 'Nevena', 'Jovanovic', '064 486 386', 'Rimska 128, Beograd', 'Privatni', 1),
(7, 'Nikola', 'Popovic', '063 325 523', 'Save Sumanovica 55, Beograd', 'Privatni', 2),
(8, 'Nikola', 'Popovic', '063 325 523', 'Save Sumanovica 55, Beograd', 'Privatni', 2),
(9, 'Nenad', 'Markovic', '065 356 678', 'Mis. Irbijeva 65, Beograd', 'Privatni', 1),
(10, 'Ivan', 'Zigic', '062 252 565', 'Kraljice Katarine 15, Beograd', 'Privatni', 1),
(11, 'Milan', 'Jovanovic', '065 565 543', '', 'Privatni', 9),
(12, 'Nevenka', 'Lepa', '063 456 768', '', 'Privatni', 9);

-- --------------------------------------------------------

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
CREATE TABLE IF NOT EXISTS `korisnik` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `korisnicko_ime` varchar(50) NOT NULL,
  `sifra` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `korisnicko_ime` (`korisnicko_ime`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf16;

--
-- Dumping data for table `korisnik`
--

INSERT INTO `korisnik` (`id`, `korisnicko_ime`, `sifra`) VALUES
(1, 'korisnik1', 'sifra'),
(2, 'korisnik2', 'sifra'),
(8, 'korisnik3', 'sifra3'),
(9, 'Vladimir', 'sifra'),
(10, 'Vlada', 'sifra');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `kontakt`
--
ALTER TABLE `kontakt`
  ADD CONSTRAINT `kontakt_ibfk_1` FOREIGN KEY (`id_korisnika`) REFERENCES `korisnik` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Oct 10, 2022 at 11:01 AM
-- Server version: 8.0.30
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `congestion_tax`
--

-- --------------------------------------------------------

--
-- Table structure for table `city`
--

CREATE TABLE `city` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `city`
--

INSERT INTO `city` (`id`, `name`) VALUES
(1, 'Gothenburg');

-- --------------------------------------------------------

--
-- Table structure for table `city_vehicle`
--

CREATE TABLE `city_vehicle` (
  `city_id` bigint NOT NULL,
  `vehicle_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `city_vehicle`
--

INSERT INTO `city_vehicle` (`city_id`, `vehicle_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6);
-- --------------------------------------------------------

--
-- Table structure for table `flyway_schema_history`
--

-- CREATE TABLE `flyway_schema_history` (
--   `installed_rank` int NOT NULL,
--   `version` varchar(50) DEFAULT NULL,
--   `description` varchar(200) NOT NULL,
--   `type` varchar(20) NOT NULL,
--   `script` varchar(1000) NOT NULL,
--   `checksum` int DEFAULT NULL,
--   `installed_by` varchar(100) NOT NULL,
--   `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `execution_time` int NOT NULL,
--   `success` tinyint(1) NOT NULL
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --
-- -- Dumping data for table `flyway_schema_history`
-- --

-- INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES
-- (1, '1', 'init', 'SQL', 'V1__init.sql', 628934198, 'tax_user1', '2022-02-28 19:23:37', 12737, 1),
-- (2, '2', 'sample data', 'SQL', 'V2__sample_data.sql', -1993376038, 'tax_user1', '2022-02-28 19:24:24', 44162, 1);

-- --------------------------------------------------------

--
-- Table structure for table `no_tax_days`
--

CREATE TABLE `no_tax_days` (
  `id` bigint NOT NULL,
  `date` datetime(6) NOT NULL,
  `city_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `no_tax_days`
--

INSERT INTO `no_tax_days` (`id`, `date`, `city_id`) VALUES
(1, '2013-12-26 00:00:00.000000', 1),
(2, '2013-12-25 00:00:00.000000', 1),
(3, '2013-11-02 00:00:00.000000', 1),
(4, '2013-06-22 00:00:00.000000', 1),
(5, '2013-06-06 00:00:00.000000', 1),
(6, '2013-05-19 00:00:00.000000', 1),
(7, '2013-05-09 00:00:00.000000', 1),
(8, '2013-05-01 00:00:00.000000', 1),
(9, '2013-04-01 00:00:00.000000', 1),
(10, '2013-03-31 00:00:00.000000', 1),
(11, '2013-03-29 00:00:00.000000', 1),
(12, '2013-01-01 00:00:00.000000', 1),
(13, '2013-01-06 00:00:00.000000', 1);
-- --------------------------------------------------------

--
-- Table structure for table `no_tax_month`
--

CREATE TABLE `no_tax_month` (
  `city_id` bigint NOT NULL,
  `is_april` tinyint(1) NOT NULL DEFAULT '0',
  `is_august` tinyint(1) NOT NULL DEFAULT '0',
  `is_december` tinyint(1) NOT NULL DEFAULT '0',
  `is_february` tinyint(1) NOT NULL DEFAULT '0',
  `is_january` tinyint(1) NOT NULL DEFAULT '0',
  `is_july` tinyint(1) NOT NULL DEFAULT '0',
  `is_june` tinyint(1) NOT NULL DEFAULT '0',
  `is_march` tinyint(1) NOT NULL DEFAULT '0',
  `is_may` tinyint(1) NOT NULL DEFAULT '0',
  `is_november` tinyint(1) NOT NULL DEFAULT '0',
  `is_october` tinyint(1) NOT NULL DEFAULT '0',
  `is_september` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `no_tax_month`
--

INSERT INTO `no_tax_month` (`city_id`, `is_april`, `is_august`, `is_december`, `is_february`, `is_january`, `is_july`, `is_june`, `is_march`, `is_may`, `is_november`, `is_october`, `is_september`) VALUES
(1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `preference`
--

CREATE TABLE `preference` (
  `max_tax_per_day` int NOT NULL DEFAULT '60',
  `number_of_tax_free_days_after_holiday` int NOT NULL DEFAULT '0',
  `number_of_tax_free_days_before_holiday` int NOT NULL DEFAULT '0',
  `single_charge_interval_in_min` int NOT NULL DEFAULT '0',
  `city_entity_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `preference`
--

INSERT INTO `preference` (`max_tax_per_day`, `number_of_tax_free_days_after_holiday`, `number_of_tax_free_days_before_holiday`, `single_charge_interval_in_min`, `city_entity_id`) VALUES
(60, 0, 1, 60, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tariff`
--

CREATE TABLE `tariff` (
  `id` bigint NOT NULL,
  `charge` decimal(19,2) NOT NULL,
  `from_time` time NOT NULL,
  `to_time` time NOT NULL,
  `city_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `tariff`
--

INSERT INTO `tariff` (`id`, `charge`, `from_time`, `to_time`, `city_id`) VALUES
(1, '13.00', '15:00:00', '15:29:59', 1),
(2, '13.00', '06:30:00', '06:59:59', 1),
(3, '13.00', '08:00:00', '08:29:59', 1),
(4, '13.00', '17:00:00', '17:59:59', 1),
(5, '8.00', '18:00:00', '18:29:59', 1),
(6, '0.00', '00:00:00', '05:59:59', 1),
(7, '8.00', '06:00:00', '06:29:59', 1),
(8, '8.00', '08:30:00', '14:59:59', 1),
(9, '18.00', '07:00:00', '07:59:59', 1),
(10, '0.00', '18:30:00', '23:59:59', 1),
(11, '18.00', '15:30:00', '16:59:59', 1);

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE `vehicle` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`id`, `name`) VALUES
(2, 'Bus'),
(7, 'Car'),
(3, 'Diplomat'),
(1, 'Emergency'),
(6, 'Foreign'),
(5, 'Military'),
(8, 'Motorbike'),
(4, 'Motorcycle');

-- --------------------------------------------------------

--
-- Table structure for table `tax_days`
--

CREATE TABLE `tax_days` (
  `city_id` bigint NOT NULL,
  `is_friday` tinyint(1) NOT NULL DEFAULT '1',
  `is_monday` tinyint(1) NOT NULL DEFAULT '1',
  `is_saturday` tinyint(1) NOT NULL DEFAULT '0',
  `is_sunday` tinyint(1) NOT NULL DEFAULT '0',
  `is_thursday` tinyint(1) NOT NULL DEFAULT '1',
  `is_tuesday` tinyint(1) NOT NULL DEFAULT '1',
  `is_wednesday` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `tax_days`
--

INSERT INTO `tax_days` (`city_id`, `is_friday`, `is_monday`, `is_saturday`, `is_sunday`, `is_thursday`, `is_tuesday`, `is_wednesday`) VALUES
(1, 1, 1, 0, 0, 1, 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `city`
--
ALTER TABLE `city`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_qsstlki7ni5ovaariyy9u8y79` (`name`);

--
-- Indexes for table `city_vehicle`
--
ALTER TABLE `city_vehicle`
  ADD PRIMARY KEY (`city_id`,`vehicle_id`),
  ADD KEY `FK6gxcrb31rg88jb5trenouy0kw` (`vehicle_id`);

--
-- Indexes for table `flyway_schema_history`
-- --
-- ALTER TABLE `flyway_schema_history`
--   ADD PRIMARY KEY (`installed_rank`),
--   ADD KEY `flyway_schema_history_s_idx` (`success`);

--
-- Indexes for table `no_tax_days`
--
ALTER TABLE `no_tax_days`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKn29qpqp4dr0k2s51vygvgle2o` (`city_id`);

--
-- Indexes for table `no_tax_month`
--
ALTER TABLE `no_tax_month`
  ADD PRIMARY KEY (`city_id`);

--
-- Indexes for table `preference`
--
ALTER TABLE `preference`
  ADD PRIMARY KEY (`city_entity_id`);

--
-- Indexes for table `tariff`
--
ALTER TABLE `tariff`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKfw2gmot2dl3snaf8icecfecau` (`city_id`);

--
-- Indexes for table `vehicle`
--
ALTER TABLE `vehicle`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_k7ifbpeh918n2gxjtk7vutv40` (`name`);

--
-- Indexes for table `tax_days`
--
ALTER TABLE `tax_days`
  ADD PRIMARY KEY (`city_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `city`
--
ALTER TABLE `city`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `no_tax_days`
--
ALTER TABLE `no_tax_days`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tariff`
--
ALTER TABLE `tariff`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `vehicle`
--
ALTER TABLE `vehicle`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `city_vehicle`
--
ALTER TABLE `city_vehicle`
  ADD CONSTRAINT `FK6gxcrb31rg88jb5trenouy0kw` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`id`),
  ADD CONSTRAINT `FKlbpov6lv39gx94t0rbnyh6ke3` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`);

--
-- Constraints for table `no_tax_days`
--
ALTER TABLE `no_tax_days`
  ADD CONSTRAINT `FKn29qpqp4dr0k2s51vygvgle2o` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`);

--
-- Constraints for table `no_tax_month`
--
ALTER TABLE `no_tax_month`
  ADD CONSTRAINT `FK9c7go6rk65tywa1qerv86c8lj` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`);

--
-- Constraints for table `preference`
--
ALTER TABLE `preference`
  ADD CONSTRAINT `FKj2jmfemmo58h8sx771lbxavd9` FOREIGN KEY (`city_entity_id`) REFERENCES `city` (`id`);

--
-- Constraints for table `tariff`
--
ALTER TABLE `tariff`
  ADD CONSTRAINT `FKfw2gmot2dl3snaf8icecfecau` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`);

--
-- Constraints for table `tax_days`
--
ALTER TABLE `tax_days`
  ADD CONSTRAINT `FKnog0tbl7081enk9ce33tsdlpg` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

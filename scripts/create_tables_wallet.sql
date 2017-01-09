-- Original

CREATE TABLE 'inout' (
    uid         BIGINT
  , yearmonth   VARCHAR(10)
  , osseg       REAL
  , befizetes   VARCHAR(1)
  , datum       VARCHAR(10)
  , ido         VARCHAR(100)
  , cimke       VARCHAR(200)
  , pnem        VARCHAR(50)
  , etc         VARCHAR(500)
);

CREATE TABLE 'users' (
    uid       BIGINT
  , name      VARCHAR(100)
  , password  VARCHAR(100)
);

----------------------------------------------

-- Saved from phpMyAdmin

CREATE TABLE `wallet`.`users` ( `uid` BIGINT NOT NULL , `name` VARCHAR(100) NOT NULL , `password` VARCHAR(100) NOT NULL , PRIMARY KEY (`uid`)) ENGINE = MyISAM CHARSET=utf8 COLLATE utf8_hungarian_ci COMMENT = 'Wallet Users';

CREATE TABLE `wallet`.`inout` ( `uid` BIGINT NOT NULL , `yearmonth` VARCHAR(10) NOT NULL , `osszeg` REAL NOT NULL , `befizetes` VARCHAR(1) NULL , `datum` VARCHAR(10) NOT NULL , `ido` VARCHAR(100) NOT NULL , `cimke` VARCHAR(200) NULL , `pnem` VARCHAR(50) NOT NULL , `etc` VARCHAR(500) NULL ) ENGINE = MyISAM CHARSET=utf8 COLLATE utf8_hungarian_ci COMMENT = 'Wallet Others';

----------------------------------------------

-- Exported from phpMyAdmin


-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1
-- Létrehozás ideje: 2017. Jan 09. 09:53
-- Kiszolgáló verziója: 5.7.14
-- PHP verzió: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `wallet`
--

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `inout`
--

CREATE TABLE `inout` (
  `uid` bigint(20) NOT NULL,
  `yearmonth` varchar(10) COLLATE utf8_hungarian_ci NOT NULL,
  `osszeg` double NOT NULL,
  `befizetes` varchar(1) COLLATE utf8_hungarian_ci DEFAULT NULL,
  `datum` varchar(10) COLLATE utf8_hungarian_ci NOT NULL,
  `ido` varchar(100) COLLATE utf8_hungarian_ci NOT NULL,
  `cimke` varchar(200) COLLATE utf8_hungarian_ci DEFAULT NULL,
  `pnem` varchar(50) COLLATE utf8_hungarian_ci NOT NULL,
  `etc` varchar(500) COLLATE utf8_hungarian_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci COMMENT='Wallet Others';

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `users`
--

CREATE TABLE `users` (
  `uid` bigint(20) NOT NULL,
  `name` varchar(100) COLLATE utf8_hungarian_ci NOT NULL,
  `password` varchar(100) COLLATE utf8_hungarian_ci NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci COMMENT='Wallet Users';

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`uid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

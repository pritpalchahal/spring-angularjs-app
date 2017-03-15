SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: spring_angularjs_app_db
--

-- --------------------------------------------------------

--
-- Setup tables
--

CREATE TABLE IF NOT EXISTS Users (
  UserId char(30) NOT NULL,
  Password char(30) NOT NULL,
  FirstName char(30) NOT NULL,
  LastName char(30) NOT NULL,
  PRIMARY KEY(UserId),
  Created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
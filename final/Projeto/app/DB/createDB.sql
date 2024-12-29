SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema dss
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dss` ;
USE `dss` ;

-- -----------------------------------------------------
-- Table `dss`.`students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dss`.`students` (
  `id` INT NOT NULL,
  `password` TEXT NOT NULL,
  `type` INT NOT NULL,  -- 0 - normal, 1 - athlete, 2 - employed
  `course` INT,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_students_courses`
    FOREIGN KEY (`course`)
    REFERENCES `dss`.`courses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dss`.`ucs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dss`.`ucs` (
  `id` INT NOT NULL,
  `name` TEXT NOT NULL,
  `year` INT NOT NULL,
  `semester` INT NOT NULL,
  `policyPreference` TEXT,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dss`.`shifts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dss`.`shifts` (
  `id` INT NOT NULL,
  `capacityRoom` INT NOT NULL,
  `enrolledCount` INT NOT NULL,
  `type` INT NOT NULL,  -- 0 - theoretical, 1 - practical
  `capacity` INT,
  `uc` INT,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_shifts_ucs`
    FOREIGN KEY (`uc`)
    REFERENCES `dss`.`ucs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dss`.`timeslots`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dss`.`timeslots` (
  `id` INT NOT NULL,
  `time_start` TIME NOT NULL,
  `time_end` TIME NOT NULL,
  `weekDay` INT NOT NULL,
  `shift` INT,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_timeslots_shifts`
    FOREIGN KEY (`shift`)
    REFERENCES `dss`.`shifts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dss`.`courses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dss`.`courses` (
  `id` INT NOT NULL,
  `name` TEXT NOT NULL,
  `visibilitySchedules` BOOLEAN NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dss`.`course_director`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dss`.`course_director` (
  `id` INT NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `course_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_course_director_courses`
    FOREIGN KEY (`course_id`)
    REFERENCES `dss`.`courses` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dss`.`students_ucs`
-- -----------------------------------------------------
CREATE TABLE student_ucs (
    student_id INT NOT NULL,
    uc_id INT NOT NULL,
    PRIMARY KEY (student_id, uc_id),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (uc_id) REFERENCES ucs(id)
);

-- -----------------------------------------------------
-- Table `dss`.`students_schedule`
-- -----------------------------------------------------
CREATE TABLE student_schedule (
    student_id INT NOT NULL,
    uc_id INT NOT NULL,
    shift_id INT NOT NULL,
    PRIMARY KEY (student_id, uc_id, shift_id),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (uc_id) REFERENCES ucs(id),
    FOREIGN KEY (shift_id) REFERENCES shifts(id)
);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

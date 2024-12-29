
-- Inserir dados na tabela `courses`
INSERT INTO `dss`.`courses` (`id`, `name`, `visibilitySchedules`)
VALUES
(1, 'Engenharia Informática', TRUE),
(2, 'Engenharia Mecânica', TRUE),
(3, 'Gestão', FALSE);

-- Inserir dados na tabela `students`
INSERT INTO `dss`.`students` (`id`, `password`, `type`, `course`)
VALUES
(101, 'password123', 0, 1),  -- Estudante normal no curso de Engenharia Informática
(102, 'password456', 1, 2),  -- Estudante atleta no curso de Engenharia Mecânica
(103, 'password789', 2, 3);  -- Estudante empregado no curso de Gestão

-- Inserir dados na tabela `ucs`
INSERT INTO `dss`.`ucs` (`id`, `name`, `year`, `semester`, `policyPreference`)
VALUES
(1, 'Matemática I', 1, 1, 'Preferência teórica'),
(2, 'Programação', 1, 2, 'Preferência prática'),
(3, 'Física', 2, 1, 'Preferência teórica');

-- Inserir dados na tabela `shifts`
INSERT INTO `dss`.`shifts` (`id`, `capacityRoom`, `enrolledCount`, `type`, `capacity`, `uc`)
VALUES
(1, 101, 30, 0, 40, 1),  -- Turma teórica de Matemática I
(2, 102, 25, 1, 30, 2),  -- Turma prática de Programação
(3, 103, 20, 0, 30, 3);  -- Turma teórica de Física

-- Inserir dados na tabela `timeslots`
INSERT INTO `dss`.`timeslots` (`id`, `time_start`, `time_end`, `weekDay`, `shift`)
VALUES
(1, '08:00:00', '10:00:00', 1, 1),  -- Segunda-feira, Turma de Matemática I
(2, '10:30:00', '12:30:00', 2, 2),  -- Terça-feira, Turma de Programação
(3, '14:00:00', '16:00:00', 3, 3);  -- Quarta-feira, Turma de Física

-- Inserir dados na tabela `course_director`
INSERT INTO `dss`.`course_director` (`id`, `password`, `course_id`)
VALUES
(1, 'directorpassword1', 1),  -- Diretor do curso de Engenharia Informática
(2, 'directorpassword2', 2),  -- Diretor do curso de Engenharia Mecânica
(3, 'directorpassword3', 3);  -- Diretor do curso de Gestão
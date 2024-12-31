
-- Inserir dados na tabela `courses`
INSERT INTO `dss`.`courses` (`id`, `name`, `visibilitySchedules`)
VALUES
(1, 'Engenharia Informática', TRUE),
(2, 'Engenharia Mecânica', TRUE),
(3, 'Gestão', FALSE);

-- Inserir dados na tabela `course_director`
INSERT INTO `dss`.`course_director` (`id`, `password`, `course_id`)
VALUES
(1, 'directorpassword1', 1),  -- Diretor do curso de Engenharia Informática
(2, 'directorpassword2', 2),  -- Diretor do curso de Engenharia Mecânica
(3, 'directorpassword3', 3);  -- Diretor do curso de Gestão
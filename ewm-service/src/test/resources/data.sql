INSERT INTO users(name, email) VALUES
('user1', 'user1@user'),
('user2', 'user2@user'),
('user3', 'user3@user'),
('user4', 'user4@user'),
('user5', 'user5@user'),
('user6', 'user6@user'),
('user7', 'user7@user'),
('user8', 'user8@user'),
('user9', 'user9@user'),
('user10', 'user10@user'),
('user11', 'user11@user'),
('user12', 'user12@user');

INSERT INTO category(name) VALUES
('Category');



INSERT INTO events(title, annotation, category_id, description, event_date, is_paid, participant_limit, request_moderation, state, views, initiator_id) VALUES
('title', 'annotation', 1, 'description', '2025-10-15 15:00:00', true, 50, true, 'PUBLISHED', 30, 3),
('title1', 'annotation2', 1, 'description2', '2025-05-10 12:00:00', true, 70, true, 'PUBLISHED', 17, 4),
('title3', 'annotation3', 1, 'description3', '2025-03-14 10:00:00', false, 100, true, 'PUBLISHED', 22, 7);

INSERT INTO compilations(pinned, title) VALUES
(false, 'compilation1'),
(true, 'compilation2'),
(true, 'compilation3');

INSERT INTO compilation_events(compilation_id, event_id) VALUES
(1, 1),
(2, 1),
(3, 2);
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

INSERT INTO events(title, annotation, description, state) VALUES
('title', 'annotation', 'description', 'state'),
('title1', 'annotation2', 'description2', 'state2'),
('title3', 'annotation3', 'description3', 'state3');

INSERT INTO compilations(pinned, title) VALUES
(false, 'compilation1'),
(true, 'compilation2'),
(true, 'compilation3');

INSERT INTO compilation_events(compilation_id, event_id) VALUES
(1, 1),
(2, 1),
(3, 2);
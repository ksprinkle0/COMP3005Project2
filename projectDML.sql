INSERT INTO Member (memberID, username, password, name, email, age)
VALUES (8756, 'karen123', 'password1', 'Karen', 'karen@gmail.com', 30),
       (9034, 'will000', 'password2', 'Will', 'william@gmail.com', 25),
       (1234, 'anteater12', 'password3', 'Bob', 'bobthebob@gmail.com', 35);

INSERT INTO Trainer (trainerID, code, name)
VALUES ('101', '1111', 'Jim'),
       ('102', '2222', 'Lisa'),
       ('103', '3333', 'Terry');

INSERT INTO Admin (code)
VALUES ('0000');

INSERT INTO MemberDashboard (memberID, exercise_routine, fitness_goal, weight, steps, calories_burnt, activity)
VALUES (8756, '20 push ups, 30 sit ups, 100 jumping jacks', 'Lose 10lbs', 150, 5000, 2000, 60),
       (9034, 'Jogging, Weightlifting', 'Gain muscle', 180, 7000, 2500, 90),
       (1234, 'Swimming, Cycling', 'Run 10 miles', 160, 6000, 2200, 75);

INSERT INTO TrainerAvailability (trainerID, trainer_name, hour)
VALUES ('101', 'Jim', '06:00'),
       ('101', 'Jim', '07:00'),
       ('101', 'Jim', '08:00'),
       ('101', 'Jim', '09:00'),
       ('101', 'Jim', '10:00'),
       ('102', 'Lisa', '06:00'),
       ('102', 'Lisa', '07:00'),
       ('102', 'Lisa', '16:00'),
       ('102', 'Lisa', '17:00'),
       ('102', 'Lisa', '18:00'),
       ('103', 'Terry', '13:00'),
       ('103', 'Terry', '14:00'),
       ('103', 'Terry', '15:00'),
       ('103', 'Terry', '16:00'),
       ('103', 'Terry', '20:00'),
       ('103', 'Terry', '21:00');

INSERT INTO Bookings (member_name, time, day)
VALUES ('Karen', '08:00', '2024-04-10'),
       ('Will', '16:00', '2024-04-10'),
       ('Bob', '13:00', '2024-04-11'),
       ('Karen', '07:00', '2024-04-12');

INSERT INTO Equipment (name, status, maintenance_history)
VALUES ('Dumbbells', 'Good', ARRAY['2024-03-01: Cleaned']),
       ('Treadmill', 'Good', ARRAY['2024-02-20: Serviced', '2024-03-10: Inspected']),
       ('Exercise bike', 'Good', ARRAY['2024-01-10: Replaced pedals', '2024-03-20: Replaced machine']),
       ('Elliptical', 'Bad', ARRAY['2023-12-01: Reparied']),
       ('Rower', 'Good', ARRAY['2024-02-15: Replaced seat']),
       ('Bench press', 'Good', ARRAY['2024-01-25: Tightened bolts']),
       ('Cable machine', 'Bad', ARRAY['2024-01-20: Serviced', '2024-03-18: Serviced']),
       ('Leg extension', 'Good', ARRAY['2024-02-10: Cleaned', '2024-03-25: Serviced']),
       ('Stair master', 'Good', ARRAY['2024-03-05: Replaced', '2024-04-01: Inspected']),
       ('Lat pull', 'Good', ARRAY['2024-02-28: Inspected']);
	   
INSERT INTO ManageClassSchedule (classID, class_name, class_day, class_time)
VALUES (567, 'Yoga', '2024-04-10', '08:00'),
       (568, 'Yoga', '2024-04-17', '08:00'),
       (569, 'Zumba', '2024-04-11', '18:00'),
       (570, 'Pilates', '2024-04-12', '16:00'),
       (571, 'Boxing', '2024-04-14', '17:00');

INSERT INTO ManageSessionSchedule (sessionID, session_time, session_day, session_availability, trainerID, trainer_name)
VALUES (893, '08:00', '2024-04-10', TRUE, '101', 'Jim'),
       (894, '16:00', '2024-04-10', FALSE, '102', 'Lisa'),
       (895, '13:00', '2024-04-11', FALSE, '103', 'Terry'),
       (896, '07:00', '2024-04-12', TRUE, '101', 'Jim'),
       (897, '17:00', '2024-04-14', TRUE, '102', 'Lisa');

INSERT INTO MemberClassSchedule (memberID, classID, class_day, class_time, class_name)
VALUES 
    (8756, 567, '2024-04-10', '08:00', 'Yoga');

INSERT INTO MemberSessionSchedule (memberID, sessionID, session_day, session_time, trainerID, trainer_name)
VALUES 
    (9034, 894, '2024-04-10', '16:00', '102', 'Lisa'),
    (1234, 895, '2024-04-11', '13:00', '103', 'Terry');

INSERT INTO TrainerSchedule (sessionID, session_day, session_time, trainerID, memberID, member_name)
VALUES (894, '2024-04-10', '16:00', '102', 9034, 'Will'),
       (895, '2024-04-11', '13:00', '103', 1234, 'Bob');

INSERT INTO MemberBilling (memberID, member_name, bill_type, cost)
VALUES (8756, 'Karen', 'class', 30),
       (9034, 'Will', 'Session', 50),
       (1234, 'Bob', 'Session', 50);

INSERT INTO TrainerPayment (trainerID, sessionID)
VALUES ('102', 894),
       ('103', 895);
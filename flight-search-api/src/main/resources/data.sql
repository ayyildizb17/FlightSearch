INSERT INTO airport (id,city) VALUES
  (UUID(),'Istanbul'),
  (UUID(),'Ankara'),
  (UUID(),'Izmir');


INSERT INTO flight (id,departure_date, return_date, price, departure_airport_id, arrival_airport_id) VALUES
  (UUID(),'2024-02-04 12:54',null, 500.00, (SELECT id FROM airport WHERE city = 'Izmir'), (SELECT id FROM airport WHERE city = 'Ankara')),
  (UUID(),'2024-02-05 02:33', '2024-02-11 13:48', 600.00, (SELECT id FROM airport WHERE city = 'Ankara'), (SELECT id FROM airport WHERE city = 'Istanbul')),
  (UUID(),'2024-02-06 20:23',null , 700.00, (SELECT id FROM airport WHERE city = 'Istanbul'), (SELECT id FROM airport WHERE city = 'Izmir'));

INSERT INTO airport (id, city) VALUES
(UUID(), 'New York'),
(UUID(), 'London'),
(UUID(), 'Los Angeles'),
(UUID(), 'Paris'),
(UUID(), 'San Francisco'),
(UUID(), 'Amsterdam'),
(UUID(), 'Dublin'),
(UUID(), 'Barcelona'),
(UUID(), 'Chicago'),
(UUID(), 'Hong Kong'),
(UUID(), 'Atlanta'),
(UUID(), 'Sydney'),
(UUID(), 'Dallas'),
(UUID(), 'Seoul'),
(UUID(), 'Singapore'),
(UUID(), 'Bangkok'),
(UUID(), 'Tokyo');
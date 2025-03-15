--drone
CREATE TABLE IF NOT EXISTS Drone (
    serial_number VARCHAR(100) PRIMARY KEY,
    drone_model VARCHAR(50),
    weight_limit INT ,
    battery_capacity INT,
    drone_state VARCHAR(50)
);
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE001', 'LIGHTWEIGHT', 100, 100, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE002', 'MIDDLEWEIGHT', 250, 75, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE003', 'CRUISERWEIGHT', 400, 50, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE004', 'HEAVYWEIGHT', 1000, 25, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE005', 'HEAVYWEIGHT', 1000, 25, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE006', 'LIGHTWEIGHT', 100, 100, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE007', 'MIDDLEWEIGHT', 250, 75, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE008', 'CRUISERWEIGHT', 400, 50, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE009', 'HEAVYWEIGHT', 1000, 25, 'IDLE');
INSERT INTO Drone (serial_number, drone_model, weight_limit, battery_capacity, drone_state) VALUES ('DRONE010', 'CRUISERWEIGHT', 400, 50, 'IDLE');

--medication
CREATE TABLE IF NOT EXISTS Medication (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    weight INT NOT NULL,
    code VARCHAR(20) NOT NULL,
    image VARCHAR(20),
    drone_serial_number VARCHAR(100)
);
INSERT INTO Medication (id, name, weight, code, image, drone_serial_number) VALUES (1, 'Paracetamol', 20, 'CODE1', 'image1.jpg', '');
INSERT INTO Medication (id, name, weight, code, image, drone_serial_number) VALUES (2, 'Ibuprofen', 30, 'CODE2', 'image2.jpg', '');
INSERT INTO Medication (id, name, weight, code, image, drone_serial_number) VALUES (3, 'Aspirin', 40, 'CODE3', 'image3.jpg', '');
INSERT INTO Medication (id, name, weight, code, image, drone_serial_number) VALUES (4, 'Methformin', 50, 'CODE4', 'image4.jpg', '');


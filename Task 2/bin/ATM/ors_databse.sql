use ors;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    train_number VARCHAR(20) NOT NULL,
    train_name VARCHAR(100) NOT NULL,
    class_type VARCHAR(50) NOT NULL,
    date_of_journey DATE NOT NULL,
    from_place VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL
);



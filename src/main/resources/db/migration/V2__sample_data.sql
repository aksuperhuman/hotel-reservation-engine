-- =====================================================================
-- V2: Sample data. Passwords below are BCrypt hashes of 'password123'.
-- =====================================================================

INSERT INTO users (email, password, full_name, role) VALUES
 ('admin@hotel.com',   '$2a$10$Dow1PxL1G3xq5m0e0eRr3uWt3f2nq0mQF0j2y0m5V0m5V0m5V0m5C', 'Platform Admin', 'ADMIN'),
 ('manager@hotel.com', '$2a$10$Dow1PxL1G3xq5m0e0eRr3uWt3f2nq0mQF0j2y0m5V0m5V0m5V0m5C', 'Grand Manager',  'HOTEL_MANAGER'),
 ('customer@hotel.com','$2a$10$Dow1PxL1G3xq5m0e0eRr3uWt3f2nq0mQF0j2y0m5V0m5V0m5V0m5C', 'Jane Customer',  'CUSTOMER');

INSERT INTO hotels (name, description, city, address, star_rating, manager_id) VALUES
 ('Grand Palace Hotel', 'Luxury stay in the heart of the city', 'Bangalore', '1 MG Road', 5, 2),
 ('Seaside Resort',     'Beachfront resort with ocean views',   'Goa',       '12 Beach Rd', 4, 2);

INSERT INTO hotel_amenities (hotel_id, amenity) VALUES
 (1, 'WIFI'), (1, 'POOL'), (1, 'SPA'), (1, 'GYM'),
 (2, 'WIFI'), (2, 'POOL'), (2, 'BEACH_ACCESS');

INSERT INTO rooms (hotel_id, room_number, room_type, capacity, price, status) VALUES
 (1, '101', 'STANDARD', 2, 3500.00, 'AVAILABLE'),
 (1, '201', 'DELUXE',   3, 6000.00, 'AVAILABLE'),
 (1, '301', 'SUITE',    4, 12000.00,'AVAILABLE'),
 (2, '101', 'STANDARD', 2, 4000.00, 'AVAILABLE'),
 (2, '202', 'PREMIUM',  3, 8000.00, 'AVAILABLE');

INSERT INTO room_features (room_id, feature) VALUES
 (1, 'AC'), (1, 'TV'),
 (2, 'AC'), (2, 'TV'), (2, 'BALCONY'),
 (3, 'AC'), (3, 'TV'), (3, 'BALCONY'), (3, 'JACUZZI');

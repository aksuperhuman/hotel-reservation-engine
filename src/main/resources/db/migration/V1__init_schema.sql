-- =====================================================================
-- V1: Core schema for the High-Concurrency Hotel Reservation Engine
-- =====================================================================

CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255) NOT NULL,
    role            VARCHAR(32)  NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE refresh_tokens (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token           VARCHAR(512) NOT NULL UNIQUE,
    expires_at      TIMESTAMP    NOT NULL,
    revoked         BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE hotels (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    city            VARCHAR(128) NOT NULL,
    address         VARCHAR(512),
    star_rating     INT          NOT NULL DEFAULT 0,
    manager_id      BIGINT       REFERENCES users(id),
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_hotels_city ON hotels(city);
CREATE INDEX idx_hotels_star_rating ON hotels(star_rating);

CREATE TABLE hotel_amenities (
    hotel_id        BIGINT       NOT NULL REFERENCES hotels(id) ON DELETE CASCADE,
    amenity         VARCHAR(64)  NOT NULL,
    PRIMARY KEY (hotel_id, amenity)
);

CREATE TABLE hotel_images (
    id              BIGSERIAL PRIMARY KEY,
    hotel_id        BIGINT       NOT NULL REFERENCES hotels(id) ON DELETE CASCADE,
    url             VARCHAR(1024) NOT NULL
);

CREATE TABLE rooms (
    id              BIGSERIAL PRIMARY KEY,
    hotel_id        BIGINT       NOT NULL REFERENCES hotels(id) ON DELETE CASCADE,
    room_number     VARCHAR(32)  NOT NULL,
    room_type       VARCHAR(32)  NOT NULL,
    capacity        INT          NOT NULL,
    price           NUMERIC(12,2) NOT NULL,
    status          VARCHAR(32)  NOT NULL DEFAULT 'AVAILABLE',
    -- version column powers JPA optimistic locking (@Version)
    version         BIGINT       NOT NULL DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    UNIQUE (hotel_id, room_number)
);
CREATE INDEX idx_rooms_hotel ON rooms(hotel_id);
CREATE INDEX idx_rooms_type ON rooms(room_type);

CREATE TABLE room_features (
    room_id         BIGINT       NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    feature         VARCHAR(64)  NOT NULL,
    PRIMARY KEY (room_id, feature)
);

CREATE TABLE reservations (
    id              BIGSERIAL PRIMARY KEY,
    reference       VARCHAR(64)  NOT NULL UNIQUE,
    user_id         BIGINT       NOT NULL REFERENCES users(id),
    room_id         BIGINT       NOT NULL REFERENCES rooms(id),
    check_in        DATE         NOT NULL,
    check_out       DATE         NOT NULL,
    guests          INT          NOT NULL,
    total_price     NUMERIC(12,2) NOT NULL,
    status          VARCHAR(32)  NOT NULL,
    hold_expires_at TIMESTAMP,
    version         BIGINT       NOT NULL DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT chk_dates CHECK (check_out > check_in)
);
CREATE INDEX idx_reservations_room ON reservations(room_id);
CREATE INDEX idx_reservations_user ON reservations(user_id);
CREATE INDEX idx_reservations_status ON reservations(status);

-- Prevents overlapping active bookings for the same room at the database level.
-- This is the last line of defence behind the application-level locking strategies.
CREATE TABLE room_date_locks (
    room_id         BIGINT       NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    booked_date     DATE         NOT NULL,
    reservation_id  BIGINT       NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    PRIMARY KEY (room_id, booked_date)
);

CREATE TABLE payments (
    id              BIGSERIAL PRIMARY KEY,
    reservation_id  BIGINT       NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    provider        VARCHAR(32)  NOT NULL,
    amount          NUMERIC(12,2) NOT NULL,
    status          VARCHAR(32)  NOT NULL,
    transaction_ref VARCHAR(128),
    attempts        INT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_payments_reservation ON payments(reservation_id);

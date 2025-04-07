CREATE TABLE IF NOT EXISTS rooms (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255),
    capacity INTEGER,
    price_per_night INTEGER,
    hotel_id INTEGER NOT NULL,
    CONSTRAINT fk_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

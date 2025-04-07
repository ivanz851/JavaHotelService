CREATE TABLE book (
      id BIGSERIAL PRIMARY KEY,
      hotel_id BIGINT NOT NULL,
      room_id BIGINT NOT NULL,
      user_id BIGINT NOT NULL
);

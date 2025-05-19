package com.example.hotel_service.service;

import com.example.hotel.grpc.CheckRoomAvailabilityRequest;
import com.example.hotel.grpc.CheckRoomAvailabilityResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GrpcServiceHotelTest {

    @Test
    @DisplayName("roomPrice – возвращает цену")
    void roomPrice() {
        GrpcServiceHotel service = new GrpcServiceHotel();

        CheckRoomAvailabilityRequest req = CheckRoomAvailabilityRequest.newBuilder()
                .setHotelId(7)
                .setRoomId(12)
                .build();

        @SuppressWarnings("unchecked")
        StreamObserver<CheckRoomAvailabilityResponse> obs =
                mock(StreamObserver.class);

        ArgumentCaptor<CheckRoomAvailabilityResponse> captor =
                ArgumentCaptor.forClass(CheckRoomAvailabilityResponse.class);

        service.roomPrice(req, obs);

        verify(obs).onNext(captor.capture());
        verify(obs).onCompleted();

        assertThat(captor.getValue().getPrice()).isEqualTo(180.0f);
    }
}

package com.example.booking_service.service;

import com.example.hotel.grpc.CheckRoomAvailabilityRequest;
import com.example.hotel.grpc.CheckRoomAvailabilityResponse;
import com.example.hotel.grpc.HotelServiceGrpc;
import io.grpc.ManagedChannel;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class GrpcServiceBookTest {

    @Test
    void getRoomPrice_returnsPriceFromGrpc() {
        CheckRoomAvailabilityResponse grpcResponse =
                CheckRoomAvailabilityResponse.newBuilder()
                        .setPrice(199.99F)
                        .build();

        HotelServiceGrpc.HotelServiceBlockingStub stub =
                mock(HotelServiceGrpc.HotelServiceBlockingStub.class);
        when(stub.roomPrice(any(CheckRoomAvailabilityRequest.class)))
                .thenReturn(grpcResponse);

        try (MockedStatic<HotelServiceGrpc> mockedGrpc = mockStatic(HotelServiceGrpc.class)) {
            mockedGrpc.when(() -> HotelServiceGrpc.newBlockingStub(any(ManagedChannel.class)))
                    .thenReturn(stub);

            GrpcServiceBook service = new GrpcServiceBook("localhost", 0);

            float actual = service.getRoomPrice(1L, 2L);

            assertThat(actual).isEqualTo(199.99F);
            verify(stub).roomPrice(any(CheckRoomAvailabilityRequest.class));
        }
    }
}

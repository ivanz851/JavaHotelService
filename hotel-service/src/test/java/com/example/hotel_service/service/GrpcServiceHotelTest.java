package com.example.hotel_service.service;

import com.example.hotel.grpc.CheckRoomAvailabilityRequest;
import com.example.hotel.grpc.HotelServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerInterceptors;
import io.grpc.ServerServiceDefinition;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


class GrpcServiceHotelTest {

    private ManagedChannel channel;
    private HotelServiceGrpc.HotelServiceBlockingStub stub;

    @BeforeEach
    void setup() throws IOException {
        channel = InProcessChannelBuilder.forName("test").directExecutor().build();
        ServerServiceDefinition service = ServerInterceptors.intercept(
                new GrpcServiceHotel()
        );

        Server server = InProcessServerBuilder.forName("test")
                .directExecutor()
                .addService(service)
                .build()
                .start();

        stub = HotelServiceGrpc.newBlockingStub(channel);
    }

    @Test
    void testRoomPrice() {
        var request = CheckRoomAvailabilityRequest.newBuilder()
                .setHotelId(1)
                .setRoomId(2)
                .build();

        var response = stub.roomPrice(request);
        float expected = 100.0f + (1 % 10) * 10 + (2 % 5) * 5;

        assertEquals(expected, response.getPrice(), 0.001f);

    }

    @AfterEach
    void teardown() {
        channel.shutdownNow();
    }
}

package com.example.booking_service.service;

import com.example.hotel.grpc.CheckRoomAvailabilityRequest;
import com.example.hotel.grpc.CheckRoomAvailabilityResponse;
import com.example.hotel.grpc.HotelServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class GrpcServiceBook {

    private final ManagedChannel channel;
    private final HotelServiceGrpc.HotelServiceBlockingStub blockingStub;

    public GrpcServiceBook(@Value("${grpc.hotel.host}") String host,
                           @Value("${grpc.hotel.port}") int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = HotelServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public float getRoomPrice(long hotelId, long roomId) {
        CheckRoomAvailabilityRequest request = CheckRoomAvailabilityRequest.newBuilder()
                .setHotelId(hotelId)
                .setRoomId(roomId)
                .build();

        CheckRoomAvailabilityResponse response = blockingStub.roomPrice(request);
        return response.getPrice();
    }
}

package com.example.booking_service.service;

import com.example.hotel.grpc.CheckRoomAvailabilityRequest;
import com.example.hotel.grpc.CheckRoomAvailabilityResponse;
import com.example.hotel.grpc.HotelServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class GrpcServiceBook extends HotelServiceGrpc.HotelServiceImplBase {

    private final ManagedChannel channel;
    private final HotelServiceGrpc.HotelServiceBlockingStub blockingStub;

    public GrpcServiceBook(String host, int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = HotelServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void getRoomPrice(long hotelId, long roomId) {
        CheckRoomAvailabilityRequest request = CheckRoomAvailabilityRequest.newBuilder()
                .setHotelId(hotelId)
                .setRoomId(roomId)
                .build();

        try {
            CheckRoomAvailabilityResponse response = blockingStub.roomPrice(request);
            System.out.printf("Цена за номер: %.2f%n", response.getPrice());
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus());
        }
    }
}

package com.example.hotel_service.service;

import com.example.hotel.grpc.CheckRoomAvailabilityRequest;
import com.example.hotel.grpc.CheckRoomAvailabilityResponse;
import com.example.hotel.grpc.HotelServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class GrpcServiceHotel extends HotelServiceGrpc.HotelServiceImplBase {
    @Override
    public void roomPrice(CheckRoomAvailabilityRequest request,
                          StreamObserver<CheckRoomAvailabilityResponse> responseObserver) {

        long hotelId = request.getHotelId();
        long roomId = request.getRoomId();

        float price = calculatePrice(hotelId, roomId);

        CheckRoomAvailabilityResponse response = CheckRoomAvailabilityResponse.newBuilder()
                .setPrice(price)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private float calculatePrice(long hotelId, long roomId) {
        return 100.0f + (hotelId % 10) * 10 + (roomId % 5) * 5;
    }

}

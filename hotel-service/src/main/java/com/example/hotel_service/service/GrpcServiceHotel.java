package com.example.hotel_service.service;

import com.example.hotel.grpc.CheckRoomAvailabilityRequest;
import com.example.hotel.grpc.CheckRoomAvailabilityResponse;
import com.example.hotel.grpc.HotelServiceGrpc;

import com.example.hotel_service.model.Room;
import com.example.hotel_service.repository.RoomRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class GrpcServiceHotel extends HotelServiceGrpc.HotelServiceImplBase {


    private final RoomRepository roomRepository;

    public GrpcServiceHotel(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void roomPrice(CheckRoomAvailabilityRequest request,
                          StreamObserver<CheckRoomAvailabilityResponse> responseObserver) {

        long hotelId = request.getHotelId();
        long roomId = request.getRoomId();

        try {
            float price = calculatePrice(hotelId, roomId);

            CheckRoomAvailabilityResponse response = CheckRoomAvailabilityResponse.newBuilder()
                    .setPrice(price)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Room not found").asException());
        }
    }

    private float calculatePrice(long hotelId, long roomId) {
        Room room = roomRepository.findById(roomId).
                orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return room.getPricePerNight().floatValue();
    }

}

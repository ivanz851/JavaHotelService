package com.example.hotel_service.service;

import com.example.hotel.grpc.CheckRoomAvailabilityRequest;
import com.example.hotel.grpc.CheckRoomAvailabilityResponse;
import com.example.hotel.grpc.HotelServiceGrpc;

import com.example.hotel_service.dto.RoomResponse;
import com.example.hotel_service.model.Room;
import com.example.hotel_service.repository.HotelRepository;
import com.example.hotel_service.repository.RoomRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;


@GrpcService
public class GrpcServiceHotel extends HotelServiceGrpc.HotelServiceImplBase {

    private final HotelService hotelService;

    public GrpcServiceHotel(HotelService hotelService) {
        this.hotelService = hotelService;
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
            responseObserver.onError(mapExceptionToStatus(e).asException());
        }
    }

    private Status mapExceptionToStatus(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return Status.NOT_FOUND.withDescription(e.getMessage());
        } else if (e instanceof IllegalStateException) {
            return Status.FAILED_PRECONDITION.withDescription(e.getMessage());
        } else if (e instanceof UnsupportedOperationException) {
            return Status.UNIMPLEMENTED.withDescription(e.getMessage());
        } else {
            return Status.INTERNAL.withDescription("Internal server error")
                    .withCause(e);
        }
    }

    private float calculatePrice(long hotelId, long roomId) {

        List<RoomResponse> rooms = hotelService.getRoomsByHotel(hotelId);

        return rooms.stream()
                .filter(room -> room.id().equals(roomId))
                .findFirst()
                .map(RoomResponse::pricePerNight)
                .orElseThrow(() -> new IllegalArgumentException("Room not found in specified hotel"))
                .floatValue();
    }

}

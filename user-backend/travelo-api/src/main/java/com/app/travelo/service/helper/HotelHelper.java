package com.app.travelo.service.helper;

import com.app.travelo.model.entity.*;
import com.app.travelo.model.enums.BookingStatus;
import com.app.travelo.model.rest.*;
import com.app.travelo.repository.BookingRepository;
import com.app.travelo.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelHelper {
    @Autowired
    private BookingRepository bookingRepo;
    
    public  HotelDto toHotelDto(HotelEntity htl){
        return HotelDto.builder()
                .hotelCode(htl.getHotelCode())
                .hotelName(htl.getHotelName())
                .state(htl.getState())
                .city(htl.getCity())
                .address(htl.getAddress())
                .starRating(htl.getStarRating())
                .hotelImages(toHotelImageDto(htl.getHotelImages()))
                .rooms(toHotelRoomsDto(htl.getRooms()))
                .location(LocationDto.builder().locationId(htl.getLocation().getLocationId())
                        .location(htl.getLocation().getLocation())
                        .build())
                .country(htl.getCountry())
                .postalCode(htl.getPostalCode())

                .about(htl.getAbout())
                .propertyRule(htl.getPropertyRule())
                .websiteLink(htl.getWebsiteLink())
                .email(htl.getEmail())
                .amenities(htl.getAmenities().stream().map(HotelAmenityEntity::getAmenity).collect(Collectors.toList()))

                .build();
    }

    public  List<HotelRoomDto> toHotelRoomsDto(List<HotelRoomEntity> rooms) {
        return rooms.stream().map(rm-> {
                    List<BookingEntity> booking = bookingRepo.getBookingsForRoomId(rm.getHotelRoomId(), BookingStatus.BOOKED.name());
                    int booked = 0;
                    if(!booking.isEmpty()) {
                        booked = booking.stream().mapToInt(bk->bk.getBookingDetails().stream().mapToInt(BookingDetailsEntity::getNoOfRooms).sum()).sum();
                    }
                    return  HotelRoomDto.builder()
                            .hotelRoomId(rm.getHotelRoomId())
                            .price(rm.getPrice())
                            .hotelCode(rm.getHotelCode().getHotelCode())
                            .roomDescription(rm.getRoomDescription())
                            .occupancy(rm.getOccupancy())
                            .priceSlab(toPriceSlabRoomList(rm.getPriceSlab()))
                            .roomName(rm.getRoomName())
                            .roomImages(toRoomImageList(rm.getRoomImages()))
                            .bedType(rm.getBedType())
                            .amenities(rm.getAmenities().stream().map(RoomAmenityEntity::getAmenity).collect(Collectors.toList()))
                            .roomTags(rm.getRoomTags().stream().map(RoomTagEntity::getRoomTag).collect(Collectors.toList()))
                            .noOfRooms(rm.getNoOfRooms())
                            .noOfAvailableRooms( rm.getNoOfRooms() - booked)
                            .build();
                }

        ).collect(Collectors.toList());
    }

    private  List<HotelImageDto> toRoomImageList(List<RoomImageEntity> roomImages) {
        return roomImages.stream().map(img->HotelImageDto.builder()
                        .imageId(img.getImageId())
                        .image(ImageUtil.decompressBytes(img.getRoomImage()))
                        .build())
                .collect(Collectors.toList());
    }


    private  List<PriceSlabRoomDto> toPriceSlabRoomList(List<PriceSlabRoomsEntity> priceSlabRoomList) {
        return priceSlabRoomList.stream().map(ps->
                PriceSlabRoomDto.builder()
                        .id(ps.getId())
                        .hotelRoomId(ps.getHotelRoomId().getHotelRoomId())
                        .hotelCode(ps.getHotelCode().getHotelCode())
                        .priceSlabId(PriceSlabDto.builder()
                                .id(ps.getPriceSlabId().getId())
                                .priceSlab(ps.getPriceSlabId().getPriceSlab())
                                .maxAllowedGuest(ps.getPriceSlabId().getMaxAllowedGuest())
                                .maxAllowedRoom(ps.getPriceSlabId().getMaxAllowedRoom())
                                .noOfNights(ps.getPriceSlabId().getNoOfNights())
                                .build())
                        .build()

        ).collect(Collectors.toList());
    }
    private  List<HotelImageDto> toHotelImageDto(List<HotelImageEntity> hotelImages) {
        return hotelImages.stream().map(img->HotelImageDto.builder()
                        .imageId(img.getImageId())
//                        .image(Base64.getEncoder().encodeToString(img.getHotelImage()))
                        .image(ImageUtil.decompressBytes(img.getHotelImage()))
                        .build())
                .collect(Collectors.toList());
    }
}

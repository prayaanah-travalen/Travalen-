package com.app.travelo.services.impl;

import com.app.travelo.model.entity.*;
import com.app.travelo.model.enums.BookingStatus;
import com.app.travelo.model.rest.*;
import com.app.travelo.repositories.*;
import com.app.travelo.services.HotelService;
import com.app.travelo.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Slf4j
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private PriceSlabRepository priceSlabRepo;

    @Autowired
    private LocationRepository locationRepo;

    @Autowired
    private HotelRoomRepository hotelRoomRepo;

    @Autowired
    private HotelImageRepository hotelImageRepo;

    @Autowired
    private RoomImageRepository roomImageRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private RoomAmenityRepository roomAmenityRepo;

    @Autowired
    private RoomTagRepository roomTagRepo;

    @Autowired
    private HotelAmenityRepository hotelAmenityRepo;

    @Autowired
    private RoomPriceSlabRepository roomPriceSlabRepo;

    @Transactional
    @Override
    public ResponseDto<HotelDto> saveHotel(HotelRequestDto hotel, List<MultipartFile> hotelImages) {
        ResponseDto<HotelDto> response = new ResponseDto<>();
        try {
            LocationEntity location = locationRepo.findByLocation(hotel.getLocation());
            LocationEntity locationEntity = null;
            if (Objects.nonNull(location)) {
                locationEntity = location;
            } else {
                locationEntity = locationRepo.save(LocationEntity.builder().location(hotel.getLocation()).build());
            }

            deletedHotelImages(hotel.getDeletedImages()); /* Delete hotel images **/

            HotelEntity htlEntity = HotelEntity.builder()
                    .hotelName(hotel.getHotelName())
                    .location(locationEntity)
                    .city(hotel.getCity())
                    .state(hotel.getState())
                    .address(hotel.getAddress())
                    .country(hotel.getCountry())
                    .postalCode(hotel.getPostalCode())
                    .starRating(hotel.getStarRating())
                    .about(hotel.getAbout())
                    .email(hotel.getEmail())
                    .websiteLink(hotel.getWebsiteLink())
                    .user(getUserDetails())
                    .latitude(hotel.getLatitude())
                    .longitude(hotel.getLongitude())
                    .propertyRule(hotel.getPropertyRule())
                    .availability(true)
                    .build();
            htlEntity.setAmenities(toHotelAmenities(hotel.getAmenities(), htlEntity));

            List<HotelImageEntity> images = new ArrayList<>();
            if(Objects.nonNull(hotelImages))  images = toHotelImageEntityFromMultipart(hotelImages, htlEntity);
            /* Update existing hotel **/
            if(Objects.nonNull(hotel.getHotelCode())) {

                List<HotelAmenityEntity> hotelAmenities = hotelAmenityRepo.findByHotelCode(hotel.getHotelCode());
                hotelAmenityRepo.deleteAll(hotelAmenities);

                HotelEntity existingHotel = hotelRepo.getHotelsById(hotel.getHotelCode());
                if(Objects.nonNull(existingHotel)) {
                    htlEntity.setHotelCode(existingHotel.getHotelCode());
                    if(!existingHotel.getHotelImages().isEmpty()) {
                       images.addAll(existingHotel.getHotelImages());
                    }
                }
            }

            htlEntity.setHotelImages(images);

            HotelEntity savedEntity = hotelRepo.save(htlEntity);
            response.setResponse(toHotelDto(savedEntity));
            response.setSuccess("SUCCESS");

        } catch(Exception e) {
            response.setErrorMessage("Something went wrong");
            response.setErrorCode("500");
            log.debug(Arrays.toString(e.getStackTrace()));
            log.info(e.getMessage());
        }
        return response;
    }

    private void deletedHotelImages(List<Long> deletedImages) {
        List<HotelImageEntity> images = hotelImageRepo.findAllById(deletedImages);
        hotelImageRepo.deleteAll(images);
    }

    private void deletedRoomImages(List<Long> deletedImages) {
        List<RoomImageEntity> images = roomImageRepo.findAllById(deletedImages);
        roomImageRepo.deleteAll(images);
    }

    @Override
    public List<HotelDto> getHotels() {
        List<HotelEntity> hotels = new ArrayList<>();
        List<String> roles = CommonUtil.getUserRole();
        if(Objects.nonNull(roles) && !roles.isEmpty()) {
            if(roles.contains("Super Admin")) {
                hotels = hotelRepo.getAllHotels();
            } else {
                UserEntity user = userRepo.getUser(CommonUtil.getLoginUserName());
                if(Objects.nonNull(user)) {
                    hotels= hotelRepo.getHotels(user.getUserId());
                }

            }
        }

        return hotels.stream().map(this::toHotelDto).collect(Collectors.toList());
    }
    
    

    @Override
    @Transactional
    public ResponseDto<HotelRoomDto>  saveRoom(HotelRoomReqDto room, List<MultipartFile> roomImages) {
        ResponseDto<HotelRoomDto>  response = new ResponseDto<>();
        try {
            HotelEntity hotelEntity = hotelRepo.getHotelsById(room.getHotelCode());

            if(Objects.nonNull(hotelEntity)) {
                deletedRoomImages(room.getRoomDetails().getDeletedImages()); 

                HotelRoomEntity roomEntity =  toRoomEntity(room, hotelEntity, roomImages);

                List<RoomImageEntity> images = new ArrayList<>();
                if(Objects.nonNull(roomImages)) images = toRoomImageEntity(roomImages, roomEntity);

                
                if(Objects.nonNull(room.getRoomDetails().getHotelRoomId())) {
                    List<RoomTagEntity> roomTags = roomTagRepo.findByRoomCode(room.getRoomDetails().getHotelRoomId());
                    roomTagRepo.deleteAll(roomTags);

                    List<RoomAmenityEntity> roomAmenities = roomAmenityRepo.findByRoomCode(room.getRoomDetails().getHotelRoomId());
                    roomAmenityRepo.deleteAll(roomAmenities);

                    List<PriceSlabRoomsEntity> roomPriceSlabs = roomPriceSlabRepo.findByRoomId(room.getRoomDetails().getHotelRoomId());
                    roomPriceSlabRepo.deleteAll(roomPriceSlabs);

                    Optional<HotelRoomEntity> existingRoom =  hotelRoomRepo.findById(room.getRoomDetails().getHotelRoomId());

                    if(existingRoom.isPresent()) {
                        roomEntity.setHotelRoomId(existingRoom.get().getHotelRoomId());
                        if(!existingRoom.get().getRoomImages().isEmpty()) {
                           images.addAll(existingRoom.get().getRoomImages());
                        }

                    }
                }

                roomEntity.setRoomImages(images);
              

                HotelRoomEntity savedRooms = hotelRoomRepo.save(roomEntity);
                response.setResponse( toHotelRoomDto(savedRooms));
                response.setSuccess("SUCCESS");
            } else {
                response.setErrorMessage("Hotel not found");
                response.setErrorCode("400");
            }
        } catch(Exception e) {
            response.setErrorMessage("Something went wrong");
            response.setErrorCode("500");
            log.info(Arrays.toString(e.getStackTrace()));
        }
        return response;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @Override
    public HotelDto getHotelById(Long id)  {

        HotelEntity hotel= hotelRepo.getHotelsById(id);
        if(Objects.nonNull(hotel)){
            return toHotelDto(hotel);
        }
        return null;

    }

    @Override
    public ResponseDto<String> deleteRoom(HotelRoomReqDto room) {
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<HotelRoomEntity> roomEntity = hotelRoomRepo.findById(room.getRoomDetails().getHotelRoomId());
            roomEntity.ifPresent(entity->{
                this.hotelRoomRepo.delete(entity);
                response.setSuccess("SUCCESS");

            });
        } catch (Exception e) {
            response.setErrorMessage("Something went wrong");
            response.setErrorCode("500");
            log.debug(Arrays.toString(e.getStackTrace()));
        }
        

        return response;
    }

    @Override
    public ResponseDto<String> deleteHotel(HotelRequestDto hotel) {
        ResponseDto<String> response = new ResponseDto<>();
        try {
            HotelEntity hotelEntity= hotelRepo.getHotelsById(hotel.getHotelCode());
            if(Objects.nonNull(hotelEntity)) {
                hotelEntity.setAvailability(false);
                hotelRepo.save(hotelEntity);
                response.setSuccess("SUCCESS");
            }

        } catch (Exception e) {
            response.setErrorMessage("Something went wrong");
            response.setErrorCode("500");
            log.debug(Arrays.toString(e.getStackTrace()));
        }

        return response;
    }


    private List<HotelAmenityEntity> toHotelAmenities(List<String> amenities, HotelEntity htlEntity) {
        return amenities.stream().map(amenity->HotelAmenityEntity.builder()
                        .amenity(amenity)
                        .hotelCode(htlEntity)
                        .build())
                .collect(Collectors.toList());
    }

    private List<HotelImageEntity> toHotelImageEntity(List<HotelImageReqDto> hotelImages, HotelEntity htlEntity) {
        return hotelImages.stream().map(img->
                {
                    try {
                        return HotelImageEntity.builder()
                                .hotelCode(htlEntity)
                                .hotelImage(compressBytes(img.getImage().getBytes()))
                                .name(img.getImageName())
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList());
    }

    private List<HotelImageEntity> toHotelImageEntityFromMultipart(List<MultipartFile> hotelImages, HotelEntity htlEntity) {
       if(Objects.nonNull(hotelImages)) {
           return hotelImages.stream().map(img->
                   {
                       try {
                           return HotelImageEntity.builder()
                                   .hotelCode(htlEntity)
                                   .hotelImage(compressBytes(img.getBytes()))
                                   .name(img.getOriginalFilename())
                                   .build();
                       } catch (IOException e) {
                           throw new RuntimeException(e);
                       }
                   }
           ).collect(Collectors.toList());
       }
       return Collections.emptyList();
    }

    private HotelRoomEntity toRoomEntity(HotelRoomReqDto room, HotelEntity htlEntity, List<MultipartFile> roomImages) {
            RoomDetailsDto rm = room.getRoomDetails();
            HotelRoomEntity htlRoomEntity =  HotelRoomEntity.builder()
                        .roomName(rm.getRoomName())
                        .price(rm.getPrice())
                        .occupancy(rm.getOccupancy())
                        .hotelCode(htlEntity)
                        .roomDescription(rm.getRoomDescription())
                        .noOfRooms(rm.getNoOfRooms())
                        .mealsPackage(rm.getRoomPackage())
                        .extraBedCostAdult(rm.getExtraBedCostAdult())
                        .extraBedCostChild(rm.getExtraBedCostChild())
                        .build();

            htlRoomEntity.setPriceSlab(toPriceSlabRoomEntityList(htlEntity, htlRoomEntity, rm.getPriceSlab()));
            htlRoomEntity.setAmenities(toRoomAmenityEntity(rm.getAmenities(), htlRoomEntity));
            htlRoomEntity.setRoomTags(toRoomTagEnity(rm.getRoomTags(), htlRoomEntity));
            return htlRoomEntity;

    }

    private List<RoomTagEntity> toRoomTagEnity(List<String> roomTags, HotelRoomEntity htlRoomEntity) {
        return roomTags.stream().map(tags-> RoomTagEntity.builder()
                        .roomTag(tags)
                        .hotelRoomId(htlRoomEntity)
                        .build())
                .collect(Collectors.toList());
    }

    private List<RoomAmenityEntity> toRoomAmenityEntity(List<String> amenities, HotelRoomEntity htlRoomEntity) {
        return amenities.stream().map(amenity-> RoomAmenityEntity.builder()
                        .hotelRoomId(htlRoomEntity)
                        .amenity(amenity)
                        .build())
                .collect(Collectors.toList());

    }

    private List<PriceSlabRoomsEntity> toPriceSlabRoomEntityList(HotelEntity htlEntity, HotelRoomEntity htlRoomEntity, List<Long> priceSlabId) {

        return priceSlabId.stream().map(ps->{
           PriceSlabEntity priceSlabEntity = priceSlabRepo.findById(ps).get();
           return PriceSlabRoomsEntity.builder()
                   .hotelCode(htlEntity)
                   .hotelRoomId(htlRoomEntity)
                   .priceSlabId(priceSlabEntity)
                   .build();
       }).collect(Collectors.toList());

    }

    private List<RoomImageEntity> toRoomImageEntity(List<MultipartFile> roomImages, HotelRoomEntity htlRoomEntity) {

        if(Objects.nonNull(roomImages)) {
            return roomImages.stream().map(img->
                    {
                        try {
                            return RoomImageEntity.builder()
                                    .hotelRoomId(htlRoomEntity)
                                    .roomImage(compressBytes(img.getBytes()))
                                    .name(img.getOriginalFilename())
                                    .build();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).collect(Collectors.toList());
        }
        return Collections.emptyList();

    }

    private HotelRoomDto toHotelRoomDto(HotelRoomEntity rm) {
        List<BookingEntity> booking = bookingRepo.getBookingsForRoomId(rm.getHotelRoomId(), BookingStatus.BOOKED.name());
        int booked = 0;
        if(!booking.isEmpty()) {
            booked = booking.stream().mapToInt(bk->bk.getBookingDetails().stream().mapToInt(BookingDetailsEntity::getNoOfRooms).sum()).sum();
        }

       return HotelRoomDto.builder()
                .hotelRoomId(rm.getHotelRoomId())
                .price(rm.getPrice())
                .hotelCode(rm.getHotelCode().getHotelCode())
                .roomDescription(rm.getRoomDescription())
                .occupancy(rm.getOccupancy())
                .priceSlab(toPriceSlabRoomList(rm.getPriceSlab()))
                .roomName(rm.getRoomName())
                .roomImages(toRoomImageList(rm.getRoomImages()))
                .amenities(rm.getAmenities().stream().map(RoomAmenityEntity::getAmenity).collect(Collectors.toList()))
                .roomTags(rm.getRoomTags().stream().map(RoomTagEntity::getRoomTag).collect(Collectors.toList()))
                .bedType(rm.getBedType())
                .noOfRooms(rm.getNoOfRooms())
                .noOfAvailableRooms( rm.getNoOfRooms() - booked)
                .roomPackage(rm.getMealsPackage())
                .extraBedCostAdult(rm.getExtraBedCostAdult())
                .extraBedCostChild(rm.getExtraBedCostChild())
                .build();
    }


    private List<HotelRoomDto> toHotelRoomsDtoList(List<HotelRoomEntity> rooms) {
        if(Objects.nonNull(rooms)) {
            return rooms.stream().map(this::toHotelRoomDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<PriceSlabRoomDto> toPriceSlabRoomList(List<PriceSlabRoomsEntity> priceSlabRoomList) {
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
                                .build())
                        .build()

            ).collect(Collectors.toList());
    }
    private List<HotelImageDto> toRoomImageList(List<RoomImageEntity> roomImages) {
        return roomImages.stream().map(img->HotelImageDto.builder()
                        .imageId(img.getImageId())
                        .image(decompressBytes(img.getRoomImage()))
                        .imageName(img.getName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<HotelImageDto> toHotelImageDto(List<HotelImageEntity> hotelImages) {
        return hotelImages.stream().map(img->HotelImageDto.builder()
                        .imageId(img.getImageId())
                        .image(decompressBytes(img.getHotelImage()))
                        .imageName(img.getName())
                        .build())
                .collect(Collectors.toList());
    }

    // compress the image bytes before storing it in the database

    public static byte[] compressBytes(byte[] data) {

        Deflater deflater = new Deflater();
         deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toByteArray();

    }

        // uncompress the image bytes before returning it to the angular application

    public static byte[] decompressBytes(byte[] data) {

        Inflater inflater = new Inflater();

        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        byte[] buffer = new byte[1024];
        try {

            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();

        } catch (IOException ioe) {

        } catch (DataFormatException e) {

        }

        return outputStream.toByteArray();

    }

    public HotelDto toHotelDto(HotelEntity htl) {

        return  HotelDto.builder()
                .hotelCode(htl.getHotelCode())
                .hotelName(htl.getHotelName())
                .location(LocationDto.builder().locationId(htl.getLocation().getLocationId()).location(htl.getLocation().getLocation()).build())
                .state(htl.getState())
                .city(htl.getCity())
                .address(htl.getAddress())
                .starRating(htl.getStarRating())
                .hotelImages(toHotelImageDto(htl.getHotelImages()))
                .hotelRooms(toHotelRoomsDtoList(htl.getRooms()))
                .about(htl.getAbout())
                .propertyRule(htl.getPropertyRule())
                .websiteLink(htl.getWebsiteLink())
                .email(htl.getEmail())
                .amenities(htl.getAmenities().stream().map(HotelAmenityEntity::getAmenity).collect(Collectors.toList()))
                .latitude(htl.getLatitude())
                .longitude(htl.getLongitude())
                .build();
    }

    private Set<UserEntity> getUserDetails() {
        Set<UserEntity> userSet = new HashSet<>();
        userSet.add(userRepo.getUser(CommonUtil.getLoginUserName()));
        return userSet;
    }
}

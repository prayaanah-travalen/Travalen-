package com.app.travelo.services.impl;

import com.app.travelo.model.entity.*;
import com.app.travelo.model.enums.BookingStatus;
import com.app.travelo.model.rest.*;
import com.app.travelo.repositories.*;
import com.app.travelo.services.HotelService;
import com.app.travelo.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    @Autowired
    private ContactPersonRepository contactPersonRepo;



    @Transactional
    @Override
    public ResponseDto<HotelDto> saveHotel(HotelRequestDto hotel, List<MultipartFile> hotelImages) {
        ResponseDto<HotelDto> response = new ResponseDto<>();
        try {
            log.info("=== HOTEL SAVE PROCESS STARTED ===");
            log.info("Hotel Name: {}", hotel.getHotelName());
            log.info("Location: {}", hotel.getLocation());
            log.info("Hotel Code: {}", hotel.getHotelCode());
            log.info("Images count: {}", hotelImages != null ? hotelImages.size() : 0);
            log.info("Amenities: {}", hotel.getAmenities());

            // Basic field validation
            if (hotel.getHotelName() == null || hotel.getHotelName().trim().isEmpty()) {
                response.setErrorMessage("Hotel name is required");
                response.setErrorCode("400");
                return response;
            }
            if (hotel.getLocation() == null || hotel.getLocation().trim().isEmpty()) {
                response.setErrorMessage("Location is required");
                response.setErrorCode("400");
                return response;
            }

            // Handle location
            LocationEntity location = locationRepo.findByLocation(hotel.getLocation());
            LocationEntity locationEntity;
            if (Objects.nonNull(location)) {
                locationEntity = location;
                log.info("Found existing location: {}", location.getLocation());
            } else {
                locationEntity = locationRepo.save(LocationEntity.builder()
                        .location(hotel.getLocation())
                        .build());
                log.info("Created new location: {}", locationEntity.getLocation());
            }

            // Get user details
            Set<UserEntity> userDetails = getUserDetails();
            log.info("User details found: {}", userDetails != null && !userDetails.isEmpty());
            if (userDetails == null || userDetails.isEmpty() || userDetails.contains(null)) {
                log.error("USER DETAILS ARE NULL OR INVALID");
                response.setErrorMessage("User authentication required");
                response.setErrorCode("401");
                return response;
            }

            HotelEntity htlEntity;

            // Check if hotel code is valid (not null and greater than 0)
            if (hotel.getHotelCode() != null && hotel.getHotelCode() > 0) {
                log.info("Updating existing hotel with code: {}", hotel.getHotelCode());
                HotelEntity existingHotel = hotelRepo.getHotelsByIdForUpdate(hotel.getHotelCode());

                if (Objects.nonNull(existingHotel)) {
                    htlEntity = existingHotel;
                    // Update fields using direct setters instead of builder methods
                    htlEntity.setHotelName(hotel.getHotelName());
                    htlEntity.setLocation(locationEntity);
                    htlEntity.setCity(hotel.getCity());
                    htlEntity.setState(hotel.getState());
                    htlEntity.setAddress(hotel.getAddress());
                    htlEntity.setCountry(hotel.getCountry());
                    htlEntity.setPostalCode(hotel.getPostalCode());
                    htlEntity.setStarRating(hotel.getStarRating());
                    htlEntity.setAbout(hotel.getAbout());
                    htlEntity.setEmail(hotel.getEmail());
                    htlEntity.setWebsiteLink(hotel.getWebsiteLink());
                    htlEntity.setUser(userDetails);
                    htlEntity.setLatitude(hotel.getLatitude());
                    htlEntity.setLongitude(hotel.getLongitude());
                    htlEntity.setPropertyRule(hotel.getPropertyRule());
                    htlEntity.setActive(true);

                    // Handle amenities
                    List<HotelAmenityEntity> hotelAmenities = hotelAmenityRepo.findByHotelCode(hotel.getHotelCode());
                    hotelAmenityRepo.deleteAll(hotelAmenities);
                    if (hotel.getAmenities() != null && !hotel.getAmenities().isEmpty()) {
                        htlEntity.setAmenities(toHotelAmenities(hotel.getAmenities(), htlEntity));
                    }

                    // Properly handle deleted images
                    if (hotel.getDeletedImages() != null && !hotel.getDeletedImages().isEmpty()) {
                        deletedHotelImages(hotel.getDeletedImages());
                    }

                    // Handle images - keep existing images that aren't deleted and add new ones
                    List<HotelImageEntity> images = new ArrayList<>();
                    
                    // Add new images if any
                    if (Objects.nonNull(hotelImages) && !hotelImages.isEmpty()) {
                        images.addAll(toHotelImageEntityFromMultipart(hotelImages, htlEntity));
                    }
                    
                    // Add existing images that aren't marked for deletion
                    if (existingHotel.getHotelImages() != null) {
                        existingHotel.getHotelImages().forEach(img -> {
                            if (hotel.getDeletedImages() == null || !hotel.getDeletedImages().contains(img.getImageId())) {
                                images.add(img);
                            }
                        });
                    }
                    htlEntity.setHotelImages(images);

                } else {
                    log.error("Hotel with code {} not found for update", hotel.getHotelCode());
                    response.setErrorMessage("Hotel not found for update");
                    response.setErrorCode("404");
                    return response;
                }
            } else {
                // Create new hotel using builder pattern
                log.info("Creating new hotel entry");
                htlEntity = HotelEntity.builder()
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
                        .user(userDetails)
                        .latitude(hotel.getLatitude())
                        .longitude(hotel.getLongitude())
                        .propertyRule(hotel.getPropertyRule())
                        .availability(true)
                        .active(true)
                        .build();

                if (hotel.getAmenities() != null && !hotel.getAmenities().isEmpty()) {
                    htlEntity.setAmenities(toHotelAmenities(hotel.getAmenities(), htlEntity));
                }

                List<HotelImageEntity> images = new ArrayList<>();
                if (Objects.nonNull(hotelImages) && !hotelImages.isEmpty()) {
                    images = toHotelImageEntityFromMultipart(hotelImages, htlEntity);
                }
                htlEntity.setHotelImages(images);
            }

            // Save entity
            log.info("Saving hotel entity: {}", htlEntity.getHotelName());
//            log.info("Saving hotel: {}", htlEntity);
            HotelEntity savedEntity = hotelRepo.save(htlEntity);
            log.info("Hotel saved successfully with ID: {}", savedEntity.getHotelCode());

            response.setResponse(toHotelDto(savedEntity));
            response.setSuccess("SUCCESS");

        } catch (Exception e) {
            log.error("CRITICAL ERROR in saveHotel for {}: {}", hotel != null ? hotel.getHotelName() : "null", e.getMessage(), e);
            response.setErrorMessage("Something went wrong: " + e.getMessage());
            response.setErrorCode("500");
        }
        return response;
    }

//    @Override
//    public HotelAmenityEntity addAmenity(HotelAmenityEntity amenity) {
//        Optional<HotelAmenityEntity> existing = hotelAmenityRepo.findByAmenity(amenity.getAmenity());
//        return existing.orElseGet(() -> hotelAmenityRepo.save(amenity));
//    }

    @Override
    public HotelAmenityEntity addAmenity(HotelAmenityEntity amenity) {
        Optional<HotelAmenityEntity> existing = hotelAmenityRepo.findByAmenity(amenity.getAmenity());
        // If already exists, just return it instead of saving duplicate
        return existing.orElseGet(() -> hotelAmenityRepo.save(amenity));
    }

    @Override
    public List<HotelAmenityEntity> getAllAmenities() {
        return hotelAmenityRepo.findAll();
    }

    @Override
    public List<HotelDto> getHotels() {
        return List.of();
    }


    private Set<UserEntity> getUserDetails() {
        try {
            String loginUserName = CommonUtil.getLoginUserName();
            if (loginUserName == null || loginUserName.trim().isEmpty()) {
                log.error("Login user name is null or empty");
                return null;
            }
            
            UserEntity user = userRepo.getUser(loginUserName);
            if (user == null) {
                log.error("User not found for username: {}", loginUserName);
                return null;
            }
            
            // Verify user has appropriate role
            List<String> roles = CommonUtil.getUserRole();
            if (roles == null || roles.isEmpty()) {
                log.error("No roles found for user: {}", loginUserName);
                return null;
            }
            
            // Check if user has any of the allowed roles (case-insensitive)
            boolean hasPermission = roles.stream()
                .anyMatch(role -> "Super Admin".equalsIgnoreCase(role) || "Hotel Admin".equalsIgnoreCase(role));
                
            if (!hasPermission) {
                log.error("User {} does not have permission to save hotels. Roles: {}", loginUserName, roles);
                return null;
            }
            
            Set<UserEntity> userSet = new HashSet<>();
            userSet.add(user);
            log.info("Retrieved user details for: {} with roles: {}", loginUserName, roles);
            return userSet;
        } catch (Exception e) {
            log.error("Error getting user details", e);
            return null;
        }
    }
    

    private void deletedHotelImages(List<Long> deletedImages) {
        List<HotelImageEntity> images = hotelImageRepo.findAllById(deletedImages);
        hotelImageRepo.deleteAll(images);
    }

    private void deletedRoomImages(List<Long> deletedImages) {
        List<RoomImageEntity> images = roomImageRepo.findAllById(deletedImages);
        roomImageRepo.deleteAll(images);
    }

//    @Override
//    public List<HotelDto> getHotels() {
//        List<HotelEntity> hotels = new ArrayList<>();
//        List<String> roles = CommonUtil.getUserRole();
//        if(Objects.nonNull(roles) && !roles.isEmpty()) {
//            if(roles.contains("Super Admin")) {
//                hotels = hotelRepo.getAllHotels();
//            } else {
//                UserEntity user = userRepo.getUser(CommonUtil.getLoginUserName());
//                if(Objects.nonNull(user)) {
//                    hotels= hotelRepo.getHotels(user.getUserId());
//                }
//
//            }
//        }
//
//        return hotels.stream().map(this::toHotelDto).collect(Collectors.toList());
//    }

    @Override
    public Page<HotelDto> hotels(Pageable pageable) {
        Page<HotelEntity> hotels;
        List<String> roles = CommonUtil.getUserRole();

        if (roles != null && !roles.isEmpty()) {
            if (roles.contains("Super Admin")) {
                hotels = hotelRepo.getAllHotels(pageable);
            } else {
                UserEntity user = userRepo.getUser(CommonUtil.getLoginUserName());
                if (user != null) {
                    hotels = hotelRepo.findHotels(user.getUserId(), pageable);
                } else {
                    hotels = Page.empty();
                }
            }
        } else {
            hotels = Page.empty();
        }

        return hotels.map(this::toHotelDto);

    }



    @Override
    @Transactional
    public ResponseDto<HotelRoomDto>  saveRoom(HotelRoomReqDto room, List<MultipartFile> roomImages) {
        ResponseDto<HotelRoomDto>  response = new ResponseDto<>();
        try {
//            HotelEntity hotelEntity = hotelRepo.getHotelsById(room.getHotelCode());
        	HotelEntity hotelEntity = hotelRepo.getHotelsByIdForUpdate(room.getHotelCode());


            if(Objects.nonNull(hotelEntity)) {
                deletedRoomImages(room.getRoomDetails().getDeletedImages()); /* delete room images **/

                HotelRoomEntity roomEntity =  toRoomEntity(room, hotelEntity, roomImages);

                List<RoomImageEntity> images = new ArrayList<>();
                if(Objects.nonNull(roomImages)) images = toRoomImageEntity(roomImages, roomEntity);

                /* update existing room **/
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
                /* end of update existing room **/

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
        HotelEntity hotel = hotelRepo.getHotelsByIdForUpdate(id);

        if (Objects.nonNull(hotel) && Boolean.TRUE.equals(hotel.getAvailability())) {
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
            HotelEntity hotelEntity = hotelRepo.getHotelsByIdForUpdate(hotel.getHotelCode());
            if (Objects.nonNull(hotelEntity)) {
                hotelEntity.setAvailability(false); // soft delete
                hotelRepo.save(hotelEntity);
                response.setSuccess("SUCCESS");
            } else {
                response.setErrorMessage("Hotel not found");
                response.setErrorCode("404");
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

    	 // Get contact persons for this hotel
        List<ContactPersonDto> contactPersons = contactPersonRepo.findByHotelCode(htl.getHotelCode())
            .stream()
            .map(cp -> ContactPersonDto.builder()
                .id(cp.getId())
                .hotelCode(cp.getHotel().getHotelCode())
                .firstName(cp.getFirstName())
                .lastName(cp.getLastName())
                .phone(cp.getPhone())
                .whatsapp(cp.getWhatsapp())
                .email(cp.getEmail())
                .designation(cp.getDesignation())
                .build())
            .collect(Collectors.toList());

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

                .contactDetails(contactPersons)
                .build();
    }



    @Override
    @Transactional
    public ResponseDto<ContactPersonDto> saveContactPerson(ContactPersonDto contactPerson) {
        ResponseDto<ContactPersonDto> response = new ResponseDto<>();
        try {
            log.info("Saving contact person for hotel: {}", contactPerson.getHotelCode());
            
            // Get the hotel entity
            HotelEntity hotel = hotelRepo.getHotelsByIdForUpdate(contactPerson.getHotelCode());
            if (Objects.isNull(hotel)) {
                response.setErrorMessage("Hotel not found");
                response.setErrorCode("404");
                return response;
            }
            
            // Create or update contact person entity
            ContactPersonEntity contactEntity;
            if (contactPerson.getId() != null && contactPerson.getId() > 0) {
                // Update existing contact person
                contactEntity = contactPersonRepo.findById(contactPerson.getId())
                    .orElseThrow(() -> new RuntimeException("Contact person not found"));
                
                contactEntity.setFirstName(contactPerson.getFirstName());
                contactEntity.setLastName(contactPerson.getLastName());
                contactEntity.setPhone(contactPerson.getPhone());
                contactEntity.setWhatsapp(contactPerson.getWhatsapp());
                contactEntity.setEmail(contactPerson.getEmail());
                contactEntity.setDesignation(contactPerson.getDesignation());
            } else {
                // Create new contact person
                contactEntity = ContactPersonEntity.builder()
                    .hotel(hotel)
                    .firstName(contactPerson.getFirstName())
                    .lastName(contactPerson.getLastName())
                    .phone(contactPerson.getPhone())
                    .whatsapp(contactPerson.getWhatsapp())
                    .email(contactPerson.getEmail())
                    .designation(contactPerson.getDesignation())
                    .build();
            }
            
            // Save the contact person
            ContactPersonEntity savedContact = contactPersonRepo.save(contactEntity);
            
            // Convert to DTO and return response
            ContactPersonDto responseDto = ContactPersonDto.builder()
                .id(savedContact.getId())
                .hotelCode(savedContact.getHotel().getHotelCode())
                .firstName(savedContact.getFirstName())
                .lastName(savedContact.getLastName())
                .phone(savedContact.getPhone())
                .whatsapp(savedContact.getWhatsapp())
                .email(savedContact.getEmail())
                .designation(savedContact.getDesignation())
                .build();
                
            response.setResponse(responseDto);
            response.setSuccess("SUCCESS");
            
        } catch (Exception e) {
            log.error("Error saving contact person: {}", e.getMessage(), e);
            response.setErrorMessage("Something went wrong: " + e.getMessage());
            response.setErrorCode("500");
        }
        return response;
    }

}

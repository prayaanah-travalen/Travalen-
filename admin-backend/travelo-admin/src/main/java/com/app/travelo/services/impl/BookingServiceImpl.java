package com.app.travelo.services.impl;

import com.app.travelo.model.entity.*;
import com.app.travelo.model.enums.BookingStatus;
import com.app.travelo.model.rest.*;
import com.app.travelo.repositories.*;
import com.app.travelo.services.BookingService;
import com.app.travelo.services.PaymentService;
import com.app.travelo.util.CommonUtil;
import com.app.travelo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private GuestRepository guestRepo;

    @Autowired
    private HotelRoomRepository hotelRoomRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PaymentService paymentService;

    @Override
    public List<BookingDto> getBooking() {
        List<BookingEntity> bookings = new ArrayList<>();
        List<String> roles = CommonUtil.getUserRole();
        if(Objects.nonNull(roles) && !roles.isEmpty()) {
            if (roles.contains("Super Admin")) {
                bookings = bookingRepo.findAll();
            } else {
                UserEntity user = userRepo.getUser(CommonUtil.getLoginUserName());
                if(Objects.nonNull(user)) {
                    bookings = bookingRepo.getBookings(user.getUserId());
                }
            }
        }
        return bookings.stream().map(bk-> BookingDto.builder()
                .bookingId(bk.getBookingId())
                .amount(bk.getAmount())
                .adults(bk.getAdults())
                .children(bk.getChildren())
                .checkIn(DateUtil.toDateString(bk.getCheckIn()))
                .checkOut(DateUtil.toDateString(bk.getCheckOut()))
                .guestDetails(toGuestDto(bk.getGuestDetail()))
                .bookingDetails(toBookingDetailsDto(bk.getBookingDetails()))
                .location(getBookingLocations(bk.getBookingDetails()))
                .status(bk.getStatus())
                .payment(toPaymentDtoList(bk.getPayment()))
                .bookingDate(DateUtil.toDateString(bk.getRegistrationDateTime()))
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<CalendarResDto> getBookingsForCalendar(CalendarReqDto req) {
        String start = req.getStartDate().concat(" 00:00:00");
        String end = req.getEndDate().concat(" 23:59:59");
        List<BookingEntity> bookings = bookingRepo.getUserBookingWithDate(DateUtil.stringToDateTime(start), DateUtil.stringToDateTime(end));
        List<CalendarDetailDto> resDto = bookings.stream().map(bk->
                        bk.getBookingDetails().stream().map(bkd->CalendarDetailDto.builder()
                                .netBooked(bkd.getNoOfRooms())
                                .roomsToSell(bkd.getHotelRoomId().getNoOfRooms())
                                .status(bk.getStatus())
                                .roomId(bkd.getHotelRoomId().getHotelRoomId())
                                .roomName(bkd.getHotelRoomId().getRoomName())
                                .standardRate(bkd.getHotelRoomId().getPrice())
                            .build()).collect(Collectors.toList())).flatMap(Collection::stream)
            .collect(Collectors.toList());

        Map<String, Map<String, List<CalendarDetailDto>>> resp = resDto.stream()
                .collect(groupingBy(CalendarDetailDto::getRoomName, groupingBy(CalendarDetailDto::getStatus)));

       List<CalendarResDto> response = new ArrayList<>();
        resp.entrySet().forEach(entry->{
            List<CalendarDetailDto> details = new ArrayList<>();

            entry.getValue().entrySet().forEach(dt->{
                int booked = dt.getValue().stream().mapToInt(bk->bk.getNetBooked()).sum();
                int[] count = {0};
                dt.getValue().forEach(bkd->  {

                    if(count[0] <= 0){
                        details.add(
                                CalendarDetailDto.builder()
                                        .netBooked(booked)
                                        .roomsToSell(bkd.getRoomsToSell())
                                        .status(dt.getKey())
                                        .roomId(bkd.getRoomId())
                                        .roomName(bkd.getRoomName())
                                        .standardRate(bkd.getStandardRate())
                                        .build()
                        );
                    }
                    count[0] = count[0] + 1;

                });
            });
            response.add(CalendarResDto.builder().roomName(entry.getKey()).data(details).build());
        });
        return response;
    }

    @Override
    public ResponseDto<String> closeBooking(BookingDto req) {
        Optional<BookingEntity> booking = bookingRepo.findById(req.getBookingId());
        ResponseDto<String> response = new ResponseDto<>();
        if(booking.isPresent()) {
            BookingEntity entity = booking.get();
            entity.setStatus(BookingStatus.CLOSED.name());
            entity.setLastUpdatedBy(commonUtil.getUserId());
            entity.setLastUpdatedDateTime(new Date());

            bookingRepo.save(entity);
            response.setSuccess("SUCCESS");
        }
        return response;
    }

    @Override
    public ResponseDto<String> cancelBooking(BookingDto req) {
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<BookingEntity> bookingEntity = bookingRepo.findById(req.getBookingId());
            if(bookingEntity.isPresent()) {
                BookingEntity bkEntity = bookingEntity.get();
                bkEntity.setStatus(BookingStatus.CANCELLED.name());

                bookingRepo.save(bkEntity);
                paymentService.refund(bookingEntity.get());
                response.setSuccess("SUCCESS");
                response.setResponse("Cancelled");
            }

        } catch (Exception e) {
            log.info(e.getMessage());
            response.setErrorCode("400");
            response.setErrorMessage("Invalid request");
        }

        return response;
    }
    private static Integer getCount(int count) {
        return count + 1;
    }
    private List<PaymentDto> toPaymentDtoList(List<PaymentEntity> payment) {
        return payment.stream().map(pay->
            PaymentDto.builder()
                    .paymentId(pay.getPaymentId())
                    .status(pay.getStatus())
                    .transactionType(pay.getTransactionType())
                    .paymentDate(DateUtil.toDateString(pay.getRegistrationDateTime()))
                    .build()
        ).collect(Collectors.toList());
    }

    private List<LocationDto> getBookingLocations(List<BookingDetailsEntity> bookingDetails) {
        return bookingDetails.stream().map(bk->toLocationDto(bk.getHotelRoomId().getHotelCode().getLocation())).collect(Collectors.toList());
    }


    private List<BookingDetailsDto> toBookingDetailsDto(List<BookingDetailsEntity> bookingDetails) {
        return bookingDetails.stream().map(bk-> BookingDetailsDto.builder()
                .checkIn(DateUtil.toDateStringYYYYMMDD(bk.getCheckIn()))
                .checkOut(DateUtil.toDateStringYYYYMMDD(bk.getCheckOut()))
                .noOfRooms(bk.getNoOfRooms())
                .hotelRoom(toHotelRoomDto(bk.getHotelRoomId()))
                .hotel(toHotelDto(bk.getHotelRoomId().getHotelCode()))
                .location(toLocationDto(bk.getHotelRoomId().getHotelCode().getLocation()))
                .noOfRooms(bk.getNoOfRooms())
                .build()
        ).collect(Collectors.toList());
    }

    private HotelRoomDto toHotelRoomDto(HotelRoomEntity rm) {
        return HotelRoomDto.builder()
                .hotelRoomId(rm.getHotelRoomId())
                .price(rm.getPrice())
                .hotelCode(rm.getHotelCode().getHotelCode())
                .roomDescription(rm.getRoomDescription())
                .occupancy(rm.getOccupancy())
                .roomName(rm.getRoomName())
                .amenities(rm.getAmenities().stream().map(RoomAmenityEntity::getAmenity).collect(Collectors.toList()))
                .roomTags(rm.getRoomTags().stream().map(RoomTagEntity::getRoomTag).collect(Collectors.toList()))
                .bedType(rm.getBedType())
                .build();
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
                .about(htl.getAbout())
                .propertyRule(htl.getPropertyRule())
                .websiteLink(htl.getWebsiteLink())
                .email(htl.getEmail())
                .amenities(htl.getAmenities().stream().map(HotelAmenityEntity::getAmenity).collect(Collectors.toList()))
                .build();
    }

    private LocationDto toLocationDto(LocationEntity location) {
        return LocationDto.builder()
                .locationId(location.getLocationId())
                .location(location.getLocation())
                .build();
    }

    private GuestDetails toGuestDto(GuestEntity guestDetail) {
        return GuestDetails.builder()
                .email(guestDetail.getEmail())
                .firstName(guestDetail.getFirstName())
                .lastName(guestDetail.getLastName())
                .phone(guestDetail.getPhone())
                .build();
    }




}

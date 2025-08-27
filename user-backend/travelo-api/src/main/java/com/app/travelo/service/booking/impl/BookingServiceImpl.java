package com.app.travelo.service.booking.impl;

import com.app.travelo.model.entity.*;
import com.app.travelo.model.enums.BookingStatus;
import com.app.travelo.model.enums.PaymentStatus;
import com.app.travelo.model.rest.*;
import com.app.travelo.model.rest.payment.PaymentResponseDto;
import com.app.travelo.model.rest.payment.PaymentDto;
import com.app.travelo.model.rest.payment.PaymentRequestDto;
import com.app.travelo.repository.BookingRepository;
import com.app.travelo.repository.GuestRepository;
import com.app.travelo.repository.HotelRoomRepository;
import com.app.travelo.repository.PaymentRepository;
import com.app.travelo.service.booking.BookingService;
import com.app.travelo.service.email.EmailService;
import com.app.travelo.service.helper.HotelHelper;
import com.app.travelo.service.payment.PaymentService;
import com.app.travelo.util.CommonUtil;
import com.app.travelo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @Qualifier("PaymentServiceImpl")
    private PaymentService paymentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    HotelHelper hotelHelper;

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public ResponseDto<PaymentResponseDto> hotelBooking(BookingDto req) {
        ResponseDto<PaymentResponseDto> response = new ResponseDto<>();
        BookingEntity bookingEntity = null;
        try {
//            GuestEntity guest = toGuestEntity(req.getGuestDetails());
//            bookingEntity = BookingEntity.builder()
//                    .checkIn(DateUtil.stringToDate(req.getCheckIn()))
//                    .checkOut(DateUtil.stringToDate(req.getCheckOut()))
//                    .noOfHotels(String.valueOf(req.getBookingDetails().size()))
//                    .adults(req.getAdults())
//                    .children(req.getChildren())
//                    .guestDetail(guest)
//                    .amount(req.getAmount())
//                    .registrationDateTime(new Date())
//                    .registrantBy(guest.getGuestId().toString())
//
//                    .build();
//            bookingEntity.setBookingDetails(toBookingDetailEntities(bookingEntity, req.getBookingDetails()));
//
//            bookingRepo.save(bookingEntity);

            if(Objects.nonNull(req.getBookingId())) {
                Optional<BookingEntity> existing = bookingRepo.findById(req.getBookingId());
                if(existing.isPresent()) {
                    bookingEntity = existing.get();

                    PaymentResponseDto paymentResponseDto = paymentService.createPayment(toPaymentRequest(bookingEntity));
                    paymentResponseDto.setBookingId(bookingEntity.getBookingId());


                    response.setSuccess("SUCCESS");
                    response.setResponse(paymentResponseDto);
                }  else {
                    response.setErrorCode("400");
                    response.setErrorMessage("Invalid request");
                }

            } else {
                response.setErrorCode("400");
                response.setErrorMessage("Invalid request");
            }


        } catch (Exception e) {
            if(Objects.nonNull(bookingEntity)) {
                bookingEntity.setStatus(BookingStatus.FAILED.name());
                bookingRepo.save(bookingEntity);
            }

            log.info(e.getMessage());
            response.setErrorCode("400");
            response.setErrorMessage("Invalid request");
        }
        return response;
    }

    private PaymentRequestDto toPaymentRequest(BookingEntity bookingEntity) {
        PaymentRequestDto payment = PaymentRequestDto.builder()
                .status(PaymentStatus.INITIAL.name())
                .transactionType("AUTHORISATION")
                .amount(bookingEntity.getAmount())
                .bookingId(bookingEntity)
                .guest(bookingEntity.getGuestDetail())
                .build();
        return  payment;
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

                double amount = calculateRefundAmount(bkEntity);
                if(amount > 0){
                    bkEntity.setAmount(Double.toString(amount));

                    paymentService.refund(bkEntity);
                }

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

    @Override
    public List<BookingDto> getBooking(String phone) {
        GuestEntity guestEntity = guestRepo.findByPhone(phone);
        List<BookingEntity> bookings = bookingRepo.getUserBooking(guestEntity.getGuestId());
        return bookings.stream().map(this::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public PaymentDto getStatus(Long id) {
        Optional<BookingEntity> bookingEntity = bookingRepo.findById(id);
        PaymentDto paymentDto = null;
        if(bookingEntity.isPresent()) {
            BookingEntity bkEntity = bookingEntity.get();
            paymentDto = paymentService.getStatusOfBooking(bookingEntity.get().getBookingId());
        }
        return paymentDto;
    }

    @Override
    public ResponseDto<String> postponeBooking(BookingDto req) {
        return null;
    }

    @Override
    public ResponseDto<BookingDto> temporaryBooking(BookingDto req) {
        ResponseDto<BookingDto> response = new ResponseDto<>();
        BookingEntity bookingEntity = null;
        try {
            GuestEntity guest = toGuestEntity(req.getGuestDetails());
            bookingEntity = BookingEntity.builder()
                    .checkIn(DateUtil.stringToDate(req.getCheckIn()))
                    .checkOut(DateUtil.stringToDate(req.getCheckOut()))
                    .noOfHotels(String.valueOf(req.getBookingDetails().size()))
                    .adults(req.getAdults())
                    .children(req.getChildren())
                    .guestDetail(guest)
                    .amount(req.getAmount())
                    .registrationDateTime(new Date())
                    .registrantBy(guest.getGuestId().toString())
                    .status(BookingStatus.INITIAL.name())
                    .build();
            bookingEntity.setBookingDetails(toBookingDetailEntities(bookingEntity, req.getBookingDetails()));

            GuestEntity guestEntity = guestRepo.findByPhone(CommonUtil.getLoginUserPhone());
            BookingEntity existing = bookingRepo.getUserBookingWithStatus(guestEntity.getGuestId(), BookingStatus.INITIAL.name());
            if(Objects.nonNull(existing)) {
                bookingEntity.setBookingId(existing.getBookingId());
            }

            BookingEntity bkEntity = bookingRepo.save(bookingEntity);
            response.setResponse(toBookingDto(bkEntity));
            response.setSuccess("SUCCESS");
        } catch (Exception e) {

            log.info(Arrays.toString(e.getStackTrace()));
            response.setErrorCode("400");
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDto<BookingDto> addHotelsForBooking(BookingDto req) {
        ResponseDto<BookingDto> response = new ResponseDto<>();
        BookingEntity bookingEntity = null;
        try {
            GuestEntity guest = toGuestEntity(req.getGuestDetails());
            GuestEntity guestEntity = guestRepo.findByPhone(CommonUtil.getLoginUserPhone());
            BookingEntity existing = bookingRepo.getUserBookingWithStatus(guestEntity.getGuestId(), BookingStatus.INITIAL.name());
            bookingEntity = BookingEntity.builder()
                    .checkIn(DateUtil.stringToDate(req.getCheckIn()))
                    .checkOut(DateUtil.stringToDate(req.getCheckOut()))
                    .adults(req.getAdults())
                    .children(req.getChildren())
                    .guestDetail(guest)
                    .amount(req.getAmount())
                    .status(BookingStatus.INITIAL.name())
                    .registrantBy(guest.getGuestId().toString())
                    .build();
            if(Objects.nonNull(existing)) {
                bookingEntity.setBookingId(existing.getBookingId());
                bookingEntity.setBookingDetails(existing.getBookingDetails());
                bookingEntity.setRegistrationDateTime(existing.getRegistrationDateTime());
            } else {
                bookingEntity.setRegistrationDateTime(new Date());
            }

            bookingEntity.setBookingDetails(toBookingDetailEntities(bookingEntity, req.getBookingDetails()));
            bookingEntity.setNoOfHotels(String.valueOf(bookingEntity.getBookingDetails().size()));

            BookingEntity bkEntity = bookingRepo.save(bookingEntity);
            response.setResponse(toBookingDto(bkEntity));
            response.setSuccess("SUCCESS");
        } catch (Exception e) {

            log.info(Arrays.toString(e.getStackTrace()));
            response.setErrorCode("400");
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseDto<BookingDto> getBookingDetail(Long bookingId) {
        Optional<BookingEntity> bookings = bookingRepo.findById(bookingId);
        ResponseDto<BookingDto> response = new ResponseDto<>();
        bookings.ifPresent(bookingEntity -> {
            response.setResponse(toBookingDto(bookingEntity));
            response.setSuccess("SUCCESS");
        });

        return response;
    }

    private List<LocationDto> getBookingLocations(List<BookingDetailsEntity> bookingDetails) {
        return bookingDetails.stream().map(bk->toLocationDto(bk.getHotelRoomId().getHotelCode().getLocation())).collect(Collectors.toList());
    }


    private List<BookingDetailsDto> toBookingDetailsDto(List<BookingDetailsEntity> bookingDetails) {
        return bookingDetails.stream().map(bk-> BookingDetailsDto.builder()
                .checkIn(bk.getCheckIn().toString())
                .checkOut(bk.getCheckOut().toString())
                .noOfRooms(bk.getNoOfRooms())
//                .hotelRoomCode(bk.getHotelRoomId())
                .hotelCode(toHotelDto(bk.getHotelRoomId()))
                .location(toLocationDto(bk.getHotelRoomId().getHotelCode().getLocation()))
                .build()
        ).collect(Collectors.toList());
    }

    private HotelDto toHotelDto(HotelRoomEntity room){
       HotelDto hotel =  hotelHelper.toHotelDto(room.getHotelCode());
       List<HotelRoomDto> roomDto = hotel.getRooms().stream().filter(rm->rm.getHotelRoomId().equals(room.getHotelRoomId())).collect(Collectors.toList());
       hotel.setRooms(roomDto);
       return hotel;
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


    //    @Transactional
    private PaymentEntity savePaymentDetails(BookingEntity bookingEntity) {
        String paymentId = paymentService.getPaymentId().getPaymentId();
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentId(paymentId)
                .status(PaymentStatus.INITIAL.name())
                .transactionType("AUTHORISATION")
                .amount(bookingEntity.getAmount())
                .bookingId(bookingEntity)
                .guest(bookingEntity.getGuestDetail())
                .registrationDateTime(new Date())
                .build();

        return paymentRepo.save(paymentEntity);
    }


    private List<BookingDetailsEntity> toBookingDetailEntities(BookingEntity bookingEntity, List<BookingDetailsDto> bookingDetails) {
        List<BookingDetailsEntity> bookingDetailList;
        bookingDetailList = bookingDetails.stream().distinct().map(bd-> bd.getHotelCode().getRooms().stream().map(rm->{
           Optional<HotelRoomEntity> hotelRoomEntity = hotelRoomRepo.findById(rm.getHotelRoomId());
           if(hotelRoomEntity.isPresent()){
//                                List<BookingEntity> booking = bookingRepo.getBookingsForRoomId(hotelRoomEntity.get().getHotelRoomId(), BookingStatus.BOOKED.name());
//                                int availableRooms = hotelRoomEntity.get().getNoOfRooms();
//                                if(!booking.isEmpty()) {
//                                    int booked = booking.stream().mapToInt(bk->bk.getBookingDetails().stream().mapToInt(BookingDetailsEntity::getNoOfRooms).sum()).sum();
//                                    availableRooms = availableRooms - booked;
//                                }

               if(isRoomAvailable(hotelRoomEntity.get(),bd.getNoOfRooms())) {
                   Optional<BookingDetailsEntity> existingBookingDetail = Optional.of(new BookingDetailsEntity());
                   if(Objects.nonNull(bookingEntity.getBookingDetails())){
                       existingBookingDetail = bookingEntity.getBookingDetails().stream().filter(room->room.getBookingDetailId().equals(rm.getHotelRoomId())).findAny();
                   }

                   BookingDetailsEntity bookingDetail = BookingDetailsEntity.builder()
                           .bookingId(bookingEntity)
                           .checkIn(DateUtil.stringToDate(bd.getCheckIn()))
                           .checkOut(DateUtil.stringToDate(bd.getCheckOut()))
                           .hotelRoomId(hotelRoomEntity.get())
                           .noOfRooms(bd.getNoOfRooms())
                           .build();
                   existingBookingDetail.ifPresent(bookingDetailsEntity ->
                   {
                       bookingDetail.setBookingDetailId(bookingDetailsEntity.getBookingDetailId());
                       if(Objects.nonNull(bookingEntity.getBookingDetails())) bookingEntity.getBookingDetails().remove(bookingDetailsEntity);
                   });

                   return bookingDetail;

               } else {
                   try {
                       throw new Exception("Selected room not available at hotel " + hotelRoomEntity.get().getHotelCode().getHotelName());
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }
               }

           }
           return null;
       }).collect(Collectors.toList())
            ).flatMap(Collection::stream).collect(Collectors.toList());
//        if(Objects.nonNull(bookingEntity.getBookingDetails())){
//            bookingDetailList.addAll(bookingEntity.getBookingDetails());
//        }

        return bookingDetailList;
    }

    /*private List<BookingDetailsEntity> toBookingDetailEntities_back(BookingEntity bookingEntity, List<BookingDetailsDto> bookingDetails) {
        List<BookingDetailsEntity> bookingDetailList  = bookingDetails.stream().map(bd->{
                    Optional<HotelRoomEntity> hotelRoomEntity = hotelRoomRepo.findById(bd.getHotelRoomCode().getHotelRoomId());
                    if(hotelRoomEntity.isPresent()){
                        List<BookingEntity> booking = bookingRepo.getBookingsForRoomId(hotelRoomEntity.get().getHotelRoomId(), BookingStatus.BOOKED.name());
                        int availableRooms = hotelRoomEntity.get().getNoOfRooms();
                        if(!booking.isEmpty()) {
                            int booked = booking.stream().mapToInt(bk->bk.getBookingDetails().stream().mapToInt(BookingDetailsEntity::getNoOfRooms).sum()).sum();
                            availableRooms = availableRooms - booked;
                        }
                        if(isRoomAvailable(hotelRoomEntity.get(),bd.getNoOfRooms())) {
                            return BookingDetailsEntity.builder()
                                    .bookingId(bookingEntity)
                                    .checkIn(DateUtil.stringToDate(bd.getCheckIn()))
                                    .checkOut(DateUtil.stringToDate(bd.getCheckOut()))
                                    .hotelRoomId(hotelRoomEntity.get())
                                    .noOfRooms(bd.getNoOfRooms())
                                    .build();
                        } else {
                            try {
                                throw new Exception("Selected room not available at hotel " + hotelRoomEntity.get().getHotelCode().getHotelName());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                    try {
                        throw new Exception("Hotel not exists");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

        ).collect(Collectors.toList());
        if(Objects.nonNull(bookingEntity.getBookingDetails())){
            bookingDetailList.addAll(bookingEntity.getBookingDetails());
        }

        return bookingDetailList;
    }*/

    private boolean isRoomAvailable(HotelRoomEntity hotelRoomEntity, Integer noOfRoomsNeeded) {
        List<BookingEntity> booking = bookingRepo.getBookingsForRoomId(hotelRoomEntity.getHotelRoomId(), BookingStatus.BOOKED.name());
        int availableRooms = hotelRoomEntity.getNoOfRooms();
        if(!booking.isEmpty()) {
            int booked = booking.stream().mapToInt(bk->bk.getBookingDetails().stream().mapToInt(BookingDetailsEntity::getNoOfRooms).sum()).sum();
            availableRooms = availableRooms - booked;
        }
        return availableRooms >= noOfRoomsNeeded;
    }

    private GuestEntity toGuestEntity(GuestDetails guestDetails) {
        GuestEntity guestEntity = guestRepo.findByPhone(CommonUtil.getLoginUserPhone());
        if(Objects.nonNull(guestDetails)) {
            if(Objects.nonNull(guestEntity)) {
                guestEntity.setFirstName(guestDetails.getFirstName());
                guestEntity.setLastName(guestDetails.getLastName());
                guestEntity.setEmail(guestDetails.getEmail());
            } else {
                guestEntity = GuestEntity.builder()
                        .phone(guestDetails.getPhone())
                        .firstName(guestDetails.getFirstName())
                        .lastName(guestDetails.getLastName())
                        .email(guestDetails.getEmail())
                        .build();

            }

        }

        return  guestEntity;
    }

    private BookingDto toBookingDto(BookingEntity bk ) {
        return  BookingDto.builder()
                .bookingId(bk.getBookingId())
                .amount(bk.getAmount())
                .adults(bk.getAdults())
                .children(bk.getChildren())
                .checkIn(bk.getCheckIn().toString())
                .checkOut(bk.getCheckOut().toString())
                .guestDetails(toGuestDto(bk.getGuestDetail()))
                .bookingDetails(toBookingDetailsDto(bk.getBookingDetails()))
                .location(getBookingLocations(bk.getBookingDetails()))
                .status(bk.getStatus())
                .build();
    }

    private Double calculateRefundAmount(BookingEntity bkEntity) {
        double amount = 0.0;
        Date startDate = new Date();
        Date endDate   = bkEntity.getRegistrationDateTime();

        long duration  = startDate.getTime() - endDate.getTime();

//                long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
//                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
//                long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

        if(diffInDays >= 0 && diffInDays <= 2) {
            amount = (Double.parseDouble(bkEntity.getAmount())*100)/100;
        }
        else if(diffInDays >= 3 && diffInDays <= 7){
            amount = (Double.parseDouble(bkEntity.getAmount())*50)/100;
        } else  if(diffInDays >= 8 && diffInDays <= 14) {
            amount = (Double.parseDouble(bkEntity.getAmount()) * 25 ) / 100;

        }
        else  if(diffInDays >= 15) {
            amount = (Double.parseDouble(bkEntity.getAmount()) * 0 ) / 100;

        }
        return amount;
    }

}

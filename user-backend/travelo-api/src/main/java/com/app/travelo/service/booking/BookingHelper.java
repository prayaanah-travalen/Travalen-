package com.app.travelo.service.booking;

import com.app.travelo.model.entity.BookingEntity;
import com.app.travelo.model.enums.BookingStatus;
import com.app.travelo.model.enums.PaymentStatus;
import com.app.travelo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingHelper {

    @Autowired
    private BookingRepository bookingRepo;

    public void updateBookingStatus(Long bookingId, BookingStatus status) {
        Optional<BookingEntity> bookingEntity = bookingRepo.findById(bookingId);
        if(bookingEntity.isPresent()) {
            BookingEntity bkEntity = bookingEntity.get();
            bkEntity.setStatus(status.name());
            bookingRepo.save(bkEntity);
        }
    }
}

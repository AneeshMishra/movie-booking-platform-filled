package com.xyz.booking.service;

import com.example.bookingservice.entity.Booking;
import com.example.bookingservice.entity.Ticket;
import com.example.bookingservice.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatServiceClient seatServiceClient;

    public BookingService(BookingRepository bookingRepository,SeatServiceClient seatServiceClient) {
        this.bookingRepository = bookingRepository;
        this.seatServiceClient = seatServiceClient;
    }


    private Booking createBooking(Long theatreId, Long showId, LocalDateTime showTime,
                                  List<String> seatNumbers, BigDecimal ticketPrice) {

        Booking booking = new Booking();
        booking.setTheatreId(theatreId);
        booking.setShowId(showId);
        booking.setShowTime(showTime);

        List<Ticket> tickets = seatNumbers.stream().map(seat -> {
            Ticket ticket = new Ticket();
            ticket.setSeatNumber(seat);
            ticket.setPrice(ticketPrice);
            ticket.setFinalPrice(ticketPrice);
            return ticket;
        }).toList();

        booking.setTickets(tickets);

        applyDiscounts(booking);

        return booking;
    }

    private void applyDiscounts(Booking booking) {
        List<Ticket> tickets = booking.getTickets();
        if (tickets == null || tickets.isEmpty()) {
            return;
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            BigDecimal price = ticket.getPrice();

            // Rule 1: 50% discount on the third ticket
            if ((i + 1) % 3 == 0) {  // 3rd, 6th, 9th tickets etc.
                price = price.multiply(BigDecimal.valueOf(0.5));
            }

            // Rule 2: 20% discount for afternoon shows
            LocalTime showTime = booking.getShowTime().toLocalTime();
            if (showTime.isAfter(LocalTime.of(12, 0)) && showTime.isBefore(LocalTime.of(16, 0))) {
                price = price.multiply(BigDecimal.valueOf(0.8));
            }

            ticket.setFinalPrice(price);
            totalAmount = totalAmount.add(price);
        }

        booking.setTotalAmount(totalAmount);
    }

    /**
     * Book tickets for a specific show (theatre, timing, seats)
     */
    /**
     * Book tickets for a specific show (single booking).
     */
    @Transactional
    public Booking bookTickets(Long theatreId, Long showId, LocalDateTime showTime,
                               List<String> seatNumbers, BigDecimal ticketPrice) {
        // Step 1: Lock seats
        boolean locked = seatServiceClient.lockSeats(showId, seatNumbers);
        if (!locked) {
            throw new RuntimeException("Seats not available");
        }
        Booking booking = createBooking(theatreId, showId, showTime, seatNumbers, ticketPrice);

        // Step 3: Call Payment Service (simulate success/failure)
        boolean paymentSuccess = true; // assume success for now

        if (paymentSuccess) {
            booking.setStatus("CONFIRMED");
            bookingRepository.save(booking);

            seatServiceClient.confirmBooking(showId, seatNumbers);
        } else {
            booking.setStatus("FAILED");
            bookingRepository.save(booking);
            seatServiceClient.releaseSeats(showId, seatNumbers);
        }
        return  booking;
    }

    @Transactional
    public List<Booking> bulkBookTickets(List<BulkBookingRequest> requests) {
        List<Booking> bookings = new ArrayList<>();
        for (BulkBookingRequest request : requests) {
            // Step 1: Lock seats
            boolean locked = seatServiceClient.lockSeats(request.getShowId(), request.getSeatNumbers());
            if (!locked) {
                throw new RuntimeException("Seats not available");
            }
            Booking booking = createBooking(
                    request.getTheatreId(),
                    request.getShowId(),
                    request.getShowTime(),
                    request.getSeatNumbers(),
                    request.getTicketPrice()
            );

            // Step 3: Call Payment Service (simulate success/failure)
            boolean paymentSuccess = true; // assume success for now

            if (paymentSuccess) {
                booking.setStatus("CONFIRMED");
                bookings.add(bookingRepository.save(booking));
                seatServiceClient.confirmBooking(request.getShowId(),  request.getSeatNumbers());
            } else {
                booking.setStatus("FAILED");
                bookings.add(bookingRepository.save(booking));
                seatServiceClient.releaseSeats(request.getShowId(),  request.getSeatNumbers());
            }
        }
        return bookings;
    }
    /**
     * Cancel a booking by ID.
     */
    public boolean cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            bookingRepository.deleteById(bookingId);
            seatServiceClient.releaseSeats(booking.getShowId(),
                    List.of(booking.getSeats().split(",")));
            return true;
        }
        return false;
    }

    /**
     * Cancel multiple bookings in bulk.
     */
    public List<Long> cancelBookings(List<Long> bookingIds) {
        List<Long> cancelled = new ArrayList<>();
        for (Long id : bookingIds) {
            if (cancelBooking(id)) {
                cancelled.add(id);
            }
        }
        return cancelled;
    }

}

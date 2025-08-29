package com.xyz.seat.service;

import com.example.seatservice.entity.Seat;
import com.example.seatservice.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Allocate seats for a show (bulk insert).
     */
    public List<Seat> allocateSeats(Long theatreId, Long showId, List<String> seatNumbers) {
        List<Seat> seats = seatNumbers.stream().map(seatNum -> {
            Seat seat = new Seat();
            seat.setTheatreId(theatreId);
            seat.setShowId(showId);
            seat.setSeatNumber(seatNum);
            seat.setAvailable(true);
            return seat;
        }).toList();
        return seatRepository.saveAll(seats);
    }

    /**
     * Update seat availability (mark as unavailable/available).
     */
    public Seat updateSeatAvailability(Long seatId, boolean available) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));
        seat.setAvailable(available);
        return seatRepository.save(seat);
    }

    /**
     * Get seat inventory for a show.
     */
    public List<Seat> getSeats(Long theatreId, Long showId) {
        return seatRepository.findByTheatreIdAndShowId(theatreId, showId);
    }
    public void initializeSeats(Long showId, int totalSeats) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setShowId(showId);
            seat.setSeatNumber("S" + i);
            seat.setBooked(false);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
    }
    public List<Seat> getSeatsForShow(Long showId) {
        return seatRepository.findByShowId(showId);
    }
    // Lock seats for booking
    public boolean lockSeats(Long showId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.findByShowId(showId);
        for (Seat seat : seats) {
            if (seatNumbers.contains(seat.getSeatNumber())) {
                if (seat.isBooked() || seat.isLocked()) {
                    return false; // already taken
                }
                seat.setLocked(true);
                seatRepository.save(seat);
            }
        }
        return true;
    }

    // Confirm booking (after payment)
    public void confirmBooking(Long showId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.findByShowId(showId);
        for (Seat seat : seats) {
            if (seatNumbers.contains(seat.getSeatNumber())) {
                seat.setBooked(true);
                seat.setLocked(false);
                seatRepository.save(seat);
            }
        }
    }

    // Release seats (if payment fails or timeout)
    public void releaseSeats(Long showId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.findByShowId(showId);
        for (Seat seat : seats) {
            if (seatNumbers.contains(seat.getSeatNumber())) {
                seat.setLocked(false);
                seatRepository.save(seat);
            }
        }
    }
}

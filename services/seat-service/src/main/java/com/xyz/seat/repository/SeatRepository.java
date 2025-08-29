package com.xyz.seat.repository;

import com.example.seatservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTheatreIdAndShowId(Long theatreId, Long showId);
    List<Seat> findByShowId(Long showId);
}

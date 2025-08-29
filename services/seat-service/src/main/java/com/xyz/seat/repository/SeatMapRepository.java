package com.xyz.seat.repository;

import com.xyz.seat.model.SeatMap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SeatMapRepository extends JpaRepository<SeatMap, Long> {
    Optional<SeatMap> findByTheatreId(Long theatreId);
}

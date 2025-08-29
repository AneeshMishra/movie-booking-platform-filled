package com.xyz.movie.repository;

import com.xyz.movie.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByTheatreId(Long theatreId);
    @Query("SELECT s FROM Show s " +
            "WHERE s.movie.id = :movieId " +
            "AND s.theatre.city = :city " +
            "AND DATE(s.showTime) = :date")
    List<Show> findShowsByMovieCityAndDate(Long movieId, String city, LocalDate date);
    List<Show> findByTheatreIdAndShowTimeBetween(Long theatreId, LocalDateTime start, LocalDateTime end);
}

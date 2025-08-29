package com.xyz.seat.controller;

import com.xyz.seat.model.SeatMap;
import com.xyz.seat.repository.SeatMapRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seatmaps")
public class SeatMapController {
    private final SeatMapRepository repo;
    private final SeatService seatService;

    public SeatMapController(SeatMapRepository repo,SeatService seatService){
        this.repo = repo;
        this.seatService=seatService;
    }
    @PostMapping public SeatMap create(@RequestBody SeatMap s){ return repo.save(s); }
    @GetMapping("/theatre/{theatreId}") public SeatMap byTheatre(@PathVariable Long theatreId){ return repo.findByTheatreId(theatreId).orElseThrow(); }

    // Allocate seats for a show
    @PostMapping("/allocate")
    public List<Seat> allocateSeats(@RequestParam Long theatreId,
                                    @RequestParam Long showId,
                                    @RequestBody List<String> seatNumbers) {
        return seatService.allocateSeats(theatreId, showId, seatNumbers);
    }

    // Update seat availability
    @PutMapping("/{seatId}/availability")
    public Seat updateSeatAvailability(@PathVariable Long seatId,
                                       @RequestParam boolean available) {
        return seatService.updateSeatAvailability(seatId, available);
    }

    // Get seat inventory
    @GetMapping
    public List<Seat> getSeats(@RequestParam Long theatreId,
                               @RequestParam Long showId) {
        return seatService.getSeats(theatreId, showId);
    }
    @PostMapping("/initialize")
    public String initializeSeats(@RequestParam Long showId, @RequestParam int totalSeats) {
        seatService.initializeSeats(showId, totalSeats);
        return "Seats initialized for show " + showId;
    }

    @GetMapping("/{showId}")
    public List<Seat> getSeatsForShow(@PathVariable Long showId) {
        return seatService.getSeatsForShow(showId);
    }
}


package com.xyz.movie.controller;

import com.xyz.movie.model.Show;
import com.xyz.movie.repository.ShowRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {
    private final ShowRepository repo;
    private final ShowService showService;

    public ShowController(ShowRepository repo, ShowService showService ){this.repo=repo;
    this.showService=showService;}

    @GetMapping public List<Show> list(@RequestParam(required=false) Long theatreId){ if(theatreId!=null) return repo.findByTheatreId(theatreId); return repo.findAll(); }
    @GetMapping("findMovieByCity")
    public List<Show> browseShows(
            @RequestParam Long movieId,
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return showService.getShows(movieId, city, date);
    }
    // Create show
    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return showService.createShow(show);
    }

    // Update show
    @PutMapping("/{showId}")
    public Show updateShow(@PathVariable Long showId, @RequestBody Show updatedShow) {
        return showService.updateShow(showId, updatedShow);
    }

    // Delete show
    @DeleteMapping("/{showId}")
    public String deleteShow(@PathVariable Long showId) {
        showService.deleteShow(showId);
        return "Show deleted successfully";
    }

    // List shows for a theatre on a given date
    @GetMapping("/theatre/{theatreId}")
    public List<Show> listShows(@PathVariable Long theatreId, @RequestParam String date) {
        return showService.listShowsForTheatre(theatreId, LocalDate.parse(date));
    }
}

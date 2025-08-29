package com.xyz.movie.service



@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final SeatServiceClient seatServiceClient;

    public ShowService(ShowRepository showRepository,SeatServiceClient seatServiceClient) {
        this.showRepository = showRepository;
        this.seatServiceClient = seatServiceClient;
    }

    public List<Show> getShows(Long movieId, String city, LocalDate date) {
        return showRepository.findShowsByMovieCityAndDate(movieId, city, date);
    }
    // Create new show
    public Show createShow(Show show) {
        // Call Seat Service to initialize seats
        seatServiceClient.initializeSeats(savedShow.getId(), totalSeats);

        return showRepository.save(show);
    }

    // Update existing show
    public Show updateShow(Long showId, Show updatedShow) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found: " + showId));

        show.setMovieId(updatedShow.getMovieId());
        show.setScreenName(updatedShow.getScreenName());
        show.setShowTime(updatedShow.getShowTime());
        show.setPrice(updatedShow.getPrice());
        return showRepository.save(show);
    }

    // Delete show
    public void deleteShow(Long showId) {
        if (!showRepository.existsById(showId)) {
            throw new RuntimeException("Show not found: " + showId);
        }
        showRepository.deleteById(showId);
    }

    // List shows for a theatre on a given day
    public List<Show> listShowsForTheatre(Long theatreId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return showRepository.findByTheatreIdAndShowTimeBetween(theatreId, startOfDay, endOfDay);
    }
}


}
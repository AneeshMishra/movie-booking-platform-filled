package com.xyz.booking.client;
@FeignClient(name = "seat-service", url = "http://localhost:8082/seats")
public interface SeatServiceClient {

    @PostMapping("/lock")
    boolean lockSeats(@RequestParam Long showId, @RequestBody List<String> seatNumbers);

    @PostMapping("/confirm")
    void confirmBooking(@RequestParam Long showId, @RequestBody List<String> seatNumbers);

    @PostMapping("/release")
    void releaseSeats(@RequestParam Long showId, @RequestBody List<String> seatNumbers);
}

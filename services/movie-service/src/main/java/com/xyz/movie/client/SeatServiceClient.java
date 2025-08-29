package com.xyz.movie.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seat-service", url = "http://localhost:8082")  // Seat Service URL
public interface SeatServiceClient {

    @PostMapping("/seats/initialize")
    void initializeSeats(@RequestParam Long showId, @RequestParam int totalSeats);
}

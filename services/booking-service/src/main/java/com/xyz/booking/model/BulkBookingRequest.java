package com.xyz.booking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BulkBookingRequest {
    private Long theatreId;
    private Long showId;
    private LocalDateTime showTime;
    private List<String> seatNumbers;
    private BigDecimal ticketPrice;

    // Getters and Setters
    public Long getTheatreId() { return theatreId; }
    public void setTheatreId(Long theatreId) { this.theatreId = theatreId; }
    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }
    public LocalDateTime getShowTime() { return showTime; }
    public void setShowTime(LocalDateTime showTime) { this.showTime = showTime; }
    public List<String> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<String> seatNumbers) { this.seatNumbers = seatNumbers; }
    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
}

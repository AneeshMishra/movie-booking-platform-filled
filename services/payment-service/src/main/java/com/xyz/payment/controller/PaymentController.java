package com.xyz.payment.controller;

import com.xyz.payment.model.Payment;
import com.xyz.payment.repository.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentRepository repo;
    public PaymentController(PaymentRepository repo){this.repo=repo;}

    @PostMapping("/initiate")
    public ResponseEntity<?> initiate(@RequestBody Map<String,Object> req){
        Long bookingId = ((Number)req.get("bookingId")).longValue();
        Payment p = new Payment();
        p.setBookingId(bookingId);
        p.setStatus("PENDING");
        p.setCreatedAt(OffsetDateTime.now());
        repo.save(p);
        return ResponseEntity.ok(Map.of("paymentId", p.getId(), "checkoutUrl", "https://mock.checkout/"+p.getId()));
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody Map<String,Object> payload){
        Long paymentId = ((Number)payload.get("paymentId")).longValue();
        String status = (String)payload.get("status");
        Payment p = repo.findById(paymentId).orElseThrow();
        p.setStatus(status);
        p.setExternalRef((String)payload.get("externalRef"));
        repo.save(p);
        return ResponseEntity.ok(Map.of("received", true));
    }
}

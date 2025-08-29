package com.xyz.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody Map<String,Object> req){
        System.out.println("Notification queued: " + req);
        return ResponseEntity.ok(Map.of("status","queued"));
    }
}

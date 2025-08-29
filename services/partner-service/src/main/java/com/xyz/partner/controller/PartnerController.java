package com.xyz.partner.controller;

import com.xyz.partner.model.Partner;
import com.xyz.partner.repository.PartnerRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {
    private final PartnerRepository repo;
    public PartnerController(PartnerRepository repo){this.repo = repo;}
    @PostMapping public Partner create(@RequestBody Partner p){ return repo.save(p); }
    @GetMapping public List<Partner> list(){ return repo.findAll(); }
}

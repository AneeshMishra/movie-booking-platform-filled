package com.xyz.partner.repository;

import com.xyz.partner.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {}

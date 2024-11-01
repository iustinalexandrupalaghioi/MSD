package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.VATRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VATRateRepository extends JpaRepository<VATRate, Long> {

}

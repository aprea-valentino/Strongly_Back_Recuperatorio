package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    
}
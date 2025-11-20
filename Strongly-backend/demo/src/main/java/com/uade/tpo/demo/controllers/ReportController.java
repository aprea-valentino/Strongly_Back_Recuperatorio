package com.uade.tpo.demo.controllers;

import com.uade.tpo.demo.entity.dto.ReportResponse;
import com.uade.tpo.demo.entity.Report;
import com.uade.tpo.demo.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import com.uade.tpo.demo.entity.dto.ReportPathsRequest;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService service;
    public ReportController(ReportService service) { this.service = service; }

   
    private ReportResponse toResponseDto(Report r) {
        ReportResponse dto = new ReportResponse();
        dto.setId(r.getId());
        dto.setMessage("Report recibido");
        dto.setCreatedAt(r.getCreatedAt());
        dto.setImageUrls(r.getImages() == null ? java.util.List.of() :
            r.getImages().stream().map(img -> img.getUrl()).collect(Collectors.toList()));
        return dto;
    }

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReportFromPaths(@Valid @RequestBody ReportPathsRequest dto) {
        try {
            Report saved = service.createReportFromLocalPaths(dto.getFullname(), dto.getProblem(), dto.getDescription(), dto.getMultipartFile());

            ReportResponse resp = toResponseDto(saved);

            URI location = URI.create("/api/reports/" + saved.getId());
            return ResponseEntity.created(location).body(resp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(
                java.util.Map.of("error", "No se pudo crear el reporte", "detail", ex.getMessage())
            );
        }
    }
}

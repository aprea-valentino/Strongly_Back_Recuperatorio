package com.uade.tpo.demo.controllers;
import com.uade.tpo.demo.entity.dto.ReportRequest;
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

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService service;
    public ReportController(ReportService service) { this.service = service; }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createReport(@Valid @ModelAttribute ReportRequest dto, BindingResult br) {
        if (br.hasErrors()) {
            return ResponseEntity.badRequest().body(br.getAllErrors());
        }
        try {
            Report saved = service.createReport(dto.getFullname(), dto.getProblem(), dto.getDescription(), dto.getFiles());

            ReportResponse resp = toResponseDto(saved);

            // Location header opcional
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
    private ReportResponse toResponseDto(Report r) {
        ReportResponse dto = new ReportResponse();
        dto.setId(r.getId());
        dto.setMessage("Report recibido");
        dto.setCreatedAt(r.getCreatedAt());
        dto.setImageUrls(r.getImages() == null ? java.util.List.of() :
            r.getImages().stream().map(img -> img.getUrl()).collect(Collectors.toList()));
        return dto;
    }
}

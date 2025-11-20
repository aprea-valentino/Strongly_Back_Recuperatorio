package com.uade.tpo.demo.entity.dto;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Long id;
    private String message;
    private Instant createdAt;
    private List<String> imageUrls;

}

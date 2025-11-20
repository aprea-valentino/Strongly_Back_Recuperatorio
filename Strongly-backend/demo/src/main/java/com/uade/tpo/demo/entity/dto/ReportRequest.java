package com.uade.tpo.demo.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequest {
    @NotBlank(message = "Es requerido el nombre completo")
    @Size(max = 255)
    private String fullname;

    @NotBlank(message = "Es requerida la problematica del report")
    @Size(max = 255)
    private String problem;

    @NotBlank(message = "Es requerida la descripcion del report")
    private String description;

    // Para multipart/form-data
    private MultipartFile[] files;
}

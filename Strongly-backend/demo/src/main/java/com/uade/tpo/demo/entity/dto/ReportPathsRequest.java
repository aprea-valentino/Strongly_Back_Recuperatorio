package com.uade.tpo.demo.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportPathsRequest {
    @NotBlank(message = "Es requerido el nombre completo")
    @Size(max = 255)
    private String fullname;

    @NotBlank(message = "Es requerida la problematica del report")
    @Size(max = 255)
    private String problem;

    @NotBlank(message = "Es requerida la descripcion del report")
    private String description;

    // Lista de rutas absolutas en el servidor (p.ej. C:/uade/file.png)
    private List<String> multipartFile;
}

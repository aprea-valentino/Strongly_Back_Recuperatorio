package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Report;
import com.uade.tpo.demo.entity.ReportImage;
import com.uade.tpo.demo.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.util.StringUtils;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final Path uploadRoot;
    private final Path allowedLocalRoot;
    private final long maxFileSize;
    private final Set<String> allowedTypes;

    public ReportServiceImpl(ReportRepository reportRepository,
                             @Value("${app.upload.dir:uploads}") String uploadDir,
                             @Value("${app.upload.allowed-local-root:}") String allowedLocalRootStr,
                             @Value("${app.upload.max-file-size:5242880}") long maxFileSize,
                             @Value("${app.upload.allowed-types:image/png,image/jpeg,image/jpg}") String allowedTypesStr) {
        this.reportRepository = reportRepository;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (allowedLocalRootStr != null && !allowedLocalRootStr.isBlank()) {
            this.allowedLocalRoot = Paths.get(allowedLocalRootStr).toAbsolutePath().normalize();
        } else {
            this.allowedLocalRoot = null;
        }
        this.maxFileSize = maxFileSize;
        this.allowedTypes = Arrays.stream(allowedTypesStr.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

  
    @Override
    @Transactional
    public Report createReportFromLocalPaths(String fullname, String problem, String description, List<String> localPaths) throws IOException {
        Report r = new Report();
        r.setFullname(fullname);
        r.setProblem(problem);
        r.setDescription(description);

        r = reportRepository.save(r);

        if (localPaths != null && !localPaths.isEmpty()) {
            Path dir = uploadRoot.resolve("reports").resolve(String.valueOf(r.getId()));
            Files.createDirectories(dir);

            Path allowedRoot = (allowedLocalRoot != null) ? allowedLocalRoot : uploadRoot;

            for (String raw : localPaths) {
                if (raw == null || raw.isBlank()) continue;

                Path p = Paths.get(raw).toAbsolutePath().normalize();

                
                if (!p.startsWith(allowedRoot)) {
                    throw new IllegalArgumentException("Ruta no permitida: " + raw);
                }

               
                try {
                    Path real = p.toRealPath();
                    if (!real.startsWith(allowedRoot)) {
                        throw new IllegalArgumentException("Ruta no permitida (symlink): " + raw);
                    }
                    p = real;
                } catch (IOException ex) {
                    throw new IllegalArgumentException("No se puede acceder a la ruta: " + raw + " -> " + ex.getMessage());
                }

                if (!Files.exists(p) || !Files.isReadable(p) || Files.isDirectory(p)) {
                    throw new IllegalArgumentException("Archivo no encontrado o no legible: " + raw);
                }

                long size = Files.size(p);
                if (size > maxFileSize) {
                    throw new IllegalArgumentException("Archivo demasiado grande: " + raw);
                }

                String contentType = Files.probeContentType(p);
                if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
                    throw new IllegalArgumentException("Tipo de archivo no permitido: " + contentType + " (" + raw + ")");
                }

                String original = StringUtils.cleanPath(p.getFileName().toString());
                String filename = System.currentTimeMillis() + "_" + original;
                Path dest = dir.resolve(filename);

                // Copy stream-wise
                Files.copy(p, dest, StandardCopyOption.REPLACE_EXISTING);

                ReportImage img = new ReportImage();
                img.setUrl("/uploads/reports/" + r.getId() + "/" + filename);
                r.addImage(img);
            }

            r = reportRepository.save(r);
        }

        return r;
    }
}

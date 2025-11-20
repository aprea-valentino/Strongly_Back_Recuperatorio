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
import org.springframework.util.StringUtils;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final Path uploadRoot;
    private final long maxFileSize;
    private final Set<String> allowedTypes;

    public ReportServiceImpl(ReportRepository reportRepository,
                             @Value("${app.upload.dir:uploads}") String uploadDir,
                             @Value("${app.upload.max-file-size:5242880}") long maxFileSize,
                             @Value("${app.upload.allowed-types:image/png,image/jpeg,image/jpg}") String allowedTypesStr) {
        this.reportRepository = reportRepository;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.maxFileSize = maxFileSize;
        this.allowedTypes = Arrays.stream(allowedTypesStr.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Report createReport(String fullname, String problem, String description, MultipartFile[] files) throws IOException {
        Report r = new Report();
        r.setFullname(fullname);
        r.setProblem(problem);
        r.setDescription(description);

        
        r = reportRepository.save(r);

        if (files != null && files.length > 0) {
            Path dir = uploadRoot.resolve("reports").resolve(String.valueOf(r.getId()));
            Files.createDirectories(dir);
            for (MultipartFile mf : files) {
                if (mf == null || mf.isEmpty()) continue;

                // valido content type
                String contentType = mf.getContentType();
                if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
                    throw new IllegalArgumentException("Tipo de archivo no permitido: " + contentType);
                }

                // valido size
                if (mf.getSize() > maxFileSize) {
                    throw new IllegalArgumentException("Archivo demasiado grande: " + mf.getOriginalFilename());
                }

                String original = StringUtils.cleanPath(Paths.get(mf.getOriginalFilename()).getFileName().toString());
                String filename = System.currentTimeMillis() + "_" + original;
                Path dest = dir.resolve(filename);
                Files.copy(mf.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

                ReportImage img = new ReportImage();
                img.setUrl("/uploads/reports/" + r.getId() + "/" + filename);
                r.addImage(img);
            }
            r = reportRepository.save(r);
        }

        return r;
    }
}

package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Report;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ReportService {
	Report createReport(String fullname, String problem, String description, MultipartFile[] files) throws IOException;
}

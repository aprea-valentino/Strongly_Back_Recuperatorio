package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Report;

import java.io.IOException;
import java.util.List;

public interface ReportService {

	
	Report createReportFromLocalPaths(String fullname, String problem, String description, List<String> localPaths) throws IOException;
}

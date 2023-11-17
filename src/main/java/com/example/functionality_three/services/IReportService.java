package com.example.functionality_three.services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IReportService {
    ResponseEntity<Map<String, String>> createReport(String reportType, int howMany);
}

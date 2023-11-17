package com.example.functionality_three.DTOs;

import java.util.Map;

public class ReportDTO {
    Map<String,String> report;

    public ReportDTO(Map<String, String> report) {
        this.report = report;
    }

    public Map<String, String> getReport(){
        return report;
    }
}

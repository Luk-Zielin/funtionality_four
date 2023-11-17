package com.example.functionality_three.controllers;

import com.example.functionality_three.repositories.FoldersJpaRepository;
import com.example.functionality_three.repositories.MetadataJpaRepository;
import com.example.functionality_three.services.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {
    final
    MetadataJpaRepository metadataRepository;
    final
    FoldersJpaRepository foldersRepository;
    ReportService reportService;
    public ReportController(FoldersJpaRepository foldersRepository, MetadataJpaRepository metadataRepository){
        this.foldersRepository = foldersRepository;
        this.metadataRepository = metadataRepository;
        reportService = new ReportService(metadataRepository, foldersRepository);
    }
    @GetMapping("/{reportType}")
    public ResponseEntity<Map<String,String>> getReportFiles(@PathVariable String reportType, @RequestParam(required = false) Integer howmany){
        if(howmany == null){
            return reportService.createReport(reportType,1);
        }
        return reportService.createReport(reportType,howmany);
    }



}

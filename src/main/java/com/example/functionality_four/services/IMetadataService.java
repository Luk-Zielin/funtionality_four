package com.example.functionality_four.services;

import com.example.functionality_four.DTOs.FileMetadataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface IMetadataService {
    String createFile(FileMetadataDTO fileMetadataDTO, Model model);
    String deleteFile(String filename, Model model);
    String readFile(String filename, Model model);
    String updateFile(String filename, FileMetadataDTO updatedFile, Model model);
    ResponseEntity<FileMetadataDTO> readFile(String filename);
    ResponseEntity<FileMetadataDTO> createFile(FileMetadataDTO fileMetadataDTO);
    ResponseEntity<FileMetadataDTO> updateFile(String filename, FileMetadataDTO updatedFile);
}

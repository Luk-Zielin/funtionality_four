package com.example.functionality_four.services;

import com.example.functionality_four.DTOs.FolderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface IFoldersService {
    String createFolder(FolderDTO folderDTO, Model model);
    String deleteFolder(String folderName, Model model);
    String readFolder(String folderName, Model model);
    String updateFolder(String folderName, FolderDTO updatedFolder, Model model);

    ResponseEntity<FolderDTO> updateFolder(String folderName, FolderDTO updatedFolder);

    ResponseEntity<FolderDTO> createFolder(FolderDTO newFolder);
}

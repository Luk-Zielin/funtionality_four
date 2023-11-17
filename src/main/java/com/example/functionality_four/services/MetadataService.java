package com.example.functionality_four.services;

import com.example.functionality_four.DTOs.FileMetadataDTO;
import com.example.functionality_four.entities.FileMetadata;
import com.example.functionality_four.entities.Folder;
import com.example.functionality_four.repositories.FoldersJpaRepository;
import com.example.functionality_four.repositories.MetadataJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetadataService implements IMetadataService {

    private final MetadataJpaRepository metadataRepository;

    private final FoldersJpaRepository foldersRepository;
    @Autowired
    public MetadataService(MetadataJpaRepository metadataJpaRepository, FoldersJpaRepository foldersJpaRepository){
        metadataRepository = metadataJpaRepository;
        foldersRepository = foldersJpaRepository;
    }
    @Override
    public String createFile(FileMetadataDTO fileMetadataDTO, Model model) {
        if (fileMetadataDTO==null){
            return "files/add";
        }
        List<Folder> folders = new ArrayList<>();
        for (String folder :
                fileMetadataDTO.getFolders().split("(,)|(, )")) {
            foldersRepository.findByName(folder).ifPresent(folders::add);
        }
        FileMetadata fileMetadata = new FileMetadata(fileMetadataDTO.getFilename(),
                Integer.parseInt(fileMetadataDTO.getSize().split(" ")[0]),
                folders
        );
        if (metadataRepository.findByFilename(fileMetadata.getFilename()).isPresent()) {
            return readFile(fileMetadata.getFilename(), model);
        }
        FileMetadata savedFile = metadataRepository.save(fileMetadata);
        return readFile(savedFile.getFilename(), model);
    }


    public String readFile(String filename, Model model) {
        if(filename==null){
            return "files/search";
        }
        return metadataRepository.findByFilename(filename)
                .map(fileMetadata -> {
                    String string = fileMetadata.getParentFolders().stream().map(Folder::toString).reduce((s1, s2) -> s1.isEmpty() ? s2 : s1 + ", " + s2)
                            .orElse("");
                    FileMetadataDTO file = new FileMetadataDTO(
                            fileMetadata.getFilename(),
                            fileMetadata.getSize(),
                            string
                    );
                    model.addAttribute("file",file);
                    return "files/get";
                }).orElse("redirect:/files");
    }
    public ResponseEntity<FileMetadataDTO> readFile(String filename) {
        if(filename==null){
            return ResponseEntity.badRequest().build();
        }
        return metadataRepository.findByFilename(filename)
                .map(fileMetadata -> {
                    String string = fileMetadata.getParentFolders().stream().map(Folder::toString).reduce((s1, s2) -> s1.isEmpty() ? s2 : s1 + ", " + s2)
                            .orElse("");
                    FileMetadataDTO file = new FileMetadataDTO(
                            fileMetadata.getFilename(),
                            fileMetadata.getSize(),
                            string
                    );

                    return ResponseEntity.ok().body(file);
                }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<FileMetadataDTO> createFile(FileMetadataDTO fileMetadataDTO) {
        if (fileMetadataDTO==null){
            return ResponseEntity.badRequest().build();
        }
        List<Folder> folders = new ArrayList<>();
        for (String folder :
                fileMetadataDTO.getFolders().split("(,)|(, )")) {
            foldersRepository.findByName(folder).ifPresent(folders::add);
        }
        FileMetadata fileMetadata = new FileMetadata(fileMetadataDTO.getFilename(),
                Integer.parseInt(fileMetadataDTO.getSize().split(" ")[0]),
                folders
        );
        if (metadataRepository.findByFilename(fileMetadata.getFilename()).isPresent()) {
            return readFile(fileMetadata.getFilename());
        }
        FileMetadata savedFile = metadataRepository.save(fileMetadata);
        return readFile(savedFile.getFilename());
    }

    @Override
    public ResponseEntity<FileMetadataDTO> updateFile(String filename, FileMetadataDTO updatedFile) {
        return metadataRepository.findByFilename(filename)
                .map(existingFile -> {
                    existingFile.setSize(Integer.parseInt(updatedFile.getSize().split(" ")[0]));
                    List<Folder> folders = new ArrayList<>();

                    for (String folder:
                            updatedFile.getFolders().split("(,)|(, )")) {
                        foldersRepository.findByName(folder).ifPresent(folders::add);
                    }
                    existingFile.setParentFolders(folders);
                    metadataRepository.save(existingFile);
                    return ResponseEntity.ok(updatedFile);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public String updateFile(String filename,FileMetadataDTO updatedFile, Model model) {
        return metadataRepository.findByFilename(filename)
                .map(existingFile -> {
                    existingFile.setSize(Integer.parseInt(updatedFile.getSize().split(" ")[0]));
                    List<Folder> folders = new ArrayList<>();

                    for (String folder:
                            updatedFile.getFolders().split("(,)|(, )")) {
                        foldersRepository.findByName(folder).ifPresent(folders::add);
                    }
                    existingFile.setParentFolders(folders);
                    metadataRepository.save(existingFile);
                    model.addAttribute("file", existingFile);
                    return "redirect:/files";
                })
                .orElse("files/edit");
    }

    @Override
    public String deleteFile(String filename, Model model) {
        if(filename==null){
            return "files/delete";
        }
        return metadataRepository.findByFilename(filename)
                .map(existingFile -> {
                    metadataRepository.delete(existingFile);

                    return "redirect:/files";
                })
                .orElse("redirect:/files");
    }




}

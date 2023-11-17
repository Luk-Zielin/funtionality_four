package com.example.functionality_three.services;

import com.example.functionality_three.DTOs.FolderDTO;
import com.example.functionality_three.entities.Folder;
import com.example.functionality_three.repositories.FoldersJpaRepository;
import com.example.functionality_three.repositories.MetadataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoldersService implements IFoldersService{
    private final MetadataJpaRepository metadataRepository;

    private final FoldersJpaRepository foldersRepository;

    @Autowired
    public FoldersService(MetadataJpaRepository metadataRepository, FoldersJpaRepository foldersRepository) {
        this.metadataRepository = metadataRepository;
        this.foldersRepository = foldersRepository;
    }


    @Override
    public String createFolder(FolderDTO folderDTO, Model model) {
        if (folderDTO==null){
            return "folders/add";
        }
        List<Folder> childFolders = new ArrayList<>();
        for (String folder :
                folderDTO.getChildFolders()){
            foldersRepository.findByName(folder).ifPresent(childFolders::add);
        }

        Folder folder = new Folder(folderDTO.getName(),
                childFolders
        );
        if (foldersRepository.findByName(folder.getName()).isPresent()) {

            return readFolder(folder.getName(), model);
        }

        Folder savedFolder = foldersRepository.save(folder);
        return readFolder(savedFolder.getName(), model);
    }

    @Override
    public String deleteFolder(String folderName, Model model) {
        if(folderName==null){
            return "folders/delete";
        }
        return foldersRepository.findByName(folderName)
                .map(existingFolder -> {
                    foldersRepository.delete(existingFolder);

                    return "redirect:/folders";
                })
                .orElse("redirect:/folders");
    }

    @Override
    public String readFolder(String folderName, Model model) {
        if(folderName==null){
            return "folders/search";
        }
        return foldersRepository.findByName(folderName)
                .map(folder -> {
                    List<String> childFolders = folder.getChildFolders().stream().map(Folder::toString).toList();
                    FolderDTO folderToAdd = new FolderDTO(
                            folder.getName(),
                            childFolders
                    );
                    model.addAttribute("folder",folderToAdd);
                    return "folders/get";
                }).orElse("redirect:/folders");
    }
    public ResponseEntity<FolderDTO> readFolder(String folderName) {
        if(folderName==null){
            return ResponseEntity.badRequest().build();
        }
        return foldersRepository.findByName(folderName)
                .map(folder -> {
                    List<String> childFolders = folder.getChildFolders().stream().map(Folder::toString).toList();
                    FolderDTO folderToAdd = new FolderDTO(
                            folder.getName(),
                            childFolders
                    );

                    return ResponseEntity.ok().body(folderToAdd);
                }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public String updateFolder(String folderName, FolderDTO updatedFolder, Model model) {
        return foldersRepository.findByName(folderName)
                .map(existingFolder -> {
                    List<Folder> childFolders = new ArrayList<>();
                    for (String folder:
                            updatedFolder.getChildFolders()) {
                        foldersRepository.findByName(folder).ifPresent(childFolders::add);
                    }
                    existingFolder.setChildFolders(childFolders);
                    foldersRepository.save(existingFolder);
                    model.addAttribute("folder", existingFolder);
                    return "redirect:/folders";
                })
                .orElse("folders/edit");
    }

    @Override
    public ResponseEntity<FolderDTO> updateFolder(String folderName, FolderDTO updatedFolder) {
        return foldersRepository.findByName(folderName)
                .map(existingFolder -> {
                    List<Folder> childFolders = new ArrayList<>();
                    for (String folder:
                            updatedFolder.getChildFolders()) {
                        foldersRepository.findByName(folder).ifPresent(childFolders::add);
                    }
                    existingFolder.setChildFolders(childFolders);
                    foldersRepository.save(existingFolder);
                    return ResponseEntity.ok(updatedFolder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<FolderDTO> createFolder(FolderDTO newFolder) {
        if (newFolder==null){
            return ResponseEntity.badRequest().build();
        }
        List<Folder> childFolders = new ArrayList<>();
        for (String folder :
                newFolder.getChildFolders()){
            foldersRepository.findByName(folder).ifPresent(childFolders::add);
        }

        Folder folder = new Folder(newFolder.getName(),
                childFolders
        );
        if (foldersRepository.findByName(folder.getName()).isPresent()) {

            return readFolder(folder.getName());
        }

        Folder savedFolder = foldersRepository.save(folder);
        return readFolder(savedFolder.getName());
    }
}

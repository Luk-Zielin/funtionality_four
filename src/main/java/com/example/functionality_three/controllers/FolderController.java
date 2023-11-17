package com.example.functionality_three.controllers;

import com.example.functionality_three.DTOs.FolderDTO;
import com.example.functionality_three.repositories.FoldersJpaRepository;
import com.example.functionality_three.repositories.MetadataJpaRepository;
import com.example.functionality_three.services.FoldersService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/folders")
public class FolderController {
    final
    MetadataJpaRepository metadataRepository;
    final
    FoldersJpaRepository foldersRepository;
    FoldersService foldersService;

    public FolderController(MetadataJpaRepository metadataRepository, FoldersJpaRepository foldersRepository, FoldersService foldersService) {
        this.metadataRepository = metadataRepository;
        this.foldersRepository = foldersRepository;
        this.foldersService = foldersService;
    }

    @GetMapping("/{folderName}")
    public ResponseEntity<FolderDTO> getFileMetadata(@PathVariable String folderName){

        return foldersService.readFolder(folderName);
    }
    @RequestMapping("/search")
    public String getFileMetadata(Model model, @RequestParam(value = "searchName",required = false) String name){

        return foldersService.readFolder(name, model);
    }
    @GetMapping
    public String startPage(){
        return "folders/foldersmenu";
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FolderDTO> postFile(@RequestBody FolderDTO folderDTO) {
        return foldersService.createFolder(folderDTO);
    }
    @PostMapping("/add/new")
    public String postFromMenu(Model model,@RequestParam(value = "foldername",required = false) String name,
                               @RequestParam(value = "childfolders", required = false) String childFolders) {
        List<String> childList = new ArrayList<>();
        if(childFolders!=null) {
            childList.addAll(List.of(childFolders.split("(,)|(, )")));
        }
        FolderDTO folderDTO = new FolderDTO(name, childList);
        return foldersService.createFolder(folderDTO, model);
    }
    @RequestMapping("/add")
    public String addPage(){
        return "folders/add";
    }
    @PutMapping("/{folderName}")
    public ResponseEntity<FolderDTO> putFolder(@PathVariable String folderName, @RequestBody FolderDTO updatedFolder) {
        return foldersService.updateFolder(folderName, updatedFolder);
    }
    @DeleteMapping("/{folderName}")
    public String deleteFolder(@PathVariable String folderName, Model model) {
        return foldersService.deleteFolder(folderName, model);
    }
    @PostMapping()
    public ResponseEntity<FolderDTO> postFolder(@RequestBody FolderDTO newFolder){
        return foldersService.createFolder(newFolder);
    }
    @GetMapping("/delete")
    public String deleteMenu(){
        return "folders/delete";
    }
    @DeleteMapping("/delete")
    public String deleteFromMenu(Model model, @RequestParam(value = "searchName",required = false) String name){
        return foldersService.deleteFolder(name, model);
    }
    @GetMapping("/edit")
    public String editFile(){
        return "folders/edit";
    }
    @PutMapping("/edit")
    public String editFromMenu(Model model,@RequestParam(value = "foldername",required = false) String name,
                               @RequestParam(value = "childfolders", required = false) String childFolders) {
        List<String> childList = List.of(childFolders.split("(,)|(, )"));
        FolderDTO folderDTO = new FolderDTO(name, childList);
        return foldersService.updateFolder(name, folderDTO, model);
    }

}

package com.example.functionality_three.controllers;

import com.example.functionality_three.DTOs.FileMetadataDTO;
import com.example.functionality_three.repositories.FoldersJpaRepository;
import com.example.functionality_three.repositories.MetadataJpaRepository;
import com.example.functionality_three.services.MetadataService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/files")
public class MetadataController {
    final
    MetadataJpaRepository metadataRepository;
    final
    FoldersJpaRepository foldersRepository;
    MetadataService metadataService;
    public MetadataController(FoldersJpaRepository foldersRepository, MetadataJpaRepository metadataRepository){
        this.foldersRepository = foldersRepository;
        this.metadataRepository = metadataRepository;
        metadataService = new MetadataService(metadataRepository, foldersRepository);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<FileMetadataDTO> getFileMetadata(@PathVariable String filename){
        return metadataService.readFile(filename);
    }
    @RequestMapping("/search")
    public String getFileMetadata(Model model, @RequestParam(value = "searchName",required = false) String name){
        return metadataService.readFile(name, model);
    }
    @GetMapping
    public String startPage(){
        return "files/filesmenu";
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileMetadataDTO> postFile(@RequestBody FileMetadataDTO fileMetadataDTO) {
        return metadataService.createFile(fileMetadataDTO);
    }
    @PostMapping("/add/new")
    public String postFromMenu(Model model,@RequestParam(value = "filename",required = false) String name,
                               @RequestParam(value = "size",required = false) long size,
                               @RequestParam(value = "folders",required = false) String folders){
        FileMetadataDTO fileMetadataDTO = new FileMetadataDTO(name, size, folders);

        return metadataService.createFile(fileMetadataDTO, model);
    }
    @RequestMapping("/add")
    public String addPage(){
        return "files/add";
    }
    @PutMapping("/{filename}")
    public ResponseEntity<FileMetadataDTO> putFile(@PathVariable String filename, @RequestBody FileMetadataDTO updatedFile) {
        return metadataService.updateFile(filename, updatedFile);
    }
    @DeleteMapping("/{filename}")
    public String deleteFile(@PathVariable String filename, Model model) {
        return metadataService.deleteFile(filename, model);
    }
    @GetMapping("/delete")
    public String deleteMenu(){
        return "files/delete";
    }
    @DeleteMapping("/delete")
    public String deleteFromMenu(Model model, @RequestParam(value = "searchName",required = false) String name){
        return metadataService.deleteFile(name, model);
    }
    @GetMapping("/edit")
    public String editFile(){
        return "files/edit";
    }
    @PutMapping("/edit")
    public String editFromMenu(Model model,@RequestParam(value = "filename",required = false) String name,
                               @RequestParam(value = "size",required = false) long size,
                               @RequestParam(value = "folders",required = false) String folders){
        FileMetadataDTO fileMetadataDTO = new FileMetadataDTO(name, size, folders);
        return metadataService.updateFile(name, fileMetadataDTO, model);
    }
}

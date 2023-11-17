package com.example.functionality_three.entities;


import jakarta.persistence.*;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@Entity
@EntityScan("com.example.model")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String name;



    @ManyToMany(mappedBy = "parentFolders")
    private List<FileMetadata> fileMetadataList;
    @ManyToMany
    @Column(nullable = true)
    private List<Folder> childFolders;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Folder> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(List<Folder> childFolders) {
        this.childFolders = childFolders;
    }

    public Folder(){

    }
    public List<FileMetadata> getFileMetadataList() {
        return fileMetadataList;
    }

    public void setFileMetadataList(List<FileMetadata> fileMetadataList) {
        this.fileMetadataList = fileMetadataList;
    }

    public Folder(String name, List<Folder> childFolders) {
        this.name = name;
        this.childFolders = childFolders;
    }



    public Folder(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

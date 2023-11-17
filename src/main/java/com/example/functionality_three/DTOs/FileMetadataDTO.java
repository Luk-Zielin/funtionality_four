package com.example.functionality_three.DTOs;

import com.example.functionality_three.entities.FileMetadata;
import com.example.functionality_three.entities.Folder;

public class FileMetadataDTO {
    private String filename;
    private String size;
    private String folders;

    public FileMetadataDTO(String filename, long size, String folders) {
        this.filename = filename;
        this.size = (size+" B");
        this.folders = folders;
    }

    public FileMetadataDTO(FileMetadata fileMetadata) {
        this.filename = fileMetadata.getFilename();
        this.size = fileMetadata.getSize()+" B";
        fileMetadata.getParentFolders().stream().map(Folder::toString).reduce("", String::concat);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = (size+" B");
    }

    public String getFolders() {
        return folders;
    }

    public void setFolders(String folders) {
        this.folders = folders;
    }
}

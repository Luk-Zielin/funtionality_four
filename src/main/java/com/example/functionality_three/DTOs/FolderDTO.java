package com.example.functionality_three.DTOs;

import java.util.List;

public class FolderDTO {
    private String name;
    private List<String> childFolders;

    public FolderDTO(String name, List<String> childFolders) {
        this.name = name;
        this.childFolders = childFolders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(List<String> childFolders) {
        this.childFolders = childFolders;
    }
}

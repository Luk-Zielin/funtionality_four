package com.example.functionality_four.repositories;

import com.example.functionality_four.entities.FileMetadata;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetadataJpaRepository extends JpaRepository<FileMetadata,String> {
    Optional<FileMetadata> findByFilename(String filename) throws EntityNotFoundException;
}

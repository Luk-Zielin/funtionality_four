package com.example.functionality_three.repositories;

import com.example.functionality_three.entities.Folder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface FoldersJpaRepository extends JpaRepository<Folder,String> {
    Optional<Folder> findByName(String filename) throws EntityNotFoundException;
    @Query("SELECT f.name, AVG(fm.size) FROM Folder f " +
            "Left JOIN f.fileMetadataList fm " +
            "GROUP BY f.id ORDER BY (AVG(fm.size)) DESC ")
    List<Object[]> findFolderNameAndAverageSize();

    @Query("SELECT f.name, SUM(fm.size) FROM Folder f " +
            "Left JOIN f.fileMetadataList fm " +
            "GROUP BY f.id ORDER BY (AVG(fm.size)) DESC ")
    List<Object[]> findFolderNameSum();

    @Query("SELECT f.name, COUNT(fm) FROM Folder f " +
            "LEFT JOIN f.fileMetadataList fm " +
            "GROUP BY f.id")
    List<Object[]> findFolderNameAndNumberOfFiles();


}

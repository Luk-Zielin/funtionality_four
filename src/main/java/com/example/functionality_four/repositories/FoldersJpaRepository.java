package com.example.functionality_four.repositories;

import com.example.functionality_four.entities.Folder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

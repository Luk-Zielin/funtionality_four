package com.example.functionality_four.services;

import com.example.functionality_four.entities.FileMetadata;
import com.example.functionality_four.entities.Folder;
import com.example.functionality_four.repositories.FoldersJpaRepository;
import com.example.functionality_four.repositories.MetadataJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReportService{
    private final MetadataJpaRepository metadataRepository;
    private final FoldersJpaRepository foldersRepository;
    @Autowired
    public ReportService(MetadataJpaRepository metadataJpaRepository, FoldersJpaRepository foldersJpaRepository){
        metadataRepository = metadataJpaRepository;
        foldersRepository = foldersJpaRepository;
    }

    @Override
    public ResponseEntity<Map<String, String>> createReport(String reportType, int howMany) {
        HashMap<String,String > report = new HashMap<>();
        List<FileMetadata> files = metadataRepository.findAll();
        List<Folder> folders = foldersRepository.findAll();
        boolean correctName= false;
        switch(reportType){
            case "files","Files":
                long sum = files.stream().mapToLong(FileMetadata::getSize).sum();
                report.put("Sum of all files: ",String.valueOf(sum));
                OptionalDouble average = files.stream().mapToLong(FileMetadata::getSize).average();
                average.ifPresent(value -> report.put("Average of all files: ", String.valueOf(value)));
                Optional<FileMetadata> biggestFile = files.stream().max(Comparator.comparingLong(FileMetadata::getSize));
                Optional<FileMetadata> smallestFile = files.stream().min(Comparator.comparingLong(FileMetadata::getSize));
                biggestFile.ifPresent(fileMetadata -> report.put("Biggest file: ", fileMetadata.getFilename() +"="+ fileMetadata.getSize()));
                smallestFile.ifPresent(fileMetadata -> report.put("Smallest file: ", fileMetadata.getFilename() +"="+ fileMetadata.getSize()));
                report.put("The number of files: ",String.valueOf(files.size()));
                correctName=true;
                break;
            case "folders","Folders":
                Map<String, Double> foldersAverage = foldersRepository.findFolderNameAndAverageSize().stream()
                        .collect(Collectors.toMap(
                                array -> {
                                    String folderName = (String) array[0];
                                    return folderName != null ? folderName : "Unknown";
                                },
                                array -> {
                                    Double averageSize = (Double) array[1];
                                    return averageSize != null ? averageSize : 0.0;
                                }
                        ));
                Map<String, Long> foldersSum = foldersRepository.findFolderNameSum().stream()
                        .collect(Collectors.toMap(
                                array -> {
                                    String folderName = (String) array[0];
                                    return folderName != null ? folderName : "Unknown";
                                },
                                array -> {
                                    Long folderSum = (Long) array[1];
                                    return folderSum != null ? folderSum : 0;
                                }
                        ));
                Map<String, Long> folderFilesAmount = foldersRepository.findFolderNameAndNumberOfFiles().stream()
                        .collect(Collectors.toMap(
                                array -> {
                                    String folderName = (String) array[0];
                                    return folderName != null ? folderName : "Unknown";
                                },
                                array -> {
                                    Long amount = (Long) array[1];
                                    return amount != null ? amount : 0;
                                }
                        ));
                Map<String, Double> topNFoldersAvg = foldersAverage.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(howMany)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ));
                Map<String,Long> topNFoldersSum = getTopN(foldersSum, howMany);
                Map<String,Long> topNFoldersFiles = getTopN(folderFilesAmount, howMany);
                report.put("Top " + howMany + " Folders by Sum of File Size: ", topNFoldersSum.toString());
                report.put("Top " + howMany + " Folders by File Count: ", topNFoldersFiles.toString());
                report.put("Top " + howMany + " Folders by Average File Size: ", topNFoldersAvg.toString());
                correctName=true;
                break;
            case "length","Length":
                List<Folder> longestChain = findLongestChain(folders);
                StringBuilder longestChainString = new StringBuilder();
                for (Folder folder:
                     longestChain) {
                    if(folder.getChildFolders().size()==0)
                        longestChainString.append(folder.getName()).append(" : (").append(folder.getFileMetadataList().size()).append(")");
                    else
                        longestChainString.append(folder.getName()).append(" : (").append(folder.getFileMetadataList().size()).append(") -> ");
                }
                report.put("Longest file chain: ", longestChainString.toString());
                correctName=true;
                break;
        }
        if(correctName) {
            return ResponseEntity.ok(report);
        }
        return ResponseEntity.badRequest().build();
    }
    private Map<String, Long> getTopN(Map<String,Long> map, int howMany){
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(howMany)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public static List<Folder> findLongestChain(List<Folder> folders) {
        List<Folder> longestChain = new ArrayList<>();

        for (Folder folder : folders) {
            List<Folder> currentChain = new ArrayList<>();
            findLongestChainHelper(folder, currentChain, longestChain);
        }

        return longestChain;
    }
    private static void findLongestChainHelper(Folder folder, List<Folder> currentChain, List<Folder> longestChain) {
        if (folder == null) {
            return;
        }

        currentChain.add(folder);

        if (currentChain.size() > longestChain.size()) {
            longestChain.clear();
            longestChain.addAll(currentChain);
        }

        if (folder.getChildFolders() != null) {
            for (Folder child : folder.getChildFolders()) {
                findLongestChainHelper(child, currentChain, longestChain);
            }
        }

        currentChain.remove(currentChain.size() - 1); // Backtrack
    }
}

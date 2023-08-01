package local;

import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;
import util.S3Service;
import util.S3ServiceImpl;
import util.QueryService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LocalQueryService implements QueryService {

    private final Map<String, Set<String>> map;
    private final S3Service aws = new S3ServiceImpl();

    public LocalQueryService() {
        map = LocalDictionary.deserializeDictionary();
        this.updateDictionary();
    }

    @Override
    public synchronized void closeService() {
        LocalDictionary.serializeDictionary(map);
    }

    @Override
    public synchronized void updateDictionary() {
        Set<String> resumes = aws.listResumes();
        Map<String, String> jobMap = new HashMap<>();
        resumes.forEach(r -> {
            String jobId = aws.startTextDetection(r);
            jobMap.put(jobId, r);
        });
        jobMap.entrySet().forEach(p -> {
            GetDocumentTextDetectionResponse response = aws.getJobResults(p.getKey());
            response.blocks().forEach(block -> {
                if(block.blockTypeAsString().compareTo("WORD") == 0) {
                    this.addValue(block.text().toLowerCase(), p.getValue());
                }
            });

        });

    }

    @Override
    public synchronized Set<String> lookupKeyword(String keyword) {
        return map.containsKey(keyword) ? map.get(keyword) : new HashSet<>();
    }

    private void addValue(String keyword, String resume) {
        if (map.containsKey(keyword)) {
            // Key exists, get the corresponding Set and add the value to it
            map.get(keyword).add(resume);
        } else {
            // Key does not exist, create a new Set with the value and put it in the HashMap
            Set<String> newSet = new HashSet<>();
            newSet.add(resume);
            map.put(keyword, newSet);
        }
    }

    public void printDictionary() {
        map.forEach((k, set) -> {
            System.out.println("Key: " + k + ", set: " + set + ".");
        });
    }
}

package local;

import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;
import util.AWSService;
import util.AWSServiceImpl;
import util.QueryService;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LocalQueryService implements QueryService {

    private final Map<String, Set<String>> map;
    private final AWSService aws = new AWSServiceImpl();

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
        resumes.forEach(r -> {
            String jobId = aws.startTextDetection(r);
            GetDocumentTextDetectionResponse response = aws.getJobResults(jobId);
            System.out.println(response.hasBlocks());
            response.blocks().forEach(block -> {
                System.out.println(block.blockTypeAsString());
                System.out.println(block.text());
                if(block.blockTypeAsString() == "WORD") {
                    this.addValue(block.text(), r);
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

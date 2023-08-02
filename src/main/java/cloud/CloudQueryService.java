package cloud;

import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;
import util.QueryService;
import util.S3Service;
import util.S3ServiceImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CloudQueryService implements QueryService {

    private final S3Service s3Client;
    private final DynamoServiceImpl dynamoDbClient;
    public CloudQueryService() {

        this.s3Client = new S3ServiceImpl();
        this.dynamoDbClient = new DynamoServiceImpl("ResumeTable");
        this.updateDictionary();
    }

    @Override
    public void closeService() {

    }

    @Override
    public void updateDictionary() {
        Set<String> s3Resumes = s3Client.listResumes();
        Set<String> processedResumes = dynamoDbClient.getAllValues(true);

        Set<String> resumes = new HashSet<>(s3Resumes.stream()
                .filter(item -> !processedResumes.contains(item))
                .collect(Collectors.toSet()));

        resumes.addAll(s3Resumes.stream()
                .filter(item -> !processedResumes.contains(item))
                .collect(Collectors.toSet()));

        Map<String, String> jobMap = new HashMap<>();
        resumes.forEach(r -> {
            String jobId = s3Client.startTextDetection(r);
            jobMap.put(jobId, r);
        });
        jobMap.entrySet().forEach(p -> {
            GetDocumentTextDetectionResponse response = s3Client.getJobResults(p.getKey());
            response.blocks().forEach(block -> {
                if(block.blockTypeAsString().compareTo("WORD") == 0) {
                    this.dynamoDbClient.putItemInTable(block.text().toLowerCase(), p.getValue());
                }
            });

        });
    }

    @Override
    public Set<String> lookupKeyword(String keyword) {
        return this.dynamoDbClient.keywordQuery(keyword.toLowerCase());
    }

    @Override
    public void printDictionary() {
        dynamoDbClient.scan();
    }
}

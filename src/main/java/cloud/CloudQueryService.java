package cloud;

import software.amazon.awssdk.services.textract.model.GetDocumentTextDetectionResponse;
import util.GetPropertyValues;
import util.QueryService;
import util.S3Service;
import util.S3ServiceImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CloudQueryService implements QueryService {

    private final S3Service s3Service;
    private final DynamoServiceImpl dynamoService;
    public CloudQueryService() {

        this.s3Service = new S3ServiceImpl();
        this.dynamoService = new DynamoServiceImpl(GetPropertyValues.getTableName());
        this.updateDictionary();
    }

    @Override
    public void closeService() {
        this.s3Service.close();
        this.dynamoService.close();
    }

    @Override
    public void updateDictionary() {
        Set<String> s3Resumes = s3Service.listResumes();
        Set<String> processedResumes = dynamoService.getAllValues(false);

        Set<String> resumes = s3Resumes.stream()
                .filter(item -> !processedResumes.contains(item)).collect(Collectors.toSet());

        resumes.addAll(s3Resumes.stream()
                .filter(item -> !processedResumes.contains(item))
                .collect(Collectors.toSet()));

        Map<String, String> jobMap = new HashMap<>();
        resumes.forEach(r -> {
            String jobId = s3Service.startTextDetection(r);
            jobMap.put(jobId, r);
        });
        jobMap.forEach((key, value) -> {
            GetDocumentTextDetectionResponse response = s3Service.getJobResults(key);
            response.blocks().forEach(block -> {
                if (block.blockTypeAsString().compareTo("WORD") == 0) {
                    this.dynamoService.putItemInTable(block.text().toLowerCase(), value);
                }
            });

        });
    }

    @Override
    public Set<String> lookupKeyword(String keyword) {
        return this.dynamoService.keywordQuery(keyword.toLowerCase());
    }

    @Override
    public void printDictionary() {
        dynamoService.scan();
    }
}

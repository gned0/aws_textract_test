package util;

import java.util.List;

public interface DynamoService {

    List<String> getAllValues();

    void putItemInTable(String keyword, String resume);

    List<String> keywordQuery(String keyword);

}

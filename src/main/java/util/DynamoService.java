package util;

import java.util.List;
import java.util.Set;

public interface DynamoService {

    Set<String> getAllValues();

    void putItemInTable(String keyword, String resume);

    List<String> keywordQuery(String keyword);

}

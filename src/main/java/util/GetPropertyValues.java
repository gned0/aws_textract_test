package util;

import software.amazon.awssdk.regions.Region;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GetPropertyValues {

    private static String bucketName;
    private static String tableName;
    private static Region region;

    static  {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/main/resources/config.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Properties properties = new Properties();
        try {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bucketName = properties.getProperty("s3.bucket.name");
        tableName = properties.getProperty("ddb.table.name");
        region = Region.of(properties.getProperty("aws.region.name"));

    }

    public static String getBucketName() {
        return bucketName;
    }

    public static String getTableName() {
        return tableName;
    }

    public static Region getRegion() {
        return region;
    }
}

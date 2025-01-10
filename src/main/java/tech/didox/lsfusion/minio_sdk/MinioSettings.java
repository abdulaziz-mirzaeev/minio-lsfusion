package tech.didox.lsfusion.minio_sdk;

import io.minio.MinioClient;

import java.util.Properties;

public class MinioSettings {
    private static MinioSettings instance;
    private static Properties properties;

    public String endpoint;
    public String accessKey;
    public String secretKey;

    private MinioSettings(){}

    public static MinioSettings getInstance(String endpoint, String accessKey, String secretKey) {
        if (instance == null) {
            instance = new MinioSettings();

            instance.endpoint = endpoint;;
            instance.accessKey = accessKey;
            instance.secretKey = secretKey;
        }

        return instance;
    }

    public MinioClient getClient() {
        return MinioClient.builder()
            .endpoint(this.endpoint)
            .credentials(this.accessKey, this.secretKey)
            .build();
    }
}

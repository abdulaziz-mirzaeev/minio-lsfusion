package tech.didox.lsfusion.minio_sdk;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MinioConfig {
    private static Properties properties;

    public static String getProperty(String key) {
        if (properties == null) {
            initProperties();
        }

        return properties.getProperty(key);
    }

    private static void initProperties() {
        try {
            properties = new Properties();
            InputStream stream = new FileInputStream("conf/settings.properties");
            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

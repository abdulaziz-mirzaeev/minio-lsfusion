package tech.didox.lsfusion.minio_sdk.actions;

import com.google.common.base.Throwables;
import io.minio.MinioClient;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;
import lsfusion.server.physics.dev.integration.internal.to.InternalAction;
import tech.didox.lsfusion.minio_sdk.MinioConfig;
import tech.didox.lsfusion.minio_sdk.MinioSettings;

public abstract class MinioAction extends InternalAction {
    public MinioAction(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    public MinioClient getMinioClient(ExecutionContext<ClassPropertyInterface> context) {
        try {
            String endpoint = (String) this.findProperty("minioEndpoint[]").read(context);
            String accessKey = (String) this.findProperty("minioAccessKey[]").read(context);
            String secretKey = (String) this.findProperty("minioSecretKey[]").read(context);

            if (endpoint == null) {
                endpoint = MinioConfig.getProperty("minio.endpoint");
            }

            if (accessKey == null) {
                accessKey = MinioConfig.getProperty("minio.accessKey");
            }

            if (secretKey == null) {
                secretKey = MinioConfig.getProperty("minio.secretKey");
            }

            return MinioSettings.getInstance(endpoint, accessKey, secretKey).getClient();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}

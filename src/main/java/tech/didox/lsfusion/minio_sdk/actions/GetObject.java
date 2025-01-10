package tech.didox.lsfusion.minio_sdk.actions;

import com.google.common.base.Throwables;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lsfusion.base.file.FileData;
import lsfusion.server.data.sql.exception.SQLHandledException;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;

import java.io.InputStream;
import java.sql.SQLException;

public class GetObject extends MinioAction {
    public GetObject(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    @Override
    protected void executeInternal(ExecutionContext<ClassPropertyInterface> context) throws SQLException, SQLHandledException {
        try {
            String endpoint = (String) this.findProperty("minioEndpoint[]").read(context);
            System.out.println("MyDebug: " + endpoint);
            String bucketName = (String) this.getParam(0, context);
            String path = (String) this.getParam(1, context);

            MinioClient client = this.getMinioClient(context);

            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .build();

            InputStream stream = client.getObject(args);
            byte[] file = stream.readAllBytes();

            this.findProperty("storageObject[]").change(new FileData(file), context);
            stream.close();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}

package tech.didox.lsfusion.minio_sdk.actions;

import com.google.common.base.Throwables;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lsfusion.base.file.FileData;
import lsfusion.server.data.sql.exception.SQLHandledException;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

public class PutObject extends MinioAction {
    public PutObject(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    @Override
    protected void executeInternal(ExecutionContext<ClassPropertyInterface> context)
            throws SQLException, SQLHandledException
    {
        try {
            String bucketName = (String) this.getParam(0, context);
            String objectPath = (String) this.getParam(1, context);
            String contentType = (String) this.getParam(2, context);
            FileData file = (FileData) this.getParam(3, context);

            MinioClient client = this.getMinioClient(context);

            // Ensure the bucket exists (optional: create if not exists)
            boolean isExist = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                client.makeBucket(io.minio.MakeBucketArgs.builder().bucket(bucketName).build());
                System.out.println("Bucket created: " + bucketName);
            }

            PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectPath)
                .stream(new ByteArrayInputStream(file.getBytes()), file.getBytes().length, -1)
                .contentType(contentType)
                .build();

            client.putObject(args);
            this.findProperty("putStorageObjectSuccess[]").change(true, context);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}

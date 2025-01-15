package tech.didox.lsfusion.minio_sdk.actions;

import com.google.common.base.Throwables;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lsfusion.base.file.FileData;
import lsfusion.base.file.RawFileData;
import lsfusion.server.data.sql.exception.SQLHandledException;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Base64;

public class GetObject extends MinioAction {
    public GetObject(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    @Override
    protected void executeInternal(ExecutionContext<ClassPropertyInterface> context) throws SQLException, SQLHandledException {
        try {
            String bucketName = (String) this.getParam(0, context);
            String path = (String) this.getParam(1, context);

            MinioClient client = this.getMinioClient(context);

            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .build();

            InputStream stream = client.getObject(args);
            RawFileData rawFile = new RawFileData(stream);
            stream.close();

            this.findProperty("storageObject[]").change(new FileData(rawFile, getFileExtension(path)), context);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}

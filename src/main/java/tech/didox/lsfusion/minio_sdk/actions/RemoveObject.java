package tech.didox.lsfusion.minio_sdk.actions;

import com.google.common.base.Throwables;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lsfusion.server.data.sql.exception.SQLHandledException;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;

import java.sql.SQLException;

public class RemoveObject extends MinioAction {
    public RemoveObject(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    @Override
    protected void executeInternal(ExecutionContext<ClassPropertyInterface> context) throws SQLException, SQLHandledException {
        try {
            String bucketName = (String) this.getParam(0, context);
            String path = (String) this.getParam(1, context);

            MinioClient client = this.getMinioClient(context);

            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .build();

            client.removeObject(args);
            this.findProperty("removeStorageObjectSuccess[]").change(true, context);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}

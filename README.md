# Minio Storage SDK for LSFusion

---------------------------------------------

## Pre-requisites
> [!NOTE]
> You must have a running Minio Object Storage instance

## Usage

### 1. Add the library
Add the library to your project through Maven. The following snippet should be added to the `pom.xml` file.
```xml
<dependency>
    <groupId>io.github.abdulaziz-mirzaeev</groupId>
    <artifactId>minio-lsfusion</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 2. Configure
Three variables  are needed:
- `minio.endpoint`: URL/Endpoint of Minio Object Storage
- `minio.accessKey`
- `minio.secretKey`

There are three ways you can set these variables:
1. Write them in `conf/settings.properties` file of your lsFusion project
```properties
minio.endpoint=http://localhost:9000
minio.accessKey=...
minio.secretKey=...
```
2. Specify the variables on startup (for development only)
```lsf
REQUIRE SystemEvents, MinioSDK;

onStarted() + {
    IF NOT minioSettings() THEN NEW ms = MinioSettings {
        accessKey(ms) <- '...';
        secretKey(ms) <- '...';
        endpoint(ms) <- 'http://localhost:9000';
    }
    APPLY;
}
```
3. Fill the variables on LsFusion admin panel. Location: `MasterData > Minio Storage Settings`.

> [!NOTE]
> Priority will be given to the values filled in admin panel.

### 3. Use

API

- `getStorageObject(STRING bucketName, STRING objectPath)`
- `putStorageObject(STRING bucketName, STRING objectPath, FILE file)`
- `removeStorageObject(STRING bucketName, STRING objectPath)`

Examples:

**getStorageObject**
```lsf
REQUIRE MinioSDK;

getStorageObject('json', '52dde6d9-0fad-4e07-ab29-880fbd285097.json');
exportFile() <- storageObject();
```

**putStorageObject**
```lsf
REQUIRE MinioSDK;

generateUUID();
putStorageObject('json', (CONCAT '.', generatedUUID(), extension(f)), f);
// putStorageObjectSuccess() boolean property will be filled
exportString() <- 'success';
```

**removeStorageObject**
```lsf
REQUIRE MinioSDK;

removeStorageObject('json', '52dde6d9-0fad-4e07-ab29-880fbd285097.json');
// removeStorageObjectSuccess() boolean property will be filled
exportString() <- 'success';
```

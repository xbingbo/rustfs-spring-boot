package io.github.xbingbo.rustfs.model;

/*
 * 上传文件结果
 * */
public class UploadResult {
    // 桶名
    private final String bucket;
    // 文件名
    private final String key;
    // 路径
    private final String path;

    public UploadResult(String bucket, String key) {
        this.bucket = bucket;
        this.key = key;
        this.path = bucket + "/" + key;
    }

    public String getKey() {
        return key;
    }

    public String getBucket() {
        return bucket;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "UploadResult{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}

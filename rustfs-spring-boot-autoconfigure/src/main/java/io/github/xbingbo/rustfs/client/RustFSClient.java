package io.github.xbingbo.rustfs.client;

import io.github.xbingbo.rustfs.exception.RustFSErrorCode;
import io.github.xbingbo.rustfs.exception.RustFSException;
import io.github.xbingbo.rustfs.model.CopyResult;
import io.github.xbingbo.rustfs.model.UploadResult;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/**
 * Rustfs对象存储客户端封装
 */
public class RustFSClient {
    /**
     * 桶名
     */
    private final String bucket;

    /**
     * 桶操作客户端
     */
    private final S3Client client;

    /**
     * 预签名客户端
     */
    private final S3Presigner presigner;

    public RustFSClient(S3Client client, S3Presigner presigner, String bucket) {
        this.bucket = bucket;
        this.client = client;
        this.presigner = presigner;
        this.ensureBucketExists();
    }

    // 获取预览连接，使用默认的桶和文件路径
    public String getPreviewUrl(String key) {
        return this.getPreviewUrl(this.bucket, key);
    }

    // 获取预览连接，指定文件路径和有效期
    public String getPreviewUrl(String key, Duration duration) {
        return this.getPreviewUrl(this.bucket, key, duration);
    }

    // 获取预览连接，指定桶和文件路径，默认有效期为5分钟
    public String getPreviewUrl(String bucket, String key) {
        return this.getPreviewUrl(bucket, key, Duration.ofMinutes(5));
    }

    // 获取预览连接，指定桶、文件路径和有效期
    public String getPreviewUrl(String bucket, String key, Duration duration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();
        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .getObjectRequest(getObjectRequest)
                        .signatureDuration(duration)
                        .build();
        return this.presigner.presignGetObject(presignRequest).url().toString();
    }

    // 上传文件到默认桶，使用提供的键和内容
    public UploadResult uploadFile(String key, String content) {
        return this.uploadFile(this.bucket, key, content);
    }

    // 上传文件到指定桶，使用提供的键和内容
    public UploadResult uploadFile(String bucket, String key, String content) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();
            this.client.putObject(request, RequestBody.fromString(content));
            return new UploadResult(bucket, key);
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.UPLOAD_ERROR, e);
        }
    }

    // 上传文件到默认桶，使用提供的键和路径
    public UploadResult uploadFile(String key, Path path) {
        return this.uploadFile(this.bucket, key, path);
    }

    // 上传文件到指定桶，使用提供的键和路径
    public UploadResult uploadFile(String bucket, String key, Path path) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();
            this.client.putObject(request, RequestBody.fromFile(path));
            return new UploadResult(bucket, key);
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.UPLOAD_ERROR, e);
        }
    }

    // 上传文件到默认桶，使用提供的键和字节数组
    public UploadResult uploadFile(String key, byte[] data) {
        return this.uploadFile(this.bucket, key, data);
    }

    // 上传文件到指定桶，使用提供的键和字节数组
    public UploadResult uploadFile(String bucket, String key, byte[] data) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();
            this.client.putObject(request, RequestBody.fromBytes(data));
            return new UploadResult(bucket, key);
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.UPLOAD_ERROR, e);
        }
    }

    // 上传文件到默认桶，使用提供的键和字节缓冲区
    public UploadResult uploadFile(String key, ByteBuffer byteBuffer) {
        return this.uploadFile(this.bucket, key, byteBuffer);
    }

    // 上传文件到指定桶，使用提供的键和字节缓冲区
    public UploadResult uploadFile(String bucket, String key, ByteBuffer byteBuffer) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();
            this.client.putObject(request, RequestBody.fromByteBuffer(byteBuffer));
            return new UploadResult(bucket, key);
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.UPLOAD_ERROR, e);
        }
    }

    // 上传文件到默认桶，使用提供的键和输入流
    public UploadResult uploadFile(String key, InputStream inputStream, long contentLength) {
        return this.uploadFile(this.bucket, key, inputStream, contentLength);
    }

    // 上传文件到指定桶，使用提供的键和输入流
    public UploadResult uploadFile(
            String bucket, String key, InputStream inputStream, long contentLength) {
        try {
            PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).build();
            this.client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
            return new UploadResult(bucket, key);
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.UPLOAD_ERROR, e);
        }
    }

    // 下载文件到默认桶，使用提供的键和本地路径
    public void downloadFile(String key, String filePath) {
        this.downloadFile(this.bucket, key, filePath);
    }

    // 下载文件到指定桶，使用提供的键和本地路径
    public void downloadFile(String bucket, String key, String filePath) {
        try {
            GetObjectRequest request = GetObjectRequest.builder().bucket(bucket).key(key).build();
            this.client.getObject(request, Paths.get(filePath));
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.DOWNLOAD_ERROR, e);
        }
    }

    // 删除默认桶中的文件，使用提供的键
    public Boolean deleteFile(String bucket, String key) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
            this.client.deleteObject(request);
            return true;
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.DELETE_ERROR, e);
        }
    }

    // 删除默认桶中的文件，使用提供的键
    public Boolean deleteFile(String key) {
        return this.deleteFile(this.bucket, key);
    }

    // 复制默认桶中的文件到目标位置，使用提供的源键和目标键
    public CopyResult copyFile(String sourceKey, String destinationKey) {
        return this.copyFile(this.bucket, sourceKey, this.bucket, destinationKey);
    }

    // 复制默认桶中的文件到目标位置，使用提供的源键和目标桶及键
    public CopyResult copyFile(String sourceKey, String destinationBucket, String destinationKey) {
        return this.copyFile(this.bucket, sourceKey, destinationBucket, destinationKey);
    }

    // 复制指定桶中的文件到目标位置，使用提供的源桶、源键和目标桶及键
    public CopyResult copyFile(
            String sourceBucket, String sourceKey, String destinationBucket, String destinationKey) {
        try {
            CopyObjectRequest request =
                    CopyObjectRequest.builder()
                            .sourceBucket(sourceBucket)
                            .sourceKey(sourceKey)
                            .destinationBucket(destinationBucket)
                            .destinationKey(destinationKey)
                            .build();
            this.client.copyObject(request);
            return new CopyResult(destinationBucket, destinationKey);
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.COPY_ERROR, e);
        }
    }

    // 检查默认桶中是否存在指定的文件，使用提供的键
    public Boolean existFile(String key) {
        return this.existFile(this.bucket, key);
    }

    // 检查指定桶中是否存在指定的文件，使用提供的键
    public Boolean existFile(String bucket, String key) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder().bucket(bucket).key(key).build();
            this.client.headObject(request);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            throw new RustFSException(RustFSErrorCode.EXIST_ERROR, e);
        } catch (Exception e) {
            throw new RustFSException(RustFSErrorCode.EXIST_ERROR, e);
        }
    }

    // 关闭客户端连接
    public void close() {
        this.client.close();
        this.presigner.close();
    }

    public String getBucket() {
        return bucket;
    }

    // 确保桶存在，如果不存在则创建它
    private void ensureBucketExists() {
        try {
            this.client.headBucket(HeadBucketRequest.builder().bucket(this.bucket).build());
        } catch (Exception e) {
            this.client.createBucket(CreateBucketRequest.builder().bucket(this.bucket).build());
        }
    }
}

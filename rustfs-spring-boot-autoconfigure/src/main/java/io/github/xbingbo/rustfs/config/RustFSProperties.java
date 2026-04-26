package io.github.xbingbo.rustfs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 相关配置
 */
@ConfigurationProperties(prefix = "rustfs")
public class RustFSProperties {
    /**
     * 服务地址
     */
    private String endpoint;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 区域
     */
    private String region = "us-east-1";

    private boolean pathStyleAccess = true;

    /**
     * 桶名
     */
    private String bucket = "unknown";

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isPathStyleAccess() {
        return pathStyleAccess;
    }

    public void setPathStyleAccess(boolean pathStyleAccess) {
        this.pathStyleAccess = pathStyleAccess;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}

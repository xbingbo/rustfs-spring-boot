package io.github.xbingbo.rustfs.autoconfigure;

import io.github.xbingbo.rustfs.client.RustFSClient;
import io.github.xbingbo.rustfs.config.RustFSProperties;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * 自动配置
 */
@AutoConfiguration
@ConditionalOnClass(value = {S3Client.class, S3Presigner.class})
@ConditionalOnProperty(
        prefix = "rustfs",
        name = {"client-id", "client-secret"})
@EnableConfigurationProperties(RustFSProperties.class)
public class RustFSAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RustFSAutoConfiguration.class);

    /**
     * 创建桶操作客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public S3Client s3Client(RustFSProperties properties) {
        S3Client s3Client =
                S3Client.builder()
                        .endpointOverride(URI.create(properties.getEndpoint()))
                        .credentialsProvider(
                                StaticCredentialsProvider.create(
                                        AwsBasicCredentials.create(
                                                properties.getClientId(), properties.getClientSecret())))
                        .region(Region.of(properties.getRegion()))
                        .forcePathStyle(properties.isPathStyleAccess())
                        .build();
        log.info("[rustfs] s3 client initialized with region {}", properties.getRegion());
        return s3Client;
    }

    /**
     * 创建预签名客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public S3Presigner s3Presigner(RustFSProperties properties) {
        S3Presigner s3Presigner =
                S3Presigner.builder()
                        .endpointOverride(URI.create(properties.getEndpoint()))
                        .credentialsProvider(
                                StaticCredentialsProvider.create(
                                        AwsBasicCredentials.create(
                                                properties.getClientId(), properties.getClientSecret())))
                        .region(Region.of(properties.getRegion()))
                        .build();
        log.info("[rustfs] s3 presigner initialized with region {}", properties.getRegion());
        return s3Presigner;
    }

    /**
     * 创建RustFS客户端
     *
     * @param s3Client
     * @param s3Presigner
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RustFSClient rustFSClient(
            S3Client s3Client, S3Presigner s3Presigner, RustFSProperties properties) {
        RustFSClient rustFSClient = new RustFSClient(s3Client, s3Presigner, properties.getBucket());
        log.info("[rustfs] rustfs client initialized with region {} bucket {} endpoint {}",
                properties.getRegion(), properties.getBucket(), properties.getEndpoint());
        return rustFSClient;
    }
}

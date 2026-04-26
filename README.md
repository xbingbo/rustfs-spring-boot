# rustfs-spring-boot

`rustfs-spring-boot` 是一个独立的 Spring Boot RustFS 集成 SDK，提供：

- `rustfs-spring-boot-starter`：业务应用直接引入
- `rustfs-spring-boot-autoconfigure`：自动装配实现（由 starter 间接引入）

## Maven 坐标

```xml
<dependency>
    <groupId>io.github.xbingbo</groupId>
    <artifactId>rustfs-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 配置

```yaml
rustfs:
  endpoint: http://127.0.0.1:9000
  client-id: rustfsadmin
  client-secret: rustfsadmin
  region: us-east-1
  bucket: mybucket
  path-style-access: true
```

## 使用示例

```java
package com.ibingbo.demo;

import client.io.github.xbingbo.rustfs.RustFSClient;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
public class DemoApplication {

    @Resource
    private RustFSClient rustFSClient;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/file/url")
    public Object fileUrl() {
        return this.rustFSClient.getPreviewUrl("11-11.jpeg");
    }
}
```

## 本地构建

```bash
mvn clean compile
```

## 发布到 Central

```bash
export GPG_TTY=$(tty)
export MAVEN_GPG_PASSPHRASE=******
mvn clean deploy -DskipTests -Dgpg.keyname=<YOUR_GPG_KEY_ID>
```

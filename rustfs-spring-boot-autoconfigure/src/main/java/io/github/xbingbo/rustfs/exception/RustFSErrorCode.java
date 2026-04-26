package io.github.xbingbo.rustfs.exception;

/**
 * 错误码枚举类
 */
public enum RustFSErrorCode {
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(1000, "未知错误"),
    /**
     * 配置错误
     */
    CONFIG_ERROR(1001, "配置错误"),
    /**
     * 网络通信错误
     */
    NETWORK_ERROR(1002, "网络通信错误"),
    /**
     * JSON解析错误
     */
    JSON_PARSE_ERROR(1003, "JSON解析错误"),
    /**
     * 初始化错误
     */
    INITIALIZE_ERROR(1004, "初始化错误"),
    /**
     * 上传错误
     */
    UPLOAD_ERROR(1005, "上传文件错误"),
    /**
     * 下载错误
     */
    DOWNLOAD_ERROR(1006, "下载文件错误"),
    /**
     * 删除错误
     */
    DELETE_ERROR(1007, "删除文件错误"),
    /**
     * 复制错误
     */
    COPY_ERROR(1008, "复制文件错误"),
    /**
     * 检查文件存在性错误
     */
    EXIST_ERROR(1009, "检查文件存在性错误");

    private final int code;
    private final String message;

    RustFSErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

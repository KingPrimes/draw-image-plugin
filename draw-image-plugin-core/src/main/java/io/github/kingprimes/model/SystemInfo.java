package io.github.kingprimes.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统信息
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class SystemInfo {
    /**
     * 操作系统名称
     */
    String osName;
    /**
     * 操作系统架构
     */
    String osArch;

    /**
     * 计算机名称
     */
    String computerName;

    /**
     * 计算机 IP 地址
     */
    String computerIp;
}

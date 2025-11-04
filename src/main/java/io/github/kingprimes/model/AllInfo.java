package io.github.kingprimes.model;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 综合信息类
 * <p>包含系统各个方面的信息汇总，如CPU、JVM、系统基本信息和磁盘文件系统信息</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class AllInfo {
    /**
     * CPU 信息
     * <p>包含CPU型号、核心数、使用率等详细信息</p>
     *
     * @see CpuInfo
     */
    CpuInfo cpuInfo;
    /**
     * JVM 信息
     * <p>包含JVM版本、内存使用情况等详细信息</p>
     *
     * @see JvmInfo
     */
    JvmInfo jvmInfo;
    /**
     * 系统信息
     * <p>包含操作系统名称、架构、计算机名称和IP地址等基本信息</p>
     *
     * @see SystemInfo
     */
    SystemInfo systemInfo;
    /**
     * 盘符信息
     * <p>包含系统所有磁盘分区的使用情况信息</p>
     *
     * @see SysFileInfos
     */
    SysFileInfos sysFileInfos;
    /**
     * 项目版本
     */
    PackageVersion packageVersion;

    public record PackageVersion(String name, String version) {
    }
}

package io.github.kingprimes.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * JVM 信息
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class JvmInfo {
    /**
     * JVM 版本
     */
    private String version;
    /**
     * JVM 最大内存
     */
    private long maxMemory;
    /**
     * JVM 已用内存
     */
    private long usedMemory;
    /**
     * JVM 空闲内存
     */
    private long freeMemory;
    /**
     * JVM 内存使用率
     */
    private double usedMemoryRatio;
    /**
     * JVM 空闲内存占比
     */
    private double freeMemoryRatio;


}

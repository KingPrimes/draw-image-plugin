package io.github.kingprimes.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 内存信息
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class MemInfo {
    /**
     * 内存总大小
     */
    private long totalMemory;
    /**
     * 内存已用大小
     */
    private long usedMemory;
    /**
     * 内存空闲大小
     */
    private long freeMemory;
    /**
     * 内存使用率
     */
    private double usedMemoryRatio;
    /**
     * 内存空闲占比
     */
    private double freeMemoryRatio;
}

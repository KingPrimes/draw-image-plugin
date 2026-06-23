package io.github.kingprimes.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * CPU 信息
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class CpuInfo {
    /**
     * CPU 型号
     */
    private String model;
    /**
     * CPU 核心数
     */
    private int cores;
    /**
     * CPU 线程数
     */
    private int threads;
    /**
     * CPU 频率
     */
    private double frequency;
    /**
     * CPU 缓存大小
     */
    private int cacheSize;
    /**
     * CPU 用户使用率
     */
    private double userUsage;

    /**
     * CPU 等待率
     */
    private double waitUsage;

    /**
     * CPU 系统使用率
     */
    private double sysUsage;
    /**
     * CPU 空闲率
     */
    private double idleUsage;
}

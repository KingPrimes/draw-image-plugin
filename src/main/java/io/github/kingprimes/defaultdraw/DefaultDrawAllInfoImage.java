package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认所有系统信息图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawAllInfoImage {


    /**
     * 计算图像所需的高度
     *
     * @param allInfo 所有系统信息数据
     * @return 图像所需的高度
     */
    private static int calculateImageHeight(AllInfo allInfo) {
        int height = IMAGE_MARGIN_TOP + IMAGE_TITLE_HEIGHT + IMAGE_ROW_HEIGHT; // 顶部边距 + 标题高度 + 第一个部分间距

        // CPU信息部分高度
        if (allInfo.getCpuInfo() != null) {
            height += IMAGE_ROW_HEIGHT + 10; // 部分标题行高 + 上边距
            // CPU信息行数 (按新布局: 型号1行 + 核心数/线程数1行 + 频率/缓存1行 + 用户/系统使用率1行 + 等待/空闲率1行)
            height += IMAGE_ROW_HEIGHT * 5; // 5行信息
        }

        // JVM信息部分高度
        if (allInfo.getJvmInfo() != null) {
            height += IMAGE_ROW_HEIGHT + 10; // 部分标题行高 + 上边距
            // JVM信息行数 (按新布局: 版本1行 + 内存信息1行 + 使用率/空闲率1行)
            height += IMAGE_ROW_HEIGHT * 3; // 3行信息
        }

        // 系统信息部分高度
        if (allInfo.getSystemInfo() != null) {
            height += IMAGE_ROW_HEIGHT + 10; // 部分标题行高 + 上边距
            // 系统信息行数 (按新布局: 操作系统/架构1行 + 计算机名/IP1行)
            height += IMAGE_ROW_HEIGHT * 2; // 2行信息
        }

        // 磁盘信息部分高度
        if (allInfo.getSysFileInfos() != null && allInfo.getSysFileInfos().getSysFileInfos() != null) {
            height += (IMAGE_ROW_HEIGHT * 2) + 10; // 部分标题行高 + 上边距
            // 磁盘信息行数 (按新布局: 每个磁盘2行信息 + 10像素间距)
            int diskCount = allInfo.getSysFileInfos().getSysFileInfos().size();
            height += diskCount * (FONT_SIZE * 2 + IMAGE_ROW_HEIGHT); // 每个磁盘2行信息 + 10像素间距
        }

        if (allInfo.getPackageVersion() != null) {
            height += (IMAGE_ROW_HEIGHT * 2) + 10; // 部分标题行高 + 上边距
            // 包版本信息行数 (按新布局: 包名/版本1行)
            height += IMAGE_ROW_HEIGHT; // 1行信息
        }

        // 底部边距
        height += IMAGE_MARGIN + IMAGE_FOOTER_HEIGHT; // 底部边距 + 底部署名区域

        return Math.max(height, 800); // 最小高度为800像素
    }

    /**
     * 绘制所有系统信息图片
     *
     * @param allInfo 所有系统信息数据
     * @return 生成的图片字节数组，格式为 PNG
     */
    public static byte[] drawAllInfoImage(AllInfo allInfo) {
        // 计算图像高度
        int height = calculateImageHeight(allInfo);

        // 创建画布
        ImageCombiner combiner = new ImageCombiner(IMAGE_WIDTH, height, ImageCombiner.OutputFormat.PNG);

        // 填充背景色
        combiner.setFont(FONT)
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, height)
                // 绘制双层边框
                .drawTooRoundRect();

        // 绘制标题
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT.deriveFont(Font.BOLD, 48))
                .addCenteredText("系统信息", IMAGE_MARGIN * 2);

        int startY = IMAGE_MARGIN + IMAGE_TITLE_HEIGHT + IMAGE_ROW_HEIGHT;

        // 绘制CPU信息
        if (allInfo.getCpuInfo() != null) {
            startY = drawCpuInfo(combiner, allInfo.getCpuInfo(), startY);
        }

        // 绘制包版本信息
        if (allInfo.getPackageVersion() != null) {
            startY = drawPackageVersion(combiner, allInfo.getPackageVersion(), startY);
        }

        // 绘制JVM信息
        if (allInfo.getJvmInfo() != null) {
            startY = drawJvmInfo(combiner, allInfo.getJvmInfo(), startY);
        }

        // 绘制系统信息
        if (allInfo.getSystemInfo() != null) {
            startY = drawSystemInfo(combiner, allInfo.getSystemInfo(), startY);
        }

        // 绘制磁盘信息
        if (allInfo.getSysFileInfos() != null && allInfo.getSysFileInfos().getSysFileInfos() != null) {
            drawDiskInfo(combiner, allInfo.getSysFileInfos(), startY);
        }

        // 添加背景图片
        BufferedImage backgroundImage = ImageIOUtils.getRandomXiaoMeiWangImage();
        int maxImageWidth = IMAGE_WIDTH / 3;
        int maxImageHeight = height / 3;
        combiner.drawImageWithAspectRatio(backgroundImage,
                IMAGE_WIDTH - maxImageWidth + 80,
                height - maxImageHeight - IMAGE_MARGIN,
                maxImageWidth,
                maxImageHeight);

        addFooter(combiner, height - 40);

        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }

    /**
     * 绘制CPU信息部分
     *
     * @param combiner ImageCombiner实例
     * @param cpuInfo  CPU信息
     * @param startY   起始Y坐标
     * @return 更新后的Y坐标
     */
    private static int drawCpuInfo(ImageCombiner combiner, CpuInfo cpuInfo, int startY) {
        // 绘制部分标题背景
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRect(IMAGE_MARGIN, startY, IMAGE_WIDTH - 2 * IMAGE_MARGIN, IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .drawLine(IMAGE_MARGIN, startY, IMAGE_WIDTH - IMAGE_MARGIN, startY)
                .drawLine(IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT, IMAGE_WIDTH - IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addText("CPU 信息", IMAGE_MARGIN + 20, startY + IMAGE_ROW_HEIGHT - 10);

        int y = startY + IMAGE_ROW_HEIGHT + 10 + FONT_SIZE;

        // 绘制CPU详细信息
        combiner.setColor(TEXT_COLOR);

        // 型号为单独一行
        if (cpuInfo.getModel() != null) {
            combiner.addText("型号: " + cpuInfo.getModel(), IMAGE_MARGIN + 20, y);
            y += IMAGE_ROW_HEIGHT;
        }

        // 核心数、线程数为一行
        combiner.addText("核心数: " + cpuInfo.getCores(), IMAGE_MARGIN + 20, y);
        combiner.addText("线程数: " + cpuInfo.getThreads(), IMAGE_MARGIN + 500, y);
        y += IMAGE_ROW_HEIGHT;

        // 频率、缓存大小为一行
        combiner.addText("频率: " + cpuInfo.getFrequency() + " MHz", IMAGE_MARGIN + 20, y);
        if (cpuInfo.getCacheSize() > 0) {
            combiner.addText("缓存大小: " + cpuInfo.getCacheSize() + " KB", IMAGE_MARGIN + 500, y);
        }
        y += IMAGE_ROW_HEIGHT;

        // 用户、系统使用率为一行
        combiner.addText("用户使用率: " + ALL_INFO_PERCENT_FORMAT.format(cpuInfo.getUserUsage()) + "%", IMAGE_MARGIN + 20, y);
        combiner.addText("系统使用率: " + ALL_INFO_PERCENT_FORMAT.format(cpuInfo.getSysUsage()) + "%", IMAGE_MARGIN + 500, y);
        y += IMAGE_ROW_HEIGHT;

        // 等待、空闲率为一行
        combiner.addText("等待率: " + ALL_INFO_PERCENT_FORMAT.format(cpuInfo.getWaitUsage()) + "%", IMAGE_MARGIN + 20, y);
        combiner.addText("空闲率: " + ALL_INFO_PERCENT_FORMAT.format(cpuInfo.getIdleUsage()) + "%", IMAGE_MARGIN + 500, y);
        y += IMAGE_ROW_HEIGHT;

        return y;
    }

    private static int drawPackageVersion(ImageCombiner combiner, AllInfo.PackageVersion packageVersion, int startY) {
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRect(IMAGE_MARGIN, startY, IMAGE_WIDTH - 2 * IMAGE_MARGIN, IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .drawLine(IMAGE_MARGIN, startY, IMAGE_WIDTH - IMAGE_MARGIN, startY)
                .drawLine(IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT, IMAGE_WIDTH - IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addText("版本 信息", IMAGE_MARGIN + 20, startY + IMAGE_ROW_HEIGHT - 10);

        int y = startY + IMAGE_ROW_HEIGHT + 10 + FONT_SIZE;

        combiner.setColor(TEXT_COLOR);

        if (packageVersion != null) {
            combiner.addText("名称: " + packageVersion.name(), IMAGE_MARGIN + 20, y);
            combiner.addText("版本: " + packageVersion.version(), IMAGE_MARGIN + 500, y);
        }
        y += IMAGE_ROW_HEIGHT;
        return y;
    }

    /**
     * 绘制JVM信息部分
     *
     * @param combiner ImageCombiner实例
     * @param jvmInfo  JVM信息
     * @param startY   起始Y坐标
     * @return 更新后的Y坐标
     */
    private static int drawJvmInfo(ImageCombiner combiner, JvmInfo jvmInfo, int startY) {
        // 绘制部分标题背景
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRect(IMAGE_MARGIN, startY, IMAGE_WIDTH - 2 * IMAGE_MARGIN, IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .drawLine(IMAGE_MARGIN, startY, IMAGE_WIDTH - IMAGE_MARGIN, startY)
                .drawLine(IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT, IMAGE_WIDTH - IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addText("JVM 信息", IMAGE_MARGIN + 20, startY + IMAGE_ROW_HEIGHT - 10);

        int y = startY + IMAGE_ROW_HEIGHT + 10 + FONT_SIZE;

        // 绘制JVM详细信息
        combiner.setColor(TEXT_COLOR);

        // 版本为一行
        if (jvmInfo.getVersion() != null) {
            combiner.addText("版本: " + jvmInfo.getVersion(), IMAGE_MARGIN + 20, y);
            y += IMAGE_ROW_HEIGHT;
        }

        // 最大、已用、空闲内存为一行
        // 转换为MB显示
        long maxMemoryMB = jvmInfo.getMaxMemory() / (1024 * 1024);
        long usedMemoryMB = jvmInfo.getUsedMemory() / (1024 * 1024);
        long freeMemoryMB = jvmInfo.getFreeMemory() / (1024 * 1024);

        combiner.addText("最大内存: " + ALL_INFO_MEMORY_FORMAT.format(maxMemoryMB) + " MB", IMAGE_MARGIN + 20, y);
        combiner.addText("已用内存: " + ALL_INFO_MEMORY_FORMAT.format(usedMemoryMB) + " MB", IMAGE_MARGIN + 380, y);
        combiner.addText("空闲内存: " + ALL_INFO_MEMORY_FORMAT.format(freeMemoryMB) + " MB", IMAGE_MARGIN + 740, y);
        y += IMAGE_ROW_HEIGHT;

        // 使用率空闲率为一行
        combiner.addText("内存使用率: " + ALL_INFO_PERCENT_FORMAT.format(jvmInfo.getUsedMemoryRatio()) + "%", IMAGE_MARGIN + 20, y);
        combiner.addText("内存空闲率: " + ALL_INFO_PERCENT_FORMAT.format(jvmInfo.getFreeMemoryRatio()) + "%", IMAGE_MARGIN + 380, y);
        y += IMAGE_ROW_HEIGHT;

        return y;
    }

    /**
     * 绘制系统信息部分
     *
     * @param combiner   ImageCombiner实例
     * @param systemInfo 系统信息
     * @param startY     起始Y坐标
     * @return 更新后的Y坐标
     */
    private static int drawSystemInfo(ImageCombiner combiner, SystemInfo systemInfo, int startY) {
        // 绘制部分标题背景
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRect(IMAGE_MARGIN, startY, IMAGE_WIDTH - 2 * IMAGE_MARGIN, IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .drawLine(IMAGE_MARGIN, startY, IMAGE_WIDTH - IMAGE_MARGIN, startY)
                .drawLine(IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT, IMAGE_WIDTH - IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addText("系统信息", IMAGE_MARGIN + 20, startY + IMAGE_ROW_HEIGHT - 10);

        int y = startY + IMAGE_ROW_HEIGHT + 10 + FONT_SIZE;

        // 绘制系统详细信息
        combiner.setColor(TEXT_COLOR);

        // 操作系统、系统架构为一行
        if (systemInfo.getOsName() != null) {
            combiner.addText("操作系统: " + systemInfo.getOsName(), IMAGE_MARGIN + 20, y);
        }
        if (systemInfo.getOsArch() != null) {
            combiner.addText("系统架构: " + systemInfo.getOsArch(), IMAGE_MARGIN + 500, y);
        }
        y += IMAGE_ROW_HEIGHT;

        // 计算机名、IP地址为一行
        if (systemInfo.getComputerName() != null) {
            combiner.addText("计算机名: " + systemInfo.getComputerName(), IMAGE_MARGIN + 20, y);
        }
        if (systemInfo.getComputerIp() != null) {
            combiner.addText("IP地址: " + systemInfo.getComputerIp(), IMAGE_MARGIN + 500, y);
        }
        y += IMAGE_ROW_HEIGHT;

        return y;
    }

    /**
     * 绘制磁盘信息部分
     *
     * @param combiner     ImageCombiner实例
     * @param sysFileInfos 磁盘信息
     * @param startY       起始Y坐标
     */
    private static void drawDiskInfo(ImageCombiner combiner, SysFileInfos sysFileInfos, int startY) {
        List<SysFileInfos.SysFileInfo> fileInfos = sysFileInfos.getSysFileInfos();
        if (fileInfos == null || fileInfos.isEmpty()) {
            return;
        }

        // 绘制部分标题背景
        combiner.setColor(CARD_BACKGROUND_COLOR)
                .fillRect(IMAGE_MARGIN, startY, IMAGE_WIDTH - 2 * IMAGE_MARGIN, IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .drawLine(IMAGE_MARGIN, startY, IMAGE_WIDTH - IMAGE_MARGIN, startY)
                .drawLine(IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT, IMAGE_WIDTH - IMAGE_MARGIN, startY + IMAGE_ROW_HEIGHT);

        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addText("磁盘信息", IMAGE_MARGIN + 20, startY + IMAGE_ROW_HEIGHT - 10);

        int y = startY + IMAGE_ROW_HEIGHT + 10 + FONT_SIZE;

        // 绘制每个磁盘的信息
        combiner.setColor(TEXT_COLOR);

        for (SysFileInfos.SysFileInfo fileInfo : fileInfos) {

            // 盘符、类型、文件系统为一行
            if (fileInfo.getDirName() != null) {
                combiner.addText("盘符: " + fileInfo.getDirName(), IMAGE_MARGIN + 20, y);
            }
            if (fileInfo.getTypeName() != null) {
                combiner.addText("类型: " + fileInfo.getTypeName(), IMAGE_MARGIN + 360, y);
            }
            if (fileInfo.getFileType() != null) {
                combiner.addText("文件系统: " + fileInfo.getFileType(), IMAGE_MARGIN + 720, y);
            }
            y += IMAGE_ROW_HEIGHT;

            // 总大小、已使用、使用率为一行
            if (fileInfo.getTotal() != null) {
                double totalGB = fileInfo.getTotal() / (1024.0 * 1024.0 * 1024.0);
                combiner.addText("总大小: " + ALL_INFO_MEMORY_FORMAT.format(totalGB) + " GB", IMAGE_MARGIN + 20, y);
            }
            if (fileInfo.getUsed() != null) {
                double usedGB = fileInfo.getUsed() / (1024.0 * 1024.0 * 1024.0);
                combiner.addText("已使用: " + ALL_INFO_MEMORY_FORMAT.format(usedGB) + " GB", IMAGE_MARGIN + 360, y);
            }
            if (fileInfo.getTotal() != null && fileInfo.getUsed() != null) {
                double total = fileInfo.getTotal();
                double used = fileInfo.getUsed();
                double usagePercent = (used / total) * 100;
                combiner.addText("使用率: " + ALL_INFO_PERCENT_FORMAT.format(usagePercent) + "%", IMAGE_MARGIN + 720, y);
            }
            y += IMAGE_ROW_HEIGHT + FONT_SIZE;
        }
    }
}
package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.model.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 系统信息卡片渲染器 — 两列卡片 + 看板娘，列流式布局
 *
 * @author KingPrimes
 * @version 1.0.8
 */
final class DefaultDrawAllInfoImage {

    private static final int CONTENT_X = 50;
    private static final int COLS = 2;
    private static final int COL_GAP = 20;
    private static final int CARD_RADIUS = 14;
    private static final int CARD_PAD = 20;

    private static final int TITLE_Y = 80;
    private static final int CONTENT_START_Y = 150;

    private DefaultDrawAllInfoImage() {
        throw new AssertionError("Cannot instantiate");
    }

    public static byte[] drawAllInfoImage(AllInfo allInfo) {
        List<InfoCard> cards = getInfoCards(allInfo);
        if (cards.isEmpty()) return new byte[0];

        int n = cards.size();
        boolean isOdd = n % COLS != 0;
        int cardW = 562;
        int textW = cardW - CARD_PAD * 2;
        int CANVAS_W = CONTENT_X + COLS * cardW + (COLS - 1) * COL_GAP + CONTENT_X;

        int[] colX = new int[COLS];
        for (int c = 0; c < COLS; c++) colX[c] = CONTENT_X + c * (cardW + COL_GAP);

        // 预计算高度
        int[] cardHeights = new int[n];
        for (int i = 0; i < n; i++) cardHeights[i] = cards.get(i).height();

        // 列流式 Y 终点
        int[] colEndY = new int[COLS];
        java.util.Arrays.fill(colEndY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            colEndY[col] += cardHeights[i] + COL_GAP;
        }
        for (int c = 0; c < COLS; c++) {
            if (colEndY[c] > CONTENT_START_Y) colEndY[c] -= COL_GAP;
        }

        int totalHeight;
        if (isOdd) {
            int tallerEnd = Math.max(colEndY[0], colEndY[1]);
            totalHeight = Math.max(tallerEnd, colEndY[1] + cardW);
        } else {
            int maxEnd = CONTENT_START_Y;
            for (int c = 0; c < COLS; c++) maxEnd = Math.max(maxEnd, colEndY[c]);
            totalHeight = maxEnd + 10 + cardW;
        }

        ImageCombiner cb = new ImageCombiner(CANVAS_W, totalHeight, ImageCombiner.OutputFormat.PNG);
        cb.setColor(PAGE_BACKGROUND_COLOR).fillRect(0, 0, CANVAS_W, totalHeight);
        cb.drawTooRoundRect();

        cb.setColor(TITLE_COLOR).setFont(FONT.deriveFont(Font.BOLD, 44))
                .addCenteredText("系统信息", TITLE_Y);
        cb.setColor(DIVIDER_COLOR).drawLine(CONTENT_X, TITLE_Y + 50, CANVAS_W - CONTENT_X, TITLE_Y + 50);

        int[] drawY = new int[COLS];
        java.util.Arrays.fill(drawY, CONTENT_START_Y);
        for (int i = 0; i < n; i++) {
            int col = i % COLS;
            cards.get(i).draw(cb, colX[col], drawY[col], cardW, cardHeights[i], textW);
            drawY[col] += cardHeights[i] + COL_GAP;
        }

        int standingX = colX[1];
        int standingY = isOdd ? colEndY[1] : totalHeight - cardW;
        cb.drawStandingAt(standingX, standingY, cardW, cardW);
        addFooter(cb, totalHeight - 25);
        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    private static List<InfoCard> getInfoCards(AllInfo allInfo) {
        List<InfoCard> cards = new ArrayList<>();
        if (allInfo.getCpuInfo() != null) cards.add(new CpuCard(allInfo.getCpuInfo()));
        if (allInfo.getPackageVersion() != null) cards.add(new VersionCard(allInfo.getPackageVersion()));
        if (allInfo.getJvmInfo() != null) cards.add(new JvmCard(allInfo.getJvmInfo()));
        if (allInfo.getSystemInfo() != null) cards.add(new SystemCard(allInfo.getSystemInfo()));
        if (allInfo.getSysFileInfos() != null && allInfo.getSysFileInfos().getSysFileInfos() != null) {
            for (SysFileInfos.SysFileInfo fi : allInfo.getSysFileInfos().getSysFileInfos()) {
                cards.add(new DiskCard(fi));
            }
        }
        return cards;
    }

    static int cardPad(int lines) {
        return CARD_PAD * 2 + 10 + lines * 34 + 40;
    }

    static void drawHeader(ImageCombiner cb, int x, int y, int w, int h, String title, Color accent) {
        cb.setColor(CARD_BACKGROUND_COLOR).fillRoundRect(x, y, w, h, CARD_RADIUS, CARD_RADIUS);
        cb.setColor(accent).fillRect(x + CARD_RADIUS, y + 2, w - 2 * CARD_RADIUS, 5);
        cb.setColor(DIVIDER_COLOR).setStroke(1).drawRoundRect(x, y, w, h, CARD_RADIUS, CARD_RADIUS);
        int ix = x + CARD_PAD;
        cb.setColor(accent).setFont(FONT.deriveFont(Font.BOLD, 24f));
        cb.addText(title, ix, y + CARD_PAD + 28);
    }

    static void drawLine(ImageCombiner cb, int x, int y, int tw, String label, String value,
                         String label2, String value2, Font font, Color color) {
        int ix = x + CARD_PAD;
        cb.setColor(color).setFont(font);
        cb.addText(label + value, ix, y);
        if (label2 != null && value2 != null) {
            cb.addText(label2 + value2, ix + tw / 2 + 20, y);
        }
    }

    static String fmt(Number n) {
        return ALL_INFO_PERCENT_FORMAT.format(n);
    }

    static String fmtMem(double d) {
        return ALL_INFO_MEMORY_FORMAT.format(d);
    }

    interface InfoCard {
        int height();

        void draw(ImageCombiner cb, int x, int y, int w, int h, int tw);
    }

    record CpuCard(CpuInfo cpu) implements InfoCard {
        public int height() {
            return cardPad(6);
        }

        public void draw(ImageCombiner cb, int x, int y, int w, int h, int tw) {
            drawHeader(cb, x, y, w, h, "CPU 信息", TITLE_COLOR);
            Font f = FONT.deriveFont(18f);
            int cy = y + CARD_PAD + 42;
            cb.setColor(TEXT_COLOR).setFont(f);
            String model = cpu.getModel() != null ? cpu.getModel() : "未知";
            if (model.length() > 28) model = model.substring(0, 26) + "..";
            cb.addText("型号 " + model, x + CARD_PAD, cy += 34);
            drawLine(cb, x, cy += 34, tw, "核心 ", String.valueOf(cpu.getCores()), "线程 ", String.valueOf(cpu.getThreads()), f, TEXT_COLOR);
            drawLine(cb, x, cy += 34, tw, "频率 ", cpu.getFrequency() + " GHz", "缓存 ", cpu.getCacheSize() + " KB", f, TEXT_SECONDARY_COLOR);
            drawLine(cb, x, cy += 34, tw, "用户 ", fmt(cpu.getUserUsage()) + "%", "系统 ", fmt(cpu.getSysUsage()) + "%", f, ACCENT_GOLD_COLOR);
            drawLine(cb, x, cy + 34, tw, "等待 ", fmt(cpu.getWaitUsage()) + "%", "空闲 ", fmt(cpu.getIdleUsage()) + "%", f, TEXT_SECONDARY_COLOR);
        }
    }

    record VersionCard(AllInfo.PackageVersion pv) implements InfoCard {
        public int height() {
            return cardPad(1);
        }

        public void draw(ImageCombiner cb, int x, int y, int w, int h, int tw) {
            drawHeader(cb, x, y, w, h, "版本信息", new Color(0x3498DB));
            Font f = FONT.deriveFont(20f);
            int cy = y + CARD_PAD + 42;
            cb.setColor(TEXT_COLOR).setFont(f);
            cb.addText("名称 " + pv.name(), x + CARD_PAD, cy += 34);
            cb.setColor(TEXT_SECONDARY_COLOR).setFont(f);
            cb.addText("版本 " + pv.version(), x + CARD_PAD + tw / 2 + 20, cy);
        }
    }

    record JvmCard(JvmInfo jvm) implements InfoCard {
        public int height() {
            return cardPad(4);
        }

        public void draw(ImageCombiner cb, int x, int y, int w, int h, int tw) {
            drawHeader(cb, x, y, w, h, "JVM 信息", new Color(0xE67E22));
            Font f = FONT.deriveFont(18f);
            int cy = y + CARD_PAD + 42;
            cb.setColor(TEXT_COLOR).setFont(f);
            cb.addText("版本 " + (jvm.getVersion() != null ? jvm.getVersion() : "未知"), x + CARD_PAD, cy += 34);
            long max = jvm.getMaxMemory() / (1024 * 1024);
            long used = jvm.getUsedMemory() / (1024 * 1024);
            long free = jvm.getFreeMemory() / (1024 * 1024);
            drawLine(cb, x, cy += 34, tw, "最大 ", max + " MB", "已用 ", used + " MB", f, TEXT_COLOR);
            cb.setColor(TEXT_SECONDARY_COLOR).setFont(f);
            cb.addText("空闲 " + free + " MB", x + CARD_PAD, cy += 34);
            drawLine(cb, x, cy + 34, tw, "使用率 ", fmt(jvm.getUsedMemoryRatio()) + "%", "空闲率 ", fmt(jvm.getFreeMemoryRatio()) + "%", f, ACCENT_GOLD_COLOR);
        }
    }

    record SystemCard(SystemInfo si) implements InfoCard {
        public int height() {
            return cardPad(2);
        }

        public void draw(ImageCombiner cb, int x, int y, int w, int h, int tw) {
            drawHeader(cb, x, y, w, h, "系统信息", new Color(0x27AE60));
            Font f = FONT.deriveFont(20f);
            int cy = y + CARD_PAD + 42;
            drawLine(cb, x, cy += 34, tw, "OS ", si.getOsName(), "架构 ", si.getOsArch(), f, TEXT_COLOR);
            drawLine(cb, x, cy + 34, tw, "计算机 ", si.getComputerName(), "IP ", si.getComputerIp(), f, TEXT_SECONDARY_COLOR);
        }
    }

    record DiskCard(SysFileInfos.SysFileInfo fi) implements InfoCard {
        public int height() {
            return cardPad(2);
        }

        public void draw(ImageCombiner cb, int x, int y, int w, int h, int tw) {
            drawHeader(cb, x, y, w, h, "磁盘 " + fi.getDirName(), new Color(0x9B59B6));
            Font f = FONT.deriveFont(20f);
            int cy = y + CARD_PAD + 42;
            drawLine(cb, x, cy += 34, tw, "类型 ", fi.getTypeName(), "文件系统 ", fi.getFileType(), f, TEXT_COLOR);
            double total = fi.getTotal() != null ? fi.getTotal() / (1024.0 * 1024.0 * 1024.0) : 0;
            double used = fi.getUsed() != null ? fi.getUsed() / (1024.0 * 1024.0 * 1024.0) : 0;
            double pct = total > 0 ? (used / total) * 100 : 0;
            drawLine(cb, x, cy + 34, tw, "总 ", fmtMem(total) + " GB", "已用 ", fmtMem(used) + " GB (" + fmt(pct) + "%)", f, ACCENT_GOLD_COLOR);
        }
    }
}

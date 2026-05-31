package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.utils.Fonts;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

/**
 * 默认绘图常量类（暗色主题）
 * <p>该类包含仅在defaultdraw包内使用的绘图常量和工具方法</p>
 *
 * @author KingPrimes
 * @version 1.0.8
 */
public final class DrawConstants {

    /**
     * 底部署名文本
     */
    public static final String FOOTER_TEXT = "Posted by: KingPrimes";

    /**
     * 字符集
     */
    public static final Font FONT = Fonts.FONT_TEXT;

    public static final Font FONT_WARFRAME_ICON = Fonts.FONT_WARFRAME_ICON.deriveFont(32F);

    /**
     * 标准图像宽度
     */
    public static final int IMAGE_WIDTH = 1200;
    /**
     * 标准图像边距
     */
    public static final int IMAGE_MARGIN = 40;
    /**
     * 标准图像标题高度
     */
    public static final int IMAGE_TITLE_HEIGHT = 50;
    /**
     * 标准图像行高度
     */
    public static final int IMAGE_ROW_HEIGHT = 50;
    /**
     * 标准图像上边距
     */
    public static final int IMAGE_MARGIN_TOP = 60;
    /**
     * 标准图像底部高度
     */
    public static final int IMAGE_FOOTER_HEIGHT = 40;

    // ============================= 暗色主题颜色 =============================
    /**
     * 页面背景色 — 深海军蓝
     */
    public static final Color PAGE_BACKGROUND_COLOR = new Color(0x1a1a2e);
    /**
     * 卡片背景色
     */
    public static final Color CARD_BACKGROUND_COLOR = new Color(0x16213e);
    /**
     * 标题/强调色 — 蓝色
     */
    public static final Color TITLE_COLOR = new Color(0x4585e9);
    /**
     * 主文本颜色 — 白色
     */
    public static final Color TEXT_COLOR = new Color(0xffffff);
    /**
     * 次要文本颜色 — 浅灰
     */
    public static final Color TEXT_SECONDARY_COLOR = new Color(0xaaaaaa);
    /**
     * 弱化文本颜色 — 灰色
     */
    public static final Color TEXT_MUTED_COLOR = new Color(0x787878);
    /**
     * 强调色 — 红粉色
     */
    public static final Color ACCENT_COLOR = new Color(0xe94560);
    /**
     * 绿色强调
     */
    public static final Color ACCENT_GREEN_COLOR = new Color(0x45e996);
    /**
     * 金色强调（奖励相关）
     */
    public static final Color ACCENT_GOLD_COLOR = new Color(0xffd700);
    /**
     * 分割线颜色
     */
    public static final Color DIVIDER_COLOR = new Color(0x3c3c5a);
    /**
     * 进攻方 / 红色状态
     */
    public static final Color ATTACKER_COLOR = new Color(0xFF6B6B);
    /**
     * 防守方 / 绿色状态
     */
    public static final Color DEFENDER_COLOR = new Color(0x4CAF50);
    /**
     * 值得参与 / 成功
     */
    public static final Color WORTH_COLOR = new Color(0x27ae60);
    /**
     * 不值得参与 / 危险
     */
    public static final Color NOT_WORTH_COLOR = new Color(0xe74c3c);

    // 双衍王境情绪颜色
    public static final Color EMOTION_SAD_COLOR = new Color(74, 144, 217);
    public static final Color EMOTION_FEAR_COLOR = new Color(155, 89, 182);
    public static final Color EMOTION_JOY_COLOR = new Color(241, 196, 15);
    public static final Color EMOTION_ANGER_COLOR = new Color(231, 76, 60);
    public static final Color EMOTION_ENVY_COLOR = new Color(46, 204, 113);

    // ============================= 循环颜色 =============================
    /**
     * 循环温暖状态颜色
     */
    public static final Color ALL_CYCLE_WARM_COLOR = new Color(0xff8000);
    /**
     * 循环寒冷状态颜色
     */
    public static final Color ALL_CYCLE_COLD_COLOR = new Color(0x00B4FF);

    // ============================= 裂隙颜色 =============================
    /**
     * 裂隙时间 — 紧急
     */
    public static final Color ACTIVE_MISSION_TIME_LOW_COLOR = new Color(0xff6b6b);
    /**
     * 裂隙时间 — 中等
     */
    public static final Color ACTIVE_MISSION_TIME_MEDIUM_COLOR = new Color(0xAA7E1E);
    /**
     * 裂隙时间 — 充裕
     */
    public static final Color ACTIVE_MISSION_TIME_HIGH_COLOR = new Color(0x1dd1a1);
    /**
     * 虚空 T1 (Lith)
     */
    public static final Color VOID_T1_COLOR = new Color(0x514234);
    /**
     * 虚空 T2 (Meso)
     */
    public static final Color VOID_T2_COLOR = new Color(0x75562B);
    /**
     * 虚空 T3 (Neo)
     */
    public static final Color VOID_T3_COLOR = new Color(0x9F9E9E);
    /**
     * 虚空 T4 (Axi)
     */
    public static final Color VOID_T4_COLOR = new Color(0xC1BE39);
    /**
     * 虚空 T5 (Requiem)
     */
    public static final Color VOID_T5_COLOR = new Color(0x872A2C);

    // ============================= 系统信息 =============================
    /**
     * 系统信息百分比格式
     */
    public static final DecimalFormat ALL_INFO_PERCENT_FORMAT = new DecimalFormat("0.00");
    /**
     * 系统信息内存格式
     */
    public static final DecimalFormat ALL_INFO_MEMORY_FORMAT = new DecimalFormat("0.00");

    // ============================= 订阅 =============================
    /**
     * 订阅蓝色
     */
    public static final Color SUBSCRIBE_IMAGE_BLUE_COLOR = new Color(0x1c84c6);
    /**
     * 订阅紫色
     */
    public static final Color SUBSCRIBE_IMAGE_PURPLE_COLOR = new Color(0x6110f3);
    /**
     * 订阅红色
     */
    public static final Color SUBSCRIBE_IMAGE_RED_COLOR = new Color(0xff0000);
    /**
     * 订阅棕色
     */
    public static final Color SUBSCRIBE_IMAGE_BROWN_COLOR = new Color(0xaf5244);

    /**
     * 看板娘绘制比例
     */
    public static final double STANDING_RATIO = 0.3;

    // ============================= 工具方法 =============================

    // 私有构造函数，防止实例化
    private DrawConstants() {
        throw new AssertionError("Cannot instantiate DrawConstants class");
    }

    /**
     * 按百分比缩放尺寸
     *
     * @param w   原始宽度
     * @param h   原始高度
     * @param pct 百分比 (0.0 ~ 1.0)
     * @return 缩放后的宽高 [w, h]
     */
    public static box scaleByPct(int w, int h, double pct) {
        return new box((int) (w * pct), (int) (h * pct));
    }

    /**
     * 添加底部署名到图像合成器
     *
     * @param combiner 图像合成器实例
     * @param y        底部署名的Y坐标
     */
    public static void addFooter(ImageCombiner combiner, int y) {
        combiner.setFont(FONT).setColor(TEXT_MUTED_COLOR).addCenteredText(FOOTER_TEXT, y);
    }

    /**
     * 添加底部署名到图像合成器（指定颜色）
     *
     * @param combiner 图像合成器实例
     * @param color    文本颜色
     * @param y        底部署名的Y坐标
     */
    public static void addFooter(ImageCombiner combiner, Color color, int y) {
        combiner.setFont(FONT).setColor(color).addCenteredText(FOOTER_TEXT, y);
    }

    public static byte[] getBytes(box sz, int standingX, int standingY, int canvasH, ImageCombiner cb) {
        cb.drawStandingAt(standingX, standingY, sz.x(), sz.y());

        addFooter(cb, canvasH - 25);
        cb.combine();
        try (ByteArrayOutputStream bos = cb.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流", e);
        }
    }

    public record box(int x, int y) {
    }
}
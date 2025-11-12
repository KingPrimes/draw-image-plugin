package io.github.kingprimes.utils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 字体工具类，提供常用字体定义和字体加载功能
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class Fonts {

    // 默认字体大小
    public static final int DEFAULT_FONT_SIZE = 24;
    public static final int SMALL_FONT_SIZE = 18;
    public static final int LARGE_FONT_SIZE = 32;
    // 系统默认字体
    public static final Font FONT_SANS_SERIF = new Font(Font.SANS_SERIF, Font.PLAIN, DEFAULT_FONT_SIZE);
    public static final Font FONT_SERIF = new Font(Font.SERIF, Font.PLAIN, DEFAULT_FONT_SIZE);
    public static final Font FONT_MONOSPACED = new Font(Font.MONOSPACED, Font.PLAIN, DEFAULT_FONT_SIZE);
    private static final Logger logger = Logger.getLogger(Fonts.class.getName());
    // 字体缓存，避免重复创建字体对象
    private static final Map<String, Font> FONT_CACHE = new HashMap<>();
    // 基础字体
    public static final Font FONT_TEXT = createFont("fonts/SourceHanSerifCN-Bold.ttf", Font.TRUETYPE_FONT, Font.PLAIN, LARGE_FONT_SIZE);
    // 艺术字体
    public static final Font FONT_WARFRAME_ICON = createFont("fonts/warframe_font_icon.ttf", Font.TRUETYPE_FONT, Font.PLAIN, LARGE_FONT_SIZE);

    /**
     * 创建字体并缓存
     *
     * @param fontPath classpath下的字体文件路径
     * @param style    字体样式
     * @param size     字体大小
     * @return 创建的字体对象
     */
    public static Font createFont(String fontPath, int fontFormat, int style, int size) {
        String key = fontPath + ":" + style + ":" + size;

        if (FONT_CACHE.containsKey(key)) {
            return FONT_CACHE.get(key);
        }

        try {
            ClassLoader classLoader = Fonts.class.getClassLoader();
            java.net.URL resource = classLoader.getResource(fontPath);

            if (resource == null) {
                logger.severe(String.format("找不到字体文件: %s", fontPath));
                return new Font(Font.SANS_SERIF, style, size);
            }

            Font font = Font.createFont(fontFormat, resource.openStream());
            font = font.deriveFont(style, size);
            FONT_CACHE.put(key, font);
            return font;
        } catch (Exception e) {
            logger.severe(String.format("创建字体失败: %s", e.getMessage()));
            return new Font(Font.SANS_SERIF, style, size);
        }
    }

    /**
     * 获取缓存的字体数量
     *
     * @return 缓存字体数量
     */
    public static int getCachedFontCount() {
        return FONT_CACHE.size();
    }

    /**
     * 清除字体缓存
     */
    public static void clearFontCache() {
        FONT_CACHE.clear();
    }
}

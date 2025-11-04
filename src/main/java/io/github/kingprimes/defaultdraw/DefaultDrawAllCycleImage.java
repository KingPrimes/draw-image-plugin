package io.github.kingprimes.defaultdraw;

import io.github.kingprimes.image.ImageCombiner;
import io.github.kingprimes.image.ImageIOUtils;
import io.github.kingprimes.image.TextUtils;
import io.github.kingprimes.model.worldstate.AllCycle;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static io.github.kingprimes.defaultdraw.DrawConstants.*;

/**
 * 默认所有循环图片绘制工具类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
final class DefaultDrawAllCycleImage {


    /**
     * 绘制所有循环图片
     *
     * @param allCycle 所有循环数据
     * @return 生成的图片字节数组，格式为 PNG
     */
    public static byte[] drawAllCycleImage(AllCycle allCycle) {
        BufferedImage coldImage = ImageIOUtils.getColdImage();
        BufferedImage dayImage = ImageIOUtils.getDayImage();
        BufferedImage nightImage = ImageIOUtils.getNightImage();
        // 创建画布
        ImageCombiner combiner = new ImageCombiner(IMAGE_WIDTH, ALL_CYCLE_HEIGHT, ImageCombiner.OutputFormat.PNG);
        // 填充背景色
        combiner.setFont(FONT)
                .setColor(PAGE_BACKGROUND_COLOR)
                .fillRect(0, 0, IMAGE_WIDTH, ALL_CYCLE_HEIGHT)
                // 绘制双层边框
                .drawTooRoundRect();

        // 绘制标题
        combiner.setColor(TITLE_COLOR)
                .setFont(FONT)
                .addCenteredText("平原查询结果", IMAGE_MARGIN + IMAGE_TITLE_HEIGHT / 2)
                .drawImageWithAspectRatio(ImageIOUtils.getRandomXiaoMeiWangImage(), IMAGE_WIDTH / 2 + 120, 110, IMAGE_WIDTH / 2, ALL_CYCLE_HEIGHT - 150);

        // 绘制表头
        int tableY = IMAGE_MARGIN + IMAGE_TITLE_HEIGHT + ALL_CYCLE_TABLE_HEADER_HEIGHT;
        combiner.setFont(FONT)
                .setColor(TEXT_COLOR);
        int headerY = tableY + FONT_SIZE;
        combiner.addText("地球", 100, headerY);
        combiner.addText("夜灵平野", 250, headerY);
        combiner.addText("福尔图娜", 450, headerY);
        combiner.addText("魔胎之境", 650, headerY);
        combiner.addText("扎里曼", 850, headerY);

        // 绘制状态行
        int stateY = headerY + 80 + FONT_SIZE;
        String earthState = allCycle.getEarthCycle().getState();
        String cetusState = allCycle.getCetusCycle().getState();
        String vallisState = allCycle.getVallisCycle().getState();
        String cambionState = allCycle.getCambionCycle().getActive();
        String zarimanState = allCycle.getZarimanCycle().getState();

        // 设置地球状态颜色
        if (allCycle.getEarthCycle().isDay()) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        combiner.addText(earthState, 100, stateY);

        // 设置夜灵平野状态颜色
        if (allCycle.getCetusCycle().getIsDay()) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        combiner.addText(cetusState, 280, stateY);

        // 设置福尔图娜状态颜色
        if (allCycle.getVallisCycle().isWarm()) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        combiner.addText(vallisState, 480, stateY);

        // 设置魔胎之境状态颜色
        if ("FASS".equals(allCycle.getCambionCycle().getActive())) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        combiner.addText(cambionState, 660, stateY);

        // 设置扎里曼状态颜色
        if (allCycle.getZarimanCycle().isCorpus()) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        combiner.addText(zarimanState, 850, stateY);

        int imageY = stateY + 40 + FONT_SIZE;
        // 绘制图片行
        // 绘制地球图标 (太阳/月亮)
        if (allCycle.getEarthCycle().isDay()) {
            combiner.drawImage(dayImage, 70, imageY);
        } else {
            combiner.drawImage(nightImage, 70, imageY);
        }

        // 绘制夜灵平野图标 (太阳/月亮)
        if (allCycle.getCetusCycle().getIsDay()) {
            combiner.drawImage(dayImage, 250, imageY);

        } else {
            combiner.drawImage(nightImage, 250, imageY);
        }

        // 绘制福尔图娜图标 (太阳/雪花)
        if (allCycle.getVallisCycle().isWarm()) {
            combiner.drawImage(dayImage, 450, imageY);

        } else {
            combiner.drawImage(coldImage, 450, imageY);
        }

        // 绘制魔胎之境图标 (太阳/月亮)
        if ("FASS".equals(allCycle.getCambionCycle().getActive())) {
            combiner.drawImage(dayImage, 645, imageY);
        } else {
            combiner.drawImage(nightImage, 645, imageY);
        }

        if (!allCycle.getZarimanCycle().isCorpus()) {
            combiner.drawImage(ImageIOUtils.getFC_CORPUS(), 850, imageY);
        } else {
            combiner.drawImage(ImageIOUtils.getFC_GRINEER(), 850, imageY);
        }

        // 绘制时间行
        int timeY = imageY + ALL_CYCLE_ROW_HEIGHT * 2 + FONT_SIZE;
        String earthTime = allCycle.getEarthCycle().getTimeLeft();
        String cetusTime = allCycle.getCetusCycle().getTimeLeft();
        String vallisTime = allCycle.getVallisCycle().getTimeLeft();
        String cambionTime = allCycle.getCambionCycle().getTimeLeft();
        String zarimanTime = allCycle.getZarimanCycle().getTimeLeft();
        // 设置地球时间颜色
        if (allCycle.getEarthCycle().isDay()) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        int earthTimeX = 100 + (TextUtils.getFortWidth(earthState, FONT) - TextUtils.getFortWidth(earthTime, FONT)) / 2;
        combiner.addText(earthTime, earthTimeX, timeY);

        // 设置夜灵平野时间颜色
        if (allCycle.getCetusCycle().getIsDay()) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        int cetusTimeX = 285 + (TextUtils.getFortWidth(cetusState, FONT) - TextUtils.getFortWidth(cetusTime, FONT)) / 2;
        combiner.addText(cetusTime, cetusTimeX, timeY);

        // 设置福尔图娜时间颜色
        if (allCycle.getVallisCycle().isWarm()) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        int vallisTimeX = 485 + (TextUtils.getFortWidth(vallisState, FONT) - TextUtils.getFortWidth(vallisTime, FONT)) / 2;
        combiner.addText(vallisTime, vallisTimeX, timeY);

        // 设置魔胎之境时间颜色
        if ("FASS".equals(allCycle.getCambionCycle().getActive())) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        int cambionTimeX = 670 + (TextUtils.getFortWidth(cambionState, FONT) - TextUtils.getFortWidth(cambionTime, FONT)) / 2;
        combiner.addText(cambionTime, cambionTimeX, timeY);

        // 绘制扎里曼时间颜色
        if (allCycle.getZarimanCycle().isCorpus()) {
            combiner.setColor(ALL_CYCLE_WARM_COLOR);
        } else {
            combiner.setColor(ALL_CYCLE_COLD_COLOR);
        }
        int zarimanTimeX = 860 + (TextUtils.getFortWidth(zarimanState, FONT) - TextUtils.getFortWidth(zarimanTime, FONT)) / 2;
        combiner.addText(zarimanTime, zarimanTimeX, timeY);

        addFooter(combiner, ALL_CYCLE_HEIGHT - 40);

        combiner.combine();
        try (ByteArrayOutputStream bos = combiner.getCombinedImageOutStream()) {
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("无法获取图像输出流: %s".formatted(e.getMessage()), e);
        }
    }
}

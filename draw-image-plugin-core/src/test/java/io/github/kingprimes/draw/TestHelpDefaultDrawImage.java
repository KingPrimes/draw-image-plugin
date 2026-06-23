package io.github.kingprimes.draw;

import io.github.kingprimes.common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestHelpDefaultDrawImage {

    @Test
    public void drawHelpDefaultImage() throws IOException {
        List<String> collect = List.of(
                "帮助", "版本", "更新HTML", "更新WM", "更新WM紫卡",
                "更新RM紫卡", "紫卡倾向", "更新信条", "更新翻译", "自动更新",
                "警报", "突击", "执刑官猎杀", "奸商", "仲裁",
                "每日特惠", "入侵", "裂隙", "九重天裂隙", "钢铁裂隙",
                "钢铁奖励", "平原", "赏金", "双衍王境", "电波",
                "紫卡倾向变动", "翻译", "/WR", "/WM", "/RM",
                "/CD", "/XT", "信条武器", "金垃圾", "银垃圾",
                "核桃", "开核桃", "紫卡分析", "订阅", "取消订阅",
                "1999日历"
        );
        byte[] bytes = new DefaultDrawImagePlugin().drawHelpImage(collect);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)), Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_help.png")));

    }
}

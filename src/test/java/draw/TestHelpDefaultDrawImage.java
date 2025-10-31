package draw;

import common.Constant;
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
                "HELP|帮助|指令|命令|菜单|help",
                "检查版本|版本|运行状态|状态",
                "更新HTML|更新html",
                "更新WM物品",
                "更新WM紫卡",
                "更新RM紫卡",
                "更新紫卡倾向变动",
                "更新信条",
                "更新翻译",
                "自动更新|更新版本|版本更新",
                "警报",
                "突击",
                "执刑官猎杀|猎杀|执行官|执政官|执刑官",
                "奸商",
                "仲裁Ex",
                "仲裁",
                "每日特惠|特惠",
                "入侵",
                "裂隙|裂缝",
                "九重天裂隙|九重天|九重天裂缝",
                "钢铁裂隙|钢铁裂缝",
                "钢铁奖励",
                "平原|夜灵平原|夜灵平野|福尔图娜|魔胎之境|扎里曼",
                "希图斯|地球赏金",
                "英择谛|火卫二赏金",
                "索拉里斯|金星赏金",
                "轮换|双衍王境",
                "电波",
                "紫卡倾向变动|倾向变动",
                "翻译",
                "/WR|WR|WMR|/wr|wr|wmr",
                "/WM|WM|MARKET|/市场|市场|/wm|wm|market",
                "/RM|RM|/rm|rm",
                "/CD|CD|赤毒|/cd|cd",
                "/XT|XT|信条|/xt|xt",
                "佩兰|佩兰数列|信条武器",
                "金垃圾",
                "银垃圾",
                "核桃|查核桃",
                "开核桃|砸核桃",
                "紫卡分析|分析紫卡",
                "订阅([0-9]+)? ?-? ?([0-9]+)?",
                "取消订阅([0-9]+)? ?-? ?([0-9]+)?",
                "1999|日历"
        );
        byte[] bytes = new DefaultDrawImagePlugin().drawHelpImage(collect);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)), Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_help.png")));

    }
}

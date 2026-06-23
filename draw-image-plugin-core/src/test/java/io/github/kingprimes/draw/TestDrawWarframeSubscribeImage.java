package io.github.kingprimes.draw;

import io.github.kingprimes.common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestDrawWarframeSubscribeImage {


    Map<Integer, String> subscribeEnums = createSubscribeEnumsMap();
    Map<Integer, String> subscribeMissionTypeEnums = createSubscribeMissionTypeEnumsMap();
    Map<Integer, String> subscribeInvasionReward = createInvasionRewardMap();

    private Map<Integer, String> createSubscribeEnumsMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "警报");
        map.put(2, "仲裁");
        map.put(3, "夜灵平野");
        map.put(4, "每日特惠");
        map.put(5, "活动");
        map.put(6, "入侵");
        map.put(7, "钢铁兑换");
        map.put(8, "奸商");
        map.put(9, "裂隙");
        map.put(10, "新闻");
        map.put(11, "电波");
        map.put(12, "突击");
        map.put(13, "执政官突击");
        map.put(14, "双衍王境");
        return map;
    }

    private Map<Integer, String> createSubscribeMissionTypeEnumsMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "刺杀");
        map.put(2, "强袭");
        map.put(3, "元素转换");
        map.put(4, "捕获");
        map.put(5, "异化区");
        map.put(6, "叛逃");
        map.put(7, "防御");
        map.put(8, "中断");
        map.put(9, "挖掘");
        map.put(10, "破坏");
        map.put(11, "生存");
        map.put(12, "歼灭");
        map.put(13, "自由漫步");
        map.put(14, "劫持");
        map.put(15, "清巢");
        map.put(16, "拦截");
        map.put(17, "移动防御");
        map.put(18, "奥影母艇");
        map.put(19, "救援");
        map.put(20, "前哨战");
        map.put(21, "间谍");
        map.put(22, "爆发");
        map.put(23, "虚空覆涌");
        map.put(24, "虚空洪流");
        return map;
    }

    private Map<Integer, String> createInvasionRewardMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "无");
        map.put(1, "突变原聚合物");
        map.put(2, "力场装置样本");
        map.put(3, "诱变剂物质");
        map.put(4, "Orokin 催化剂");
        map.put(5, "Orokin 反应堆");
        map.put(6, "Forma");
        map.put(7, "特殊功能槽连接器");
        return map;
    }

    @Test
    public void testDrawWarframeSubscribeImage() throws IOException {
        byte[] bytes = new DefaultDrawImagePlugin().drawWarframeSubscribeImage(subscribeEnums, subscribeMissionTypeEnums, subscribeInvasionReward);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)), Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_warframe_subscribe.png")));

    }

}

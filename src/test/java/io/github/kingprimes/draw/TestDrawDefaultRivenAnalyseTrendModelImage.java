package io.github.kingprimes.draw;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import io.github.kingprimes.common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.RivenAnalyseTrendModel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

public class TestDrawDefaultRivenAnalyseTrendModelImage {

    @Test
    public void testDrawRivenAnalyseTrendImage() throws Exception {
        // 从JSON文件中读取测试数据
        List<RivenAnalyseTrendModel> rivenAnalyseTrendModels = new JsonMapper().readValue(
                TestDrawDefaultRivenAnalyseTrendModelImage.class.getResourceAsStream("/rivenAnalyseTrend.json"),
                new TypeReference<List<RivenAnalyseTrendModel>>() {});

        // 创建绘图插件实例
        DefaultDrawImagePlugin plugin = new DefaultDrawImagePlugin();

        // 调用绘制方法
        byte[] imageBytes = plugin.drawRivenAnalyseTrendImage(rivenAnalyseTrendModels);

        // 将结果保存到文件以便查看
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_riven_analyse_trend.png")));
    }
}
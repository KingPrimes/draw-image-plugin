package draw;

import com.alibaba.fastjson2.JSON;
import common.Constant;
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
        String jsonString = JSON.parseObject(
                TestDrawDefaultRivenAnalyseTrendModelImage.class.getResourceAsStream("/rivenAnalyseTrend.json"),
                String.class);
        List<RivenAnalyseTrendModel> rivenAnalyseTrendModels = JSON.parseArray(jsonString, RivenAnalyseTrendModel.class);

        // 创建绘图插件实例
        DefaultDrawImagePlugin plugin = new DefaultDrawImagePlugin();

        // 调用绘制方法
        byte[] imageBytes = plugin.drawRivenAnalyseTrendImage(rivenAnalyseTrendModels);

        // 将结果保存到文件以便查看
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageBytes)), Constant.PNG,
                new File(Constant.DRAW_PATH.formatted("draw_riven_analyse_trend.png")));
    }
}
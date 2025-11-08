package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.worldstate.KnownCalendarSeasons;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * 测试1999日历季节图像绘制功能
 */
public class TestDrawKnownCalendarSeasonsImage {

    private final DefaultDrawImagePlugin plugin = new DefaultDrawImagePlugin();

    /**
     * 测试绘制1999日历季节图像
     */
    @Test
    public void testDrawKnownCalendarSeasonsImage() throws IOException {
        List<KnownCalendarSeasons> knownCalendarSeasons = Constant.WORLD_STATE.getKnownCalendarSeasons();
        // 绘制图像
        byte[] bytes = plugin.drawKnownCalendarSeasonsImage(knownCalendarSeasons);
        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_known_calendar_seasons.png")));
        }
    }
}
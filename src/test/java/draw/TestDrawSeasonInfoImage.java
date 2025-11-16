package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.worldstate.SeasonInfo;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 电波图像绘制测试类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
public class TestDrawSeasonInfoImage {

    /**
     * 测试绘制电波图像
     */
    @Test
    public void drawSeasonInfoImage() throws IOException {
        // 从测试常量类中获取SeasonInfo数据
        SeasonInfo seasonInfo = Constant.WORLD_STATE.getSeasonInfo();

        if (seasonInfo == null) {
            System.out.println("警告: 测试数据中没有SeasonInfo数据，使用模拟数据");
            // 创建模拟数据进行测试
            seasonInfo = createMockSeasonInfo();
        }

        // 使用默认绘图插件绘制电波图像
        byte[] bytes = new DefaultDrawImagePlugin().drawSeasonInfoImage(seasonInfo);
        
        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_season_info.png")));
            System.out.println("电波图像已生成: draw/draw_season_info.png");
            System.out.println("图像大小: " + bytes.length + " bytes");
        }
    }

    /**
     * 创建模拟的电波数据用于测试
     *
     * @return 模拟的SeasonInfo对象
     */
    private SeasonInfo createMockSeasonInfo() {
        SeasonInfo seasonInfo = new SeasonInfo();
        seasonInfo.setSeason(13);
        seasonInfo.setPhase(2);
        seasonInfo.setParams("RadioLegionIntermission13");

        // 创建模拟挑战任务
        java.util.List<SeasonInfo.ActiveChallenges> challenges = new java.util.ArrayList<>();

        // 每日任务1
        SeasonInfo.ActiveChallenges challenge1 = new SeasonInfo.ActiveChallenges();
        challenge1.setName("完成3个入侵任务");
        challenge1.setDescription("在入侵任务中获胜3次");
        challenge1.setStanding(1000);
        challenge1.setRequired(3);
        challenge1.setDaily(true);
        challenge1.setWeekly(false);
        challenge1.setElite(false);
        challenges.add(challenge1);

        // 每日任务2
        SeasonInfo.ActiveChallenges challenge2 = new SeasonInfo.ActiveChallenges();
        challenge2.setName("完成5个裂隙任务");
        challenge2.setDescription("在裂隙任务中完成5个任务");
        challenge2.setStanding(1000);
        challenge2.setRequired(5);
        challenge2.setDaily(true);
        challenge2.setWeekly(false);
        challenge2.setElite(false);
        challenges.add(challenge2);

        // 每周任务
        SeasonInfo.ActiveChallenges challenge3 = new SeasonInfo.ActiveChallenges();
        challenge3.setName("完成20个任务");
        challenge3.setDescription("在本周内完成20个任务");
        challenge3.setStanding(5000);
        challenge3.setRequired(20);
        challenge3.setDaily(false);
        challenge3.setWeekly(true);
        challenge3.setElite(false);
        challenges.add(challenge3);

        // 精英任务
        SeasonInfo.ActiveChallenges challenge4 = new SeasonInfo.ActiveChallenges();
        challenge4.setName("完成钢铁之路仲裁");
        challenge4.setDescription("在钢铁之路中完成1次仲裁任务");
        challenge4.setStanding(7000);
        challenge4.setRequired(1);
        challenge4.setDaily(false);
        challenge4.setWeekly(false);
        challenge4.setElite(true);
        challenges.add(challenge4);

        seasonInfo.setActiveChallenges(challenges);

        return seasonInfo;
    }
}
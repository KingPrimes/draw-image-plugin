package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.worldstate.SyndicateMission;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 集团任务图像绘制测试类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
public class TestDrawSyndicateImage {

    /**
     * 测试绘制单个集团任务（Nodes模式）
     */
    @Test
    public void drawSyndicateMissionNodes() throws IOException {
        List<SyndicateMission> syndicateMissions = Constant.WORLD_STATE.getSyndicateMissions();

        if (syndicateMissions == null || syndicateMissions.isEmpty()) {
            System.out.println("没有找到集团任务数据");
            return;
        }

        // 查找第一个包含nodes的集团任务
        SyndicateMission nodesMission = syndicateMissions.stream()
                .filter(sm -> sm.getNodes() != null && !sm.getNodes().isEmpty())
                .findFirst()
                .orElse(null);

        if (nodesMission == null) {
            System.out.println("没有找到包含节点的集团任务");
            return;
        }

        String syndicateName = nodesMission.getTag() != null ? nodesMission.getTag().getName() : "Unknown";
        System.out.println("测试 Nodes 模式 - 集团: " + syndicateName);
        System.out.println("节点数量: " + nodesMission.getNodes().size());

        byte[] bytes = new DefaultDrawImagePlugin().drawSyndicateImage(nodesMission);

        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_syndicate_nodes_test.png")));
            System.out.println("Nodes模式图像已保存");
        }
    }

    /**
     * 测试绘制单个集团任务（Jobs模式）
     */
    @Test
    public void drawSyndicateMissionJobs() throws IOException {
        List<SyndicateMission> syndicateMissions = Constant.WORLD_STATE.getSyndicateMissions();

        if (syndicateMissions == null || syndicateMissions.isEmpty()) {
            System.out.println("没有找到集团任务数据");
            return;
        }

        // 查找第一个包含jobs的集团任务
        SyndicateMission jobsMission = syndicateMissions.stream()
                .filter(sm -> sm.getJobs() != null && !sm.getJobs().isEmpty())
                .findFirst()
                .orElse(null);

        if (jobsMission == null) {
            System.out.println("没有找到包含任务的集团任务");
            return;
        }

        String syndicateName = jobsMission.getTag() != null ? jobsMission.getTag().getName() : "Unknown";
        System.out.println("测试 Jobs 模式 - 集团: " + syndicateName);
        System.out.println("任务数量: " + jobsMission.getJobs().size());

        // 打印每个任务的详细信息
        for (int i = 0; i < jobsMission.getJobs().size(); i++) {
            var job = jobsMission.getJobs().get(i);
            System.out.println("\n任务 " + (i + 1) + ":");
            System.out.println("  - 类型: " + job.getType());
            System.out.println("  - 等级: Lv." + job.getMinLevel() + " - Lv." + job.getMaxLevel());
            System.out.println("  - 无尽: " + job.getEndless());
            System.out.println("  - 保险库: " + job.getIsVault());
            if (job.getRewardPool() != null && job.getRewardPool().getRewards() != null) {
                System.out.println("  - 奖励数量: " + job.getRewardPool().getRewards().size());
            }
        }

        byte[] bytes = new DefaultDrawImagePlugin().drawSyndicateImage(jobsMission);

        if (bytes.length > 0) {
            ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                    Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_syndicate_jobs_test.png")));
            System.out.println("\nJobs模式图像已保存");
        }
    }

    /**
     * 测试空数据情况
     */
    @Test
    public void drawEmptySyndicateMission() {
        System.out.println("测试空数据情况");

        // 测试null
        byte[] bytes1 = new DefaultDrawImagePlugin().drawSyndicateImage(null);
        System.out.println("null输入 - 结果长度: " + bytes1.length);

        // 测试空对象
        SyndicateMission emptySm = new SyndicateMission();
        byte[] bytes2 = new DefaultDrawImagePlugin().drawSyndicateImage(emptySm);
        System.out.println("空对象输入 - 结果长度: " + bytes2.length);

        System.out.println("空数据测试完成");
    }
}
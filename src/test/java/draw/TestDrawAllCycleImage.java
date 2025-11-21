package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.WorldState;
import io.github.kingprimes.model.worldstate.AllCycle;
import io.github.kingprimes.model.worldstate.CetusCycle;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;


public class TestDrawAllCycleImage {

    @Test
    public void drawAllCycleImage() throws IOException {
        WorldState worldState = Constant.WORLD_STATE;
        AllCycle allCycle = new AllCycle()
                .setEarthCycle(worldState.getEarthCycle())
                .setCetusCycle(worldState.getCetusCycle())
                .setCambionCycle(worldState.getCambionCycle())
                .setVallisCycle(worldState.getVallisCycle())
                .setZarimanCycle(worldState.getZarimanCycle());
        byte[] bytes = new DefaultDrawImagePlugin().drawAllCycleImage(allCycle);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)), Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_all_cycle.png")));

    }

    @Test
    public void testCetusCycle() {
        WorldState worldState = Constant.WORLD_STATE;
        CetusCycle cetusCycle = worldState.getCetusCycle();
        System.out.println(cetusCycle.getTimeLeft());
    }
}

package draw;


import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.WorldState;
import io.github.kingprimes.model.worldstate.ActiveMission;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class TestDrawActiveMission {
    @Test
    public void drawActiveMission() throws IOException {
        WorldState worldState = Constant.WORLD_STATE;
        List<ActiveMission> list = worldState.getActiveMissions()
                .stream()
                .filter(am -> !am.getHard())
                .sorted(Comparator.comparing(ActiveMission::getModifier))
                .toList();
        byte[] bytes = new DefaultDrawImagePlugin().drawActiveMissionImage(list);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)), Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_active_mission.png")));
    }

    @Test
    public void drawActiveMissionHard() throws IOException {
        WorldState worldState = Constant.WORLD_STATE;
        List<ActiveMission> list = worldState.getActiveMissions()
                .stream()
                .filter(ActiveMission::getHard)
                .sorted(Comparator.comparing(ActiveMission::getModifier))
                .toList();
        byte[] bytes = new DefaultDrawImagePlugin().drawActiveMissionImage(list);
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)), Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_active_mission_hard.png")));
    }
}

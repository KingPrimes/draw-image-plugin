package draw;

import common.Constant;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.*;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestDrawAllInfoImage {

    @Test
    public void drawAllInfoImage() throws IOException {
        // 创建测试数据
        AllInfo allInfo = createTestAllInfo();

        byte[] bytes = new DefaultDrawImagePlugin().drawAllInfoImage(allInfo);
        ImageIO.write(ImageIO.read(new java.io.ByteArrayInputStream(bytes)),
                Constant.PNG, new File(Constant.DRAW_PATH.formatted("draw_all_info.png")));
    }

    private AllInfo createTestAllInfo() {
        // 创建CPU信息
        CpuInfo cpuInfo = new CpuInfo()
                .setModel("Intel(R) Core(TM) i7-9750H CPU @ 2.60GHz")
                .setCores(6)
                .setThreads(12)
                .setFrequency(2600)
                .setCacheSize(12288)
                .setUserUsage(25.5)
                .setSysUsage(15.2)
                .setWaitUsage(2.1)
                .setIdleUsage(57.2);

        // 创建JVM信息
        JvmInfo jvmInfo = new JvmInfo()
                .setVersion("17.0.2+8-LTS")
                .setMaxMemory(2048 * 1024 * 1024L) // 2GB
                .setUsedMemory(1024 * 1024 * 1024L) // 1GB
                .setFreeMemory(1024 * 1024 * 1024L) // 1GB
                .setUsedMemoryRatio(50.0)
                .setFreeMemoryRatio(50.0);

        // 创建系统信息
        SystemInfo systemInfo = new SystemInfo()
                .setOsName("Windows 11")
                .setOsArch("amd64")
                .setComputerName("TEST-PC")
                .setComputerIp("192.168.1.100");

        // 创建磁盘信息
        List<SysFileInfos.SysFileInfo> sysFileInfoList = new ArrayList<>();

        SysFileInfos.SysFileInfo sysFileInfo1 = new SysFileInfos.SysFileInfo()
                .setDirName("C:\\")
                .setTypeName("本地磁盘")
                .setFileType("NTFS")
                .setTotal(512L * 1024 * 1024 * 1024) // 512GB
                .setUsed(256L * 1024 * 1024 * 1024); // 256GB

        SysFileInfos.SysFileInfo sysFileInfo2 = new SysFileInfos.SysFileInfo()
                .setDirName("D:\\")
                .setTypeName("本地磁盘")
                .setFileType("NTFS")
                .setTotal(1024L * 1024 * 1024 * 1024) // 1TB
                .setUsed(768L * 1024 * 1024 * 1024); // 768GB

        sysFileInfoList.add(sysFileInfo1);
        sysFileInfoList.add(sysFileInfo2);

        SysFileInfos sysFileInfos = new SysFileInfos()
                .setSysFileInfos(sysFileInfoList);

        // 创建完整AllInfo对象
        return new AllInfo()
                .setCpuInfo(cpuInfo)
                .setJvmInfo(jvmInfo)
                .setSystemInfo(systemInfo)
                .setSysFileInfos(sysFileInfos)
                .setPackageVersion(new AllInfo.PackageVersion("NyxBot", "1.1.0"));
    }
}
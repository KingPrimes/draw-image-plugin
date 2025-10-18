package io.github.kingprimes;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * 插件管理器，用于加载和管理DrawImagePlugin插件
 *
 * @author KingPrimes
 * @version 1.0.2
 */
@SuppressWarnings("unused")
public final class PluginManager {

    private static final Logger LOGGER = Logger.getLogger(PluginManager.class.getName());

    private final List<DrawImagePlugin> plugins = new ArrayList<>();

    // 存储已加载的本地库，避免重复加载
    private final List<String> loadedLibraries = new ArrayList<>();


    /**
     * 从指定目录加载插件
     *
     * @param pluginDir 插件目录路径
     */
    public void loadPlugins(String pluginDir) {
        File dir = new File(pluginDir);
        if (!dir.exists() || !dir.isDirectory()) {
            if (dir.mkdir()) {
                LOGGER.info("目录不存在，已创建目录: %s".formatted(pluginDir));
            }
        }

        // 清空之前的插件
        plugins.clear();
        loadedLibraries.clear();
        LOGGER.info("开始从目录加载插件: %s".formatted(pluginDir));

        // 遍历目录中的所有插件文件
        File[] files = dir.listFiles((d, name) ->
                name.endsWith(".jar") || name.endsWith(".dll") ||
                        name.endsWith(".so") || name.endsWith(".dylib"));

        if (files == null) {
            throw new RuntimeException("在目录中未找到插件: %s".formatted(pluginDir));
        }

        LOGGER.info("找到 %d 个插件文件".formatted(files.length));

        // 分离jar文件和本地库文件
        List<File> jarFiles = new ArrayList<>();
        List<File> nativeLibraries = new ArrayList<>();

        for (File file : files) {
            String name = file.getName();
            if (name.endsWith(".jar")) {
                jarFiles.add(file);
            } else if (name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib")) {
                nativeLibraries.add(file);
            }
        }

        // 加载jar插件
        loadJarPlugins(jarFiles);

        // 加载本地库插件
        loadNativePlugins(nativeLibraries);

        LOGGER.info("插件加载完成，共加载了 %d 个插件".formatted(plugins.size()));
    }

    /**
     * 加载jar插件
     *
     * @param jarFiles jar文件列表
     */
    private void loadJarPlugins(List<File> jarFiles) {
        if (jarFiles.isEmpty()) {
            return;
        }

        // 创建URL数组
        URL[] urls = new URL[jarFiles.size()];
        for (int i = 0; i < jarFiles.size(); i++) {
            try {
                urls[i] = jarFiles.get(i).toURI().toURL();
                LOGGER.fine("加载jar文件: %s".formatted(jarFiles.get(i).getName()));
            } catch (Exception e) {
                throw new RuntimeException("无法加载插件: %s".formatted(jarFiles.get(i).getName()), e);
            }
        }

        // 创建类加载器
        URLClassLoader classLoader = new URLClassLoader(urls,
                Thread.currentThread().getContextClassLoader());

        // 使用ServiceLoader加载插件
        ServiceLoader<DrawImagePlugin> serviceLoader = ServiceLoader.load(DrawImagePlugin.class, classLoader);
        int pluginCount = 0;
        for (DrawImagePlugin plugin : serviceLoader) {
            plugins.add(plugin);
            pluginCount++;
            LOGGER.info("加载jar插件: %s (版本: %s)".formatted(plugin.getPluginName(), plugin.getPluginVersion()));
        }

        LOGGER.fine("jar插件加载完成，共加载了 %d 个插件".formatted(pluginCount));
    }

    /**
     * 加载本地库插件
     *
     * @param nativeLibraries 本地库文件列表
     */
    private void loadNativePlugins(List<File> nativeLibraries) {
        for (File libFile : nativeLibraries) {
            String libraryName = getLibraryName(libFile.getName());
            try {
                loadJNAPlugin(libraryName, libFile.getAbsolutePath());
                LOGGER.info("加载本地库插件: %s".formatted(libraryName));
            } catch (Exception e) {
                throw new RuntimeException("无法加载本地库插件: %s".formatted(libFile.getName()), e);
            }
        }
    }

    /**
     * 从文件名提取库名称
     *
     * @param fileName 文件名
     * @return 库名称
     */
    private String getLibraryName(String fileName) {
        if (fileName.startsWith("lib") &&
                (fileName.endsWith(".so") || fileName.endsWith(".dylib"))) {
            // Unix/Linux: libxxx.so -> xxx
            // macOS: libxxx.dylib -> xxx
            return fileName.substring(3, fileName.lastIndexOf('.'));
        } else if (fileName.endsWith(".dll")) {
            // Windows: xxx.dll -> xxx
            return fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }

    /**
     * 从本地库文件加载插件（支持JNA）
     *
     * @param libraryName 本地库名称（不包含平台特定的前缀和后缀）
     * @param libraryPath 本地库完整路径
     */
    private void loadJNAPlugin(String libraryName, String libraryPath) {
        LOGGER.info("尝试加载JNA本地库插件: %s 从路径: %s".formatted(libraryName, libraryPath));

        // 检查库是否已加载
        if (loadedLibraries.contains(libraryName)) {
            LOGGER.warning("本地库 %s 已经加载，跳过重复加载".formatted(libraryName));
            return;
        }

        try {
            // 将库路径添加到系统属性中，以便JNA可以找到它
            String jnaLibPath = System.getProperty("jna.library.path", "");
            String libraryDir = new File(libraryPath).getParent();
            if (!jnaLibPath.contains(libraryDir)) {
                if (!jnaLibPath.isEmpty()) {
                    jnaLibPath += File.pathSeparator;
                }
                jnaLibPath += libraryDir;
                System.setProperty("jna.library.path", jnaLibPath);
            }

            // 创建JNA插件适配器实例
            JNADrawPluginAdapter jnaPlugin = new JNADrawPluginAdapter(libraryName);
            plugins.add(jnaPlugin);

            // 将库添加到已加载列表
            loadedLibraries.add(libraryName);

            LOGGER.info("成功创建JNA本地库插件实例: %s (版本: %s)".formatted(jnaPlugin.getPluginName(), jnaPlugin.getPluginVersion()));

        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("无法加载本地库: %s".formatted(libraryPath), e);
        } catch (Exception e) {
            throw new RuntimeException("创建本地库插件实例时出错", e);
        }
    }

    /**
     * 获取所有已加载的插件
     *
     * @return 插件列表
     */
    public List<DrawImagePlugin> getAllPlugins() {
        LOGGER.fine("获取所有已加载的插件，当前共有 %d 个插件".formatted(plugins.size()));
        return new ArrayList<>(plugins);
    }

    /**
     * 根据插件名称获取插件
     *
     * @param pluginName 插件名称
     * @return 插件实例，如果未找到返回null
     */
    public DrawImagePlugin getPluginByName(String pluginName) {
        LOGGER.fine("查找插件: %s".formatted(pluginName));
        if (!plugins.isEmpty()) {
            return plugins.stream()
                    .filter(p -> p.getPluginName().equals(pluginName))
                    .peek(p -> LOGGER.info("找到插件: %s".formatted(p.getPluginName())))
                    .findFirst()
                    .orElse(null);
        }
        throw new RuntimeException("未找到插件: %s".formatted(pluginName));
    }

    /**
     * 获取默认第一个插件
     *
     * @return 插件实例
     */
    public DrawImagePlugin getFirstPlugin() {
        if (plugins.isEmpty()) {
            throw new RuntimeException("未加载任何插件");
        }
        LOGGER.fine("返回第一个加载的插件: %s".formatted(plugins.getFirst().getPluginName()));
        return plugins.getFirst();
    }

    /**
     * 获取插件数量
     *
     * @return 插件数量
     */
    public int getPluginCount() {
        return plugins.size();
    }
}

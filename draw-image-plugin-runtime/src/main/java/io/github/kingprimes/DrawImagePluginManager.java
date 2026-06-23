package io.github.kingprimes;

import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * 插件管理器，用于加载和管理DrawImagePlugin插件
 *
 * @author KingPrimes
 * @version 1.0.2
 */
@SuppressWarnings("unused")
public final class DrawImagePluginManager {

    private static final Logger LOGGER = Logger.getLogger(DrawImagePluginManager.class.getName());
    private static final Pattern SAFE_LIBRARY_NAME = Pattern.compile("^[a-zA-Z0-9._-]+$");

    private final List<DrawImagePlugin> plugins = new ArrayList<>();

    // 存储已加载的本地库，避免重复加载
    private final List<String> loadedLibraries = new ArrayList<>();

    // Native 插件加载器（可为 null，不配置则不加载 native 插件）
    private final NativePluginLoader nativePluginLoader;

    /**
     * 创建一个不加载 native 插件的管理器
     */
    public DrawImagePluginManager() {
        this.nativePluginLoader = null;
    }

    /**
     * 创建一个指定 native 插件加载器的管理器
     *
     * @param nativePluginLoader native 插件加载器
     */
    public DrawImagePluginManager(NativePluginLoader nativePluginLoader) {
        this.nativePluginLoader = Objects.requireNonNull(nativePluginLoader);
    }


    /**
     * 从指定目录加载插件
     * <p>
     * <b>注意</b>：本方法涉及 URLClassLoader 和 ServiceLoader，
     * 请在 platform thread 上调用，不要使用 Virtual Thread 初始化。
     * </p>
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

        // 释放旧插件资源（图片缓存、原生内存等）
        for (DrawImagePlugin old : plugins) {
            try {
                old.releaseMemory();
            } catch (Exception e) {
                LOGGER.warning("释放旧插件资源失败: %s".formatted(e.getMessage()));
            }
        }
        // 清理 Native 加载器全局资源（如 JNA 平台线程池）
        if (nativePluginLoader != null) {
            try {
                nativePluginLoader.cleanup();
            } catch (Exception e) {
                LOGGER.warning("清理 Native 加载器失败: %s".formatted(e.getMessage()));
            }
        }

        plugins.clear();
        loadedLibraries.clear();
        LOGGER.info("开始从目录加载插件: %s".formatted(pluginDir));

        // 遍历目录中的所有插件文件
        File[] files = dir.listFiles((d, name) ->
                name.endsWith(".jar") || name.endsWith(".dll") ||
                        name.endsWith(".so") || name.endsWith(".dylib"));

        if (files == null || files.length == 0) {
            LOGGER.warning("在目录中未找到插件...将加载默认实现.");
            plugins.add(new DefaultDrawImagePlugin());
            return;
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
        if (nativeLibraries.isEmpty()) {
            return;
        }
        if (nativePluginLoader == null) {
            LOGGER.warning("检测到 native 插件文件，但未配置 NativePluginLoader，跳过加载。" +
                    "如需 native 支持，请添加 draw-image-plugin-native-jna 依赖并创建 JnaNativePluginLoader。");
            return;
        }
        for (File libFile : nativeLibraries) {
            String libraryName = getLibraryName(libFile.getName());
            try {
                loadNativePlugin(libraryName, libFile.getAbsolutePath());
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
     * 通过 NativePluginLoader 加载本地库插件
     *
     * @param libraryName 本地库名称（不包含平台特定的前缀和后缀）
     * @param libraryPath 本地库完整路径
     */
    private void loadNativePlugin(String libraryName, String libraryPath) {
        LOGGER.info("尝试加载本地库插件: %s 从路径: %s".formatted(libraryName, libraryPath));

        if (!SAFE_LIBRARY_NAME.matcher(libraryName).matches()) {
            throw new RuntimeException("拒绝加载: 库名包含不安全字符: %s".formatted(libraryName));
        }
        File libFile = new File(libraryPath);
        if (!libFile.isFile()) {
            throw new RuntimeException("库文件不存在: %s".formatted(libraryPath));
        }

        // 检查库是否已加载
        if (loadedLibraries.contains(libraryName)) {
            LOGGER.warning("本地库 %s 已经加载，跳过重复加载".formatted(libraryName));
            return;
        }

        try {
            DrawImagePlugin plugin = nativePluginLoader.loadNativePlugin(libraryName, libraryPath);
            plugins.add(plugin);
            loadedLibraries.add(libraryName);
            LOGGER.info("成功创建本地库插件实例: %s (版本: %s)".formatted(plugin.getPluginName(), plugin.getPluginVersion()));
        } catch (Exception e) {
            throw new RuntimeException("创建本地库插件实例时出错: %s".formatted(libraryPath), e);
        }
    }

    /**
     * 获取所有已加载的插件
     *
     * @return 插件列表
     */
    public List<DrawImagePlugin> getAllPlugins() {
        if (!plugins.isEmpty()) {
            LOGGER.fine("获取所有已加载的插件，当前共有 %d 个插件".formatted(plugins.size()));
            return new ArrayList<>(plugins);
        } else {
            LOGGER.warning("未找到插件，返回默认实现。");
            return Collections.singletonList(new DefaultDrawImagePlugin());
        }
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
        LOGGER.warning("未找到插件: %s 将返回默认实现。".formatted(pluginName));
        return new DefaultDrawImagePlugin();
    }

    /**
     * 获取默认第一个插件
     *
     * @return 插件实例
     */
    public DrawImagePlugin getFirstPlugin() {
        if (plugins.isEmpty()) {
            LOGGER.warning("未找到插件，返回默认实现。");
            return new DefaultDrawImagePlugin();
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

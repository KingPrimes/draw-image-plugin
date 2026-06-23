# DrawImagePlugin

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kingprimes/draw-image-plugin-core.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.kingprimes/draw-image-plugin-core)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
![Java](https://img.shields.io/badge/java-21-orange.svg)

> 一个 Java 图像合成与绘图插件框架，专为 Warframe 游戏数据可视化设计，支持 Java 插件和 Native 动态库热插拔。

---

## 目录

- [模块架构](#模块架构)
- [模块说明](#模块说明)
- [快速开始：实现一个 Java 插件](#快速开始实现一个-java-插件)
- [继承默认实现（部分覆写）](#继承默认实现部分覆写)
- [作为宿主集成](#作为宿主集成)
- [支持 Native 动态库插件](#支持-native-动态库插件)
- [Spring Boot 集成示例](#spring-boot-集成示例)
- [Maven 坐标](#maven-坐标)
- [插件目录结构](#插件目录结构)
- [构建](#构建)

---

## 模块架构

本项目拆分为三个独立模块，职责分明：

```
draw-image-plugin
├── draw-image-plugin-core         # 🎯 插件 SDK（你只需要这个来开发插件）
├── draw-image-plugin-runtime      # ⚙️ 宿主运行时（加载和管理插件）
└── draw-image-plugin-native-jna   # 🔌 Native 桥接（加载 .dll/.so 动态库）
```

### 依赖关系

```
core ─── 无 JNA 依赖，仅 jackson + lombok
  ↑
runtime ─── 依赖 core，管理插件生命周期
  ↑
native-jna ─── 依赖 core + jna，可选
```

---

## 模块说明

### `draw-image-plugin-core`

**定位**：插件 SDK，所有插件开发者依赖的唯一模块。

**包含**：
- `DrawImagePlugin` — 插件核心接口
- `DefaultDrawImagePlugin` — 官方默认实现，可直接继承后部分覆写
- 所有数据模型（WorldState、Market、枚举等）
- 图像工具类（合成、滤镜、文字渲染等）
- 字体和图像资源
- `NativePluginLoader` — Native 插件加载接口定义

### `draw-image-plugin-runtime`

**定位**：宿主运行时，统一管理插件生命周期。

**包含**：
- `DrawImagePluginManager` — 插件管理器（扫描目录、加载 jar、注册插件）
- `SwitchableDrawImagePlugin` — 热切换代理（运行时无感切换）
- 支持 jar 和 native 两种插件来源

### `draw-image-plugin-native-jna`

**定位**：Native 动态库桥接，将 `.dll` / `.so` / `.dylib` 适配为 `DrawImagePlugin`。

**包含**：
- `JNADrawPluginAdapter` — JNA 适配器（JSON 序列化传参）
- `JnaNativePluginLoader` — NativePluginLoader 实现

---

## 快速开始：实现一个 Java 插件

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.kingprimes</groupId>
    <artifactId>draw-image-plugin-core</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 实现接口

```java
package com.example;

import io.github.kingprimes.DrawImagePlugin;
import io.github.kingprimes.model.*;
import io.github.kingprimes.model.market.*;
import io.github.kingprimes.model.worldstate.*;
import java.util.List;
import java.util.Map;

public class MyPlugin implements DrawImagePlugin {

    @Override
    public byte[] drawHelpImage(List<String> helpInfo) {
        // 你的绘图逻辑
        return new byte[0];
    }

    // ... 实现其余 drawXXX 方法

    @Override
    public String getPluginName() {
        return "MyPlugin";
    }

    @Override
    public String getPluginVersion() {
        return "1.0.0";
    }

    @Override
    public void releaseMemory() {
        // 释放资源
    }
}
```

### 3. 添加 SPI 声明（二选一）

**方式 A（推荐）：使用 AutoService 注解处理器**

```xml
<dependency>
    <groupId>com.google.auto.service</groupId>
    <artifactId>auto-service</artifactId>
    <version>1.1.1</version>
    <optional>true</optional>
</dependency>
```

然后在插件类上加上 `@AutoService` 注解：

```java
import com.google.auto.service.AutoService;

@AutoService(DrawImagePlugin.class)
public class MyPlugin implements DrawImagePlugin {
    // ...
}
```

编译后自动生成 `META-INF/services/io.github.kingprimes.DrawImagePlugin`，无需手动维护。

**方式 B：手动创建**

创建 `src/main/resources/META-INF/services/io.github.kingprimes.DrawImagePlugin`，内容：

```
com.example.MyPlugin
```

### 4. 打包

```bash
mvn clean package
```

将生成的 jar 放入宿主程序的 `plugin/` 目录即可。

---

## 继承默认实现（部分覆写）

不想从头实现所有方法？继承 `DefaultDrawImagePlugin`，只改你想改的：

```java
package com.example;

import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;

public class MyCustomPlugin extends DefaultDrawImagePlugin {

    @Override
    public String getPluginName() {
        return "MyCustomPlugin";
    }

    @Override
    public String getPluginVersion() {
        return "1.0.0";
    }

    // 只覆写你想改的方法
    @Override
    public byte[] drawHelpImage(List<String> helpInfo) {
        // 自定义帮助图
        return new byte[0];
    }
    // 其余方法自动继承默认实现
}
```

> 同样需要添加 SPI 声明才能被宿主发现。推荐使用 `@AutoService(DrawImagePlugin.class)` 注解，或手动创建 `META-INF/services/io.github.kingprimes.DrawImagePlugin`。

> 默认插件的名称、版本、作者可通过 `plugin.properties` 配置文件修改，无需改代码。

---

## 作为宿主集成

### Maven 依赖

```xml
<dependency>
    <groupId>io.github.kingprimes</groupId>
    <artifactId>draw-image-plugin-runtime</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 基本使用

```java
// 1. 创建管理器
DrawImagePluginManager manager = new DrawImagePluginManager();

// 2. 扫描插件目录
manager.loadPlugins("./plugin");

// 3. 获取插件
DrawImagePlugin plugin = manager.getFirstPlugin();
byte[] image = plugin.drawHelpImage(helpData);
```

### 热切换

```java
// 创建代理
DrawImagePlugin initial = manager.getFirstPlugin();
SwitchableDrawImagePlugin active = new SwitchableDrawImagePlugin(initial);

// 运行时切换
active.switchTo(manager.getPluginByName("OtherPlugin"));

// 业务代码始终注入 active，无需重建 Bean
```

---

## 支持 Native 动态库插件

### Maven 依赖

```xml
<dependency>
    <groupId>io.github.kingprimes</groupId>
    <artifactId>draw-image-plugin-runtime</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>io.github.kingprimes</groupId>
    <artifactId>draw-image-plugin-native-jna</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 代码

```java
// 创建 native 加载器
NativePluginLoader jnaLoader = new JnaNativePluginLoader();

// 传入管理器，同时支持 jar + 动态库
DrawImagePluginManager manager = new DrawImagePluginManager(jnaLoader);
manager.loadPlugins("./plugin");

// 两种插件统一使用
DrawImagePlugin active = manager.getFirstPlugin();
```

### 数据通讯协议

Java 端将数据对象序列化为 JSON，通过 JNA `Memory` 传入 native 端。数据格式：

```
[4 字节数据长度][JSON 数据]
```

native 端返回格式相同：`[4 字节数据长度][图像字节数据]`，由 Java 端读取后通过 `nativeFree` 释放。

---

## Spring Boot 集成示例

```java
@Configuration
public class DrawImagePluginConfig {

    @Bean
    public DrawImagePluginManager pluginManager() {
        NativePluginLoader jnaLoader = new JnaNativePluginLoader();
        return new DrawImagePluginManager(jnaLoader);
    }

    @Bean
    public DrawImagePlugin drawImagePlugin(DrawImagePluginManager manager) {
        manager.loadPlugins("./plugin");
        SwitchableDrawImagePlugin plugin = new SwitchableDrawImagePlugin(
            manager.getFirstPlugin());
        return plugin;
    }
}
```

NyxBot 控制器切换示例：

```java
@RestController
@RequestMapping("/config/plugin/image")
public class DrawImagePluginController {

    private final DrawImagePluginManager manager;
    private final SwitchableDrawImagePlugin activePlugin;

    public DrawImagePluginController(
            DrawImagePluginManager manager,
            SwitchableDrawImagePlugin activePlugin) {
        this.manager = manager;
        this.activePlugin = activePlugin;
    }

    @PostMapping("/load")
    public AjaxResult load(@RequestParam String pluginName) {
        DrawImagePlugin plugin = manager.getPluginByName(pluginName);
        if (plugin == null) {
            return AjaxResult.error("插件不存在");
        }
        activePlugin.switchTo(plugin);
        return AjaxResult.success("已切换到: " + plugin.getPluginName());
    }

    @GetMapping("/list")
    public List<String> list() {
        return manager.getAllPlugins().stream()
            .map(p -> p.getPluginName() + " v" + p.getPluginVersion())
            .toList();
    }

    @GetMapping("/current")
    public String current() {
        return activePlugin.getPluginName();
    }

    @PostMapping("/reload")
    public AjaxResult reload() {
        manager.loadPlugins("./plugin");
        activePlugin.switchTo(manager.getFirstPlugin());
        return AjaxResult.success("插件已重载");
    }
}
```

---

## Maven 坐标

| 模块 | 坐标 |
|------|------|
| core | `io.github.kingprimes:draw-image-plugin-core:2.0.0` |
| runtime | `io.github.kingprimes:draw-image-plugin-runtime:2.0.0` |
| native-jna | `io.github.kingprimes:draw-image-plugin-native-jna:2.0.0` |

---

## 插件目录结构

```
your-app/
└── plugin/
    ├── my-java-plugin.jar          # Java 插件（SPI）
    ├── my-native-plugin.dll        # Windows 动态库
    ├── libmy-native-plugin.so      # Linux 动态库
    └── libmy-native-plugin.dylib   # macOS 动态库
```

`DrawImagePluginManager` 自动识别后缀名，分流加载：
- `*.jar` → `URLClassLoader` + `ServiceLoader`
- `*.dll / *.so / *.dylib` → `JnaNativePluginLoader`

---

## 构建

```bash
# 编译
mvn clean compile

# 打包
mvn clean package -DskipTests

# 安装到本地仓库
mvn clean install -DskipTests

# 发布到 Maven Central（需要 GPG 密钥和 Central 凭证）
mvn clean deploy -DskipTests
```

---

## 许可证

本项目采用 Apache License 2.0 许可证。

## 作者

**KingPrimes** — [GitHub](https://github.com/kingprimes)

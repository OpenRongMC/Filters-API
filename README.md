# 创造栏筛选 API

简体中文 · [English](./README.en.md)

为创造模式物品栏提供分类按钮。扩展可将物品分组，也可用「未分类」入口让未归类物品保持可见。交通套件用它整理数量很大的建筑与站务物品。

## 下载与安装

- 版本 · [v1.0.0-port.1+26.3](https://github.com/OpenRongMC/Filters-API/releases/tag/v1.0.0-port.1%2B26.3) (已发布)
- 下载文件 · `rongmc-filters-1.0.0-port.1.26.3.jar`
- 运行环境 · Minecraft Java 26.3, Fabric, Java 25
- Fabric Loader · `>=0.19.3`
- Fabric 模组 ID · `rongmc_filters`
- JAR 声明的依赖 · [Fabric API](https://modrinth.com/mod/fabric-api)

把发行 JAR 放入对应实例的 `mods/` 目录。和使用筛选功能的模组一起装，版本以依赖它的模组发布说明为准。整合包联机时使用其指定的构建。

## 如何验证

进入创意模式，打开支持筛选的交通模组页签，切换两个分类并检查未分类物品。排查布局异常时，记录同时修改创造栏的模组。

## 开发

与同一版本的 MTR 和平台映射一起联调。交通套件的 RongMC 发布构建使用 Java 25，应以配套工作区和发行 JAR 的元数据为准。

## 文档与反馈

- [原 README（旧版指南、贡献者与署名）](https://github.com/OpenRongMC/Filters-API/blob/8f804fea5652afd35aa24d3892362f25a935e8eb/README.md)
- [上游项目与文档](https://github.com/ZiYueCommentary/Filters-API)
- [报告 RongMC 移植问题](https://github.com/OpenRongMC/Filters-API/issues) · 请附游戏版本、两端 JAR 文件名、复现步骤和相关日志。

## 许可与署名

请参阅本仓库的 [LICENSE](./LICENSE) 及原项目的许可、署名和第三方资源说明。

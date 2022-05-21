# moyu-framework

A SpringBoot framework for rapid development

MoYu SpringBoot 快速开发脚手架

### MoYu 框架结构设计

```
moyu-framework MoYu框架
│ 
├── moyu-autoconfiguers         # 自动装配模块
│  ├── moyu-framework-common    # 公共服务依赖
│  └── moyu-web-autoconfiguers  # Web快速开发 自动装配
│ 
├─ moyu-base                    # 基础模块（可独立依赖）
│  ├── moyu-model               # 公共模型
│  └── moyu-util                # 公共工具类
│
└─ moyu-sample                  # demo工程
```

### MoYu 框架版本说明

#### 1 开发版本

- SNAPSHOT 开发版本，发布在 MoYu 专属仓库中，供 MoYu 小组开发调试使用，内容持续更新

> 最新版本 `0.0.1-SNAPSHOT`

- 开发版引用方式

1. pom文件加入MoYu共有仓库

```
<repositories>
    <repository>
        <id>moyu-group-snapshots</id>
        <name>moyu-group-repository-snapshots</name>
        <url>https://maven.ffis.me/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

2. 然后父工程依赖 moyu-framework

```
<parent>
    <artifactId>moyu-framework</artifactId>
    <groupId>io.github.moyu-group</groupId>
    <version>0.0.1-SNAPSHOT</version>
</parent>
```

3. 之后导入需要的模块即可，例如导入moyu-web快速开发模块，根据框架的demo工程的配置就行

```
<dependency>
    <groupId>io.github.moyu-group</groupId>
    <artifactId>moyu-web-autoconfiguers</artifactId>
</dependency>
```

#### 2 线上版本

- RELEASE 线上版本，发布到 Maven 中央仓库中

> 暂未发布


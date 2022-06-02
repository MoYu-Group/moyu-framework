# moyu-framework

A SpringBoot framework for rapid development

MoYu SpringBoot 快速开发脚手架

### 什么是 MoYu 框架？
作为一个开发人员，你是否有以下烦恼？

每次有个新想法，想实现某个功能，但是从0搭建框架，引入各种配置，搞了半天还没开始进行功能开发，甚至想了一下繁琐的配置就放弃了刚刚的新想法；

MoYu 框架就是为了解决这个痛点诞生的，将 Java 后端 Web 开发常用的配置都抽离到一个框架中，需要开发时直接引入框架即可直接进行功能开发，并且开发风格保持统一，岂不美哉～

引入 MoYu 框架会提供如下能力：
- 统一的依赖版本控制
- 统一的日志切面配置
- 统一的异常处理机制
- 统一的接口返回定义
- 提供一套默认的错误页面
- 提供常用工具类（BeanUtil / Zip压缩处理 / 雪花ID等）
- 提供常用服务的自动装配（Spring Security / Redis / Spring Data JPA / Mybatis Plus 等）
- 所有服务都可以配置和扩展

### MoYu 框架结构设计

```
moyu-framework MoYu框架
│ 
├── moyu-autoconfiguers         # 自动装配模块
│  ├── moyu-framework-common    # 公共服务依赖
│  ├── moyu-web-autoconfiguers  # Web快速开发 自动装配
│  ├── moyu-security-autoconfigures          # spring security 安全模块 自动装配（TODO）
│  ├── moyu-orm-jpa-autoconfigures           # spring data jpa 自动装配（TODO）
│  ├── moyu-orm-mybatis-plus-autoconfigures  # mybatis-plus 自动装配（TODO）
│  └── moyu-redis-autoconfigures             # redis 自动装配（TODO）
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

### TODO List

- Spring Security 自动装配
- Redis 自动装配
- Spring Data JPA 自动装配
- Mybatis Plus 自动装配


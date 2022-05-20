# moyu-framework

A SpringBoot framework for rapid development

MoYu SpringBoot 快速开发脚手架

### MoYu 框架项目结构设计

```
moyu-framework MoYu框架
│ 
├── moyu-autoconfiguers         # 自动装配模块
│  ├── moyu-framework-common    # 公共服务自动装配
│  └── moyu-web-autoconfiguers  # Web开发自动装配
│ 
└─ moyu-base                    # 基础模块（可独立依赖）
    ├── moyu-model              # 公共模型
    └── moyu-util               # 公共工具类
```

### MoYu 框架版本说明

- SNAPSHOT 开发版本，发布在 MoYu 专属仓库中，供 MoYu 小组开发调试使用
    - 最新版本 `0.0.1-SNAPSHOT`
- RELEASE 线上版本，发布到 Maven 中央仓库中
    - 暂未发布


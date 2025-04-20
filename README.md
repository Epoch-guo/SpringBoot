# 信息工程学院竞赛管理系统

## 项目简介
该系统是一个基于Spring Boot开发的竞赛管理平台，用于信息工程学院的各类竞赛管理。系统支持学生报名参赛、教师创建竞赛和评分、管理员进行系统管理等功能。

## 技术栈
- Spring Boot 3.3.5
- MyBatis 3.0.3
- MySQL 8.0
- Knife4j (Swagger) 4.5.0
- Sa-Token 1.39.0
- Druid 1.2.23
- Lombok 1.18.30

## 项目结构
```
SpringBoot-Web/
├── common/             # 通用模块
│   └── src/main/java/com/epoch/
│       ├── constant/   # 常量定义
│       ├── exception/  # 全局异常处理
│       ├── json/       # JSON处理工具
│       ├── result/     # 统一返回结果
│       └── utils/      # 工具类
├── pojo/               # 实体类模块
│   └── src/main/java/com/epoch/entity/
│       ├── User.java            # 用户实体
│       ├── Contest.java         # 竞赛实体
│       ├── Registration.java    # 报名实体
│       ├── TeamMember.java      # 团队成员实体
│       ├── Question.java        # 题目实体
│       ├── Submission.java      # 提交实体
│       ├── Score.java           # 评分实体
│       ├── Notification.java    # 通知实体
│       ├── Log.java             # 日志实体
│       └── Config.java          # 配置实体
└── server/             # 服务端模块
    └── src/main/
        ├── java/com/epoch/
        │   ├── config/          # 配置类
        │   ├── constant/        # 常量定义
        │   ├── controller/      # 控制层
        │   ├── dto/             # 数据传输对象
        │   ├── handler/         # 处理器
        │   ├── interceptor/     # 拦截器
        │   ├── mapper/          # 数据访问层
        │   ├── service/         # 服务层
        │   ├── vo/              # 视图对象
        │   └── CompetitionApplication.java    # 启动类
        └── resources/
            ├── mapper/          # MyBatis映射文件
            ├── markdown/        # API文档Markdown文件
            ├── application.yaml # 主配置文件
            └── application-dev.yaml # 开发环境配置
```

## 功能模块
1. 用户管理
   - 学生、教师、管理员角色
   - 用户注册、登录、信息管理
   
2. 竞赛管理
   - 教师创建、编辑竞赛
   - 竞赛信息展示
   - 竞赛状态管理
   
3. 报名管理
   - 学生个人/团队报名
   - 报名审核
   - 团队成员管理
   
4. 题目管理
   - 选择题、编程题、设计题
   - 题目内容与评分标准
   
5. 提交与评分
   - 学生提交答案
   - 教师评分
   - 成绩统计
   
6. 通知管理
   - 系统公告
   - 竞赛提醒
   - 审核结果通知
   
7. 系统管理
   - 系统日志
   - 系统配置
   - 权限管理

## 运行说明
1. 确保已安装JDK 17或更高版本
2. 配置MySQL数据库并在application-dev.yaml中设置连接信息
3. 使用Maven构建项目：`mvn clean package`
4. 运行项目：`java -jar server/target/server-1.0-SNAPSHOT.jar`
5. 访问Swagger文档：`http://localhost:8080/doc.html`

## 接口文档
系统使用Knife4j提供API文档，运行项目后可以通过访问 `http://localhost:8080/doc.html` 查看详细接口文档。 
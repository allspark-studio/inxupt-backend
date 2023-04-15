# 开发文档

## 开发环境

- JDK1.8
- IntelliJ IDEA
- IDEA插件：
    - Alibaba Java Coding Guidelines plugin
    - Free Mybatis plugin
    - Lombok
- SprintBoot 2.3.1
- MySQL 5.7
- Redis 7.0
- RabbitMQ 3.12
- ElasticSearch 7.9

## 第三方环境/插件

- 阿里云OSS
- 阿里云SMS
- QQ邮箱SMTP
- Swagger
- Mybatis-generator
- PageHelper

## 本地开发环境搭建：

### 安装 Dcoker
> 略，参考网络资料

### MySQL
1. 启动 mysql 容器

`docker run -d --name mysql5.7 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password sql:5.7`

2. 进入 mysql 容器

`docker exec -it mysql5.7 /bin/bash`
`mysql -uroot -p`
`password`

3. 创建数据库

`create database inxupt_dev default character set utf8mb4 collate utf8mb4_unicode_ci;`

4. 初始化数据库表

将 `inxupt.sql` 全部内容复制，粘贴到终端中，或者用 navicat 等数据库管理工具导入

### Redis

1. 下载 redis 默认配置文件

   http://download.redis.io/redis-stable/redis.conf

2. 将 redis.conf 中的文件内容作如下修改

```text
# bind 127.0.0.1 # 将这一行注释，否则不能从容器外访问 redis
protected-mode no    # 关闭保护模式
```

3. 将 redis.conf 拷贝到 `{your_path}` 目录

4. 启动 Redis 镜像，其中 your_path 替换为你自己的目录

`docker run -d --name redis -p 6379:6379 -v {your_path}\redis.conf:/etc/redis/redis.conf redis /etc/redis/redis.conf`

例如

`docker run -d --name redis -p 6379:6379 -v C:\Users\Administrator\redis.conf:/etc/redis/redis.conf redis /etc/redis/redis.conf`

### RabbitMQ
`docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=allspark -e RABBITMQ_DEFAULT_PASS=password -e RABBITMQ_DEFAULT_VHOST=inxupt_dev rabbitmq:3.12-rc-management`

### ElasticSearch

> ES 太占内存，并且启动配置较为麻烦，可以先不在本地启动，仅搜索功能不能调试，不影响开发调试主流程

## 启动 SpringBoot 主程序
在终端输入命令行 `mvn:spring-boot:run`

或者直接在 JetBrains IDEA 右边 Maven tab 栏单击

`spring-boot -> spring-boot:run`
## Swagger 地址
http://localhost:8080/swagger-ui/index.html




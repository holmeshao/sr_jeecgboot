# 构建阶段
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

# 构建jar包
RUN mvn clean package -DskipTests

# 运行阶段
FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER jeecgos@163.com

# ===== 可在 build/run 时覆盖的默认参数 =====
ARG SPRING_DATASOURCE_PLATFORM=postgresql
ARG POSTGRES_HOST=jeecg-boot-pgsql
ARG POSTGRES_PORT=5432
ARG POSTGRES_DB=nacos
ARG POSTGRES_USER=postgres
ARG POSTGRES_PWD=postgres

ENV SPRING_DATASOURCE_PLATFORM=${SPRING_DATASOURCE_PLATFORM} \
    POSTGRES_HOST=${POSTGRES_HOST} \
    POSTGRES_PORT=${POSTGRES_PORT} \
    POSTGRES_DB=${POSTGRES_DB} \
    POSTGRES_USER=${POSTGRES_USER} \
    POSTGRES_PWD=${POSTGRES_PWD} \
    # 固定 nacos.home 位置，和官方镜像一致
    NACOS_HOME=/home/nacos

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
 && mkdir -p ${NACOS_HOME}/conf ${NACOS_HOME}/plugins

# ---------- 关键：复制静态应用配置 ----------
COPY conf/application.properties ${NACOS_HOME}/conf/
# 覆盖首选的 JDBC 配置，使 Nacos 启动即连接 PostgreSQL
COPY conf/jdbc.properties ${NACOS_HOME}/conf/
COPY nacos-plugins/*.jar ${NACOS_HOME}/plugins/

WORKDIR /jeecg-cloud-nacos
# 从构建阶段复制jar包
COPY --from=builder /build/target/jeecg-cloud-nacos-3.8.1.jar ./

ENTRYPOINT ["java","-Dnacos.home=/home/nacos","-jar","/jeecg-cloud-nacos/jeecg-cloud-nacos-3.8.1.jar"]
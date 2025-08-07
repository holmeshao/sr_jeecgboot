# FROM registry.cn-hangzhou.aliyuncs.com/jeecgdocker/mysql:8.0.19
FROM postgres:17.5

MAINTAINER jeecgos@163.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# COPY ./tables_nacos.sql /docker-entrypoint-initdb.d
# COPY ./jeecgboot-mysql-5.7.sql /docker-entrypoint-initdb.d
# COPY ./tables_xxl_job.sql /docker-entrypoint-initdb.d

# PostgreSQL初始化脚本
#COPY ./tables_nacos.sql /docker-entrypoint-initdb.d/
#COPY ./jeecgboot-mysql-5.7.sql /docker-entrypoint-initdb.d/
#COPY ./tables_xxl_job.sql /docker-entrypoint-initdb.d/

# 创建日志目录
RUN mkdir -p /var/log/postgresql && \
    chown postgres:postgres /var/log/postgresql

# 设置权限
RUN chown -R postgres:postgres /docker-entrypoint-initdb.d/
FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:21-anolis

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-dataingest-cloud

WORKDIR /jeecg-dataingest-cloud

EXPOSE 7009

ADD ./target/jeecg-boot-module-dataingest-3.8.0.jar ./

CMD sleep 60;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-boot-module-dataingest-3.8.0.jar 
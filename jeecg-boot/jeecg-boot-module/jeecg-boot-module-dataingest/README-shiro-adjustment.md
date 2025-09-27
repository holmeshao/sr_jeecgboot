### 数据摄入模块 Shiro 配置调整说明

本次调整目标：避免与基础模块 `org.jeecg.config.shiro.ShiroConfig` 的 Bean 名称冲突（`shiroConfig`）。

变更内容：
- 移除模块内重复配置类 `org.jeecg.dataingest.config.ShiroConfig`；
- 在 `application-dataingest.yml` 增加 `jeecg.shiro.excludeUrls`，通过基础 Shiro 读取 yml 的排除列表，实现匿名访问白名单；

受影响文件：
- 删除：`src/main/java/org/jeecg/dataingest/config/ShiroConfig.java`
- 修改：`src/main/resources/application-dataingest.yml`

新增 yml 片段：
```yaml
jeecg:
  shiro:
    excludeUrls: /dataingest/health/**,/dataingest/actuator/**,/dataingest/swagger-ui/**,/dataingest/v3/api-docs/**
```

影响与兼容性：
- 保留并复用基础模块 Shiro 的统一实现（JWT 过滤器、Redis 缓存、异步支持等）；
- 数据摄入模块需要放行的接口统一通过 yml 管理；
- 不影响其他服务；

验证方式：
1. 重新构建并启动 `jeecg-boot-dataingest`；
2. 访问以下 URL 应无需鉴权：
   - `/dataingest/health/**`
   - `/dataingest/actuator/**`
   - `/dataingest/swagger-ui/**`
   - `/dataingest/v3/api-docs/**`
3. 访问其他 `/dataingest/**` 需携带 JWT；

回滚方案：
- 如需恢复模块内自定义 Shiro，可重新添加被删除的 `ShiroConfig.java`，但需变更 Bean 名或包名以避免与基础配置冲突。



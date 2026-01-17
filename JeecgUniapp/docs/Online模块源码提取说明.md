# Online 模块源码提取说明

## 源码位置

JeecgBoot 的 Online 模块源码在以下两个 JAR 包中：

1. **hibernate-re-3.8.0.2-sources.jar**
   - 路径：`\\wsl.localhost\Ubuntu\opt\repository\org\jeecgframework\boot\hibernate-re\3.8.0.2\`
   - 大小：894 字节（非常小，可能只包含少量源码）
   - 用途：Online 模块核心

2. **codegenerate-1.4.9-sources.jar**
   - 路径：`\\wsl.localhost\Ubuntu\opt\repository\org\jeecgframework\boot\codegenerate\1.4.9\`
   - 大小：3469 字节（也很小）
   - 用途：代码生成

## 提取方法

由于这两个 JAR 包都非常小（不到 4KB），可能只包含少量的源码或者只是占位文件。

### 方法1：使用 jar 命令提取

```bash
# 创建目标目录
mkdir -p jeecg-boot/jeecg-module-system/jeecg-online-sources/hibernate-re
mkdir -p jeecg-boot/jeecg-module-system/jeecg-online-sources/codegenerate

# 提取 hibernate-re 源码
cd jeecg-boot/jeecg-module-system/jeecg-online-sources/hibernate-re
jar -xf "\\wsl.localhost\Ubuntu\opt\repository\org\jeecgframework\boot\hibernate-re\3.8.0.2\hibernate-re-3.8.0.2-sources.jar"

# 提取 codegenerate 源码
cd ../codegenerate
jar -xf "\\wsl.localhost\Ubuntu\opt\repository\org\jeecgframework\boot\codegenerate\1.4.9\codegenerate-1.4.9-sources.jar"
```

### 方法2：重命名为 .zip 后解压

```bash
# 复制并重命名为 .zip
cp "\\wsl.localhost\Ubuntu\opt\repository\org\jeecgframework\boot\hibernate-re\3.8.0.2\hibernate-re-3.8.0.2-sources.jar" hibernate-re-sources.zip
cp "\\wsl.localhost\Ubuntu\opt\repository\org\jeecgframework\boot\codegenerate\1.4.9\codegenerate-1.4.9-sources.jar" codegenerate-sources.zip

# 使用 unzip 或其他工具解压
unzip hibernate-re-sources.zip -d hibernate-re-sources
unzip codegenerate-sources.zip -d codegenerate-sources
```

## 注意事项

1. **JAR 包很小**：这两个 sources.jar 文件都非常小（不到 4KB），可能：
   - 只包含少量关键源码
   - 只是占位文件
   - 实际源码可能在其他地方

2. **检查主 JAR 包**：
   - `hibernate-re-3.8.0.2.jar` (500KB) - 这个才是主要的 JAR 包
   - `codegenerate-1.4.9.jar` (76KB) - 这个也是主要的 JAR 包
   - 如果 sources.jar 内容不足，可能需要反编译主 JAR 包

3. **下一步行动**：
   - 先提取 sources.jar 查看内容
   - 如果内容不足，再考虑反编译主 JAR 包
   - 或者直接查看 JeecgBoot 的 GitHub 仓库

## 建议

由于 sources.jar 文件很小，建议：
1. 先手动查看这两个 JAR 包的内容
2. 如果内容不足，直接查看 JeecgBoot 的 GitHub 仓库
3. 或者使用反编译工具（如 CFR）反编译主 JAR 包

---

**创建时间**：2025-01-15  
**状态**：待执行

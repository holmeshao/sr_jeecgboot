/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.io.FileUtil
 */
package org.jeecg.modules.online.cgform.d;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class f {
    public static File a(List<String> list, String string) throws RuntimeException {
        File file = FileUtil.touch((String)string);
        if (file == null) {
            return null;
        }
        if (!file.getName().endsWith(".zip")) {
            return null;
        }
        ZipOutputStream zipOutputStream = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            for (String string2 : list) {
                int n;
                File file2 = new File(string2 = URLDecoder.decode(string2, "UTF-8").replace("\u751f\u6210\u6210\u529f\uff1a", ""));
                if (file2 == null || !file2.exists()) continue;
                byte[] byArray = new byte[4096];
                String string3 = null;
                string3 = file2.getAbsolutePath().indexOf("src\\") != -1 ? file2.getAbsolutePath().substring(file2.getAbsolutePath().indexOf("src\\") - 1) : file2.getAbsolutePath().substring(file2.getAbsolutePath().indexOf("src/") - 1);
                zipOutputStream.putNextEntry(new ZipEntry(string3));
                FileInputStream fileInputStream = new FileInputStream(file2);
                while ((n = fileInputStream.read(byArray)) != -1) {
                    zipOutputStream.write(byArray, 0, n);
                }
                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                }
                catch (IOException iOException) {
                    System.out.println("ZipUtil toZip close exception" + iOException);
                }
            }
            fileOutputStream.close();
        }
        catch (Exception exception) {
            throw new RuntimeException("zipFile error from ZipUtils", exception);
        }
        return file;
    }
}


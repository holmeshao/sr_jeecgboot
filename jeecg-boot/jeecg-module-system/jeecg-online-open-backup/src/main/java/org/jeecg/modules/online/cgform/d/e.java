/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.servlet.ServletOutputStream
 *  javax.servlet.http.HttpServletResponse
 *  org.apache.commons.io.FilenameUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.jeecg.modules.online.cgform.d;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class e {
    private static final Logger a = LoggerFactory.getLogger(e.class);

    public static void a(HttpServletResponse httpServletResponse, String string, String string2) {
        httpServletResponse.setCharacterEncoding("UTF-8");
        File file = new File(string);
        if (!file.exists()) {
            throw new NullPointerException("Specified file not found");
        }
        if (string2 == null || string2.isEmpty()) {
            throw new NullPointerException("The file name can not null");
        }
        httpServletResponse.setHeader("content-type", "application/octet-stream");
        httpServletResponse.setContentType("application/octet-stream");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(string2, "UTF-8"));
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            a.error(unsupportedEncodingException.getMessage(), (Throwable)unsupportedEncodingException);
        }
        byte[] byArray = new byte[1024];
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);){
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            int n = bufferedInputStream.read(byArray);
            while (n != -1) {
                servletOutputStream.write(byArray, 0, n);
                n = bufferedInputStream.read(byArray);
            }
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
        }
    }

    public static void a(HttpServletResponse httpServletResponse, List<String> list, String string) throws IOException {
        String string2 = string + ".zip";
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("content-type", "application/octet-stream");
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(string2, "UTF-8"));
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream((OutputStream)httpServletResponse.getOutputStream()));
             DataOutputStream dataOutputStream = new DataOutputStream(zipOutputStream);){
            zipOutputStream.setMethod(8);
            for (String string3 : list) {
                int n;
                File file = new File(string3);
                if (!file.exists()) continue;
                String string4 = file.getName();
                zipOutputStream.putNextEntry(new ZipEntry(string4));
                InputStream inputStream = Files.newInputStream(file.toPath(), new OpenOption[0]);
                byte[] byArray = new byte[1024];
                while ((n = inputStream.read(byArray)) != -1) {
                    dataOutputStream.write(byArray, 0, n);
                }
                inputStream.close();
                zipOutputStream.closeEntry();
            }
        }
        catch (IOException iOException) {
            a.error(iOException.getMessage(), (Throwable)iOException);
        }
    }

    /*
     * Exception decompiling
     */
    public static String a(String var0, String var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static File a(File file) {
        if (!file.exists()) {
            return file;
        }
        File file2 = new File(file.getAbsolutePath());
        File file3 = file2.getParentFile();
        int n = 1;
        String string = FilenameUtils.getExtension((String)file2.getName());
        String string2 = FilenameUtils.getBaseName((String)file2.getName());
        while ((file2 = new File(file3, string2 + "(" + n++ + ")." + string)).exists()) {
        }
        return file2;
    }

    private static File a(String string) {
        File file = new File(string);
        e.b(file.getParentFile());
        return file;
    }

    public static void b(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}


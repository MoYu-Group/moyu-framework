package io.github.moyugroup.base.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具类
 * <p>
 * Created by fanfan on 2022/05/25.
 */
public class ZipUtil {

    private ZipUtil() {
        // empty
    }

    /**
     * 压缩文件
     *
     * @param filePath 待压缩的文件路径
     * @return 压缩后的文件
     */
    public static File zip(String filePath) throws IOException {
        File zipFile = null;
        File sourceFile = new File(filePath);
        if (sourceFile.exists()) {
            // 压缩文件名=源文件名.zip
            String zipName = sourceFile.getName() + ".zip";
            zipFile = new File(sourceFile.getParent(), zipName);
            if (zipFile.exists()) {
                zipFile.delete(); // 删除旧的文件
            }
            try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));) {
                // 添加对应的文件Entry
                addEntry("/", sourceFile, zos);
            } catch (IOException e) {
                throw e;
            }
        }
        return zipFile;
    }

    /**
     * 添加zip文件Entry
     *
     * @param baseDir    base路径
     * @param sourceFile 源文件
     * @param zos        zip文件输出流
     * @throws IOException
     */
    private static void addEntry(String baseDir, File sourceFile, ZipOutputStream zos) throws IOException {
        // 按目录分级
        String entry = baseDir + sourceFile.getName();
        if (sourceFile.isDirectory()) {
            for (File file : sourceFile.listFiles()) {
                // 递归添加文件Entry
                addEntry(entry + "/", file, zos);
            }
        } else {
            byte[] buffer = new byte[1024 * 10];
            try (FileInputStream fis = new FileInputStream(sourceFile); BufferedInputStream bis = new BufferedInputStream(fis, buffer.length);) {
                int read = 0;
                zos.putNextEntry(new ZipEntry(entry));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            }
        }
    }

    /**
     * 解压zip文件
     *
     * @param filePath 文件路径
     */
    public static void unzip(String filePath) throws IOException {
        File zipFile = new File(filePath);
        if (zipFile.exists()) {
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null && !entry.isDirectory()) {
                    File target = new File(zipFile.getParent(), entry.getName());
                    if (!target.getParentFile().exists()) {
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target))) {
                        int read;
                        byte[] buffer = new byte[1024 * 10];
                        while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                            bos.write(buffer, 0, read);
                        }
                        bos.flush();
                    }
                }
                zis.closeEntry();
            } catch (IOException e) {
                throw e;
            }
        }
    }
}

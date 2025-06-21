package com.example.utils;

import com.example.exception.BusinessException;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 响应工具类，用于将文件内容写入HTTP响应
 */
public class ResponseUtils
{
    private static final String IMAGE_CONTENT_TYPE = "image/%s";
    private static final String VIDEO_CONTENT_TYPE = "video/%s";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    /**
     * 将文本文件内容写入HTTP响应
     *
     * @param response HTTP响应对象
     * @param path     文件路径
     * @throws BusinessException 如果文件路径不合法或文件读取失败
     * @throws IOException       如果发生IO错误
     */
    public static void writeTextFileToResponse(HttpServletResponse response, String path) throws BusinessException, IOException
    {
        // 检查文件路径是否合法
        if (path.contains("../") || path.contains("..\\"))
        {
            throw new BusinessException("Illegal file path");
        }

        File file = new File(path);

        // 设置响应内容类型和头信息
        response.setContentType(TEXT_PLAIN);
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        // 获取响应的PrintWriter对象
        PrintWriter writer = response.getWriter();

        // 使用FileReader读取文件内容
        try (FileReader fileReader = new FileReader(path, StandardCharsets.UTF_8); BufferedReader bufferedReader = new BufferedReader(fileReader))
        {
            String line;
            // 逐行读取文件内容并写入响应
            while ((line = bufferedReader.readLine()) != null)
            {
                writer.println(line);
            }
        }
        catch (IOException e)
        {
            // 捕获IO异常并抛出BusinessException
            throw new BusinessException("Error reading file", e);
        }
        finally
        {
            // 确保PrintWriter被关闭
            writer.close();
        }
    }

    /**
     * 将图片内容写入HTTP响应。
     * <p>
     * 此方法负责将指定路径的图片文件读取并发送给客户端。
     * 它首先调用通用的文件写入方法，然后设置响应的MIME类型为图片类型，
     * 以便客户端能够正确解析接收到的数据。
     *
     * @param response HTTP响应对象，用于设置响应头和写入数据。
     * @param path     图片文件的本地路径。
     * @param type     图片的格式后缀（例如 "jpg"），用于确定正确的MIME类型。
     * @throws BusinessException 如果文件路径非法或文件不是图片类型，抛出此异常。
     * @throws IOException       如果在读取或写入文件时发生IO错误，抛出此异常。
     */
    public static void writeImageToResponse(HttpServletResponse response, String path, String type) throws BusinessException, IOException
    {
        File file = new File(path);
        // 检查文件是否为图片类型（此处应有 isImageFile 的实现，但未给出）
        if (!isImageFile(file))
        {
            throw new BusinessException("File is not an image");
        }

        // 调用通用文件写入方法
        writeFileToResponse(response, path);

        // 设置响应的MIME类型为图片类型
        response.setContentType(String.format(IMAGE_CONTENT_TYPE, type));
    }

    /**
     * 将视频文件内容写入HTTP响应。
     * <p>
     * 此方法与图片写入类似，但处理的是视频文件。它调用通用文件写入方法，
     * 然后根据视频文件的类型设置正确的MIME类型。
     *
     * @param response HTTP响应对象，用于设置响应头和写入数据。
     * @param path     视频文件的本地路径。
     * @param type     视频的格式后缀（例如 "mp4"），用于确定正确的MIME类型。
     * @throws BusinessException 如果文件路径非法或无法读取文件，抛出此异常。
     * @throws IOException       如果在读取或写入文件时发生IO错误，抛出此异常。
     */
    public static void writeVideoFileToResponse(HttpServletResponse response, String path, String type) throws BusinessException, IOException
    {
        // 调用通用文件写入方法
        writeFileToResponse(response, path, 4096);

        // 根据视频文件类型设置响应的MIME类型
        response.setContentType(String.format(VIDEO_CONTENT_TYPE, type));
    }

    /**
     * 通用方法，用于将任意类型的文件写入到HTTP响应中。
     * <p>
     * 此方法检查文件路径的合法性，读取文件内容，并将其写入到HTTP响应中。
     * 它还设置响应的内容长度，这有助于客户端预估下载进度。
     *
     * @param response HTTP响应对象，用于设置响应头和写入数据。
     * @param path     文件的本地路径。
     * @throws BusinessException 如果文件路径非法或文件读取失败，抛出此异常。
     * @throws IOException       如果在读取或写入文件时发生IO错误，抛出此异常。
     */
    public static void writeFileToResponse(HttpServletResponse response, String path) throws BusinessException, IOException
    {
        // 检查文件路径的合法性，防止目录遍历攻击
        if (path.contains("../") || path.contains("..\\"))
        {
            throw new BusinessException("Illegal file path");
        }

        // 创建File对象，准备读取文件
        File file = new File(path);

        // 设置响应的Content-Disposition头，指示文件将以附件形式发送
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s", file.getName()));
        // 设置响应的内容长度
        response.setContentLengthLong(file.length());

        // 使用FileInputStream读取文件，使用response.getOutputStream()写入响应
        try (FileInputStream fileInputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream())
        {
            // 创建缓冲区，用于临时存储读取的数据
            byte[] buffer = new byte[1024];
            int bytesRead;

            // 循环读取文件，直到读完所有内容
            while ((bytesRead = fileInputStream.read(buffer)) != -1)
            {
                // 将读取的数据写入到响应流中
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e)
        {
            // 捕获并重新抛出IO异常，封装为BusinessException
            throw new BusinessException("Error reading file", e);
        }
    }

    /**
     * 将本地文件的内容写入到HTTP响应中。
     * <p>
     * 此方法接收一个HTTP响应对象和本地文件的路径，将文件内容读取并发送给客户端。
     * 它还包括一个可配置的缓冲区大小参数，用于控制读取文件的块大小。
     *
     * @param response   HTTP响应对象，用于设置响应头和写入文件数据。
     * @param path       本地文件的完整路径。
     * @param bufferSize 用于读取文件的缓冲区大小，必须大于零。
     * @throws BusinessException 如果缓冲区大小小于等于零，或文件路径非法，或文件读取失败。
     * @throws IOException       如果在读取或写入文件时发生IO错误。
     */
    public static void writeFileToResponse(HttpServletResponse response, String path, int bufferSize) throws BusinessException, IOException
    {
        // 验证缓冲区大小是否合理
        if (bufferSize <= 0)
        {
            throw new BusinessException("buffer size must larger than zero");
        }

        // 检查文件路径的安全性，防止目录遍历攻击
        if (path.contains("../") || path.contains("..\\"))
        {
            throw new BusinessException("Illegal file path");
        }

        // 创建File对象，指向本地文件
        File file = new File(path);

        // 设置响应的Content-Disposition头，表明文件将作为附件发送
        response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", file.getName()));

        // 设置响应的内容长度，使客户端能够提前知道文件大小
        response.setContentLengthLong(file.length());

        // 使用FileInputStream读取本地文件
        // 使用response.getOutputStream()写入HTTP响应
        try (FileInputStream fileInputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream())
        {
            // 创建缓冲区，用于读取文件数据
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            // 循环读取文件，直到文件末尾
            while ((bytesRead = fileInputStream.read(buffer)) != -1)
            {
                // 将缓冲区中的数据写入到HTTP响应流中
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e)
        {
            // 捕获IO异常，封装为BusinessException并抛出
            throw new BusinessException("Error reading file", e);
        }
    }

    /**
     * 检查文件是否为图片类型
     *
     * @param file 文件对象
     * @return 如果文件为图片类型返回true，否则返回false
     * @throws IOException 如果发生IO错误
     */
    private static boolean isImageFile(File file) throws IOException
    {
        // 使用 Files.probeContentType 方法获取文件的 MIME 类型
        String mimeType = java.nio.file.Files.probeContentType(file.toPath());

        // 检查 MIME 类型是否不为空且以 "image" 开头
        return mimeType != null && mimeType.startsWith("image");
    }

}

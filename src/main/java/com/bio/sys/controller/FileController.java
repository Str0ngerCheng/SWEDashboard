package com.bio.sys.controller;


import com.bio.common.utils.AESUtil;
import com.bio.common.utils.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/reportfile")
public class FileController {
    //存放--服务器上zip文件的目录
    private String directory = "E:\\Test\\";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/test")
    @RequiresAuthentication
    public String test() {
        return "welcome";
    }

    @PostMapping("/uploadfile")
    @ResponseBody
    @CrossOrigin
    @RequiresPermissions("bio:report:edit")
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        //获得上传文件的文件名
        String fileName = file.getOriginalFilename();
        //设置本地的文件地址并完成文件名的拼接
//        String filePath = "D:\\test\\";//这里的地址文件上传到的地方
        File dest = new File(directory + fileName.replace('/', '-'));
        String error = "";
        try {
            //将文件复制到本地。
            file.transferTo(dest);
            return "上传成功";
        } catch (IOException e) {
            error = e.getMessage();
        }
        return "上传失败！" + error;
    }

    @ResponseBody
    @CrossOrigin
    @GetMapping("/downloadbynamestopic")
    //导出专题附件，和查询导出原理相同，唯一不同的就是zip名字
    public void downloadAllFilebyNamesTopic(HttpServletResponse response, String[] names) {

        File directoryFile = new File(directory);
        if (!directoryFile.isDirectory() && !directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        if (names.length < 1) {
            return;
        }
        String prefix = "";
        try {
            String name = java.net.URLDecoder.decode(names[0], "UTF-8");
            String[] nametemp = name.split(" ")[0].split("-");
            prefix = nametemp[0] + "-" + nametemp[1] + "-" + nametemp[2] + "-" + nametemp[3] + "-"
                    + nametemp[4] + "-" + nametemp[5] + "-" + nametemp[6];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //设置最终输出zip文件的目录+文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String zipFileName = prefix + "周报附件汇总" + ".zip";
        String strZipPath = directory + "\\" + zipFileName;

        ZipOutputStream zipStream = null;
        FileInputStream zipSource = null;
        BufferedInputStream bufferStream = null;
        File zipFile = new File(strZipPath);
        try {
            //构造最终压缩包的输出流
            zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < names.length; i++) {
                //解码获取真实路径与文件名
                String realFileName = java.net.URLDecoder.decode(names[i], "UTF-8");

                File file = new File(directory + realFileName + '\\');

                //未对文件不存在时进行操作，后期优化。
                if (file.exists()) {
                    zipSource = new FileInputStream(file);//将需要压缩的文件格式化为输入流
                    /**
                     * 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样这里的name就是文件名,
                     * 文件名和之前的重复就会导致文件被覆盖
                     */
                    ZipEntry zipEntry = new ZipEntry(realFileName);//在压缩目录中文件的名字
                    zipStream.putNextEntry(zipEntry);//定位该压缩条目位置，开始写入文件到压缩包中
                    bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                    int read = 0;
                    byte[] buf = new byte[1024 * 10];
                    while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
                        zipStream.write(buf, 0, read);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (null != bufferStream) bufferStream.close();
                if (null != zipStream) {
                    zipStream.flush();
                    zipStream.close();
                }
                if (null != zipSource) zipSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //判断系统压缩文件是否存在：true-把该压缩文件通过流输出给客户端后删除该压缩文件  false-未处理
        if (zipFile.exists()) {
            downFile(response, zipFileName, strZipPath);
            zipFile.delete();
        }
    }

    @ResponseBody
    @CrossOrigin
    @GetMapping("/downloadbynamesquery")
    //导出查询附件，和专题导出原理相同，唯一不同的就是zip名字
    public void downloadAllFilebyNamesQuery(HttpServletResponse response, String[] names) {

        File directoryFile = new File(directory);
        if (!directoryFile.isDirectory() && !directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        if (names.length < 1) {
            return;
        }

        //设置最终输出zip文件的目录+文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String zipFileName = "周报附件汇总" + formatter.format(new Date()) + ".zip";
        String strZipPath = directory + "\\" + zipFileName;

        ZipOutputStream zipStream = null;
        FileInputStream zipSource = null;
        BufferedInputStream bufferStream = null;
        File zipFile = new File(strZipPath);
        try {
            //构造最终压缩包的输出流
            zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < names.length; i++) {
                //解码获取真实路径与文件名
                String realFileName = java.net.URLDecoder.decode(names[i], "UTF-8");

                File file = new File(directory + realFileName + '\\');

                //未对文件不存在时进行操作，后期优化。
                if (file.exists()) {
                    zipSource = new FileInputStream(file);//将需要压缩的文件格式化为输入流
                    /**
                     * 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样这里的name就是文件名,
                     * 文件名和之前的重复就会导致文件被覆盖
                     */
                    ZipEntry zipEntry = new ZipEntry(realFileName);//在压缩目录中文件的名字
                    zipStream.putNextEntry(zipEntry);//定位该压缩条目位置，开始写入文件到压缩包中
                    bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                    int read = 0;
                    byte[] buf = new byte[1024 * 10];
                    while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
                        zipStream.write(buf, 0, read);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (null != bufferStream) bufferStream.close();
                if (null != zipStream) {
                    zipStream.flush();
                    zipStream.close();
                }
                if (null != zipSource) zipSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //判断系统压缩文件是否存在：true-把该压缩文件通过流输出给客户端后删除该压缩文件  false-未处理
        if (zipFile.exists()) {
            downFile(response, zipFileName, strZipPath);
            zipFile.delete();
        }
    }

    @GetMapping(value = "/ifFileExist")
    @RequiresAuthentication
    @ResponseBody
    public Result<String> ifFileExist(String[] filenames) {
        //String filenamedecode=java.net.URLDecoder.decode(filename);

        for (String filename : filenames) {
            File file = new File(directory + filename);
            // 如果文件路径所对应的文件存在,则返回ok
            if (file.exists()) {
                return Result.ok();
            }
        }
        return Result.fail("文件不存在！");
    }


    //下载单个附件
    @GetMapping("/downloadreportfile")
    @RequiresAuthentication
    public void downReportFile(HttpServletResponse response, String filename) {
        if (filename != null) {
            FileInputStream is = null;
            BufferedInputStream bs = null;
            OutputStream os = null;
            String filenamedecode = java.net.URLDecoder.decode(filename);
            String path = directory + filenamedecode;
            try {
                File file = new File(path);
                if (file.exists()) {
                    //设置Headers
                    response.setHeader("Content-Type", "application/octet-stream");
                    //设置下载的文件的名称-该方式已解决中文乱码问题
                    response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"), "ISO8859-1"));
                    is = new FileInputStream(file);
                    bs = new BufferedInputStream(is);
                    os = response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = bs.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                } else {
                    String error = Base64.encodeBase64String("下载的文件资源不存在".getBytes());
                    response.sendRedirect("/error/404");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (bs != null) {
                        bs.close();
                    }
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void downFile(HttpServletResponse response, String filename, String path) {
        if (filename != null) {
            FileInputStream is = null;
            BufferedInputStream bs = null;
            OutputStream os = null;
            try {
                File file = new File(path);
                if (file.exists()) {
                    //设置Headers
                    response.setHeader("Content-Type", "application/octet-stream");
                    //设置下载的文件的名称-该方式已解决中文乱码问题
                    response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"), "ISO8859-1"));
                    is = new FileInputStream(file);
                    bs = new BufferedInputStream(is);
                    os = response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = bs.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                } else {
                    String error = Base64.encodeBase64String("下载的文件资源不存在".getBytes());
                    //response.sendRedirect("/error/404");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (bs != null) {
                        bs.close();
                    }
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*
     *
     * 对外暴露的文件接口
     *
     * */
    @GetMapping("/downloadFileByURL")
    public ResponseEntity<Resource> downloadCacheFile(HttpServletRequest request, @RequestParam("fileName") String fileName) {
        try {
            String savePath = directory;
            // 不需要再次decode
            //String filenamedecode = java.net.URLDecoder.decode(fileName);
            fileName = AESUtil.AESDncode(fileName);
            LOGGER.info("fileName: " + fileName);

            // 获取本地文件系统中的文件资源
            FileSystemResource resource = new FileSystemResource(savePath + fileName);

            // 解析文件的 mime 类型
            String mediaTypeStr = URLConnection.getFileNameMap().getContentTypeFor(fileName);
            // 无法判断MIME类型时，作为流类型
            mediaTypeStr = (mediaTypeStr == null) ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mediaTypeStr;
            // 实例化MIME
            MediaType mediaType = MediaType.parseMediaType(mediaTypeStr);

            /*
             * 构造响应的头
             */
            HttpHeaders headers = new HttpHeaders();
            // 下载之后需要在请求头中放置文件名，该文件名按照ISO_8859_1编码。
            String userAgent = request.getHeader("user-agent").toLowerCase();

            if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {
                // win10 ie edge 浏览器 和其他系统的ie
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                // 不是IE的时候
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            }

            //String filenames = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(mediaType);

            /*
             * 返还资源
             */
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.getInputStream().available())
                    .body(resource);
        } catch (IOException e) {
            LOGGER.error("文件读写错误", e);
            return null;
        }
    }
}
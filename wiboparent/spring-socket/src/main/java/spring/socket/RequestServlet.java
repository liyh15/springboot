package spring.socket;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求对象
 */
@ToString
public class RequestServlet implements Serializable {

    public final static int CR = 13;

    public final static int LF = 10;

    // 请求方法
    private String method;

    // 请求路径
    private String path;

    // 请求协议
    private String protocol;

    // 请求输入流
    private InputStream inputStream;

    private Map<String, List<LyhMutiPartFile>> lyhMutiPartFileMap = new HashMap<>();

    public byte[] getBodyBype() {
        return bodyBype;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setBodyBype(byte[] bodyBype) {
        this.bodyBype = bodyBype;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    // 请求头参数
    private Map<String, String> headerMap = new HashMap<>();

    // 请求参数
    private Map<String, List<String>> paramMap = new HashMap<>();

    // 请求体数组
    private byte [] bodyBype;

    /**
     * 添加请求头
     * @param key
     * @param value
     */
    public void putHeaderMap(String key, String value) {
        headerMap.put(key, value);
    }

    /**
     * 获取请求头参数
     * @param key
     */
    public String getHeaderMap(String key) {
        return headerMap.get(key);
    }

    /**
     * 添加请求参数
     * @param key
     * @param value
     */
    public void putParamMap(String key, String value) {
        if (paramMap.containsKey(key)) {
            // 如果参数包含这个key，则添加在该key对应的集合后面
            paramMap.get(key).add(value);
        } else {
            // 如果不包含，则新增该key值的集合
            List<String> valueList = new ArrayList<>();
            valueList.add(value);
            paramMap.put(key, valueList);
        }
    }

    /**
     * 获取请求参数
     * @param key
     * @return
     */
    public String getParamMap(String key) {
        return paramMap.get(key).get(0);
    }

    /**
     * 获取请求参数集合
     * @param key
     * @return
     */
    public List<String> getParamMapList(String key) {
        return paramMap.get(key);
    }


    /**
     * 读取请求体里面的数据(当执行到具体的servlet时执行)
     * @return
     */
    private byte [] readHttpBody() throws IOException {
        if (!"0".equals(headerMap.get(SocketServer.CONTENT_LENGTH))) {
            // 如果请求体长度不为0，则处理请求体
            if (SocketServer.MUTIPART_FORM_DATA.equals(headerMap.get(SocketServer.CONTENT_TYPE))) {
                /*
                  请求状态，1:表示即将读取内容头，2:表示读取第二行内容头 3：读取文件具体内容
                */
                int status = 0;
                String boundary = headerMap.get(SocketServer.BOUNDARY);
                String endLine = headerMap.get(SocketServer.BOUNDARY) + "--";
                String readLine = null;
                String twoReadLine = null;
                while (!endLine.equals(readLine = readOneLine())) {
                    // 如果没有到末尾，则读取
                    if (status == 1) {
                        // 如果等于1，则读取的是内容头
                        twoReadLine = readOneLine();
                        if (StringUtils.isBlank(twoReadLine)) {
                            // TODO 如果读取文件头的第二行为空，表示此为表单文件，则先解析文件头再读取文件
                            int a = readLine.indexOf("; ");
                            String nameParam = readLine.substring(a + 1);
                            int b = nameParam.indexOf("=");
                            String name = nameParam.substring(b + 2, nameParam.length() - 1);
                            String body = read();
                            putParamMap(name ,body);
                        } else {
                            // TODO 如果读取文件头的第二行不为空，表示此为大文件，则读取一行空，并解析大文件
                            int a = readLine.lastIndexOf("; filename=\"");
                            String fileName = readLine.substring(a + 1, readLine.length() - 1);
                            int b = readLine.indexOf("; name=\"");
                            String name = readLine.substring(b + 1, a - 1);
                            LyhMutiPartFile lyhMutiPartFile = new LyhMutiPartFile();
                            // 读取一行空
                            readOneLine();

                        }
                    }
                    if (boundary.equals(readLine)) {
                        // 如果读取到分隔符
                        status = 1;
                    }
                }
            }
        }
        byte [] bodyByte = new byte[Integer.valueOf(headerMap.get(SocketServer.CONTENT_LENGTH))];
        
        inputStream.read(bodyByte);
        return bodyByte;
    }

    /**
     * 读取字符直到分隔符
     * @return
     */
    private String read() throws UnsupportedEncodingException {
       String readLine = null;
       StringBuilder stringBuilder = new StringBuilder();
       int a = 0;
       while (!(readLine = readOneLine()).contains(headerMap.get(SocketServer.BOUNDARY))) {
          if (a == 0) {
              stringBuilder.append(readLine);
          } else {
              stringBuilder.append(CR).append(LF).append(readLine);
          }
           a = 1;
       }
       return stringBuilder.toString();
    }

    /**
     * 读取HTTP一行
     *
     */
    private String readOneLine() throws UnsupportedEncodingException {
        int h = 0;
        int n = 0;
        ByteOutputStream outputStream = new ByteOutputStream();
        try {
            while((n = inputStream.read()) != -1) {
                if (CR == h && LF == n) {
                    byte [] bytes = outputStream.getBytes();
                    return URLDecoder.decode(new String(bytes, "UTF-8").trim(), "UTF-8");
                }
                outputStream.write(n);
                h = n;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte [] bytes = outputStream.getBytes();
        return URLDecoder.decode(new String(bytes, "UTF-8").trim(), "UTF-8");
    }

    public LyhMutiPartFile getLyhMutiPartFile(String name) {
         return lyhMutiPartFileMap.get(name).get(0);
    }

    public List<LyhMutiPartFile> getLyhMutiPartFileList(String name) {
        return lyhMutiPartFileMap.get(name);
    }

    public byte[] readFile() throws UnsupportedEncodingException {
        int a = 0;
        ByteOutputStream outputStream = new ByteOutputStream();
        byte [] bytes = readOneLineFile();
        String str = new String(bytes).trim();
        if (headerMap.get(SocketServer.BOUNDARY).equals(str)) {
            return outputStream.getBytes();
        }
        outputStream.write(bytes);
        return null;
    }

    /**
     * 读取HTTP一行
     *
     */
    private byte[] readOneLineFile() throws UnsupportedEncodingException {
        int h = 0;
        int n = 0;
        ByteOutputStream outputStream = new ByteOutputStream();
        try {
            while((n = inputStream.read()) != -1) {
                outputStream.write(n);
                if (CR == h && LF == n) {
                    return outputStream.getBytes();
                }
                h = n;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.getBytes();
    }
}

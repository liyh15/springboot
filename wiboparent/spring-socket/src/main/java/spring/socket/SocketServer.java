package spring.socket;


import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;

public class SocketServer {

    private ServerSocket serverSocket;

    public final static int CR = 13;

    public final static int LF = 10;

    public final static String GET = "GET";

    public final static String POST = "POST";

    public final static String BOUNDARY = "boundary";

    public final static String CONTENT_TYPE = "Content-Type";

    public final static String CONTENT_LENGTH = "content-length";

    public final static String MUTIPART_FORM_DATA = "multipart/form-data";

    public final static String APPLICATION_JSON = "application/json";

    public final static String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public final static String TEXT_XML = "text/xml";
    /**
     * 初始化ServerSocket
     * @param port
     */
    public void init(Integer port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * 开始接受请求
     */
    private void acceptServerSocket() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            RequestServlet requestServlet = new RequestServlet();
            ResponseServlet responseServlet = new ResponseServlet();
            requestServlet.setInputStream(inputStream);
            responseServlet.setOutputStream(socket.getOutputStream());
            // 读取请求行
            readRequestLine(requestServlet);
            // 读取请求头
            readRequestHeader(requestServlet);
            byte [] bodyByte = readHttpBody(Integer.valueOf(requestServlet.getHeaderMap(CONTENT_LENGTH)), requestServlet.getInputStream());
            System.out.println(new String(bodyByte, "UTF-8"));
            // TODO 将处理请求体放到各自的子线程中
            // TODO readRequestBody(requestServlet);
        }
    }

    /**
     * 读取请求行
     */
    private void readRequestLine(RequestServlet servlet) throws UnsupportedEncodingException {
        String line = readOneLine(servlet.getInputStream());
        String [] lineList = line.split(" ");
        if (lineList.length >= 3) {
            // 如果数组长度大于3的话才解析请求行
            servlet.setMethod(lineList[0]);
            servlet.setPath(lineList[1]);
            servlet.setProtocol(lineList[2]);
            // 解析路径里面的参数
            initGetParam(servlet);
        }
    }

    /**
     * 初始化get请求头参数
     * @param servlet
     */
    private void initGetParam(RequestServlet servlet) {
        if (GET.equals(servlet.getMethod())) {
            String path = servlet.getPath();
            if (path.contains("?")) {
                // 获取?的第一个位置
               int index = path.indexOf("?");
               String paramAll = path.substring(index + 1);
               if (StringUtils.isNotBlank(paramAll)) {
                   String [] params = paramAll.split("&");
                   for (String param : params) {
                       if (param.contains("=")) {
                           String [] paramKeyValue = param.split("=");
                           String key = paramKeyValue[0];
                           String value = paramKeyValue[1];
                           servlet.putParamMap(key, value);
                       }
                   }
               }
            }
        }
    }

    /**
     * 读取请求头
     */
    private void readRequestHeader(RequestServlet servlet) throws UnsupportedEncodingException {
        String line = null;
        while (StringUtils.isNotBlank(line = readOneLine(servlet.getInputStream()))) {
            if (line.contains(": ")) {
                String [] lineList = line.split(": ");
                if (lineList[1].contains("; ")) {
                    String [] twoList = lineList[1].split("; ");
                    // 第一位为Content-Type
                    if (!twoList[0].contains("=")) {
                        servlet.putHeaderMap(lineList[0], twoList[0]);
                        for (int i = 1; i < twoList.length; i ++) {
                            if (twoList[i].contains("=")) {
                                String [] twoListList = twoList[i].split("=");
                                servlet.putHeaderMap(twoListList[0], twoListList[1]);
                            }
                        }
                    } else {
                        for (int i = 0; i < twoList.length; i ++) {
                            if (twoList[i].contains("=")) {
                                String [] twoListList = twoList[i].split("=");
                                servlet.putHeaderMap(twoListList[0], twoListList[1]);
                            }
                        }
                    }

                } else {
                    servlet.putHeaderMap(lineList[0], lineList[1]);
                }
            }
        }
    }

    /**
     * 读取请求体
     */
    private void readRequestBody(RequestServlet servlet) throws IOException {
        if (POST.equals(servlet.getMethod()) && !"0".equals(servlet.getHeaderMap(CONTENT_LENGTH))) {
            // 如果是APPLICATION_X_WWW_FORM_URLENCODED请求，走这里
            if (APPLICATION_X_WWW_FORM_URLENCODED.equals(servlet.getHeaderMap(CONTENT_TYPE))) {
                byte [] bodyByte = readHttpBody(Integer.valueOf(servlet.getHeaderMap(CONTENT_LENGTH)), servlet.getInputStream());
                servlet.setBodyBype(bodyByte);
                initBodyByte(servlet);
            }
        }
    }

    /**
     * 解析请求体
     */
    private void initBodyByte(RequestServlet servlet) throws UnsupportedEncodingException {
        switch (servlet.getHeaderMap(CONTENT_TYPE)) {
            case APPLICATION_X_WWW_FORM_URLENCODED:
                initApplicationXwwwFormUrlEncoded(servlet.getBodyBype(), servlet);
                break;
        }
    }

    /**
     * 解析application/x-www-form-urlencoded
     * @param bodyByte
     */
    private void initApplicationXwwwFormUrlEncoded(byte [] bodyByte, RequestServlet servlet) throws UnsupportedEncodingException {
         String bodyStr = URLDecoder.decode(new String(bodyByte, "UTF-8"), "UTF-8");
         String [] bodyList = bodyStr.split("&");
         for (String bodyParam : bodyList) {
             if (bodyParam.contains("=")) {
                 String [] bodyParamList = bodyParam.split("=");
                 servlet.putParamMap(bodyParamList[0], bodyParamList[1]);
             }
         }
    }

    /**
     * 读取HTTP一行
     *
     * @param inputStream
     */
    private String readOneLine(InputStream inputStream) throws UnsupportedEncodingException {
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

    /**
     * 读取请求体里面的数据
     * @param contentLength
     * @param inputStream
     * @return
     */
    private byte [] readHttpBody(Integer contentLength, InputStream inputStream) throws IOException {
        byte [] bodyByte = new byte[contentLength];
        inputStream.read(bodyByte);
        return bodyByte;
    }

    public static void main(String[] args) throws IOException {
        SocketServer socketServer = new SocketServer();
        socketServer.init(8080);
        socketServer.acceptServerSocket();
    }
}

package spring.socket;

import lombok.Data;

import java.io.OutputStream;
import java.io.Serializable;

@Data
public class ResponseServlet implements Serializable {

    private OutputStream outputStream;
}

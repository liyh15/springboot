package spring.socket.servlet;

import lombok.Data;
import spring.socket.RequestServlet;
import spring.socket.ResponseServlet;

@Data
public class BaseServlet {

    public void doService(RequestServlet requestServlet, ResponseServlet responseServlet) {
    }

    public void doGet(RequestServlet requestServlet, ResponseServlet responseServlet) {
    }

    public void doPost(RequestServlet requestServlet, ResponseServlet responseServlet) {
    }
}

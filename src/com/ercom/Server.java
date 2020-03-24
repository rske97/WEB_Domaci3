package com.ercom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server() {
        try {
            ServerSocket ss = new ServerSocket(1997);
            while (true) {
                Socket sock = ss.accept();
                new Thread(new ServerThread(sock)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

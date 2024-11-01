package ru.otus.october.http.server;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private HttpServer server;
    private Socket socket;
    private Dispatcher dispatcher;

    public ClientHandler(HttpServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.dispatcher = server.getDispatcher();
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[8192];
                int n = 0;
                n = socket.getInputStream().read(buffer);
                String rawRequest = new String(buffer, 0, n);
                HttpRequest request = new HttpRequest(rawRequest);
                request.info();
                dispatcher.execute(request, socket.getOutputStream());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
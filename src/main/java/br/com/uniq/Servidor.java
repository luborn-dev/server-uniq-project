package br.com.uniq;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private Socket socket;
    private ServerSocket serverSocket;
    public void iniciarServidor() throws IOException {

        try {
            serverSocket = new ServerSocket(3002);
            while (true) {
                System.out.println("oi");
                socket = serverSocket.accept();
                System.out.println(socket);
                System.out.println("Nova conexão");
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Servidor().iniciarServidor();
    }
}

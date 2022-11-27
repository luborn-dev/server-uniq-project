package br.com.uniq;

import br.com.uniq.database.daos.PatientDAO;
import br.com.uniq.database.dbos.Patient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private ServerSocket serverSocket;

    public Servidor(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();
                System.out.println("Novo cliente se conectou");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            PatientDAO.signUp(new Patient("Adalberto", "12345678901", 69,"senha123"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ServerSocket serverSocket = new ServerSocket(3000);
        Servidor server = new Servidor(serverSocket);
        server.startServer();
    }
}

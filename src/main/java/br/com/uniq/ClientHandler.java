package br.com.uniq;

import br.com.uniq.database.daos.PatientDAO;
import br.com.uniq.database.dbos.Patient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream receptor;
    private ObjectOutputStream transmissor;
    private MeuObj clientInfo;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.transmissor = new ObjectOutputStream(socket.getOutputStream());
            this.receptor    = new ObjectInputStream(socket.getInputStream());
//            this.clientInfo = (MeuObj) receptor.readObject();
//            System.out.println(clientInfo.getNome()+clientInfo.getCpf()+clientInfo.getIdade()+clientInfo.getSenha());
            clientHandlers.add(this);
//            broadcastMessage("SERVER: "+clientUsername+" has entered the chat!");

        } catch (IOException e){
            closeEverything(socket, transmissor, receptor);
        }

    }

    @Override
    public void run() {

        while(socket.isConnected()){

            Object recebidos;

            try {
                recebidos = receptor.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            try {
                if (recebidos instanceof br.com.uniq.MeuObj){
                    MeuObj messageFromClient = (MeuObj) recebidos;
                    System.out.println("MeuObj");

                    System.out.println(
                            "Nome: " + messageFromClient.getNome() +
                            "\nCpf: " + messageFromClient.getCpf() +
                            "\nIdade: " + messageFromClient.getIdade() +
                            "\nSenha: " + messageFromClient.getSenha());
                    try{
                        PatientDAO.signUp(new Patient(messageFromClient.getNome(),messageFromClient.getCpf(),
                                messageFromClient.getIdade(),messageFromClient.getSenha()));
                        transmissor.writeObject(new CastingToDb("Sucesso","ok"));
                    } catch (Exception e){
                        System.out.println(e);
                    }

                }
                if (recebidos instanceof br.com.uniq.MyString){
                    MyString recebidosCasted = (MyString) recebidos;
                    System.out.println("Eh mystring");
                    System.out.println(recebidosCasted.getCpf());
                }

//                broadcastMessage(messageFromClient);

            } catch (Exception e){
                System.out.println(e);
                closeEverything(socket, transmissor, receptor);
                break;
            }
        }
    }
    public void broadcastMessage(MeuObj messageToSend){
        for (ClientHandler clientHandler : clientHandlers){
            try{
                if (!clientHandler.clientInfo.equals(clientInfo)) {
                    clientHandler.transmissor.writeObject(messageToSend);
                    clientHandler.transmissor.flush();
                }
            } catch (IOException e){
                closeEverything(socket, transmissor, receptor);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
//        broadcastMessage("SERVER: "+clientUsername+ " has left the chat");
    }

    public void closeEverything(Socket socket, ObjectOutputStream transmissor, ObjectInputStream receptor){
        removeClientHandler();
        try{
            if (receptor != null){
                receptor.close();
            }
            if (transmissor != null){
                transmissor.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

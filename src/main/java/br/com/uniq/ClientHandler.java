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

            Object recebidos = null;

            try {
                recebidos = receptor.readObject();
            } catch (IOException | ClassNotFoundException e) {
            }

            try {
                if (recebidos != null){
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
//                        Thread.currentThread().interrupt(); @TODO
                        } catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    if (recebidos instanceof br.com.uniq.LoginModelo){
                        System.out.println("LoginModelo");
                        LoginModelo recebidosCasted = (LoginModelo) recebidos;
                        System.out.println(recebidosCasted.getCpf());
                        try{
                            boolean isSignUp = PatientDAO.isSignUp(recebidosCasted.getCpf());
                            if(isSignUp){
                                System.out.println("Sucesso - Encontrado");
                                transmissor.writeObject(new CastingToDb("Usuario encontrado","ok"));
                            } else{
                                System.out.println("Erro - Nao encontrado");
                                transmissor.writeObject(new CastingToDb("Usuário não encontrado","erro"));
                            }
                        } catch (Exception e){
                            System.out.println("Erro - #10");
                            transmissor.writeObject(new CastingToDb("Erro interno","erro"));
                        }
                        }
                    }
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

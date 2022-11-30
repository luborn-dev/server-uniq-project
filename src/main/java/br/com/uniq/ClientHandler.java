package br.com.uniq;

import br.com.uniq.database.daos.ExamesDAO;
import br.com.uniq.database.daos.PatientDAO;
import br.com.uniq.database.dbos.Patient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        ObjectInputStream receptor;
        ObjectOutputStream transmissor;

        try {
            receptor = new ObjectInputStream(socket.getInputStream());
            transmissor = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(socket.isConnected()){

            Object recebidoDoCliente = null;

            try {
                recebidoDoCliente = receptor.readObject();
            } catch (IOException | ClassNotFoundException e) {
            }

            try {
                if (recebidoDoCliente != null){
                    if (recebidoDoCliente instanceof ModeloDeCadastro){
                        ModeloDeCadastro cadastroRecebidoDoCliente = (ModeloDeCadastro) recebidoDoCliente;
                        System.out.println("Recebido tentativa de cadastro");
                        System.out.println(
                                "\nNome: " + cadastroRecebidoDoCliente.getNome() +
                                        "\nCpf: " + cadastroRecebidoDoCliente.getCpf() +
                                        "\nIdade: " + cadastroRecebidoDoCliente.getIdade() +
                                        "\nSenha: " + cadastroRecebidoDoCliente.getSenha());
                        try{
                            PatientDAO.cadastrarNovoUsuario(new Patient(
                                    cadastroRecebidoDoCliente.getNome(),
                                    cadastroRecebidoDoCliente.getCpf(),
                                    cadastroRecebidoDoCliente.getIdade(),
                                    cadastroRecebidoDoCliente.getSenha()
                                    ));
                            transmissor.writeObject(new RespostaDoServidor("Usuário cadastrado!","ok"));
                        } catch (Exception e){
                            transmissor.writeObject(new RespostaDoServidor("Erro ao cadastrar usuário","erro"));
                            System.out.println(e);
                        }
                    }
                    if (recebidoDoCliente instanceof ModeloDeLogin){
                        ModeloDeLogin loginRecebidoDoCliente = (ModeloDeLogin) recebidoDoCliente;
                        System.out.println("Recebido tentativa de Login");
                        System.out.println(loginRecebidoDoCliente.getCpf());
                        try{
                            boolean isSignUp = PatientDAO.checarSeUsuarioJaEstaRegistrado(loginRecebidoDoCliente.getCpf());
                            if(isSignUp){
                                System.out.println("Sucesso - Encontrado");
                                String nomeDoUsuarioLogado = PatientDAO.nomeDoUsuarioRegistrado;

                                transmissor.writeObject(new RespostaDoServidor(nomeDoUsuarioLogado,"ok"));
                            } else{
                                System.out.println("Erro - Nao encontrado");
                                transmissor.writeObject(new RespostaDoServidor("Usuário não encontrado","erro"));
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            System.out.println("Erro - #10");
                            transmissor.writeObject(new RespostaDoServidor("Erro interno","erro"));
                            }
                        }
                    if (recebidoDoCliente instanceof String){
                        String cpfRecebidoDoCliente = (String) recebidoDoCliente;
                        System.out.println("Recebido tentativa de carregar exames");
                        System.out.println(cpfRecebidoDoCliente);
                        try{
                            ArrayList<ModeloDeExames> exames = new ArrayList<>();
                            exames = ExamesDAO.checarExames(cpfRecebidoDoCliente);
                            if(exames != null){
                                System.out.println("Sucesso - Exames encontrados");
                                transmissor.writeObject(new RespostaDoServidor(exames,"ok"));
                            } else{
                                System.out.println("Erro - Nao encontrado");
                                transmissor.writeObject(new RespostaDoServidor("Exames não encontrados","erro"));
                            }
                        } catch (Exception e){
                            System.out.println("Erro - #10");
                            e.printStackTrace();
                            transmissor.writeObject(new RespostaDoServidor("Erro interno","erro"));
                        }
                    }
                    }
            } catch (Exception e){
                System.out.println(e);
                fecharTodasConexoes(socket, transmissor, receptor);
                break;
            }
        }
    }

    public void fecharTodasConexoes(Socket socket, ObjectOutputStream transmissor, ObjectInputStream receptor){
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

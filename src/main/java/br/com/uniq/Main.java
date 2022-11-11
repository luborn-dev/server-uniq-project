package br.com.uniq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

// THIS IS JUST FOR TEST
// ALL THE CODE BELOW WILL CHANGE
// THIS WILL BE OUR TEMPLATE TO START WORKING WITH THE SERVER SIDE.

public class Main {
    public static void main(String[] args) {
        try
        {
            ServerSocket pedido  = new ServerSocket (7777);

            // fio do telefone fixo preso na parede
            Socket conexao = pedido.accept(); // 1 ponta do fio

            BufferedReader receptor =
                    new BufferedReader (
                            new InputStreamReader(
                                    conexao.getInputStream ()));

            String texto;

            do
            {
                texto = receptor.readLine ();
                System.out.println (texto);
            }
            while (!texto.equalsIgnoreCase("FIM"));

            receptor.close();
            conexao.close();

            pedido.close();
        }
        catch (Exception erro)
        {
            System.err.println (erro.getMessage());
        }
    }
}
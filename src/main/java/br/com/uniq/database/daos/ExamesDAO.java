package br.com.uniq.database.daos;

import br.com.uniq.ModeloDeExames;
import br.com.uniq.database.ConnectionFactory;
import br.com.uniq.database.dbos.Patient;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ExamesDAO {

    public static String cpfDoUsuario;

    public static ArrayList<ModeloDeExames> checarExames(String cpf) throws Exception {
        if (cpf == null) {
            throw new Exception("Paciente não fornecido");
        }
        if (cpf.length() != 11) {
            throw new Exception("CPF inválido!");
        }
        if (!checarSeUsuarioJaEstaRegistrado(cpf)) {
            throw new Exception("Paciente não encontrado, insira outro CPF");
        }

        Connection connection = ConnectionFactory.getConnection();

        ArrayList<ModeloDeExames> exames = null;
        try {
            String sql;
            sql = "SELECT * from EXAMES " +
                    "WHERE CPF_Paciente = ? ";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, cpf);
                ResultSet resultSet = ps.executeQuery();
                exames = new ArrayList<>();
                while (resultSet.next()) {
                    String nome_medico = resultSet.getString("Doutor");
                    String espec_medico = resultSet.getString("Especializacao_Doutor");
                    String tipo_exame = resultSet.getString("Tipo_Exame_Area");
                    Date data_realizacao_exame = resultSet.getDate("Data_Hora_Exame");
                    String conclusao = resultSet.getString("Conclusao");
                    String status = resultSet.getString("Status");
                    String nomeDaClinica = resultSet.getString("Clinica");
                    String cpfPaciente = resultSet.getString("CPF_Paciente");
                    exames.add(new ModeloDeExames(nome_medico, espec_medico, tipo_exame,
                            data_realizacao_exame,conclusao,status,nomeDaClinica,cpfPaciente));
                }
            }
            System.out.println("Paciente encontrado com sucesso!");
        } catch (Exception e) {
            throw new Exception("Erro ao encontrar paciente: " + cpf);
        }
        System.out.println(exames);
        return exames;
    }

    public static boolean checarSeUsuarioJaEstaRegistrado(String cpf) throws Exception {
        if (cpf == null) {
            throw new Exception("CPF não inserido.");
        }
        if (cpf.length() != 11) {
            throw new Exception("CPF inválido!");
        }
        boolean jaRegistrado = false;
        Connection connection = ConnectionFactory.getConnection();

        String sql;
//        PARA CONEXAO CLOUD
//        sql = "SELECT NOME_Paciente, CPF_Paciente from [dbo].[PACIENTES] WHERE CPF_Paciente = ?";
        sql = "SELECT NOME_Paciente, CPF_Paciente from PACIENTES WHERE CPF_Paciente = ?";

        if (connection != null) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jaRegistrado = true;
            }
            else {
                jaRegistrado = false;
            }
        }
        return jaRegistrado;
    }
}

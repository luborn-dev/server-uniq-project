package br.com.uniq.database.daos;

import br.com.uniq.database.ConnectionFactory;
import br.com.uniq.database.dbos.Patient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatientDAO {

    public static String nomeDoUsuarioRegistrado;

    public static void cadastrarNovoUsuario(Patient patient) throws Exception {
        if (patient == null) {
            throw new Exception("Paciente não fornecido");
        }
        if (patient.getCpf().length() != 11) {
            throw new Exception("CPF inválido!");
        }
        if (patient.getIdade() > 120) {
            throw new Exception("Idade inválida!");
        }
        if (checarSeUsuarioJaEstaRegistrado(patient.getCpf())) {
            throw new Exception("Paciente já registrado, insira outro CPF");
        }

        Connection connection = ConnectionFactory.getConnection();

        try {
            String sql;
//            PARA CONEXAO EM CLOUD
//            sql = "INSERT INTO [dbo].[PACIENTES] " +
//                    "(Nome_Paciente, CPF_Paciente, Idade, Senha) " +
//                    "VALUES (?, ?, ?, ?)";
            sql = "INSERT INTO PACIENTES " +
                    "(Nome_Paciente, CPF_Paciente, Idade, Senha) " +
                    "VALUES (?, ?, ?, ?)";
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, patient.getNome());
                ps.setString(2, patient.getCpf());
                ps.setInt(3, patient.getIdade());
                ps.setString(4,patient.getSenha());

                ps.executeUpdate();
                System.out.println("Paciente registrado com sucesso!");
            }
        } catch (Exception e) {
            throw new Exception("Erro ao registrar paciente: " + patient.getCpf());
        }
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
                nomeDoUsuarioRegistrado = rs.getString("NOME_Paciente");
                jaRegistrado = true;
            }
            else {
                jaRegistrado = false;
            }
        }
        return jaRegistrado;
    }
}

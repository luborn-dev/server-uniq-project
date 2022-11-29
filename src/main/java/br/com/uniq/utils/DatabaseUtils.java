package br.com.uniq.utils;

import br.com.uniq.database.ConnectionFactory;
import br.com.uniq.database.dbos.Patient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseUtils {

    public static void deletePatient(String cpfPatient) throws Exception {
        PatientUtils.validateCPF(cpfPatient);
        Connection connection = ConnectionFactory.getConnection();

        try {
            String sql;
//            PARA CONEXAO CLOUD
//            sql = "DELETE FROM [dbo].[Pacientes] " +
//                    "WHERE cpf_paciente = ?";
            sql = "DELETE FROM PACIENTES " +
                    "WHERE CPF_Paciente = ?";

            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, cpfPatient);
                ps.executeUpdate();
                connection.close();
            }
        } catch (Exception e) {
            throw new Exception("Não foi possível deletar o paciente");
        }
    }

    public static Patient getPatientByCPF(String cpfPatient) throws Exception {
        PatientUtils.validateCPF(cpfPatient);
        Connection connection = ConnectionFactory.getConnection();
        Patient patient = null;

        String sql;
//        PARA CONEXAO CLOUD
//        sql = "SELECT * FROM [dbo].[Pacientes] " +
//                "WHERE cpf_paciente = ?";
        sql = "SELECT * FROM PACIENTES " +
                "WHERE cpf_paciente = ?";

        if (connection != null) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, cpfPatient);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                patient = new Patient(
                        rs.getString("nome"),
                        rs.getString("cpf_paciente"),
                        rs.getInt("idade"),
                        rs.getString("senha"));
            }
        }
        return patient;
    }
}

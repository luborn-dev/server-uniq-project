package br.com.uniq;

import java.io.Serializable;

public class PedidoDeOperacao2 extends Comunicado{
    private String cpfPatient;
//    private String name;
//    private int age;
//    private String password;

    public PedidoDeOperacao2(String cpfPatient) {
        this.cpfPatient = cpfPatient;
//        this.name = name;
//        this.age = age;
//        this.password = password;
    }

    public String getCpfPatient() {
        return cpfPatient;
    }

//    public String getName() {
//        return name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public String getPassword() {
//        return password;
//    }

    @Override
    public String toString() {
        return "PedidoDeOperacao{" +
                "cpfPatient='" + cpfPatient;
    }
}

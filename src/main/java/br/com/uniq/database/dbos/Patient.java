package br.com.uniq.database.dbos;

import java.util.Date;
import java.util.Objects;

public class Patient {
    private String name;
    private Integer cpfPatient;
    private Date birthDate;
    private char genre;

    private String password;

    public String getName() {
        return name;
    }

    public Integer getCpfPatient() {
        return cpfPatient;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public char getGenre() {
        return genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCpfPatient(Integer cpfPatient) {
        this.cpfPatient = cpfPatient;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setGenre(char genre) {
        this.genre = genre;
    }

    public Patient(String name, Integer cpfPatient, Date birthDate, String password) {
        this.name = name;
        this.cpfPatient = cpfPatient;
        this.birthDate = birthDate;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return genre == patient.genre
            && Objects.equals(name, patient.name) && Objects.equals(cpfPatient, patient.cpfPatient)
            && Objects.equals(birthDate, patient.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cpfPatient, birthDate, genre);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", cpfPatient=" + cpfPatient +
                ", birthDate=" + birthDate +
                ", genre=" + genre +
                '}';
    }

}

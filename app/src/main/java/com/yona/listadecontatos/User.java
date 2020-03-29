package com.yona.listadecontatos;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String email;
    private String password;
    private String cpf;

    public User(String txtname, String txtemail, String txtpass, String txtcpf) {
        this.name = txtname;
        this.password = txtpass;
        this.email = txtemail;
        this.cpf = txtcpf;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getpass() {
        return password;
    }

    public void setpass(String password) {
        this.password = password;
    }

    public String getcpf() {
        return cpf;
    }

    public void setcpf(String city) {
        this.cpf = cpf;
    }

    @Override public String toString() {
        return  name;
    }
}

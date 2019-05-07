package apsprojchat;

import java.io.BufferedWriter;

public class ClienteAtivos {
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BufferedWriter getBfw() {
        return bfw;
    }

    public void setBfw(BufferedWriter bfw) {
        this.bfw = bfw;
    }

    private String nome;
    private BufferedWriter bfw;

    public ClienteAtivos(String nome, BufferedWriter bfw) {
        this.nome = nome;
        this.bfw = bfw;
    }


}

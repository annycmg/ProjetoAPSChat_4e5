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

    public String getLetrasUtilizadas() {
        return letrasUtilizadas;
    }

    private String letrasUtilizadas ="";

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    private int vida;
    private BufferedWriter bfw;

    public ClienteAtivos(String nome, BufferedWriter bfw) {
        this.nome = nome;
        this.bfw = bfw;
    }
    public String addletrasUtilizadas(char letra){
        letrasUtilizadas += letra;
        return letrasUtilizadas;
    }
    public void limpaletrasUtilizadas(){
        letrasUtilizadas = "";
    }

}

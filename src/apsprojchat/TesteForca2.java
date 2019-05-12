package apsprojchat;

import java.util.Random;
import java.util.Scanner;

public class TesteForca2 {


    // dicas

    public static String[] dicas = {"COMERCIALIZAÇÃO INTERNACIONAL INDEVIDA DE RECURSOS BIOLÓGICOS.", //dica: BIOPIRATARIA
            "COMPONENTES QUE SE DESINTEGRAM SEM RESULTADOS NEGATIVOS.", //dica: BIODEGRADAVEL
            "REAPROVEITAMENTO DE MATERIA-PRIMA.", // dica: RECICLAGEM
            "AÇÕES HUMANAS QUE VISAM SUPRIR NECESSIDADES ATUAIS DO SER HUMANO SEM COMPROMETER AS GERAÇÕES FUTURAS.", // dica: SUSTENTEBILIDADE
            "CAMPO DE ESTUDO DAS RELAÇÕES DOS SERES VIVOS ENTRE SI E COM O MEIO EM QUE VIVEM.", // dica: ECOLOGIA
            "PARTES QUE SOBRAM DE PROCESSOS DERIVADOS DE ATIVIDADE HUMANA E ANIMAL.", // dica: RESIDUO
            "CONJUNTO DE TODAS AS ESPÉCIES DE SERES VIVOS EXISTENTES NA BIOSFERA.", // dica: BIODIVERSIDADE
            "DEGRADAÇÃO DAS PROPRIEDADES FÍSICAS OU QUÍMICAS DO ECOSSISTEMAS.", // dica: POLUICAO
            "BACIA QUE ABRANGE 7 MILHÕES DE QUILÔMETROS QUADRADOS, DOS QUAIS 5 MILHÕES E MEIO QUILÔMETROS QUADRADOS SÃO COBERTOS PELA FLORESTA TROPICAL."}; // dica: AMAZONIA


    //dica = getNpalavra(); // dica dá um get no indice da palavra sorteada, isso une uma palavra com uma dica
    //doc = "DICA: " + getDicas()[dica]; // traz a dica referente a palavra sorteada
    // 9 palavras

    static String[] palavras = {"BIOPIRATARIA", "BIODEGRADAVEL", "RECICLAGEM", "SUSTENTABILIDADE", "ECOLOGIA",
            "RESIDUO", "BIODIVERSIDADE", "POLUICAO", "AMAZONIA"};

    public static Boolean gameStart = false;
    private static int numSorteado;

    public static String getDica(){
        return dicas[numSorteado];
    }
    public static String[] getDicas() {
        return dicas;
    }
    private static String sorteada;
    public static String getPalavraSorteada(){
        return palavras[numSorteado];
    }

    private static String letrasUtilizadas;

    public static String getLetrasUtilizadas() {
        return letrasUtilizadas;
    }
    public static void setLetrasUtilizadas(String letrasUtilizadas) {
        TesteForca2.letrasUtilizadas = letrasUtilizadas;
    }
    public static void IndiceSorteado(){
        numSorteado = new Random().nextInt(palavras.length);
    }

    public static String metodoForcaStart(){
        IndiceSorteado();
        sorteada = palavras[numSorteado];
        char[] acertos = new char[sorteada.length()];
        for (int i = 0; i < acertos.length; i++) {
            acertos[i] = 0;
        }
        letrasUtilizadas = "";
        for (int i = 0; i < sorteada.length(); i++) {
            System.out.print("_ ");
        }
         return ("\n"
                //+ "Você tem " + vidas + " vidas "
                //+ "\nletras utilizadas: " + letrasUtilizadas
                + "\nDica da palavra : " + getDica()
                + "\nQual letra vc deseja tentar ? ");
    }

    public static void metodoForca() {
        //scanner para que o usuario consiga entrar com dados
        Scanner in = new Scanner(System.in);

        // a variavel recebe o resultado do indice e faz com que seja igual ao indice da palavra
        String dicaSorteada = "DICA : " + getDicas()[numSorteado];

        //variavel recebe a palavra sorteada
        sorteada = (palavras[numSorteado]); //palavras na posição indice sorteado
        char[] acertos = new char[sorteada.length()];
        for (int i = 0; i < acertos.length; i++) {
            acertos[i] = 0;
        }

       letrasUtilizadas = "";

        char letra;
        boolean ganhou = false;
        int vidas = sorteada.length();

        for (int i = 0; i < sorteada.length(); i++) {
            System.out.print("_ ");
        }
        System.out.println("\n");


        //System.out.println(sorteada); // imprime a palavra pra testar

        do {

            System.out.println("\n"
                    + "Você tem " + vidas + " vidas "
                    + "\nletras utilizadas: " + letrasUtilizadas
                    + "\nDica da palavra : " + dicaSorteada
                    + "\nQual letra vc deseja tentar ? ");
            letra = in.next().toUpperCase().charAt(0);
            letrasUtilizadas += "" + letra;

            boolean perdeVida = true;

            for (int i = 0; i < sorteada.length(); i++) {
                // verifica se a letra digitada
                // é igual a letra da palavra sorteada
                // na posição i

                if (letra == sorteada.charAt(i)) {
                    //System.out.println("TEM ESSA LETRA na posicao" + i);
                    acertos[i] = 1;
                    perdeVida = false;
                }
            }

            if (perdeVida)
                vidas--;

            System.out.println("\n");
            ganhou = true;
            for (int i = 0; i < sorteada.length(); i++) { //esse for faz com que pegue o tamanho da palabra sorteada e incremente
                if (acertos[i] == 0) {
                    System.out.print("_ ");
                    ganhou = false;
                } else {
                    System.out.println(" " + sorteada.charAt(i) + " ");
                }
            }
            System.out.println("\n");

        } while (!ganhou && vidas > 0);

        if (vidas != 0) {
            System.out.println("\nParabéns você ganhou ! ");
        } else {
            System.out.println("\nVocê perdeu !");
            System.out.println("A palavra era " + sorteada);
        }

    }


    public static void main(String[] args) {
        metodoForca();
    }
}


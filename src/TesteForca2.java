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


    public static void metodoForca() {


        //cria objeto que randomiza os indices
        Random random = new Random();

        //scanner para que o usuario consiga entrar com dados
        Scanner in = new Scanner(System.in);

        // 9 palavras
        String[] palavras = {"BIOPIRATARIA", "BIODEGRADAVEL", "RECICLAGEM", "SUSTENTABILIDADE", "ECOLOGIA",
                "RESIDUO", "BIODIVERSIDADE", "POLUICAO", "AMAZONIA"};


        //recebe na variavel o tamanho do vetor de palabras que é 9
        int quantPalavras = palavras.length;

        //a variavel recebe um valor int randomizado do indice de uma das palavras
        int indiceSorteado = random.nextInt(quantPalavras);

        int dica = indiceSorteado;

        String dicaSorteada = "DICA : " + getDicas()[dica];

        //variavel recebe a palavra sorteada
        String sorteada = (palavras[indiceSorteado]); //palavras na posição indice sorteado

        char[] acertos = new char[sorteada.length()];
        for (int i = 0; i < acertos.length; i++) {
            acertos[i] = 0;
        }

        String letrasUtilizadas = "";

        char letra;
        boolean ganhou = false;
        int vidas = sorteada.length();


        for (int i = 0; i < sorteada.length(); i++) {
            System.out.println("_");
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
                    System.out.println("\t_");
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

    public static String[] getDicas() {
        return dicas;
    }


    public static void main(String[] args) {
        metodoForca();
    }
}



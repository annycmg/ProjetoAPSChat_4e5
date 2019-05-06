/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apsprojchat;

/**
 *
 * @author Anny
 */
public class Forca {
    private static int INICIO = 0;
    private static int TENTATIVA = 1;
    private static int DECISAO = 3;
    private String palavravez = "";
    int npalavra, dica, tamanho, letra = 0;
    private int state = INICIO;
    String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
        "Y", "Z"};
     
    String[] dicas = {"COMERCIALIZAÇÃO INTERNACIONAL INDEVIDA DE RECURSOS BIOLÓGICOS.", //dica: BIOPIRATARIA
        "COMPONENTES QUE SE DESINTEGRAM SEM RESULTADOS NEGATIVOS.", //dica: BIODEGRADAVEL
        "REAPROVEITAMENTO DE MATERIA-PRIMA.", // dica: RECICLAGEM
        "AÇÕES HUMANAS QUE VISAM SUPRIR NECESSIDADES ATUAIS DO SER HUMANO SEM COMPROMETER AS GERAÇÕES FUTURAS.", // dica: SUSTENTEBILIDADE
        "CAMPO DE ESTUDO DAS RELAÇÕES DOS SERES VIVOS ENTRE SI E COM O MEIO EM QUE VIVEM.", // dica: ECOLOGIA
        "PARTES QUE SOBRAM DE PROCESSOS DERIVADOS DE ATIVIDADE HUMANA E ANIMAL.", // dica: RESIDUO
        "CONJUNTO DE TODAS AS ESPÉCIES DE SERES VIVOS EXISTENTES NA BIOSFERA.", // dica: BIODIVERSIDADE
        "DEGRADAÇÃO DAS PROPRIEDADES FÍSICAS OU QUÍMICAS DO ECOSSISTEMAS.", // dica: POLUICAO
        "BACIA QUE ABRANGE 7 MILHÕES DE QUILÔMETROS QUADRADOS, DOS QUAIS 5 MILHÕES E MEIO QUILÔMETROS QUADRADOS SÃO COBERTOS PELA FLORESTA TROPICAL."}; // dica: AMAZONIA
    
    String[] palavras = {"BIOPIRATARIA", "BIODEGRADAVEL", "RECICLAGEM", "SUSTENTABILIDADE", "ECOLOGIA",
        "RESIDUO", "BIODIVERSIDADE", "POLUICAO", "AMAZONIA"}; // 9 palavras
        
    /**Retorna mensagens que são enviadas ao cliente, variavel theOutput
    * @param como parametro a entrada do usuário pelo teclado
    * @return theOutput
    */
    public String Inserir(String Input) {
        String theOutput = null;
        if (state == INICIO) {
            npalavra = (int) (Math.random() * 9); // indice da palavra sorteada palavra
            dica = getNpalavra();
            //letra = getNletra();
            palavravez = getPalavras()[getNpalavra()]; // palavra cujo indice foi sorteado
            tamanho = getPalavravez().length(); // tamanho da palavra
            theOutput = "Qual a palavra? DICA: " + getDicas()[dica];
            state = TENTATIVA;
        } else if (state == TENTATIVA) {
            if (Input.equalsIgnoreCase(getPalavravez())) {
                theOutput = "PARABENS, VOCE ACERTOU! Deseja jogar novamente? (S/N)";
                state = DECISAO;
            } else {
                theOutput = "VOCE FOI ENFORCADO. A palavra era: " + getPalavravez() + ". Quer jogar novamente? (S/N)";
                state = DECISAO;
            }    
        } else if (state == DECISAO) {
            if (Input.equalsIgnoreCase("S")) {
                theOutput = "PRESSIONE UMA TECLA PARA COMECAR";
                state = INICIO;
            } else {
                theOutput = "TCHAU";
            }
        }
        return theOutput;
    }
    
    // Retorna a palavra que deve ser adivinhada  
    public String getPalavravez() {
        return palavravez;
    }
   
    // Retorna o indice da palavra sorteada, dentre as 9 já cadastradas
    public int getNpalavra() {
        return npalavra;
    }
   
    // Retorna a dica referente a palavara sorteada
    public String[] getDicas() {
        return dicas;
    }
    
    // Retorna o vetor de palavras
    public String[] getPalavras() {
        return palavras;
    }
}

package apsprojchat;

/**
 *
 * @author Anny
 */
public class Forca {
    private static int INICIO = 0;
    private static int TENTATIVA = 1;
    private static int DECISAO = 3;
    private static boolean gameStart = false;
    private String palavravez = ""; // palavra que deve ser adivinhada
    private int npalavra,  dica,  tamanho = 0;
    private int state = INICIO;
    private String[] sensor; // ve quantas letras tem na palavra sorteada e a converte em "_" 
    private String[] dicas = {"COMERCIALIZAÇÃO INTERNACIONAL INDEVIDA DE RECURSOS BIOLÓGICOS.", //dica: BIOPIRATARIA
        "COMPONENTES QUE SE DESINTEGRAM SEM RESULTADOS NEGATIVOS.", //dica: BIODEGRADAVEL
        "REAPROVEITAMENTO DE MATERIA-PRIMA.", // dica: RECICLAGEM
        "AÇÕES HUMANAS QUE VISAM SUPRIR NECESSIDADES ATUAIS DO SER HUMANO SEM COMPROMETER AS GERAÇÕES FUTURAS.", // dica: SUSTENTEBILIDADE
        "CAMPO DE ESTUDO DAS RELAÇÕES DOS SERES VIVOS ENTRE SI E COM O MEIO EM QUE VIVEM.", // dica: ECOLOGIA
        "PARTES QUE SOBRAM DE PROCESSOS DERIVADOS DE ATIVIDADE HUMANA E ANIMAL.", // dica: RESIDUO
        "CONJUNTO DE TODAS AS ESPÉCIES DE SERES VIVOS EXISTENTES NA BIOSFERA.", // dica: BIODIVERSIDADE
        "DEGRADAÇÃO DAS PROPRIEDADES FÍSICAS OU QUÍMICAS DO ECOSSISTEMAS.", // dica: POLUICAO
        "BACIA QUE ABRANGE 7 MILHÕES DE QUILÔMETROS QUADRADOS, DOS QUAIS 5 MILHÕES E MEIO QUILÔMETROS QUADRADOS SÃO COBERTOS PELA FLORESTA TROPICAL."}; // dica: AMAZONIA        
    private String[] palavras = {"BIOPIRATARIA", "BIODEGRADAVEL", "RECICLAGEM", "SUSTENTABILIDADE", "ECOLOGIA",
        "RESIDUO", "BIODIVERSIDADE", "POLUICAO", "AMAZONIA"};    
    
    /**Retorna a palavra que deve ser adivinhada
    * @return palavravez
    */
    public String getPalavravez() {
        return palavravez;
    }
    /**Retorna o indice da palavra sorteada
    * @return npalavra
    */
    public int getNpalavra() {
        return npalavra;
    }
    /**Retorna a dica referente a palavara
    * @return dicas
    */
    public String[] getDicas() {
        return dicas;
    }
    /**Retorna o vetor de palavras
    * @return palavras
    */

    public boolean isGameStart() {
        return gameStart;
    }

    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    public String[] getPalavras() {
        return palavras;
    }
   
    public String processInput(String doc ) {
        //*** Parte de randomização das palavras/dicas e transformação da palavra sorteada em "_ _ _"
        if (state == INICIO) {
            npalavra = (int) (Math.random() * 9); //indice da palavra sorteada palavra, 9 = qtd de palavras
            dica = getNpalavra(); // dica dá um get no indice da palavra sorteada, isso une uma palavra com uma dica
            palavravez = getPalavras()[getNpalavra()]; // palavra cujo indice foi sorteado
            //doc.insertString(doc, "Escolha uma letra ou Adivinhe a palavra? \r\n" , cen);

            doc = "DICA: " + getDicas()[dica]; // traz a dica referente a palavra sorteada
            tamanho = getPalavravez().length(); // tamanho da palavra
            for (int i = 0; i < 17; i++){ // 16 é o número máximo de letras numa palavra
                sensor[i] = "_ "; // converte as letras da palavra em "_"
            }
            state = TENTATIVA;         
        //*** Parte de randomização das palavras/dicas   
            
        } else if (state == TENTATIVA) {
            if (doc.equalsIgnoreCase(getPalavravez())) {
                doc = "PARABENS, VOCE ACERTOU! Deseja jogar novamente? (S/N)";
                state = DECISAO;
            } else {
                doc = "VOCE FOI ENFORCADO. A palavra era: " + getPalavravez() + ". Quer jogar novamente? (S/N)";
                state = DECISAO;            
            }
        } else if (state == DECISAO) {
            if (doc.equalsIgnoreCase("S")) {
                doc = "PRESSIONE UMA TECLA PARA COMECAR";
                state = INICIO;
            } else {
                doc = "TCHAU";
            }
        }
        return doc;
    }
 }
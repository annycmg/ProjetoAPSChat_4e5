
package apsprojchat;

/**
 * @author Anny 
 */
   
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;  
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread {
    
    public static void main(String[] args) {
        try{
            //Tela inicial 
            if (JOptionPane.showConfirmDialog(null, "\n\n\n        Vamos iniciar o Chat do Jogo da Forca?                 \n\n\n\n",
                    "Chat do Jogo da Forca", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // yes option
            }else {
                System.exit(0);
            }
            //Criação dos objetos para instanciar/iniciar o servidor
            JLabel lblMessage = new JLabel("\n\n\n         Porta do Servidor:");
            JTextField txtPorta = new JTextField("5151"); // nº de porta padrão
            Object[] texts = {lblMessage, txtPorta };  
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
            clientes = new ArrayList<ClienteAtivos>();
            JOptionPane.showMessageDialog(null,"\n\n\n          Servidor foi ativado na porta: "+         
            txtPorta.getText() + "                 \n\n\n");
    
            while(true){
                System.out.println("Aguardando nova conexão..."); // Console
                Socket con = server.accept();
                System.out.println("Novo cliente conectado..."); // Console
                Thread thread = new Servidor(con);
                thread.start();   
            }
        }catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    private static ArrayList<ClienteAtivos>clientes; // variavel arraylist, que comporta um ou vários usuários (multithread)        
    private TesteForca2 testeForca2;
    private static ServerSocket server; 
    private BufferedReader bfr;
    private String jogador;
    private InputStreamReader inReader;  
    private Socket conexao;
    private InputStream input;  

    // DEBUG the APP:
    // Debug Servidor primeiro e então debug Cliente depois quantas vezes quiser para ser multithread.
    // Para a aplicação rodar todos os usuários têm que ter o MESMO NÚMERO de IP e PORTA,
    // caso contrário a conexão não é executada.
    //===============================================================================================
    // Requisito de S.O: Comando NETSTAT --->  Abrir prompt de comando e digitar: [netstat -a]
    // Aparecerá o número de IP e de porta usado na aplicação com conexão status ESTABILISHED
    //=======================================================================================
    // Passos para um chat via TCP/IP:
    // Passo 1: é preciso rodar o servidor antes do cliente, pois o servidor fica esperando o cliente ser conectado.
    // Passo 2: o servidor escuta o cliente através da declaração de IP e Porta.
    // Passo 3: para estabelecer uma conexão, o cliente precisa saber a Porta do Servidor.
    // Passo 4: quando a aplicação do cliente é iniciada, é criada uma conexão com o servidor.  
    // Passo 5: Depois que a conexão é bem sucedida o cliente eo servidor podem enviar e receber mensagens.

    public void run(){ // verifica se há alguma mensagem/conexão nova
        String message = "";
        ClienteAtivos cliente = null;
        BufferedWriter bfw = null;
        try{
            OutputStream ou =  this.conexao.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            bfw = new BufferedWriter(ouw);
            
            jogador = bfr.readLine(); 
            System.out.println(jogador+" entrou!");
            
            cliente = new ClienteAtivos(jogador, bfw);
            clientes.add(cliente);
            testeForca2.jogadores.add(jogador);
            enviarParaTodos(bfw,"Usuario: "+jogador+" se conectou no chat"); // Quando um novo cliente se conectar 
            enviarParaTodos(bfw,"use !start para iniciar o game");
                  
            while(!"Sair".equalsIgnoreCase(message) && message != null) // Se mensagem = !Sair e !=null --> Chat/Jogo continua ativo
            {           
                message = bfr.readLine();
                if(message.contains("!start")){
                    if(testeForca2.gameStart){
                        enviarTodos("Jogo já foi iniciado!\r\n");
                    }else{
                        testeForca2.gameStart = true;
                        enviarTodos("Jogo foi iniciado!");
                        enviarTodos(testeForca2.metodoForcaStart()+"Você tem " + testeForca2.vidas.get(jogador) + " vidas \n");
                    }
                }else if(message.contains("!restart")){
                    for(ClienteAtivos ca : clientes){
                        ca.limpaletrasUtilizadas();
                    }
                    enviarTodos("Jogo foi Reiniciado!");
                    enviarTodos(testeForca2.metodoForcaStart()+"Você tem " + testeForca2.vidas.get(jogador) + " vidas \n");
                }
                else if( message.length() > 1 && message.substring(0,1).contains("!")){
                    if(testeForca2.gameStart != false) {
                        if (message.length() == 2) {
                            char let = message.charAt(1);
                            String letra = testeForca2.InsereLetra(let,cliente);
                            if(letra.contains("Parabens você conseguiu")){
                                enviarTodos("Parabens o jogador: "+cliente.getNome() +" ganhou!!");
                                testeForca2.gameStart = false;
                            }else if(letra.contains("Você perdeu!")){
                                enviarTodos("O jogador: "+cliente.getNome()+" perdeu!");
                            }
                            else{
                                enviarParaTodos(bfw,"Jogou > "+letra);
                                enviarOutros(letra,bfw);
                            }
                        }
                    }else{
                        enviarOutros("Jogo não foi iniciado",bfw);
                    }
                }
                else {
                    enviarParaTodos(bfw, message);
                    //System.out.println(msg);
                }
            }
        }catch (Exception e) {
            if(e.getMessage().equals("Connection reset")){
                System.out.println("Usuario "+jogador+" saiu!"); // Se o cliente digitar ou clicar 'Sair'
                try {
                    sendToAllExit( "Usuario "+jogador+" saiu!");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }else
            e.printStackTrace();
        }                       
    }
    public void Enviar(String message){System.out.println(message);}
    
     public Servidor(Socket con){ // conexão do Servidor com o Cliente
        this.conexao = con;
        try {
            input  = con.getInputStream();
            inReader = new InputStreamReader(input); //objeto do tipo BufferedReader, 
            bfr = new BufferedReader(inReader);      //que aponta para o stream do cliente socket
            testeForca2 = new TesteForca2();         //e se conecta com a classe testeforca2
        } catch (IOException e) {
            e.printStackTrace();
        }                          
    }
    
    public void enviarOutros(String message,BufferedWriter bfw){
        try{
            bfw.write(message+"\r\n");
            bfw.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void enviarTodos(String msg){
        for(ClienteAtivos ca : clientes){
            try{
                ca.getBfw().write(msg+"\r\n");
                ca.getBfw().flush();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void sendToAllExit(String msg){
        ClienteAtivos removeCA = null;
        for(ClienteAtivos ca : clientes){
            try {
                ca.getBfw().write(msg+"\r\n");
                ca.getBfw().flush();
            }catch (Exception e)
            {
                e.getMessage();
                removeCA = ca;
            }
        }
        if(removeCA != null)
            clientes.remove(removeCA);
    }
    
    public void enviarParaTodos(BufferedWriter bwSaida, String msg) throws  IOException 
    { // quando uma msg é enviada por um cliente, ela é replicada para todos os outros da Thread
        BufferedWriter bwS;
        for(ClienteAtivos ca : clientes){
            bwS = ca.getBfw();

            if(!(bwSaida == bwS)){
                ca.getBfw().write(jogador + " disse -> " + msg+"\r\n");
                ca.getBfw().flush(); 
            }else
                Enviar(jogador + " disse -> " + msg);
        }          
    }
    
}
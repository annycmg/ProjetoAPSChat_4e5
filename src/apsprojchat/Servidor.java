
package apsprojchat;

/**
 *
 * @author Anny
 * 
 * 
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
    private static ArrayList<ClienteAtivos>clientes; // variavel arraylist, que comporta um ou vários usuários (multithread)        
    private static ServerSocket server; 
    private String nome;
    private Socket conexao;
    private InputStream input;  
    private InputStreamReader inReader;  
    private BufferedReader bfr;
    private TesteForca2 testeForca2;

    // DEBUG O APP:
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

    public void run(){ // verifica se há alguma mensagem/conexão nova
        String msg = "";
        BufferedWriter bfw = null;
        ClienteAtivos cliente = null;
        try{
            OutputStream ou =  this.conexao.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            bfw = new BufferedWriter(ouw);
            nome = bfr.readLine(); 
            System.out.println(nome+" entrou!");
            cliente = new ClienteAtivos(nome, bfw);
            clientes.add(cliente);
            sendToAll(bfw,"Usuario: "+nome+" se conectou no chat"); // Quando um novo cliente se conectar 
            sendToAll(bfw,"use !start para iniciar o game");
                  
            while(!"Sair".equalsIgnoreCase(msg) && msg != null) // Se mensagem = !Sair e !=null --> Chat/Jogo continua ativo
            {           
                msg = bfr.readLine();
                if(msg.contains("!start")){
                    if(testeForca2.gameStart){
                        sendToAlls("Jogo já foi iniciado!\r\n");
                    }else{
                        testeForca2.gameStart = true;
                        sendToAlls("Jogo foi iniciado!");
                        sendToAlls(testeForca2.metodoForcaStart());
                    }
                }
                else if(msg.substring(0,1).contains("!")){
                    if(testeForca2.gameStart != false) {
                        if (msg.length() == 2) {
                            String letra = testeForca2.InsereLetra(msg.charAt(1));
                            if(letra.contains("Parabens você conseguiu")){
                                sendToAlls("Parabens o jogador: "+cliente.getNome() +" ganhou!!");
                                testeForca2.gameStart = false;
                            }else{
                                sendToClient(letra, bfw);
                            }
                        }
                    }
                }
                else {
                    sendToAll(bfw, msg);
                    //System.out.println(msg);
                }
            }
        }catch (Exception e) {
            if(e.getMessage().equals("Connection reset")){
                System.out.println("Usuario "+nome+" saiu!"); // Se o cliente digitar ou clicar 'Sair'
                try {
                    sendToAllExit( "Usuario "+nome+" saiu!");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }else
            e.printStackTrace();
        }                       
    }
    public void sendConsole(String message){
        System.out.println(message);
    }
    public void sendToClient(String msg,BufferedWriter bfw){
        try{
            bfw.write(msg+"\r\n");
            bfw.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void sendToAlls(String msg){
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
    public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException 
    { // quando uma msg é enviada por um cliente, ela é replicada para todos os outros da Thread
        BufferedWriter bwS;
        for(ClienteAtivos ca : clientes){
            bwS = ca.getBfw();

            if(!(bwSaida == bwS)){
                ca.getBfw().write(nome + " disse -> " + msg+"\r\n");
                ca.getBfw().flush(); 
            }else
                sendConsole(nome + " disse -> " + msg);
        }          
    }
    
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
            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("5151"); // nº de porta padrão
            Object[] texts = {lblMessage, txtPorta };  
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
            clientes = new ArrayList<ClienteAtivos>();
            JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+         
            txtPorta.getText());
    
            while(true){
                System.out.println("Aguardando conexão..."); // Console
                Socket con = server.accept();
                System.out.println("Cliente conectado..."); // Console
                Thread thread = new Servidor(con);
                thread.start();   
            }
        }catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
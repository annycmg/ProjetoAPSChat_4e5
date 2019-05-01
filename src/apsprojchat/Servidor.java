
package apsprojchat;

/**
 *
 * @author Anny
 * 
 * Debug Servidor first and then debug Cliente as much as you need.
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
    private static ArrayList<BufferedWriter>clientes; // variavel arraylist, que comporta um ou vários clientes (multithread)        
    private static ServerSocket server; 
    private String nome;
    private Socket conexao;
    private InputStream input;  
    private InputStreamReader inReader;  
    private BufferedReader bfr;

    //DEBUG O APP:
    //Debug Servidor primeiro e então debug Cliente depois quants vezes quiser.
    //==================================================================

    // Passos para um chat via TCP/IP:
    // Step 1: In any Client/Server Application, we need to run the server before the client, because the server keeps waiting for the client to be connected.
    // Step 2: Server keeps listening for the client on an assigned IP & Port
    // Step 3: For establishing connection client must know the IP & Port of the server.
    // Step 4: When we start Client Application, It creates a connection to the server.
    // Step 5: After the Successful connection Client & Server Applications can send & receive messages.

    public Servidor(Socket con){ // conexão do Servidor com o Cliente
        this.conexao = con;
        try {
            input  = con.getInputStream();
            inReader = new InputStreamReader(input);
            bfr = new BufferedReader(inReader);
        } catch (IOException e) {
            e.printStackTrace();
        }                          
    }

    public void run(){ // verifica se há alguma mensagem nova
        try{                                   
            String msg;
            OutputStream ou =  this.conexao.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw); 
            clientes.add(bfw);
            nome = msg = bfr.readLine();       
            while(!"Sair".equalsIgnoreCase(msg) && msg != null)
            {           
                msg = bfr.readLine();
                sendToAll(bfw, msg);
                System.out.println(msg);                                              
            }
        }catch (Exception e) {
            e.printStackTrace();
        }                       
    }

    public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException 
    { // quando uma msg é enviada por um cliente, ela é replicada para todos os outros da Thread
        BufferedWriter bwS;
        for(BufferedWriter bw : clientes){
            bwS = (BufferedWriter)bw;
            if(!(bwSaida == bwS)){
                bw.write(nome + " -> " + msg+"\r\n");
                bw.flush(); 
            }
        }          
    }
    
    public static void main(String[] args) {
        try{
            //Criação dos objetos para instanciar/iniciar o servidor
            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("12345"); // nº de porta de porta padrão
            Object[] texts = {lblMessage, txtPorta };  
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
            clientes = new ArrayList<BufferedWriter>();
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
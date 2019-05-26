package apsprojchat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.text.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Cliente extends JFrame implements ActionListener, KeyListener{
     public static void main(String []args) throws IOException{
        try {
            Cliente app = new Cliente();
            app.connect();
            app.listen();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
    
private static final long serialVersionUID = 1L;
// variaveis do chat
private JTextPane texto;
private SimpleAttributeSet esq;
private SimpleAttributeSet dir;
private SimpleAttributeSet cen;
private StyledDocument doc;
// variaveis da interface
private JButton Sair;
private JTextField mensagem;
private JButton Enviar;
private JLabel Jogo;
private JLabel Mensagem;
private JPanel Painel;
// variaveis da conexão cliente
private OutputStream out;
private BufferedWriter bfwriter;
private JTextField IP;
private Writer writer; 
private JTextField Porta;
private Socket socket;
private JTextField Nome;


    public Cliente(){
        try {
            // Interface do chat
            Painel = new JPanel();
            texto = new JTextPane(); // LARGURA/ALTURA txtPane do Jogo
            texto.setPreferredSize(new Dimension(600,545));
            texto.setEditable(false);
            doc = texto.getStyledDocument(); // Texts do jogo da forca
            esq = new SimpleAttributeSet();  // Texts à esquerda
            StyleConstants.setAlignment(esq, StyleConstants.ALIGN_LEFT);
            StyleConstants.setForeground(esq, Color.BLACK);
            dir = new SimpleAttributeSet();  // Texts à direita
            StyleConstants.setAlignment(dir, StyleConstants.ALIGN_RIGHT);
            StyleConstants.setForeground(dir, Color.blue);
            cen = new SimpleAttributeSet();  // Texts ao centro
            StyleConstants.setAlignment(cen,StyleConstants.ALIGN_CENTER);
            StyleConstants.setForeground(cen,Color.red);
            doc.setParagraphAttributes(doc.getLength(),1,cen,false);
            doc.insertString(doc.getLength(), "BEM VINDO AO JOGO DA FORCA!!! \r\n", cen);
            Style style = doc.getStyle(StyleContext.DEFAULT_STYLE);
            StyleConstants.setFontSize(style, 19);
            
            // Interface conexão do cliente JOptionPane
            JLabel lblMessage = new JLabel("\n\n\n           Entre com NickName, IP e Porta                  \n\n\n");
            Nome = new JTextField("NickName"); // <--- nickname padrão
            IP = new JTextField("0.0.0.0"); // <--- IP padrão
            Porta = new JTextField("5151"); // <--- porta padrão
            Object[] texts = {lblMessage, Nome, IP, Porta};
            JOptionPane.showMessageDialog(null, texts);
            
            //Componentes da interface do chat
            mensagem = new JTextField(43); // largura txtField Mensagem
            mensagem.setFont(new Font("Dialog", Font.BOLD, 16)); // Tamanho da fonte no txtField
            Jogo = new JLabel("JOGO DA FORCA");           
            Mensagem = new JLabel("MENSAGEM");
            Enviar = new JButton("Enviar");
            Sair = new JButton("Sair");
            Enviar.addActionListener(this);
            Sair.addActionListener(this);
            Enviar.addKeyListener(this);
            mensagem.addKeyListener(this);
            JScrollPane scroll = new JScrollPane(texto);
            Painel.setBackground(new Color(179,220,184));
            Painel.add(Jogo);
            Painel.add(scroll);
            Painel.add(Mensagem);
            Painel.add(mensagem);
            Painel.add(Enviar);
            Painel.add(Sair);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            texto.setBorder(BorderFactory.createLineBorder(new Color(119,164,125),3));
            mensagem.setBorder(BorderFactory.createLineBorder(new Color(119,164,125),3));
            setTitle(Nome.getText());
            setContentPane(Painel);
            setSize(680, 700); // largura/altura janela principal
            setLocation(700,5);
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);
            // Interface
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }
        
    public void connect() throws IOException{  // conexão do cliente com o socket servidor
        socket = new Socket(IP.getText(),Integer.parseInt(Porta.getText())); // Socktet recebe os parametros de IP e Porta
        out = socket.getOutputStream();         //Streams de 
        writer = new OutputStreamWriter(out);   //comunicaçõ
        bfwriter = new BufferedWriter(writer);
        bfwriter.write(Nome.getText()+"\r\n");
        bfwriter.flush();
    }
    
    //Enviar mensagem para o server socket
    public void enviarMensagem(String msg) throws IOException, BadLocationException { 
        if(msg.equals("Sair")){
            bfwriter.write("Desconectado \r\n"); 
            doc.setParagraphAttributes(doc.getLength(),1,dir,false);
            doc.insertString(doc.getLength(), "Desconectado \r\n", dir); // Se o cliente logado digitar Sair no chat
         
        }else{
            bfwriter.write(msg+"\r\n"); 
            doc.setParagraphAttributes(doc.getLength(),1,dir,false); // Se não a estrutura [Nome] + "disse ->" + mensage aparecerá
            doc.insertString(doc.getLength(), Nome.getText() + " disse ->  " + mensagem.getText()+" " + "\r\n", dir);

        }
        bfwriter.flush(); // obriga a descarregar todo o conteúdo da msg
        mensagem.setText("");        
    }
    
    // Receber mensagem do server
    public void listen() throws IOException,BadLocationException{ // Toda vez que é feito o input de uma mensagem 
        InputStream in = socket.getInputStream();                  // esse método é processado pelo servidor e envia
        InputStreamReader inr = new InputStreamReader(in);         // para todos os clientes conectados
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";
        String[] aux = null;
        while(!"Sair".equalsIgnoreCase(msg))       
            if(bfr.ready()){
                msg = bfr.readLine();
            if(msg.equals("Sair")) {
                doc.setParagraphAttributes(doc.getLength(),1,esq,false);
                doc.insertString(doc.getLength(), "Servidor caiu! \r\n", esq);
            }
            else{
                if(msg.contains("se conectou no chat")) {
                    aux = msg.split("Usuario:");
                    msg = aux[1];
                }
                if(msg.contains("use !start para iniciar"))
                    msg = "use !start para iniciar o game.";
                doc.setParagraphAttributes(doc.getLength(),1,esq,false);
                doc.insertString(doc.getLength(), " " + msg+"\r\n", esq);

            }
        }
    }
    
    public void sair() throws IOException,BadLocationException{ // desconctar o socket servidor
        enviarMensagem("Sair");
        writer.close();
        bfwriter.close();
        socket.close();
        out.close();
    }
    
    public void keyPressed(KeyEvent e) { // press ENTER para enviar mensagem           
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try {
                enviarMensagem(mensagem.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }                                                          
        }                       
    }

    public void keyReleased(KeyEvent arg0) {}
    public void keyTyped(KeyEvent arg0) {}
    
    public void actionPerformed(ActionEvent e) { // Controle de eventos: Enviar e Sair        
    try {
        if(e.getActionCommand().equals(Enviar.getActionCommand()))   
            enviarMensagem(mensagem.getText());
        else
        if(e.getActionCommand().equals(Sair.getActionCommand()))
            sair();
        }catch (Exception e1) {
            e1.printStackTrace();
        }                       
    }
}
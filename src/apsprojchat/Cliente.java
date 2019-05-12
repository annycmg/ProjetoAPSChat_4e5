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
private static final long serialVersionUID = 1L;
// variaveis do chat
private JTextPane texto;
private SimpleAttributeSet esq;
private SimpleAttributeSet dir;
private SimpleAttributeSet cen;
private StyledDocument doc;
// variaveis da interface
private JTextField mensagem;
private JButton btEnviar;
private JButton btSair;
private JLabel lblHistorico;
private JLabel lblMensagem;
private JPanel pnlConteudo;
// variaveis do cliente
private Socket socket;
private OutputStream out;
private Writer writer; 
private BufferedWriter bfwriter;
private JTextField txtIP;
private JTextField txtPorta;
private JTextField txtNome;

    public Cliente(){
        // Interface conexão do cliente
        try {
            JLabel lblMessage = new JLabel("Verificar!");
            txtIP = new JTextField("0.0.0.0"); // <--- IP padrão
            txtPorta = new JTextField("5151"); // <--- porta padrão
            txtNome = new JTextField("Cliente-Servidor"); // <--- nickname padrão
            Object[] texts = {lblMessage, txtIP, txtPorta, txtNome};
            JOptionPane.showMessageDialog(null, texts);
            pnlConteudo = new JPanel();

            // Textos do chat
            texto = new JTextPane(); // LARGURA/ALTURA txtArea Históric
            texto.setPreferredSize(new Dimension(600,545));
            texto.setEditable(false);
            doc = texto.getStyledDocument();
            esq = new SimpleAttributeSet();
            StyleConstants.setAlignment(esq, StyleConstants.ALIGN_LEFT);
            StyleConstants.setForeground(esq, Color.BLACK);
            dir = new SimpleAttributeSet();
            StyleConstants.setAlignment(dir, StyleConstants.ALIGN_RIGHT);
            StyleConstants.setForeground(dir, Color.blue);
            cen = new SimpleAttributeSet();
            StyleConstants.setAlignment(cen,StyleConstants.ALIGN_CENTER);
            StyleConstants.setForeground(cen,Color.red);
//            texto.setFont(new Font("Dialog", Font.BOLD, 14)); // Tamanho da fonte na txtArea
//            texto.setEditable(false);
//            texto.setBackground(new Color(240, 240, 240));
            doc.setParagraphAttributes(doc.getLength(),1,cen,false);
            doc.insertString(doc.getLength(), "BEM VINDO AO JOGO DA FORCA!!! \r\n", cen);
//          doc.insertString(doc.getLength(), "Escolha uma letra ou Adivinhe a palavra? \r\n" , cen);
            Style style = doc.getStyle(StyleContext.DEFAULT_STYLE);
            StyleConstants.setFontSize(style, 17);
            
            //Componentes da interface do chat
            mensagem = new JTextField(53); // largura txtField Mensagem
            mensagem.setFont(new Font("Dialog", Font.BOLD, 14)); // Tamanho da fonte no txtField
            lblHistorico = new JLabel("HISTÓRICO");           
            lblMensagem = new JLabel("MENSAGEM");
            btEnviar = new JButton("Enviar");
            btEnviar.setToolTipText("Enviar Mensagem");
            btSair = new JButton("Sair");
            btSair.setToolTipText("Sair do Chat");
            btEnviar.addActionListener(this);
            btSair.addActionListener(this);
            btEnviar.addKeyListener(this);
            mensagem.addKeyListener(this);
            JScrollPane scroll = new JScrollPane(texto);
            pnlConteudo.add(lblHistorico);
            pnlConteudo.add(scroll);
            pnlConteudo.add(lblMensagem);
            pnlConteudo.add(mensagem);
            pnlConteudo.add(btSair);
            pnlConteudo.add(btEnviar);
            pnlConteudo.setBackground(Color.LIGHT_GRAY);
            texto.setBorder(BorderFactory.createLineBorder(Color.BLUE,3));
            mensagem.setBorder(BorderFactory.createLineBorder(Color.BLUE,3));
            setTitle(txtNome.getText());
            setContentPane(pnlConteudo);
            setLocationRelativeTo(null);
            setResizable(false);
            setSize(680, 700); // largura/altura janela principal
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            // Interface
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }
    
    public void conectar() throws IOException{ // conexão do cliente com o socket servidor
        socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText())); // Socktet recebe os parametros de IP e Porta
        out = socket.getOutputStream();
        writer = new OutputStreamWriter(out);
        bfwriter = new BufferedWriter(writer);
        bfwriter.write(txtNome.getText()+"\r\n");
        bfwriter.flush();
    }
    
    public void enviarMensagem(String msg) throws IOException, BadLocationException { // envio de msg do cliente para o socket servidor
        if(msg.equals("Sair")){
            bfwriter.write("Desconectado \r\n"); // <--- Caso clique no evento Sair
            doc.setParagraphAttributes(doc.getLength(),1,dir,false);
            doc.insertString(doc.getLength(), "Desconectado \r\n", dir);
            //doc.setParagraphAttributes(doc.getLength(),1,esq,false);
            //texto.append("Desconectado \r\n");
        }else{
            bfwriter.write(msg+"\r\n");
//            texto.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//            texto.append( txtNome.getText() + " disse ->  " + mensagem.getText()+"\r\n");
            doc.setParagraphAttributes(doc.getLength(),1,dir,false);
            doc.insertString(doc.getLength(), txtNome.getText() + " disse ->  " + mensagem.getText()+" " + "\r\n", dir);

        }
        bfwriter.flush();
        mensagem.setText("");        
    }
    
    public void escutar() throws IOException,BadLocationException{ // recebe msg do servidor
        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
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
//                texto.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
//                texto.append(msg+"\r\n");
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
        bfwriter.close();
        writer.close();
        out.close();
        socket.close();
    }
    
    public void actionPerformed(ActionEvent e) { // Controle de eventos: Enviar e Sair        
    try {
        if(e.getActionCommand().equals(btEnviar.getActionCommand()))   
            enviarMensagem(mensagem.getText());
        else
        if(e.getActionCommand().equals(btSair.getActionCommand()))
            sair();
        }catch (Exception e1) {
            e1.printStackTrace();
        }                       
    }
    
    public void keyPressed(KeyEvent e) { // Opção press ENTER            
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try {
                enviarMensagem(mensagem.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }                                                          
        }                       
    }
    
    @Override
    public void keyReleased(KeyEvent arg0) {}
    
    @Override
    public void keyTyped(KeyEvent arg0) {}
    
    public static void main(String []args) throws IOException{
        try {
            Cliente app = new Cliente();
            app.conectar();
            app.escutar();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
}
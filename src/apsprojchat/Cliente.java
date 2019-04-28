package apsprojchat;

import java.awt.Color;
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
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Cliente extends JFrame implements ActionListener, KeyListener{
private static final long serialVersionUID = 1L;
private JTextArea texto;
private JTextField MsgTxt;
private JButton btnSend;
private JButton btnSair;
private JLabel lblHistorico;
private JLabel lblMsg;
private JPanel pnlContent;
private Socket socket;
private OutputStream out ;
private Writer writer; 
private BufferedWriter bfwriter;
private JTextField txtIP;
private JTextField txtPorta;
private JTextField txtNome;

    public Cliente() throws IOException{     
    // Interface
    JLabel lblMessage = new JLabel("Verificar!");
    txtIP = new JTextField("192.168.0.7"); // IP padrão
    txtPorta = new JTextField("12345"); // porta padrão
    txtNome = new JTextField("Cliente"); // nickname cliente padrão              
    Object[] texts = {lblMessage, txtIP, txtPorta, txtNome };  
    JOptionPane.showMessageDialog(null, texts);              
    pnlContent = new JPanel();
    texto = new JTextArea(10,20);
    texto.setEditable(false);
    texto.setBackground(new Color(240,240,240));
    MsgTxt = new JTextField(20);
    lblHistorico = new JLabel("Histórico");
    lblMsg = new JLabel("Mensagem");
    btnSend = new JButton("Enviar");
    btnSend.setToolTipText("Enviar Mensagem");
    btnSair = new JButton("Sair");
    btnSair.setToolTipText("Sair do Chat");
    btnSend.addActionListener(this);
    btnSair.addActionListener(this);
    btnSend.addKeyListener(this);
    MsgTxt.addKeyListener(this);
    JScrollPane scroll = new JScrollPane(texto);
    texto.setLineWrap(true);  
    pnlContent.add(lblHistorico);
    pnlContent.add(scroll);
    pnlContent.add(lblMsg);
    pnlContent.add(MsgTxt);
    pnlContent.add(btnSair);
    pnlContent.add(btnSend);
    pnlContent.setBackground(Color.LIGHT_GRAY);                                 
    texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
    MsgTxt.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));                    
    setTitle(txtNome.getText());
    setContentPane(pnlContent);
    setLocationRelativeTo(null);
    setResizable(false);
    setSize(250,300);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    // Interface
    }
    
    public void conectar() throws IOException{ // conexão do cliente com o socket servidor
    socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
    out = socket.getOutputStream();
    writer = new OutputStreamWriter(out);
    bfwriter = new BufferedWriter(writer);
    bfwriter.write(txtNome.getText()+"\r\n");
    bfwriter.flush();
    }
    
    public void enviarMensagem(String msg) throws IOException{ // envio de msg do cliente para o socket servidor                         
    if(msg.equals("Sair")){
      bfwriter.write("Desconectado \r\n");
      texto.append("Desconectado \r\n");
    }else{
      bfwriter.write(msg+"\r\n");
      texto.append( txtNome.getText() + " diz -> " + MsgTxt.getText()+"\r\n");
    }
    bfwriter.flush();
    MsgTxt.setText("");        
    }
    
    public void escutar() throws IOException{ // recebe msg do servidor 
                           
    InputStream in = socket.getInputStream();
    InputStreamReader inr = new InputStreamReader(in);
    BufferedReader bfr = new BufferedReader(inr);
    String msg = "";
                           
    while(!"Sair".equalsIgnoreCase(msg))
        
        if(bfr.ready()){
            msg = bfr.readLine();
        if(msg.equals("Sair"))
            texto.append("Servidor caiu! \r\n");
        else
         texto.append(msg+"\r\n");         
        }
    }
    
    public void sair() throws IOException{ // desconctar o socket servidor
    enviarMensagem("Sair");
    bfwriter.close();
    writer.close();
    out.close();
    socket.close();
    }
    
    public void actionPerformed(ActionEvent e) {
          
    try {
        if(e.getActionCommand().equals(btnSend.getActionCommand()))   
            enviarMensagem(MsgTxt.getText());
        else
        if(e.getActionCommand().equals(btnSair.getActionCommand()))
            sair();
        }catch (IOException e1) {
            e1.printStackTrace();
        }                       
    }
    
    public void keyPressed(KeyEvent e) {
                
    if(e.getKeyCode() == KeyEvent.VK_ENTER){
        try {
            enviarMensagem(MsgTxt.getText());
        } catch (IOException e1) {
            e1.printStackTrace();
        }                                                          
    }                       
}
    
    @Override
    public void keyReleased(KeyEvent arg0) {
    // TODO Auto-generated method stub               
    }
    
    @Override
    public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub               
    }
    
    public static void main(String []args) throws IOException{
               
    Cliente app = new Cliente();
    app.conectar();
    app.escutar();
    }
    
}
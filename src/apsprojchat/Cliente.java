package apsprojchat;
   
import java.awt.Color;
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
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
 
public class Cliente extends JFrame implements ActionListener, KeyListener{
private static final long serialVersionUID = 1L;
private JTextArea texto;
private JTextField mensagem;
private JButton btEnviar;
private JButton btSair;
private JLabel lblHistorico;
private JLabel lblMensagem;
private JPanel pnlConteudo;
private Socket socket;
private OutputStream out;
private Writer writer; 
private BufferedWriter bfwriter;
private JTextField txtIP;
private JTextField txtPorta;
private JTextField txtNome;

    public Cliente(){
    // Interface
    JLabel lblMessage = new JLabel("Verificar!");
    txtIP = new JTextField("0.0.0.0"); // <--- IP padrão
    txtPorta = new JTextField("5151"); // <--- porta padrão
    txtNome = new JTextField("Cliente-Servidor"); // <--- nickname padrão              
    Object[] texts = {lblMessage, txtIP, txtPorta, txtNome };  
    JOptionPane.showMessageDialog(null, texts);              
    pnlConteudo = new JPanel();
    texto = new JTextArea(11,53); // altura/largura txtArea Histórico
    texto.setFont(new Font("Dialog",Font.BOLD,14)); // Tamanho da fonte na txtArea
    texto.setEditable(false);
    texto.setBackground(new Color(240,240,240));
    texto.append("BEM VINDO AO JOGO DA FORCA!!! \r\n");   
    mensagem = new JTextField(53); // largura txtField Mensagem
    mensagem.setFont(new Font("Dialog",Font.BOLD,15)); // Tamanho da fonte no txtField
    lblHistorico = new JLabel("Histórico");
    lblMensagem = new JLabel("Mensagem");
    btEnviar = new JButton("Enviar");
    btEnviar.setToolTipText("Enviar Mensagem");
    btSair = new JButton("Sair");
    btSair.setToolTipText("Sair do Chat");
    btEnviar.addActionListener(this);
    btSair.addActionListener(this);
    btEnviar.addKeyListener(this);
    mensagem.addKeyListener(this);
    JScrollPane scroll = new JScrollPane(texto);
    texto.setLineWrap(true);  
    pnlConteudo.add(lblHistorico);
    pnlConteudo.add(scroll);
    pnlConteudo.add(lblMensagem);
    pnlConteudo.add(mensagem);
    pnlConteudo.add(btSair);
    pnlConteudo.add(btEnviar);
    pnlConteudo.setBackground(Color.LIGHT_GRAY);                                 
    texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE)); // bordas do painel = AZUL
    mensagem.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));                    
    setTitle(txtNome.getText());
    setContentPane(pnlConteudo);
    setLocationRelativeTo(null);
    setResizable(false);
    setSize(680,360); // largura/altura janela principal
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    // Interface
    }
    
    public void conectar() throws IOException{ // conexão do cliente com o socket servidor
        socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText())); // Socktet recebe os parametros de IP e Porta
        out = socket.getOutputStream();
        writer = new OutputStreamWriter(out);
        bfwriter = new BufferedWriter(writer);
        bfwriter.write(txtNome.getText()+"\r\n");
        bfwriter.flush();
    }
    
    public void enviarMensagem(String msg) throws IOException{ // envio de msg do cliente para o socket servidor                         
        if(msg.equals("Sair")){
            bfwriter.write("Desconectado \r\n"); // <--- Caso clique no evento Sair
            texto.append("Desconectado \r\n");
        }else{
            //texto.append("BEM VINDO AO JOGO DA FORCA !!! \r\n");
            bfwriter.write(msg+"\r\n");
            texto.append( txtNome.getText() + " disse ->  " + mensagem.getText()+"\r\n");
        }
        bfwriter.flush();
        mensagem.setText("");        
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
    
    public void actionPerformed(ActionEvent e) { // Controle de eventos: Enviar e Sair        
    try {
        if(e.getActionCommand().equals(btEnviar.getActionCommand()))   
            enviarMensagem(mensagem.getText());
        else
        if(e.getActionCommand().equals(btSair.getActionCommand()))
            sair();
        }catch (IOException e1) {
            e1.printStackTrace();
        }                       
    }
    
    public void keyPressed(KeyEvent e) { // Opção press ENTER para enviar mensagem               
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try {
                enviarMensagem(mensagem.getText());
            } catch (IOException e1) {
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
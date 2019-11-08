/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import sun.rmi.server.MarshalInputStream;
/**
 *
 * @author lab3
 */
public class Servidor extends JFrame{
    private JTextField area;
    private JTextField display;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private ServerSocket servidor;
    private Socket conexao;
    private int contador=1;
    
    public Servidor(){
        
        super("Servidor");
        Container container =getContentPane();
        area=new JTextField();
        area.setEnabled(false);
        area.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent evento)
                {enviaDado(evento.getActionCommand());}
        }
        );
        container.add(area,BorderLayout.NORTH);
        display= new JTextArea();
        container.add(new JScrollPane(display),BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }
    
    public void rodaServidor(){
        try{
            servidor=new ServerSocket(5000, 2);
            while(true){
                esperaConexao();
                obtemFluxos();
                processoConexao();
                fechaConexao();
                ++contador;
            }
        }
        catch(EOFException eofException){
            System.out.println("Cliente terminou a conexao");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    public void esperaConexao()throws IOException{
        display.setText("Esperando por conexão \n");
        conexao =servidor.accept();
        display.append("Conexão"+"contador"+"recebido de : "+conexao.getInetAddress().getHostName());
        
    }
    public void obtemFluxos()throws IOException{
        
        saida=new ObjectOutputStream(conexao.getOutputStream());
        saida.flush();
        entrada=new MarshalInputStream(conexao.getInputStream());
        display.append("Fluxo de entrada/saida");
    }
    
    public void processoConexao()throws IOException{
        String mensagem ="sucesso na conexão do servidor";
        saida.writeObject(mensagem);
        saida.flush();
        area.setEnabled(true);
        
        do{
            try{
                mensagem= (String)entrada.readObject();
                display.append("\n "+mensagem);
                display.setCaretPosition(display.getText().length());
            }
            catch(ClassNotFoundException classNotFoundException){
                display.append("\n tipo de objeto desconhcido");
            }
        }while(!mensagem.equals("cliente terminou"));
    }
    
    private void fechaConexao()throws IOException{
        display.append("\n usuario terminou conexão");
        area.setEnabled(false);
        saida.close();
        entrada.close();
        conexao.close();
    }
    
    private void enviaDado(String mensagem){
        try{
            saida.writeObject("servidor "+mensagem);
            saida.flush();
            display.append("servidor "+mensagem);
        }
        catch(IOException iofException){
            display.append("erro escrevendo objeto");
        }
    }
    
    public static void main(String [] args){
        Servidor aplicacao=new Servidor();
        aplicacao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aplicacao.rodaServidor();
        
        
    }
    
    
}


import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import  java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author lab3
 */
public class Cliente extends JFrame{
    private  JTextField area;
    private  JTextArea display;
    private ObjectOutputStream saida;
    private  ObjectInputStream entrada;
    private  Socket cliente;
    private String mensagem="";
    private  String servidorconexao;
    
    public Cliente(String host){
        super("Cliente");
        servidorconexao=host;
        Container container=getContentPane();
        area=new JTextField();
        area.setEnabled(false);
        area.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
             enviaDado(evento.getActionCommand());   
            }
        });
        
        container.add(area,BorderLayout.NORTH);
        display=new JTextArea();
        container.add(new JScrollPane(display),BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
        
        
        
    }
    
    public void rodaCliente(){
        try{
            conectaServidor();
            obtemFluxos();
            processoConexao();
            fechaConexao();
        }
        catch(EOFException eoException){
            System.out.println("Servidor terminou a conexao");
        }
        catch(IOException iofException){
            iofException.printStackTrace();
        }
    }
     private void obtemFluxos() throws IOException{
         saida=new ObjectOutputStream(cliente.getOutputStream());
         saida.flush();
         entrada=new ObjectInputStream(cliente.getInputStream());
         display.append("Fluxo de entrada/saida");
     }
    private void conectaServidor()throws IOException{
        display.setText("Tentando conexão\n ");
        cliente=new Socket(InetAddress.getByName(mensagem), 5000);
        display.append("conecta para  "+cliente.getInetAddress().getHostName());
    }

    private void processoConexao()throws IOException{
        area.setEnabled(true);
        do{
            try{
                mensagem=(String)entrada.readObject();
                display.append("\n"+mensagem);
                display.setCaretPosition(display.getText().length());

            }
            catch(ClassNotFoundException classNofoundExcepiton){
                display.append("\n tipo de objeto desconhecido");
            }
        }while(!mensagem.equals("servidor terminou"));
        
    }
     
    private void fechaConexao()throws IOException{
        display.append("\n ternimou conexao");
        saida.close();
        entrada.close();
        cliente.close();
    }
    private  void enviaDado(String mensagem){
        try{
            saida.writeObject("cliente "+mensagem);
            saida.flush();
            display.append("cliente "+mensagem);
        }catch(IOException ioexcpetion){
            display.append("erro escrevendo o objeto");
        }
    }

    public static void main(String[] args) {
       Cliente aplicacao;
       if(args.length==0){
           aplicacao =new Cliente("127.0.0.1");
           
       }
       else{
           aplicacao=new Cliente(args[0]);
       }
       
       aplicacao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       aplicacao.rodaCliente();
    }
    
}

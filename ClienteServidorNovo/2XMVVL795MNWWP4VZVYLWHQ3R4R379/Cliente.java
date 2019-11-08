import java. io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Cliente extends JFrame {
    private JTextField area;
    private JTextArea display;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private Socket cliente;
    private String mensagem = "";
    private String servidorconexao;


    public Cliente (String host){

        super("CLIENTE");
        servidorconexao = host;
        Container container = getContentPane();
        area = new JTextField();
       area.setEnabled(false);
        area.addActionListener(
                new ActionListener(){
            public void actionPerformed(ActionEvent evento)
            {enviaDado(evento.getActionCommand());}

        }
       );
container.add(area, BorderLayout.NORTH);
display = new JTextArea();
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

    }catch(EOFException eofException){System.out.println("Servidor terminou a conex�o");}
    catch(IOException ioException){ioException.printStackTrace();}
    }


     private void obtemFluxos() throws IOException{

        saida = new ObjectOutputStream(cliente.getOutputStream());
        saida.flush();
        entrada = new ObjectInputStream(cliente.getInputStream());
        display.append("fluxo de entrada/sa�da");

    }

     private void conectaServidor() throws IOException{

         display.setText("Tentando conex�o\n");
         cliente = new Socket(InetAddress.getByName(mensagem),5000);
         display.append("conecta para"   + cliente.getInetAddress().getHostName());
         }

      private void processoConexao() throws IOException{

          area.setEnabled(true);

          do{
            try{
              mensagem = (String)entrada.readObject();
              display.append("\n" + mensagem);
              display.setCaretPosition(display.getText().length());
          }catch(ClassNotFoundException classNotFoundException){display.append("\ntipo de objeto desconhecido");}
          }while(!mensagem.equals("servidor terminou"));
      }

      private void fechaConexao() throws IOException{

          display.append("\nterminou conex�o");
          saida.close();
          entrada.close();
          cliente.close();
      }

  private void enviaDado(String mensagem){

          try{
              saida.writeObject("cliente" + mensagem);
              saida.flush();
              display.append("cliente" + mensagem);

          }catch(IOException ioException){display.append("erro escrevendo objeto");}
      }

 public static void main(String[] args) {
         // TODO code application logic here
         Cliente aplicacao;
         if(args.length == 0)
         aplicacao = new Cliente("127.0.0.1");
         else
         aplicacao = new Cliente(args[0]);

         aplicacao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         aplicacao.rodaCliente();
     }
 }


import java. io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Servidor extends JFrame {

    /**
     * @param args the command line arguments
     */

    private JTextField area;
    private JTextArea display;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private ServerSocket servidor;
    private Socket conexao;
    private int contador = 1;

    public Servidor(){

        super("Servidor");
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


    public void rodaServidor(){

    try{
    servidor = new ServerSocket(5000, 2);
    while(true){

        esperaConexao();
        obtemFluxos();
        processoConexao();
        fechaConexao();
        ++contador;
    }
    }catch(EOFException eofException){System.out.println("Cliente terminou a conex�o");}
    catch(IOException ioException){ioException.printStackTrace();}
    }

    private void esperaConexao() throws IOException{

        display.setText("Esperando por conex�o\n");
        conexao = servidor.accept();
        display.append("Conexao" + contador + "recebido de: " + conexao.getInetAddress().getHostName());
        }

    private void obtemFluxos() throws IOException{

        saida = new ObjectOutputStream(conexao.getOutputStream());
        saida.flush();
        entrada = new ObjectInputStream(conexao.getInputStream());
        display.append("fluxo de entrada/sa�da");

    }

     private void processoConexao() throws IOException{

         String mensagem = "sucesso na conex�o do servidor";
         saida.writeObject(mensagem);
         saida.flush();
         area.setEnabled(true);

         do{
           try{
             mensagem = (String)entrada.readObject();
             display.append("\n" + mensagem);
             display.setCaretPosition(display.getText().length());
         }catch(ClassNotFoundException classNotFoundException){display.append("\ntipo de objeto desconhecido");}
         }while(!mensagem.equals("clinte terminou"));
     }

     private void fechaConexao() throws IOException{

         display.append("\n usu�rio terminou conex�o");
         area.setEnabled(false);
         saida.close();
         entrada.close();
         conexao.close();
     }
     
     private void enviaDado(String mensagem){

         try{
             saida.writeObject("servidor" + mensagem);
             saida.flush();
             display.append("servidor" + mensagem);

         }catch(IOException ioException){display.append("erro escrevendo objeto");}
     }



    public static void main(String[] args) {
        // TODO code application logic here
        Servidor aplicacao = new Servidor();
        aplicacao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aplicacao.rodaServidor();
    }

}


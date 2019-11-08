
package negocio;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.rmi.registry.LocateRegistry;      
import java.rmi.registry.Registry;

    public class Client extends JFrame implements ActionListener,Runnable 
{
    List list;
    JTextArea text;
    JButton btnExit;
    JButton btnSend;
    Socket s;
    BufferedReader br;
    BufferedWriter bw;
    static String nomeClient;
    RemotePrintInterface rmiServer; 
    Registry registry;

    public Client() {
        setTitle("Chat Online - Cliente");
        getContentPane().setLayout(null);

        list = new List();
        list.setBounds(10, 47, 692, 275);
        getContentPane().add(list);
        

        text = new JTextArea();
        text.setLineWrap(true);
        text.setBounds(10, 338, 692, 74);
        getContentPane().add(text);

        btnExit = new JButton("Exit");
        btnExit.setBounds(613, 11, 89, 23);
        btnExit.addActionListener(this);
        getContentPane().add(btnExit);

        btnSend = new JButton("Send");
        btnSend.setBounds(613, 423, 89, 23);
        btnSend.addActionListener(this);
        getContentPane().add(btnSend);
        

        setSize(741,495);
        setLocation(300,0);
        
        getRootPane().setDefaultButton(btnSend);
        setVisible(true);

        try{
            /*Put the current IP address for current machine  
            if you didn't have an actual server and clients
            if you have an actual server and clients put the client IP address*/
            s = new Socket("127.0.0.1",8375); // é aqui onde coloco o IP do servidor
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write("... Um usuário se conectou! ...");
            try {
      // find the (local) object registry
      registry = LocateRegistry.getRegistry(9999);

      // find the server object
      rmiServer = (RemotePrintInterface) (registry.lookup("server"));

      rmiServer.print("Servidor Rodando"); //$NON-NLS-1$
      rmiServer.calcular();
    } catch (Throwable t) {
      t.printStackTrace();
    }
            bw.newLine();
            bw.flush();
            Thread th;
            th = new Thread(this);
            th.start();

        } catch(Exception e){
            System.out.println(e);
        }
    }


    public static void main(String[] args) throws Exception
    {
        nomeClient = JOptionPane.showInputDialog(null,"Insira seu nome:");
        new Client();

    }

    @Override
    public void run() {
        try
        {
            s.setSoTimeout(2500);
        } catch(Exception e)
        {
            //System.out.println("Exceção: " + e);
        }

        while (true)
        {
            try
            {
                list.add(br.readLine());
            }
            catch (Exception e)
            {
                //System.out.println("Exceção: " + e);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource().equals(btnExit))
             System.exit(0);
         else {
             try
             {
                 list.add("Eu (" + nomeClient + "): " + text.getText());
                 bw.write(nomeClient + ": " + text.getText());
                 bw.newLine();
                 bw.flush();
                 text.setText("");
             } catch(Exception ex)
             {
                 //System.out.println("Exceção: " + ex);
             }
         }

    }                    
}       


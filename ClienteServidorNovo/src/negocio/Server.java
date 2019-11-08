
package negocio;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;


public class Server extends JFrame implements ActionListener,Runnable,RemotePrintInterface  {

ServerSocket ss;
Socket s;
BufferedReader br;
BufferedWriter bw;
List list;
JButton btnExit;
JTextArea text2;
JButton btnSend;
static String nomeServer;
Registry registry;

public Server() {
    setTitle("Chat Online - Servidor");
    setSize(741,495);
    setLocation(300,0);
    getContentPane().setLayout(null);

    list = new List();
    list.setBounds(10, 48, 692, 275);
    getContentPane().add(list);

    btnExit = new JButton("Exit");
    btnExit.setBounds(613, 11, 89, 23);
    getContentPane().add(btnExit);
    btnExit.addActionListener(this);

    text2 = new JTextArea();
    text2.setForeground(UIManager.getColor("ToggleButton.focus"));
    text2.setLineWrap(true);
    text2.setBounds(10, 338, 692, 74);
    getContentPane().add(text2);
    

    btnSend = new JButton("Send");
    btnSend.setBounds(613, 423, 89, 23);
    getContentPane().add(btnSend);
    
    btnSend.addActionListener(this);
    
    

    getRootPane().setDefaultButton(btnSend);
    
    setVisible(true);
    
    System.setProperty("java.security.policy","C:/Users/lab5/Documents/test.policy");
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

    try
    {           
        list.add("Aguardando conexão de cliente...\n");

        ss = new ServerSocket(8375);

        s = ss.accept();
        br = new BufferedReader(new InputStreamReader(
                s.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(
                s.getOutputStream()));
        bw.write("... Você está conectado! ...");
        try {
            String name = "Calculator";
            MyCalc engine = new MyCalcImpl();
            MyCalc stub = (MyCalc) UnicastRemoteObject.exportObject(engine, 1099);
            registry = LocateRegistry.getRegistry(1099);
            System.out.println("Registering Calculator Object");
            registry.rebind(name, stub);
            
        } catch (Exception e) {
            System.err.println("Exception:" + e);
            e.printStackTrace();
        }
        bw.newLine();
        bw.flush();
        Thread th;
        th = new Thread(this);
        th.start();
        
    }
    catch(Exception e)
    {
        //System.out.println("Exceção: " + e);
    }
}

 public static void main(String[] args) throws Exception
  {
     nomeServer = JOptionPane.showInputDialog(null,"Insira seu nome:");
     new Server();      
     

  }

@Override
public void run() {
    try
    {
        s.setSoTimeout(1);
    } catch(Exception e)
    {
        System.out.println("Exceção: " + e);
    }
    while (true)
    {
        try
        {
            list.add(br.readLine());
        }
        catch (Exception ex)
        {
            //System.out.println("Exceção: " + ex);
        }
    }

}

@Override
public void actionPerformed(ActionEvent e) {
     if (e.getSource().equals(btnExit))
         System.exit(0);
     else{try{
         list.add("Eu (" + nomeServer + "): " + text2.getText());
         bw.write(nomeServer + ": " + text2.getText());
      //   bw.write(String.format(" Numero calculado: %d", new Calc().calcular()));
         bw.newLine();
         bw.flush();
         text2.setText("");
         } catch(Exception m){}
     }


}  

   
    @Override
    public void calcular() throws RemoteException {
                 int i;
        for(i=0; i<=10; i++){
            System.out.println(i);
        }
    }

    @Override
    public void print(String what) throws RemoteException {
            System.out.println(what); 
    }
    
}

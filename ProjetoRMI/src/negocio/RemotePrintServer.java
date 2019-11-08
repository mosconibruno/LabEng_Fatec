/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.rmi.RemoteException;        
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;      
import java.rmi.server.UnicastRemoteObject;

public class RemotePrintServer extends UnicastRemoteObject implements RemotePrintInterface {
  RemotePrintServer() throws RemoteException {
    super();
  }

 // the actual implementation of the method specified by RemotePrintInterface
  @Override
  public void print(final String what) throws RemoteException {
    System.out.println(what); 
  }
  
  
  public static final void main(final String args[]) {
    Registry registry;

    try {
      // create the (local) object registry
      registry = LocateRegistry.createRegistry(9999);
      // bind the object to the name "server"
      registry.rebind("server", new RemotePrintServer());
      registry.rebind("calc", new RemotePrintServer());
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

    @Override
    public void calcular() throws RemoteException {
             int i;
        for(i=0; i<=10; i++){
            System.out.println(i);
        }
       
    }
}
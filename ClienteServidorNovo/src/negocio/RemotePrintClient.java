/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.rmi.registry.LocateRegistry;      
import java.rmi.registry.Registry;

public class RemotePrintClient { // the remote print rmi client
  public static final void main(final String args[]) {
    RemotePrintInterface rmiServer;         Registry registry;

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
  }
}
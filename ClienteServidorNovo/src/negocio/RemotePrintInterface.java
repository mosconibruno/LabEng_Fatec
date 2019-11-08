/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.rmi.Remote;
import java.rmi.RemoteException;

// the RemotePrint interface
public interface RemotePrintInterface extends Remote {

  /** A method to be remote-accessible
   * @param what the string to print
   * @throws RemoteException a possible exception */
  public abstract void print(final String what) throws RemoteException;
  
  public abstract void calcular() throws RemoteException;

}
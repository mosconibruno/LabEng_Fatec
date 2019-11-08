/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author lab5
 */
public class MyCalcImpl implements MyCalc {

    @Override
    public int add(int a, int b) throws RemoteException {
        return (a + b);
    }
    
}

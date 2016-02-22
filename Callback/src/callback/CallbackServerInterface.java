package Callback;

import java.rmi.*;

public interface CallbackServerInterface extends Remote {

    public String sayHello(String ip)
            throws java.rmi.RemoteException;

// Este método remoto permite que um cliente 
// se registe no callback
// @param callbackClientObject é uma referência para o
//        objeto do cliente; para ser utilizado pelo servidor
//        Para fazer callbacks.
    public void registerForCallback(
            CallbackClientInterface callbackClientObject
    ) throws java.rmi.RemoteException;

// Este método remoto permite que um cliente objeto
// Cancele o seu registo callback
    public void unregisterForCallback(
            CallbackClientInterface callbackClientObject)
            throws java.rmi.RemoteException;
}







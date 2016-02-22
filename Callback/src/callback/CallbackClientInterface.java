package Callback;

import java.rmi.*;

public interface CallbackClientInterface
        extends java.rmi.Remote {

// Este método remoto é invocado por um servidor callback
// para fazer uma chamada de retorno para um cliente que
// Implementa esta interface.
// param Mensagem - uma string que contem informações para o
// Cliente para processar depois de ser chamado de volta .
    public String notifyMe(String message)
            throws java.rmi.RemoteException;

} // end interface

package Callback;

import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class CallbackServer {

    public static void main(String args[]) {

        int portNum = 12345;
        String registryURL;

        try {
            System.out.println(" RMIregistry na porta: " + portNum);

            startRegistry(portNum);
            CallbackServerImpl exportedObj = new CallbackServerImpl();

            registryURL = "rmi://localhost:" + portNum + "/callback";
            //regista a instância local no registry..
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Servidor Callback iniciado.");
            exportedObj.Socket();

        }// end try
        catch (Exception re) {
            System.out.println(
                    "Exception in HelloServer.main: " + re);
        }// end catch
    } // end main

// Este método inicia um registro RMI no localhost, se
// já não existe no número de porta especificado.
    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();

            // Essa chamada lançará uma exceção
            // Se o registro não existir
        } catch (RemoteException e) {

            // Sem registo válido nessa porta
            Registry registry
                    = LocateRegistry.createRegistry(RMIPortNum);
        }
    } // end startRegistry
} // end class

package Callback;

import com.sun.corba.se.spi.activation.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CallbackServerImpl extends UnicastRemoteObject
        implements CallbackServerInterface {

    private Vector clientList;
    private String resposta;
    public String meuIP = "";
    private String novoIP;

    public CallbackServerImpl() throws RemoteException {
        super();
        clientList = new Vector();
        ciclo();
    }

    public void start_Request(String listaIp) {

        try {
            meuIP = listaIp;

            URL url = new URL("http://heartbeat.dsi.uminho.pt/heartbeat.svc/show?ip=" + listaIp);
            Socket socketServer = new Socket(url.getHost(), 80);

            PrintWriter out = new PrintWriter(socketServer.getOutputStream(), true);
            out.println("GET " + url + " HTTP/1.0");
            out.println();
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
            String inputLine = null;

            while ((inputLine = in.readLine()) != null) {
                resposta = inputLine;
            }
            in.close();
            socketServer.close();

            if (!resposta.equals(novoIP)) {
                novoIP = resposta;
                doCallbacks();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String sayHello(String ip)
            throws java.rmi.RemoteException {
        start_Request(ip);
        return (resposta);
    }

    //Método para um cliente RMI
    public void Socket() throws IOException {

        ServerSocket servidor = new ServerSocket(4321);
        // aceita um cliente
        while (true) {
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com um cliente via Socket");
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            start_Request(in.readLine());
            ps.println(resposta);
        }
    }

    public synchronized void registerForCallback(
            CallbackClientInterface callbackClientObject)
            throws java.rmi.RemoteException {

        // armazenar o objeto callback para dentro do vector
        if (!(clientList.contains(callbackClientObject))) {
            clientList.addElement(callbackClientObject);
            System.out.println("Registado novo Cliente ");

        } // end if
    }

// Este método remoto permite que um cliente objeto possa
// Cancelar o seu registo no callback
// param ID é uma identificação para o cliente ; para ser usado pelo
// O servidor para identificar o cliente registado
    public synchronized void unregisterForCallback(
            CallbackClientInterface callbackClientObject)
            throws java.rmi.RemoteException {
        if (clientList.removeElement(callbackClientObject)) {
            System.out.println("Saida do client ");
        } else {
            System.out.println(
                    "unregister: clientwasn't registered.");
        }
    }

    private synchronized void doCallbacks() throws java.rmi.RemoteException {
        // fazer callback para cada cliente registado

        for (int i = 0; i < clientList.size(); i++) {
            // converte o vector object para callback object
            CallbackClientInterface nextClient = (CallbackClientInterface) clientList.elementAt(i);
            // invoca o método callback 
            nextClient.notifyMe("Lista IPs:" + sayHello(meuIP));
        }// end for

    }

//  Método assincrono que vai permitir de x em x tempo 
//  verificar ao servidor se a lista alterou.
    private void ciclo() throws RemoteException {
        Runnable task = new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {
                        //Aqui é definido o tempo de espera
                        Thread.sleep(1 * 1000);
                        start_Request(meuIP);

                    }
                } catch (InterruptedException ex) {
                    // sleep over
                }
            }
        };
        new Thread(task).start();
    }

}

// doCallbacks

// end CallbackServerImpl class  

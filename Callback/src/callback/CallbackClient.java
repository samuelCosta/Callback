package Callback;
import java.io.*;
import java.rmi.*;


public class CallbackClient {

  public static void main(String args[]) {
    try {
      int RMIPort = 12345 ;         
      String meuIP;
      InputStreamReader is =  new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      System.out.println( "Digite o seu IP: ");
      meuIP = br.readLine();
      System.out.println( "Registado na porta: " + RMIPort);
      System.out.println( "Quantos tempo pretende estar registado? ");
      String timeDuration = br.readLine();
      int time = Integer.parseInt(timeDuration);
      String registryURL = "rmi://localhost:" + RMIPort + "/callback";  
     // Encontra o objeto remoto e lançá-lo para um
      // Objeto de interface
      CallbackServerInterface h =(CallbackServerInterface)Naming.lookup(registryURL);
      System.out.println("Lookup completed " );      
      System.out.println("Resposta do Servidor: " + h.sayHello(meuIP));
      
      //aqui é o registo para o callback
      CallbackClientInterface callbackObj =  new CallbackClientImpl();
 
      h.registerForCallback(callbackObj);
      System.out.println("Registado para callback.");
      
      try {
        Thread.sleep((time* 1000));
      }
      catch (InterruptedException ex){ 
      }
      h.unregisterForCallback(callbackObj);
      System.out.println("Saida do callback.");
    } // end try 
    
    catch (Exception e) {
      System.out.println(
        "Exception in CallbackClient: " + e);
    } // end catch
  
  } //end main
}//end class

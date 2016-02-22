package callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteSocket {

    public static void main(String[] args)
            throws UnknownHostException, IOException {

        String host = "127.0.0.1";
        int porta = 4321;

        try (
                Socket cliente = new Socket(host, porta);
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintStream saida = new PrintStream(cliente.getOutputStream());) {
            System.out.println("O cliente se conectou ao servidor!");
            System.out.println("Digite o seu IP: ");

            // lê msgs do teclado e manda pro servidor
            Scanner teclado = new Scanner(System.in);
            saida.println(teclado.nextLine());
            
            System.out.println("Resposta do servidor: ");
            System.out.println(in.readLine());

            saida.close();
            teclado.close();
            cliente.close();
        }
    }
}

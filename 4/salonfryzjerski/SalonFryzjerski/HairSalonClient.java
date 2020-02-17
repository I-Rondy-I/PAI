

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HairSalonClient {
    private static Socket socket;
    private static BufferedReader reader;
    private static PrintWriter writer;
    private static String echoString;

    // "Main" thread is responsible for reading
    public static void main(String[] args) {
        initializeHairSalonClient();
        echoString = "";
        ReceiveMessage receive = new ReceiveMessage();
        receive.start();
        try{
            Scanner scanner = new Scanner(System.in);
            do {
                echoString = scanner.nextLine();
                writer.println(echoString);
            } while(!echoString.equals("q"));
        } catch (InputMismatchException e){
            System.out.println("Exception from client: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Serious exception from client: " + e.getMessage());
        } finally {
            try {
                if(socket != null) {
                    socket.close();
                    reader.close();
                    writer.close();
                }
            } catch (IOException e){
                System.out.println("Socket still open: " + e.getMessage());
            }
        }
    }

    // initialize
    private static void initializeHairSalonClient() {
        try{
          socket = new Socket("localhost", 5000);

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);

        } catch (IOException e){
            System.out.println("Initializing exception from client: " + e.getMessage());
        }
    }

    static String getEchoString() {
        return echoString;
    }

    static BufferedReader getReader() {
        return reader;
    }
}

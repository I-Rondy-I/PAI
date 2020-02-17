
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.WrongMethodTypeException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class HairSalonServer {
    private static int i = 0;
    private static ArrayList<Echoer> hairSalonClients;

    public static void main(String[] args) {
        if(!init()){
            return;
        }
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            while(true){
                Echoer echoer = new Echoer(serverSocket.accept(),i);
                hairSalonClients.add(echoer);
                echoer.start();
                i += 1;
                System.out.println("Client " + i + " connected successfully.");
            }

        } catch (IOException e){
            System.out.println("Exception from server: " + e.getMessage());
        }
    }

    private static boolean init(){
        try{
            i = 0;
            hairSalonClients = new ArrayList<>();
            return true;
        } catch (Exception e){
            System.out.println("Init failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean notifyAllClients(Echoer echo,String message) {
        try {
            for (Echoer client : HairSalonServer.getHairSalonClients()) {
                if (!client.equals(echo)) {
                    client.getOutput().println(message);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Notify all clients failed: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<Echoer> getHairSalonClients() {
        return hairSalonClients;
    }
}




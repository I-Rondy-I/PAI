
import java.io.*;
import java.net.Socket;

public class Echoer extends Thread{
    private Socket socket;
    private int clientID;
    private BufferedReader input;
    private PrintWriter output;

    private String stringHour;
    private String name;
    private int intHour;

    Echoer(Socket socket, int clientID) {
        this.socket = socket;
        this.clientID = clientID;
        try{
            input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e){
            System.out.println("Initialize server exception: " + e.getMessage());
        }
        output.println("Welcome to my hair salon.");
    }

    @Override
    public void run() {
        try{
            output.print(printMenu());

            while(true){
                output.println("\nYour option:");
                String echoString = input.readLine();
                if(echoString == null) {
                    break;
                }
                if( echoString.equals("q")){
                    break;
                }
                switch (echoString){
                    case "1":
                        bookVisit();
                        break;
                    case "2":
                        cancelVisit();
                        break;
                    case "3": // Print available hours
                        output.println(Schedule.getInstance().printAvailableDate());
                        break;
                    default:
                        output.println("Choose one of the following option:\n1 - book;\n2 - cancel;\n3 - show available hours;\nexit - to close application.");
                        break;
                }
            }

        } catch (IOException e){
            System.out.println("Oops, exception from echoer: " + e.getMessage());
        } finally {
            try {
                if(socket != null) {
                    socket.close();
                    input.close();
                    output.close();
                    System.out.println(Thread.currentThread().getName() + " closed.");
                }
            } catch (IOException e){
                System.out.println("Socket still open. Message:" + e.getMessage());
            }
        }

    }

    private boolean bookVisit() throws IOException{
        output.println("Booking the visit...");
        output.println("Choose suitable hour for you:");
        stringHour = input.readLine();
        intHour = checkHour(stringHour);
        if(intHour < 0) {
            output.println("Visit not booked. Try again typing 1.");
            return false;
        }
        output.println("Enter your name:");
        name = input.readLine();
        if(name.length() < 2){
            output.println("Name is incomplete. Try again typing 1.");
            return false;
        }
        if(Schedule.getInstance().bookDate(intHour, name)){
            output.println("You booked the appointment at " + intHour + ":00.");
            HairSalonServer.notifyAllClients(this,"## " + intHour + ":00 is no longer available. ##");
        } else {
            output.println("Failed to book the visit at " + intHour + ":00. Type 1 to try again.");
            return false;
        }
        intHour = -1;
        return true;
    }

    private boolean cancelVisit() throws IOException{
        output.println("Canceling the visit...");
        output.println("Enter your appointment hour:");
        stringHour = input.readLine();
        intHour = checkHour(stringHour);
        if(intHour < 0) {
            output.println("Visit not canceled. Try again typing 2.");
            return false;
        }
        output.println("Imie:");
        name = input.readLine();
        if(name.length() < 2){
            output.println("Imie nie zaakceptowane. 2 - sprobuj ponownie");
            return false;
        }
        if(Schedule.getInstance().cancelDate(intHour,name)){
            output.println("Rezerwacja zostala anulowana w godz.: " + intHour + ":00.");
            HairSalonServer.notifyAllClients(this,"## " + intHour + ":00 jest dostepne. ##");
        } else {
            output.println("Nieudane anulowanie rezerwacji. 2 - sprobuj ponownie");
            return false;
        }
        intHour = -1;
        return true;
    }

    private int checkHour(String stringHour) {
        int returnInt = -1;
        int i = 0; // only 3 chances
        do {
            try {
                if(i > 0) {
                    output.println("Wprowadz cala liczbe miedzy 10 - 18 :");
                    stringHour = input.readLine();
                }
                if (stringHour.length() == 2) { // e.g 10
                    if(stringHour.matches("[0-9]+")){
                        returnInt = Integer.parseInt(stringHour);
                        if(returnInt >= 10 && returnInt <= 18)
                            return returnInt;
                    }
                } else if (stringHour.length() == 5) { // e.g 10:00
                    if(stringHour.substring(2).equals(":00")){
                        stringHour = stringHour.substring(0, 2); // "10:00".substring(0,2) = "10"
                        if(stringHour.matches("[0-9]+")){
                            returnInt = Integer.parseInt(stringHour);
                            if(returnInt >= 10 && returnInt <= 18)
                                return returnInt;
                        }
                    }
                }

            } catch (IOException | NumberFormatException e){
                System.out.println(e.getMessage());
            }
            i++;
        } while ( i < 4);
        return  -1;
    }

    private static String printMenu(){
        String s = "What do you want to do?\n" +
                "1 - Rezerwacja\n" +
                "2 - Anulowanie rezewacji\n" +
                "3 - Aktualny stan rezerwacij\n" +
                "'q' - Koniec\n";
        return s;
    }

    PrintWriter getOutput() {
        return output;
    }
}

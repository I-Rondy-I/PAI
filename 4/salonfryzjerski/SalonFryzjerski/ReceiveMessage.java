

import java.io.IOException;

// And this thread is responsible for message received from server
class ReceiveMessage extends Thread{

    @Override
    public void run() {
        String response;
        try{
            while(HairSalonClient.getReader() == null || HairSalonClient.getEchoString() == null){
            }
            do{
                response = HairSalonClient.getReader().readLine();
                if(!(response == null)) {
                    if (response.length() > 17){
                        if(response.substring(0,4).equals("## 1") && response.length() == "## 11:00 is no longer available. ##".length()){
                            new Notify(response,"Someone booked the date", 2).start();
                            continue;
                        } else if(response.substring(0,4).equals("## 1") && response.length() == "## 11:00 is now available. ##".length()){
                            new Notify(response,"Someone canceled the date", 2).start();
                            continue;
                        }
                    }
                        System.out.println(response);
                }
            } while(!HairSalonClient.getEchoString().equals("exit"));
            System.out.println("Receive closed");
        } catch (IOException e){
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Have a nice day.");
        }
    }

}
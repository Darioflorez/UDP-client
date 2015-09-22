package client;

import java.io.IOException;

/**
 * Created by Dario on 2015-09-08.
 */
public class Client {
    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Missing arguments!!!");
            return;
        }
        try{
            ClientThread client;
            client = new ClientThread(args);
            client.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

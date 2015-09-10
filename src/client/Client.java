package client;

import java.io.IOException;

/**
 * Created by Dario on 2015-09-08.
 */
public class Client {
    public static void main(String[] args){
        try{
            ClientThread client;
            client = new ClientThread();
            client.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

/**
 * Created by Dario on 2015-09-08.
 */
public class ClientThread extends Thread{
    private DatagramSocket socket = null;
    private InetAddress hostname;
    private int port = 6478;
    //private int port = 1337;
    private boolean active;
    private String state;

    public ClientThread()throws IOException{
        active = false;
        socket = new DatagramSocket();
        setState("hello");
        //hostname = InetAddress.getByName("130.242.47.93");
        hostname = InetAddress.getByName("127.0.0.1");
    }
    public void run(){
        System.out.println("Client is ready!");
        active = true;
        String msgOut;
        Scanner scanner = new Scanner(System.in);
        while(active){

            System.out.print("> insert [" + state + "]: ");
            while((msgOut = scanner.nextLine()) != null){
                sendMessage(msgOut);
                if(msgOut.equalsIgnoreCase("bye")){
                    closeConnection();
                    break;
                }
                String msg = getMessage();
                  if(msg != null){
                    if(msg.contains("Timeout")){
                        System.err.println(msg);
                        closeConnection();
                        break;
                    }
                    else if(msg.equalsIgnoreCase("ok")){
                        setState("start");
                        displayMessage("Connected!");
                    }else if(msg.equalsIgnoreCase("ready")){
                        setState("a number");
                    }else if (msg.equalsIgnoreCase("correct!")) {
                        setState("start");
                    }
                    displayMessage(msg);
                    System.out.print("> insert [" + state + "]: ");
                } else {
                    System.out.println("Server out of reach!");
                    closeConnection();
                    break;
                }
            }

        }
        scanner.close();
    }

    protected void setState(String s){
        state = s;
    }

    protected void sendMessage(String msgOut){
        byte[] bufferOut;
        DatagramPacket packetOut;
        try{
            //Send message to server
            bufferOut = msgOut.getBytes();
            packetOut = new DatagramPacket(bufferOut, bufferOut.length, hostname, port);
            socket.send(packetOut);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    protected void displayMessage(String msg) {
        System.out.println("Server: " + msg);
    }

    protected String getMessage(){
        try{
            byte[] bufferIn = new byte[256];
            DatagramPacket packetIn = new DatagramPacket(bufferIn, bufferIn.length);
            //Set Timeout
            socket.setSoTimeout(7000);
            try {
                socket.receive(packetIn);
                return new String(packetIn.getData(), 0, packetIn.getLength());
            }
            catch (SocketTimeoutException e) {
                // timeout exception.
                System.out.println("Timeout reached!!! " + e);
                return null;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    protected void closeConnection(){
        active = false;
        socket.close();
    }

}

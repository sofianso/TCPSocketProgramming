
/**
 * Write a description of class DownloadClient here.
 *
 * SOFIAN SO
 * STUDENT ID: 102261210
 */

import java.net.*;   
import java.io.*;
import java.util.Scanner;
 
public class DownloadClient{
    private Scanner userInput;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private InputStream inputFromServer = null;
    private OutputStream outputToServer = null;
    private FileOutputStream fileOS    = null;
    private BufferedOutputStream bufferedOS = null;
    private String run               = "";
    private String fileName          = "";
    private String confirmation      = "";

    
public DownloadClient (InetAddress serverAddress, int serverPort, InetAddress localAddress, int localPort) {
    try{
        while(!run.equalsIgnoreCase("N")){
//          InetAddress address = serverAddress;
//          int serverport = 2021;
//          InetAddress locAddress = localAddress;
//          int localPort = locPort;
    
             //prompts user for filename to be downloaded from server
            userInput = new Scanner(System.in);
            System.out.println("ENTER FILE NAME");
            fileName = userInput.nextLine();
          
            Socket socket = new Socket(serverAddress, serverPort, localAddress, localPort);
            System.out.println("You are now connected to the server");
 
            //get streams
            InputStream inFromServer = socket.getInputStream();
            DataInputStream incomingData = new DataInputStream(inFromServer);
            OutputStream outToServer = socket.getOutputStream();
            DataOutputStream outgoingData = new DataOutputStream(outToServer);
               
                //send out filename request to server
                dataOutputStream.writeUTF(fileName);
                System.out.println("Requesting the file...");

                //Server confirms whether file exists or not
                confirmation = dataInputStream.readUTF();
                //if requested file is found in server
                if(confirmation.equals("found")){
                    System.out.println("File found on server. Starting download.");
                    byte[] contents = new byte[1];
                    fileOS = new FileOutputStream("Server Downloaded Files/" + fileName);
                    bufferedOS = new BufferedOutputStream(fileOS);

                    int bytesRead = 0; 
                    while((bytesRead=inputFromServer.read(contents))!=-1){
                        bufferedOS.write(contents, 0, bytesRead); 
                    }
                    bufferedOS.flush(); 
                    socket.close(); 
                    System.out.println("File downloaded successfully!");
                    System.out.println("You can check the file now.");
                }
                else{
                    //if not found, display this message.
                    System.out.println("File not found.");
                }
    
                //close
                System.out.println("+++Closing connection to server+++");
                outputToServer.close();
                inputFromServer.close();
                socket.close();
                System.out.println("Successful");
                
                //asks user if they wish to continue
                System.out.println("Continue?(Y/N)");
                run = userInput.nextLine();
                serverPort += 1;
                
                //if user chooses to stop, display this message.
                if(run.equalsIgnoreCase("N")){
                    System.out.println("Exit program.");
                }
            }
    
       } catch(UnknownHostException e){
            System.out.println("Uknown Host Exception");
        } catch(SocketException e){
            System.out.println("Socket-related Exception");
        } catch(IOException e){
            System.out.println("Input-output Exception");
        }
}
    
    public static void main(String[]args){
        int localPort           = 0;
        int serverPort                = 0;
        InetAddress serverAddress     = null;
        InetAddress localAddrress   = null;
        try{
            System.out.println("Author: Sofian So");
            serverPort = Integer.parseInt(args[0]);   //first main argument
            localPort = Integer.parseInt(args[1]);  //second main argument
            serverAddress = InetAddress.getByName(args[2]);   //default
            localAddrress = InetAddress.getByName("localhost"); //default
            DownloadClient client = new DownloadClient(serverAddress, serverPort, localAddrress, localPort);
        } catch (Exception e){
      //catch exception when user does not enter argument on main
            System.out.println("Please enter two arguments ( [Server Port][Local Port] [localhost]) ");        }
    }

}

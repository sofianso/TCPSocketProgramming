
/**
 * Write a description of class FileServer here.
 *
 * SOFIAN SO
 * STUDENT ID: 102261210
 */

import java.net.*;   
import java.io.*;
 
public class FileServer{

    private ServerSocket serverSocket   = null;
    private Socket socket               = null;
    private DataInputStream  input      = null;
    private DataOutputStream output     = null;
    private String message              = "";
    private String filename             = "";

    public FileServer (int portNumber) throws Exception{
    serverSocket = new ServerSocket(portNumber);
    InetAddress ip = InetAddress.getLocalHost();
    InetAddress ipAddress = InetAddress.getLocalHost();
    System.out.println("Server information: \nLocal IP: " + ipAddress + "\nPort: "  + serverSocket.getLocalPort());
    System.out.println("Waiting for DownloadClient");

    Socket socket = serverSocket.accept();
    System.out.println("Client: " + socket.getInetAddress() + " Port: " + socket.getPort()); //retrieves client's IP address
    System.out.println("You are now connected");
    
            InputStream inFromClient = socket.getInputStream();
            DataInputStream dataInput = new DataInputStream(inFromClient);
            OutputStream outToClient = socket.getOutputStream();
            DataOutputStream dataOutput = new DataOutputStream(outToClient);
    
            String fileName = "";
            fileName = dataInput.readUTF();
            System.out.println("Requesting file: " + fileName + " and now Searching...");
            
              //search for file in directory
            File serverFiles = new File("Server Files/");
            File[] listOfFiles = serverFiles.listFiles();
            boolean isFound = false;
            for (int i = 0; i < listOfFiles.length; i++) {
                if (fileName.equals(listOfFiles[i].getName())){
                    System.out.println("File found: " + listOfFiles[i].getName());
                    isFound = true;
                    break;
                }
            }
    
            if (isFound == true){
                dataOutput.writeUTF("found");      //tells client that the file is found
                System.out.println("Processing file transfer");
                //retrieves file from directory
                File file = new File("Server Files/" + fileName);   
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                byte[] contents;
                long fileLength = file.length(); 
                long current = 0;
                while(current!=fileLength){ 
                    int size = 10000;
                    if(fileLength - current >= size)
                        current += size;    
                    else{ 
                        size = (int)(fileLength - current); 
                        current = fileLength;
                    } 
                    contents = new byte[size]; 
                    bis.read(contents, 0, size);    //reads file in bytes
                    outToClient.write(contents);    //sends out to client in bytes
                }
                dataOutput.close();
                System.out.println("File transfer completed!");
            }
            else {
                dataOutput.writeUTF("nfound");     //tells client that the file is not found
                dataOutput.close();
                System.out.println("File not found.");
            }
    
    
    socket.close();
    System.out.println("Socket is closed.");
}
    
    public static void main(String[]args) throws Exception {
        System.out.println("Author: Sofian So");
        FileServer server = new FileServer(2021);   //server port number
    }
}

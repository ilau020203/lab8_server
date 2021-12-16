package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Main {
    static int count = 0;
    static Socket socket = null;
    static ServerSocket server = null;

    public static void serverWork() throws Exception {
        try {
            server = new ServerSocket(20, 1);
            while (true) {
                socket = server.accept();

                // read file for sending
                BufferedReader inputMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // write message of sending status
                BufferedWriter outputMessage = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String filename = "Files/" + inputMessage.readLine();
                File file = new File(filename);

                if (file.exists()) {
                    outputMessage.write("You load file:: " + filename + "\n");
                    outputMessage.flush();

                    //for convert file to byte-array
                    FileInputStream fileStream = new FileInputStream(file);
                    long length = file.length();
                    byte[] bytes = new byte[(int) length];
                    int count;

                    //for send file
                    DataOutputStream outputFile = new DataOutputStream(socket.getOutputStream());
                    outputFile.writeLong(length);
                    while ((count = fileStream.read(bytes)) != -1) {
                        outputFile.write(bytes, 0, count);
                    }

                    fileStream.close();
                    outputFile.close();

                } else {
                    outputMessage.write("NOT_EXIST");
                    outputMessage.flush();
                }
                socket.close();
            }
        }
        catch (Exception e) {
            server.close();
            serverWork();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Server start");
        serverWork();
    }

}

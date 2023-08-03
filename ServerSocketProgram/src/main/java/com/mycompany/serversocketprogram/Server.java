/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.serversocketprogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mkdogan
 * This program is a server part of a server-client program
 * Commands can send via telnet
 * You can assign ports as arguments 
 * The default port is 7755
 * If you need help with commands, use "?"
 */
public class Server {

    public static void main(String[] args) throws IOException {

        int firstArg = 7755;
        if (args.length > 0) {
            try {
                firstArg = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
        }

        ServerSocket serverSocket = null;
        String clientCommand = null;

        try {
            serverSocket = new ServerSocket(firstArg);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        System.out.println("Server is ready for connection");

        while (true) {
            Socket clientSocket = null;
            clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (!clientSocket.isClosed()) { // when client socket is open enters
                clientCommand = in.readLine();
                if (clientCommand != null) {
                    switch (clientCommand) {
                        case "?" -> {
                            out.println("    date --         shows current date");
                            out.println("    hostname --     gets hostname");
                            out.println("    pwd --          prints working directory");
                            out.println("    whoami --       prints users account name");
                            out.println("    exit --         exits");
                            System.out.println("Client command = " + clientCommand);
                            
                        }

                        case "date" -> {
                            Date date = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                            String currentDate = formatter.format(date);
                            out.println(currentDate);
                            System.out.println("Client command = " + clientCommand);
                            
                        }

                        case "hostname" -> {
                            out.println(InetAddress.getLocalHost().getHostName());
                            System.out.println("Client command = " + clientCommand);
                            //out.flush();
                        }

                        case "pwd" -> {
                            out.println(System.getProperty("user.dir"));
                            System.out.println("Client command = " + clientCommand);
                            
                        }

                        case "whoami" -> {
                            out.println(System.getProperty("user.name"));
                            System.out.println("Client command = " + clientCommand);
                            
                        }

                        case "exit" -> {
                            System.out.println("Client command = " + clientCommand);
                            clientSocket.close();
                            break; // closes the client socket and goes to firs while loop to open the client socket and wait for a client socket connection. This allows the server not to go down when all connections are closed. 
                            //This provides to open and close the connection multiple times.
                            
                        }
                        default -> {
                            out.println("Client: " + clientCommand);
                            System.out.println("Client command = " + clientCommand);
                        }
                    }
                    out.flush();
                } 
                else {
                    break; // if the program has closed in unexpected way, it enters an infinite loop in inner while. With this break, the program waits in outer loop without infinite loop.
                }
            }
        }
    }
}

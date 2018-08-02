package com.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Start of main");
        if (args.length < 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);
        ExecutorService executor = null;
        //System.out.println("Server stared, Listening on port 8005");
        //int portNumber = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
            executor = Executors.newFixedThreadPool(5);
            System.out.println("Waiting for clients");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Runnable worker = new RequestHandler(clientSocket);
                executor.execute(worker);
            }
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            System.out.println("Client connected on port" + portNumber + ". Servicing requests");
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                System.out.println("Received message: " + inputLine + " from " + clientSocket.toString() );
//                out.println(inputLine);

        } catch (IOException e) {
            System.out.println("Exception caught when trying ot listen on port "
                    + portNumber + "or listening for a connection");
            System.out.println(e.getMessage());
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }

    }
}

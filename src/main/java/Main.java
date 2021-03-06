import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler implements Runnable {
    final Socket socket;
    final BufferedReader inputStream;
    final PrintWriter outputStream;

    public ClientHandler(Socket socket, BufferedReader inputStream, PrintWriter outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            String inputString = null;
            try {
                inputString = inputStream.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(inputString);
            System.out.println("READLINE");
            outputStream.println("+PONG");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            while (true) {
                // Wait for connection from client.
                clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                Thread thread = new Thread(new ClientHandler(clientSocket, in, out));

                thread.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}

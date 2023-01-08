import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Main {
    //adres ip serwera tcp w zadaniu
    public static String SERVER_IP = "172.21.48.7";
    //port serwera tcp w zadaniu
    public static int PORT_TCP = 15559;
    //flaga do wysłania do serwera tcp z zadania
    private static final String HANDSHAKE = "187088";

    /**
     * twój adres ip, wpisz w konsoli cmd ipconfig i wklej tu swój adres ip
     */
    static String localAddress = "";

    public static void main(String[] args) throws IOException {
        //DO NOT TOUCH [START]
        //create socket udp
        DatagramSocket socket = new DatagramSocket();

        //create socket tcp
        logReceive("connecting to the server: " + SERVER_IP + ":" + PORT_TCP);
        Socket clientSocket = new Socket(SERVER_IP, PORT_TCP);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // sending first line handshake
        logReceive("Sending handshake " + HANDSHAKE);
        out.println(HANDSHAKE);

        logReceive("Client connected from: " + clientSocket.getInetAddress().toString() +
                ":" + clientSocket.getPort());

        // sending ip:port
        String ipport = localAddress + ":" + socket.getLocalPort();
        logReceive("Sending ip:port " + ipport);
        out.println(ipport);

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        //DO NOT TOUCH [STOP]

        //przykład
        //aby odebrać dane z serwera udp:
        String data = receive(socket, packet);

        //aby wysłać dane do serwera udp:
        String toSend = "to send";
        response(socket, packet, toSend);

        //DO NOT TOUCH [START]
        //closing socets
        logReceive("Client socket closing");
        clientSocket.close();
        socket.close();
        logReceive("Ending");
        //DO NOT TOUCH [STOP]
    }

    public static void logReceive(String message) {
        System.out.println("[Receive]: " + message);
    }

    //get from server
    public static String receive(DatagramSocket socket, DatagramPacket packet) throws IOException {
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        logReceive(received.strip());
        return received.strip();
    }

    //send response to server
    public static void response(DatagramSocket socket, DatagramPacket packet, String toSend) throws IOException {
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        DatagramPacket response = new DatagramPacket(toSend.getBytes(StandardCharsets.UTF_8), toSend.length(), address, port);
        logSend(toSend);
        socket.send(response);
    }

    public static void logSend(String message) {
        System.out.println("[Send]: " + message);
    }
}
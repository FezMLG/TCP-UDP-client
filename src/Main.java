import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Main {
//    https://pastebin.com/raw/N21Tymr6
// https://github.com/PatrykTopolski/tcp_baza/blob/master/src/main/java/ClientUdpTcp.java

    public static int PORT_TCP = 8089; //?
    public static int PORT_UDP = 8090; //?
    public static String SERVER_IP = "localhost";
    private static final String HANDSHAKE = "3243423423423";

    public static void main(String[] args) throws IOException {
        //create socket udp
        DatagramSocket socket = new DatagramSocket();

        //create socket tcp
        InetAddress serverIp = InetAddress.getByName(SERVER_IP); // DNS
        log("IP address for server: " + serverIp.toString());
        log("Client socket opening - connecting to the server");
        Socket clientSocket = new Socket(serverIp, PORT_TCP);
        log("Client connected from: " + clientSocket.getInetAddress().toString() +
                ":" + clientSocket.getPort());
        log("Streams collecting");

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // sending first line handshake
        log("Sending handshake " + HANDSHAKE);
        out.println(HANDSHAKE);

        // sending ip:port
        String ipport = socket.getLocalAddress() + ":" + socket.getLocalPort();
        log("Sending ip:port " + ipport);
        out.println(ipport);


        // get first line from udp
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // receive packet
        socket.receive(packet);

        // get data from packet
        byte[] data = packet.getData();
        int length = packet.getLength();
        String str = new String(data, StandardCharsets.UTF_8);
        for (int i = 0; i < 7; i++) {
            str = concatenate(str, str);
        }

        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        // create response packet
        DatagramPacket response = new DatagramPacket(str.getBytes(StandardCharsets.UTF_8), length, address, port);

        // send response
        socket.send(response);

        //closing socets
        log("Client socket closing");
        clientSocket.close();
        socket.close();
        log("Ending");

    }

    public static String concatenate(String str1, String str2) {
        return str1 + str2;
    }

    public static void log(String message) {
        System.out.println("[S]: " + message);
        System.out.flush();
    }
}
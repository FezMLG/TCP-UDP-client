import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Main {
    //flaga do wysłania do serwera tcp z zadania
    private static final String HANDSHAKE = "187088";
    //adres ip serwera tcp w zadaniu
    public static String SERVER_IP = "172.21.48.7";
    //port serwera tcp w zadaniu
    public static int PORT_TCP = 15559;
    /**
     * twój adres ip, wpisz w konsoli cmd ipconfig i wklej tu swój adres ip
     * poradnik jak znaleśc:
     * https://www.med.unc.edu/it/guide/operating-systems/how-do-i-find-the-host-name-ip-address-or-physical-address-of-my-machine/
     */
    static String localAddress = "";

    public static void main(String[] args) throws IOException {
        //DO NOT TOUCH [START]
        //create socket udp
        DatagramSocket udpSocket = new DatagramSocket();

        //create socket tcp
        log("Connecting to the server: " + SERVER_IP + ":" + PORT_TCP);
        Socket tcpSocket = new Socket(SERVER_IP, PORT_TCP);
        log("Client connected from: " + tcpSocket.getInetAddress().toString() + ":" + tcpSocket.getPort());
        PrintWriter out = new PrintWriter(tcpSocket.getOutputStream(), true);

        // sending first line handshake
        logSend("Handshake " + HANDSHAKE);
        out.println(HANDSHAKE);

        // sending ip:port
        // zadanie 1
        String ipport = localAddress + ":" + udpSocket.getLocalPort();
        logSend("ip:port " + ipport);
        out.println(ipport);

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        //DO NOT TOUCH [STOP]

        //przykład
        //aby odebrać dane z serwera udp:
        String data = receive(udpSocket, packet);

        //aby wysłać dane do serwera udp:
        String toSend = "to send";
        response(udpSocket, packet, toSend);

        //DO NOT TOUCH [START]
        //closing socets
        logReceive("Client socket closing");
        tcpSocket.close();
        udpSocket.close();
        logReceive("Ending");
        //DO NOT TOUCH [STOP]
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

    public static void logReceive(String message) {
        System.out.println("[Receive]: " + message);
    }

    public static void log(String message) {
        System.out.println("[LOG]: " + message);
    }
}
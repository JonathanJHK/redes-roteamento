package redes.roteamento.util;

/*
*	Classe SendPackage padroniza o mecanismo de envio de pacotes.
*	essa classe herda de pacote
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class SendPackage extends Package {

    public static DatagramPacket receivePacket;
    public static DatagramSocket socket;
    public static BufferedReader inFromUser;
    public static byte[] sendData = new byte[1024];
    public static byte[] receiveData = new byte[1024];
    public static InetAddress IPAddress;

    /*
     *	Método de preparação de pacotes
     */
    public static void preparing() throws Exception {
        inFromUser = new BufferedReader(new InputStreamReader(System.in));
        socket = new DatagramSocket();
        IPAddress = InetAddress.getByName(router);
    }

    /*
     *	Método de envio de pacotes
     */
    public static void forwarding(byte[] sendData) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, InetAddress.getByName(router), Integer.parseInt(port));
        
        System.out.println("Enviando pacote UDP para " + router + ":" + port);
        
        socket.send(sendPacket);
    }

    /*
     *	Método de reenvio de pacotes
     */
    public static void reforwarding(byte[] sendData, int port) throws IOException {
        IPAddress = receivePacket.getAddress();
  
        DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IPAddress, port);

        socket.send(sendPacket);
    }
}

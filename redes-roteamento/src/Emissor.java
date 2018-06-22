
/*
*	Classe que representa o cliente ou emissor
*   essa classe herda de SendPackage
 */
import java.io.*;
import java.net.*;
import java.util.*;
import redes.roteamento.util.SendPackage;

public class Emissor extends SendPackage {

    /* Método principal 
	*  Faz a comunicação com o roteador, enviando um pacote	
     */
    public static void main(String args[]) throws Exception {
        input(args);
        preparing();

        forwarding(message.getBytes());
        forwarding(address.getBytes());
        forwarding(destiny.getBytes());

        DatagramPacket receivePacket = new DatagramPacket(receiveData,
                receiveData.length);

        socket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());

        socket.close();
        System.out.println("Socket cliente fechado!");
    }

    /* 
	*	Método input, responsavel por trata do recebimento dos dados pelo terminal	
     */
    public static void input(String[] array) {
        if (array.length == 0) {
            System.out.println("Obrigatorio, argumento\n exemplo: (ex) java UDPClient.java  127.0.0.1  12345  10.0.0.5   1.2.3.4  Hello");
            System.exit(0);
        }
        router = array[0];
        port = array[1];
        address = array[2];
        destiny = array[3];
        message = "";
        for (int i = 4; i != array.length; i++) {
            message += array[i] + " ";
        }
    }

}

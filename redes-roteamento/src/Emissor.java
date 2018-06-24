
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
        boolean contin = true;
        Scanner input = new Scanner(System.in);

        while (contin) {
            System.out.println("Informe o roteador, a porta, o endereço de origem, o endereço de destino:");

            router = input.next();
            port = input.next();
            address = input.next();
            destiny = input.next();
            input.nextLine();
            System.out.println("Informe a mensagem:");

            message = input.nextLine();

            preparing();

            forwarding(message.getBytes());
            forwarding(address.getBytes());
            forwarding(destiny.getBytes());

            DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);

            socket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());

            socket.close();
            System.out.println("Socket cliente fechado!");
        }

    }

}

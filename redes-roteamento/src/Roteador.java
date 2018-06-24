/*
*	Classe que representa o roteador
*   essa classe estende de SendPackage
 */
import java.io.*;
import java.net.*;
import java.util.Arrays;
import redes.roteamento.util.Route;
import redes.roteamento.util.SendPackage;
import redes.roteamento.util.Table;

class Roteador extends SendPackage {

    static int port;
    static Table table;
    static String sendMessage;
    static int ttl;

    /*
     *	Método principal da classe
     */
    public static void main(String args[]) throws Exception {
        input(args);

        socket = new DatagramSocket(port);
        started();
    }

    /*
     * 	Método started, responsavel por monitorar os pedidos de conexão 
     */
    static void started() throws IOException {
        while (true) {
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            System.out.println("Esperando por datagrama UDP na porta " + port);
            System.out.println();

            //receber os datagrama
            receiving();

            if (ttl == 0) {
                System.out.println("> Time to Live exceeded in Transit, dropping packet for " + destiny);
            } else {
                Route route = table.route(destiny);

                String ttlSend = String.valueOf(ttl);

                if (route.network.equals("default")) {
                    if (table.valueDef != null) {
                        System.out.println("> forwarding packet for " + destiny + " to next hop " + table.valueDef.network + " over interface " + table.valueDef.intface);

                        //preparando a mensagem pra ser enviado
                        sendMessage = ttlSend + ";" + address + ";" + destiny + ";" + message;

                        reforwarding(sendMessage.getBytes(), Integer.parseInt(table.valueDef.intface));
                    } else {
                        System.out.println("> destination " + destiny + " not found in routing table, dropping packet ");
                    }
                } else {
                    if (route.gateway.equals("0.0.0.0")) {
                        System.out.println("> destination reached. From " + address + " to " + destiny + " : " + message);
                    } else {
                        System.out.println("> forwarding packet for " + destiny + " to next hop " + route.network + " over interface " + route.intface);

                        //preparando a mensagem pra ser enviado
                        sendMessage = ttlSend + ";" + address + ";" + destiny + ";" + message;
                        reforwarding(sendMessage.getBytes(), Integer.parseInt(route.intface));
                    }

                }
            }

            System.out.println();

        }
    }

    /*
     *	Método change, resposavel por recuperar dados recebidos
     *	sempre que o servidor recebe um dado ele tém 1024 bytes,
     *	sendo assim, deve ser recuperado para o seu tamanho normal.
     */
    static String change(String value) {
        String other = "";

        for (int i = 0, size = value.length(); i != size; i++) {
            if ((int) value.charAt(i) > 45 && (int) value.charAt(i) < 58) {
                other += value.charAt(i);
            }
        }

        destiny = "";
        for (int i = 0, size = other.length(); i != size - 1; i++) {
            destiny += other.charAt(i);
        }

        return destiny;
    }

    /*
     *	Método receiving, responsavel por receber o pacote vindo do cliente
     */
    static void receiving() throws IOException {
        String recMessage;
        message = new String();
        socket.receive(receivePacket);
        //System.out.print("Datagrama UDP messagem  recebido...");
        
        //Tratando e organizando as mensagens recebida
        recMessage = new String(receivePacket.getData()).trim();
        
        String[] dataMessage = recMessage.split(";|;\\s"); 
//        System.out.println("menssagem recebida -> "+Arrays.toString(dataMessage));
       
        ttl = Integer.parseInt(dataMessage[0]) - 1;
        address = dataMessage[1];
        destiny = dataMessage[2];
        message = dataMessage[3];
        

    }

    /* 
     *	Método input, responsavel por trata do recebimento dos dados pelo terminal	
     */
    static void input(String[] array) {
        if (array.length == 0) {
            System.out.println("É necessário ter argumento");
            System.exit(0);
        }

        port = Integer.parseInt(array[0]);

        table = new Table(array.length - 1);

        for (String value : array) {
            table.add(value.split("/"));
        }

    }
}

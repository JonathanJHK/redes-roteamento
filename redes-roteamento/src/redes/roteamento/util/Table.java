/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes.roteamento.util;

/*
*	Classe que representa a tabela
 */
public class Table {

    public Route[] route;
    public Route valueDef;
    public int index = 0;

    /*
	*	Método construtor
     */
    public Table(int size) {
        route = new Route[size];
    }

    /*
	*	Método add, adiciona uma nova rota na tabela
     */
    public void add(String[] array) {
        if (array.length == 1) {
            return;
        }

        route[index++] = new Route(array);

        if (route[index - 1].routeDefault()) {
            valueDef = route[index - 1];
        }
    }

    /*
	*	Método route, responsavel por decidir o roteamento
     */
    public Route route(String ip) {
        Route choice = new Route("default");

        for (Route value : route) {
            //System.out.println(networkCalculation(ip, value.mask) + " ---- " + value.mask);
            //System.out.println(networkCalculation(value.network, value.mask));
            //System.out.println(networkCalculation(ip, value.mask) == networkCalculation(value.network, value.mask));
            if (networkCalculation(ip, value.mask).equals(networkCalculation(value.network, value.mask))) {
                choice = choose(value, choice);
            }
        }

        return choice;
    }

    /*
	*	Método choose, define a prioridade entre rotas, como destino.
     */
    private Route choose(Route value, Route choice) {
        if (choice.network.equals("default")) {
            return value;
        }

        if (value.iMask > choice.iMask) {
            return value;
        }

        return choice;
    }

    private String networkCalculation(String ip, int[] mask) {
        String input = ip;
        /*A "\" é um caracter especial de Strings e de expressoes regulares, 
            se nao me engano sao mesmo precisas as 4 barras. 
         */
        String[] firstParse = input.split("\\\\");

        /* Novamente o ponto e' um caracter especial para expressoes regulares
            e tem de ser precedido de uma "\" para ser considerado um caracter normal.*/
        String[] ipParse = firstParse[0].split("\\.");
        int[] decimal = new int[4];//vector que ira' conter os decimais

        for (int z = 0; z < ipParse.length; z++) { // tambem se pode usar 3 directamente uma vez que um IP tem sempre 4 octetos.
            decimal[z] = Integer.parseInt(ipParse[z]);// tens agora um vector com os 4 octetos em decimal         
        }

        //AND logico, com reutilizacao de variavel
        decimal[0] = decimal[0] & mask[0];
        decimal[1] = decimal[1] & mask[1];
        decimal[2] = decimal[2] & mask[2];
        decimal[3] = decimal[3] & mask[3];

        return decimal[0] + "" + decimal[1] + "" + decimal[2] + "" + decimal[3];
    }
}

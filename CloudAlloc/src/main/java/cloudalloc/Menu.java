/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

/**
 *
 * @author Luís Alves
 */
public class Menu {


    public static void show(int n) {
        switch(n) {
            case 0 : System.out.println("******************\n" +
                                        "1) Iniciar Sessão\n" +
                                        "0) Sair\n" +
                                        "******************");
                     break;
            case 1 : System.out.println("**********************\n" +
                                        "1) Pedir Servidor\n" +
                                        "2) Leilão de Servidor\n" +
                                        "3) Consultar dívida\n" +
                                        "4) Libertar Servidor\n" +
                                        "0) Sair\n" +
                                        "**********************");
                     break;
        }
    }
}

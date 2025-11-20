import java.util.Scanner;
public class GestionDeEntradas {
    public static void menuPrincipal(Scanner sc){
        System.out.println("Bienvenido al gestion de venta de entradas al cine! Elige una de las opciones: \n 1. Invitado. \n 2. Cuenta admin.");
        int el1 = sc.nextInt();
        if(el1 == 1){
            cuentaInvitado();
        }
        else if(el1 == 2){
            cuentaAdmin();
        }
        else{
            System.out.println("Solo se acepta 1 o 2");
            menuPrincipal(sc);
        }
    }

    public static void cuentaInvitado(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Elige una de las opciones: \n1. Comprar entradas. \n2. Devolución de entradas. \n3. Ir atrás.");
        int el2 = sc.nextInt();
        if(el2 == 1){
            comprarEntradas();
        }
        else if(el2 == 2){
            devolucionEntradas();
        }
        else if(el2 == 3){
            menuPrincipal(sc);
        }
        else{
            System.out.println("Solo se acepta 1 o 2 o 3");
            cuentaInvitado();
        }
    }
    public static void comprarEntradas(){

    }
    public static void devolucionEntradas(){

    }
    
    public static void cuentaAdmin(){

    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        menuPrincipal(sc);
    }
}


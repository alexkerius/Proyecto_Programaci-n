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

    }
    public static void cuntaAdmin(){

    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        menuPrincipal(sc);
    }
}

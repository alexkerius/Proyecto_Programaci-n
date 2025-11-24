import java.util.Scanner;
public class GestionDeEntradas {
    static double precio = 5;
    static int[][][] asientosPorPeli = new int[5][10][8];
    static String[][] horariosPorPeli = {
        {"10:00", "12:00", "13:00", "16:00", "20:00"},
        {"9:00",  "11:00", "12:00", "14:00", "17:00"},
        {"10:00", "12:30", "14:30", "16:00", "18:00"},
        {"09:30", "12:00", "15:00", "19:00", "21:00"},
        {"11:00", "13:30", "15:30", "17:00", "19:00"}
    };
    static int[][] compras = new int[400][7];
    
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
        Scanner sc = new Scanner(System.in);
        System.out.println("Que película quieres ver?\n1.Pelicula 1\n2.Pelicula 2\n3.Pelicula 3\n4.Pelicula 4\n5.Pelicula 5");
        int el3 = sc.nextInt();
        if(el3 != 1 && el3 != 2 && el3 != 3 && el3 != 3 && el3 != 4 && el3 != 5){
            System.out.println("Solo se acepta 1,2,3,4,5");
            comprarEntradas();
        }
        System.out.println("Elige asientos:\nNota: Para eligir asiento, tienes que entrar dos numeros: primero corresponde a fila y el segundo a asiento. Si ya has elegido el numero de asientos suficientes, inserta 0 para seguir al siguente paso.");
        while(true){
            printSeats(asientosPorPeli[el3-1]);
            compras[indexDePrimerNoCero(3)][3] = el3;
            int asiento = sc.nextInt();
            if(asiento == 0){
                break;
            }
            compras[indexDePrimerNoCero(4)][4] = asiento;
            asientosPorPeli[el3-1][(asiento/10)-1][(asiento%10)-1] = 1;
        } 
    }

    public static void printSeats(int[][] hall) {
        int rows = hall.length;
        int cols = hall[0].length;
        System.out.print("    ");
        for (int j = 1; j <= cols; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " | ");
            for (int j = 0; j < cols; j++) {
                System.out.print(hall[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int indexDePrimerNoCero(int k){
        for(int i = 0; i < compras.length; i++){
            if(compras[i][k] == 0){
                return i;
            }
        }
        return -1;
    }


    public static void devolucionEntradas(){

    }

  public static void cuentaAdmin(){
    Scanner sc = new Scanner(System.in);
    System.out.println("Inserte la contraseña");
    String cont = sc.nextLine();
    if (cont.equals("234"))
      adminStats();
    else{
      System.out.println("Contraseña incorrecta \n");
      cuentaAdmin();
    }
  }
  
  public static void adminStats(){
    System.out.println("\nBienvenido a las estadísticas de admin. Eliga una de las opciones: \n 1. Entradas vendidas. \n 2. Ingresos diarios. \n 3. Volver");
    Scanner sc = new Scanner(System.in);
    int stats = sc.nextInt();
    switch (stats){
      case 1:
        entradasVendidas();
        break;
      case 2:
        System.out.println("Ingresos diarios");
        break;
      case 3:
        System.out.println("Volver al menú principal");
        break;
      default:
        System.out.println("Solo se acepta 1, 2 o 3");
    }
  }
  
  public static void entradasVendidas(){
    System.out.println("\nSeleccione el día de las entradas vendidas: \n 1. Hoy \n 2. Ayer \n 3. Volver");
    Scanner sc = new Scanner(System.in);
    int día = sc.nextInt();
    int entradasHoy = (int)(Math.random() * 101); //Hay que sumar las entradas del usuario.
    int entradasAyer = (int)(Math.random() * 101);
    switch (día){
      case 1:
        ventasHoy(entradasHoy, entradasAyer); 
        break;
      case 2:
        ventasAyer(entradasHoy, entradasAyer); 
        break;
      case 3:
        adminStats();
      default:
        System.out.println("Solo se acepta 1, 2 o 3");
    }
  }
  
  public static void ventasHoy(int vh, int va){
    System.out.println("\nEntradas vendidas hoy: "+vh);
    System.out.println("Aumento respecto ayer: "+(vh*100/va));
    System.out.println(" Pulse 1 para volver a atrás. \n Pulse 2 para volver al menú principal");
    Scanner sc = new Scanner(System.in);
    int op = sc.nextInt();
    if (op==1)
      entradasVendidas();
    else if (op==2)
        System.out.println("Volver al menú principal");
    else
      System.out.println("Solo se acepta 1 o 2");
  }

  public static void ventasAyer(int vh, int va){
    System.out.println("\nEntradas vendidas ayer: "+vh);
    System.out.println("Aumento de hoy: "+(vh*100/va));
    System.out.println(" Pulse 1 para volver a atrás. \n Pulse 2 para volver al menú principal");
    Scanner sc = new Scanner(System.in);
    int op = sc.nextInt();
    if (op==1)
      entradasVendidas();
    else if (op==2)
        menuPrincipal(sc);
    else
      System.out.println("Solo se acepta 1 o 2");
  }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        menuPrincipal(sc);
    }
}


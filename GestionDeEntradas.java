import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class GestionDeEntradas {
    static double precioPorEntrada = 5;
    static int[][][][] asientosPorPeli = new int[5][5][10][8];
    static String[][] horariosPorPeli = {
        {"10:00", "12:00", "13:00", "16:00", "20:00"},
        {"9:00",  "11:00", "12:00", "14:00", "17:00"},
        {"10:00", "12:30", "14:30", "16:00", "18:00"},
        {"09:30", "12:00", "15:00", "19:00", "21:00"},
        {"11:00", "13:30", "15:30", "17:00", "19:00"}
    };
    static String[] nombres = new String[2000];
    static String[] apellidos = new String[2000];
    static String[] correos = new String[2000];
    static int[] peliculas = new int[2000];
    static int[] asientosComprados = new int[2000];
    static int[] horario = new int[2000];
    static double[] precios = new double[2000];
    static String[] codigosDePromocion = {"DESC10", "CINE20", "VIP50"};
    static double[] promoDescuentos = {0.1, 0.2, 0.5};
    
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
        sc.nextLine();
        if(el3 != 1 && el3 != 2 && el3 != 3 && el3 != 3 && el3 != 4 && el3 != 5){
            System.out.println("Solo se acepta 1,2,3,4,5");
            comprarEntradas();
        }
        System.out.println("Cuando la quieres ver?");
        for(int i = 0; i < horariosPorPeli[el3-1].length; i++){
            System.out.print(horariosPorPeli[el3-1][i] + " ");
        }
        System.out.println();
        String hora = sc.nextLine();
        int ind = -1;
        for(int i = 0; i < horariosPorPeli[el3-1].length; i++){
            if(hora.equals(horariosPorPeli[el3 - 1][i])){
                ind = i;
            }
        }
        System.out.println();
        boolean flag = true;
        int numeroDeAsientos = 0;
        while(flag){
            System.out.println("Elige asientos:\nNota: Para eligir asiento, tienes que entrar dos numeros: primero corresponde a fila y el segundo a asiento. Si ya has elegido el numero de asientos suficientes, inserta 0 para seguir al siguente paso.");
            printSeats(asientosPorPeli[el3-1][ind]);
            int asiento = sc.nextInt();
            sc.nextLine();
            if(asiento == 0){
                flag = false;
            }
            else{
                if (asiento < 11 || asiento > 108) {  
                    System.out.println("Formato incorrecto. Usa dos dígitos: fila + asiento (ej: 23).");
                    continue;
                }

                else if(asientosPorPeli[el3-1][ind][(asiento/10)-1][(asiento%10)-1] == 1){
                    System.out.println("El asiento ya está ocupado. Vuelve a elegirlo.");
                    continue;
                }
                else{
                    int ind_compra = indexDePrimerNoCero();
                    asientosComprados[ind_compra] = asiento;
                    asientosPorPeli[el3-1][ind][(asiento/10)-1][(asiento%10)-1] = 1;
                    numeroDeAsientos++;
                    horario[ind_compra] = ind; 
                    peliculas[ind_compra] = el3;
                }

            }
        }
        System.out.println("Introduce nombre");
        String nombre;
        do {
            nombre = sc.nextLine();
            if (nombre.isEmpty()) System.out.println("Campo vacío. Inserta el nombre:");
        } while (nombre.isEmpty());

        System.out.println("Introduce apellido");
        String apellido;
        do {
            apellido = sc.nextLine();
            if (apellido.isEmpty()) System.out.println("Campo vacío. Inserta el apellido:");
        } while (apellido.isEmpty());
        System.out.println("Introduce correo");
        String correo;
        do {
            correo = sc.nextLine();
            if (!correo.contains("@") || correo.length() < 5) {
                System.out.println("Correo inválido. Intenta otra vez:");
                correo = "";
            }
        } while (correo.isEmpty());
        for(int i = 0; i < nombres.length; i++){
            if(peliculas[i] == el3 && horario[i] == ind && nombres[i] == null){
                nombres[i] = nombre;
                apellidos[i] = apellido;
                correos[i] = correo;
            }
        }
        System.out.println("Introduce codigo de promocion:\nSi no lo tienes inserte 0");
        String promocion = sc.nextLine();
        double precioFinal = numeroDeAsientos * precioPorEntrada;
        for(int i = 0; i < codigosDePromocion.length; i++){
            if(promocion.equals(codigosDePromocion[i])){
                precioFinal *= promoDescuentos[i];
            }
        }
        for(int i = 0; i < nombres.length; i++){
            if(peliculas[i] == el3 && horario[i] == ind && precios[i] == 0){
                precios[i] = precioFinal / numeroDeAsientos;
            }
        }
        System.out.println(numeroDeAsientos);
        System.out.println(precioFinal);
        guardarDatos();
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

    public static int indexDePrimerNoCero(){
        for(int i = 0; i < asientosComprados.length; i++){
            if(asientosComprados[i] == 0){
                return i;
            }
        }
        return -1;
    }

    public static void devolucionEntradas(){
      System.out.println("\nInserte los datos de su compra: nombre, apellidos y email con el que se realizó la compra.");
      Scanner sc = new Scanner(System.in);
      System.out.println("Inserte su nombre:");
      String nombre = sc.nextLine();
      System.out.println("Inserte sus apellidos:");
      String apellido = sc.nextLine();
      System.out.println("Inserte su email:");
      String email = sc.nextLine();
      boolean flag = true;
      int cnt = 0;
      while(flag && cnt < nombres.length){
        if(nombre.equals(nombres[cnt]) && apellido.equals(apellidos[cnt]) && email.equals(correos[cnt])){
             confirmarDevolucion(cnt);
             flag = false;
        }
        cnt++;
      }
      if (flag == true){
        System.out.println("Datos incorrectos");
        devolucionEntradas();
      }
      
    }

    public static void confirmarDevolucion(int i){
      System.out.println("\nDesea efectuar la devolución de su entrada a " + peliculas[i] + "a las " + horario[i]);
      guardarDatos();
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
            ingresosDiarios();
            break;
          case 3:
            menuPricnipal(sc);
            break;
          default:
            System.out.println("Solo se acepta 1, 2 o 3");
        }
    }
      
    public static void entradasVendidas(){
        System.out.println("\nSeleccione el día de las entradas vendidas: \n 1. Hoy \n 2. Ayer \n 3. Volver");
        Scanner sc = new Scanner(System.in);
        int día = sc.nextInt();
        int entradasHoy = (int)(Math.random() * 101);             //Sustituir por variables no random.
        int entradasAyer = (int)(Math.random() * 101);            //Sustituir por variables no random.
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
          menuPrincipal(sc);
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

    public static void ingresosDiarios(){
            System.out.println("\nSeleccione el día de las entradas vendidas: \n 1. Hoy \n 2. Ayer \n 3. Volver");
        Scanner sc = new Scanner(System.in);
        int día = sc.nextInt();
        int dineroHoy = (int)(Math.random() * 101);             //Sustituir por variables no random.
        int dineroAyer = (int)(Math.random() * 101);            //Sustituir por variables no random.
        switch (día){
          case 1:
            ingresosHoy(dineroHoy, dineroAyer); 
            break;
          case 2:
            ingresosAyer(dineroHoy, dineroAyer); 
            break;
          case 3:
            adminStats();
          default:
            System.out.println("Solo se acepta 1, 2 o 3");}
    }
        
    public static void ingresosHoy(int ih, int ia){
        System.out.println("\nIngresos de hoy: "+ih);
        System.out.println("Aumento respecto ayer: "+(ih*100/ia)+"%");
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

    public static void ingresosAyer(int ih, int ia){
        System.out.println("\nIngresos de ayer: "+ih);
        System.out.println("Aumento de hoy: "+(ih*100/ia)+"%");
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

    public static void escribirFichero(String[][] datos, String separadorColumnas, String nombreFichero){
        try {
            String ruta = System.getProperty("user.dir") + File.separator + nombreFichero;
            FileWriter writer = new FileWriter(ruta);

            for (int i = 0; i < datos.length; i++) {
                String[] fila = datos[i];
                for (int j = 0; j < fila.length; j++) {
                    writer.append(fila[j] == null ? " " : fila[j]);
                    if (j < fila.length - 1)
                        writer.append(separadorColumnas);
                }
                writer.append(System.lineSeparator());
            }
            writer.flush();
            writer.close();

            System.out.println("Archivo guardado en: " + ruta);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[][] leerFichero(String separadorColumnas, String nombreFichero){
        String ruta = System.getProperty("user.dir") + File.separator + nombreFichero;

        List<String[]> list = new ArrayList<String[]>();
        try {
            Scanner scanner = new Scanner(new File(ruta));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] array = line.split(separadorColumnas);
                list.add(array);
            }

            scanner.close();
            System.out.println("Archivo cargado desde: " + ruta);

        } catch (FileNotFoundException e) {
            System.out.println("No existe archivo: " + ruta);
            return null;
        }

        String[][] res = new String[list.size()][];
        for (int i = 0; i < res.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public static void guardarDatos() {
        String[][] tabla = new String[nombres.length][7];

        for (int i = 0; i < nombres.length; i++) {
            tabla[i][0] = nombres[i];
            tabla[i][1] = apellidos[i];
            tabla[i][2] = correos[i];
            tabla[i][3] = String.valueOf(peliculas[i]);
            tabla[i][4] = String.valueOf(asientosComprados[i]);
            tabla[i][5] = String.valueOf(horario[i]);
            tabla[i][6] = String.valueOf(precios[i]);
        }

        escribirFichero(tabla, ";", "datos.txt");
    }

    public static void cargarDatos() {
        String[][] tabla = leerFichero(";", "datos.txt");
        if (tabla == null) return;

        for (int i = 0; i < tabla.length; i++) {
            if (tabla[i].length < 7) continue;

            nombres[i] = tabla[i][0].equals(" ") ? null : tabla[i][0];
            apellidos[i] = tabla[i][1].equals(" ") ? null : tabla[i][1];
            correos[i] = tabla[i][2].equals(" ") ? null : tabla[i][2];

            peliculas[i] = Integer.parseInt(tabla[i][3]);
            asientosComprados[i] = Integer.parseInt(tabla[i][4]);
            horario[i] = Integer.parseInt(tabla[i][5]);
            precios[i] = Double.parseDouble(tabla[i][6]);
        }
    }


    public static void main(String[] args){
        cargarDatos();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < asientosComprados.length; i++) {
            if (asientosComprados[i] != 0) { 
                int pelicula = peliculas[i] - 1;   
                int hor = horario[i];              
                int asiento = asientosComprados[i];
                int fila = (asiento / 10) - 1;
                int col = (asiento % 10) - 1;
                asientosPorPeli[pelicula][hor][fila][col] = 1; 
            }
        }
        menuPrincipal(sc);

    }
}

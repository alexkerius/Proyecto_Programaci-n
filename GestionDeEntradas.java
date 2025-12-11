import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
/**
 * Clase principal para la Gestión de Entradas de Cine.
 * Permite comprar entradas, devolverlas, administrar estadísticas y guardar/cargar datos en ficheros.
 */
public class GestionDeEntradas {
    // Escáner global para leer la entrada del usuario
    static Scanner sc = new Scanner(System.in);
    // Precio base por entrada
    static double precioPorEntrada = 5;
    // Estructura para guardar el estado de los asientos.
    // Dimensiones: [Película][Horario][Fila][Asiento]
    static int[][][][] asientosPorPeli = new int[5][5][10][8];
    // Horarios predefinidos para las 5 películas
    static String[][] horariosPorPeli = {
        {"10:00", "12:00", "13:00", "16:00", "20:00"},
        {"9:00",  "11:00", "12:00", "14:00", "17:00"},
        {"10:00", "12:30", "14:30", "16:00", "18:00"},
        {"09:30", "12:00", "15:00", "19:00", "21:00"},
        {"11:00", "13:30", "15:30", "17:00", "19:00"}
    };
    // Arrays paralelos
    // Estos arrays almacenan la información de cada entrada vendida.
    // Usan el mismo índice 'i' para referirse a la misma transacción.
    static String[] nombres = new String[2000];
    static String[] apellidos = new String[2000];
    static String[] correos = new String[2000];
    static int[] peliculas = new int[2000];
    static int[] asientosComprados = new int[2000];
    static int[] horario = new int[2000];
    static double[] precios = new double[2000];
    // Datos para descuentos
    static String[] codigosDePromocion = {"DESC10", "CINE20", "VIP50"};
    static double[] promoDescuentos = {0.1, 0.2, 0.5};
    /**
     * Muestra el menú principal al iniciar el programa.
     * Separa el flujo entre usuarios normales (invitados) y administradores.
     */
    public static void menuPrincipal(){
        System.out.println("Bienvenido al gestion de venta de entradas al cine! Elige una de las opciones: \n 1. Invitado. \n 2. Cuenta admin.");
        int el1 = sc.nextInt();
        if(el1 == 1){
            cuentaInvitado();
        }
        else if(el1 == 2){
            sc.nextLine(); // Limpiar el buffer del scanner
            cuentaAdmin();
        }
        else{
            System.out.println("Solo se acepta 1 o 2");
            menuPrincipal(); // Recursividad si se equivoca
        }
    }
    /**
     * Menú para el usuario cliente.
     */
    public static void cuentaInvitado(){
        System.out.println("Elige una de las opciones: \n1. Comprar entradas. \n2. Devolución de entradas. \n3. Ir atrás.");
        int el2 = sc.nextInt();
        if(el2 == 1){
            comprarEntradas();
        }
        else if(el2 == 2){
            devolucionEntradas();
        }
        else if(el2 == 3){
            menuPrincipal();
        }
        else{
            System.out.println("Solo se acepta 1 o 2 o 3");
            cuentaInvitado();
        }
    }
    /**
     * Lógica principal de compra.
     * Flujo: Elegir película -> Elegir hora -> Elegir asientos -> Datos personales -> Pago.
     */
    public static void comprarEntradas(){
        System.out.println("Que película quieres ver?\n1.Wicked(For goon)\n2.Barbie\n3.Roblox: The movie\n4.Yago comiendo a Dulce\n5.Pelicula 5");
        System.out.println("Si quieres volver al menu anterior, pulsa -1");
        int el3 = sc.nextInt();
        sc.nextLine(); // Limpiar buffer
        // Si el usuario introduce -1, cancela y vuelve atrás
        if (el3 == -1) {
            int idx = indexDePrimerCero(asientosComprados) - 1;
            if (idx >= 0) {
                borrarCompra(idx); 
            }
            cuentaInvitado();
        }
        // Validación de entrada
        if(el3 != 1 && el3 != 2 && el3 != 3 && el3 != 3 && el3 != 4 && el3 != 5){
            System.out.println("Solo se acepta 1,2,3,4,5");
            comprarEntradas();
        }
        // Mostrar horarios disponibles para la película seleccionada
        System.out.println("Cuando la quieres ver?");
        System.out.println("Si quieres volver al menu anterior, pulsa -1");
        for(int i = 0; i < horariosPorPeli[el3-1].length; i++){
            System.out.print(horariosPorPeli[el3-1][i] + " ");
        }
        System.out.println();
        String hora = sc.nextLine();
        // Lógica de cancelación con -1
        if (hora.equals("-1")) {
            int idx = indexDePrimerCero(asientosComprados) - 1;
            if (idx >= 0) {
                borrarCompra(idx);
            }
            comprarEntradas(); 
        }
        // Buscar el índice del horario seleccionado
        int ind = -1;
        for(int i = 0; i < horariosPorPeli[el3-1].length; i++){
            if(hora.equals(horariosPorPeli[el3 - 1][i])){
                ind = i;
            }
        }
        System.out.println();
        // Bucle de selección de asientos
        boolean flag = true;
        int numeroDeAsientos = 0;
        int[] asientosElegidos = new int[80]; // Temporal para guardar selección actual
        int startIdx = indexDePrimerCero(asientosComprados); // Marca dónde empezamos
        while(flag){
            System.out.println("Elige asientos:\nNota: Para eligir asiento, tienes que entrar dos numeros: primero corresponde a fila y el segundo a asiento. Si ya has elegido el numero de asientos suficientes, inserta 0 para seguir al siguente paso.");
            System.out.println("Si quieres volver al menu anterior, pulsa -1");
            // Muestra la matriz de asientos
            printSeats(asientosPorPeli[el3-1][ind]);
            int asiento = sc.nextInt();
            sc.nextLine();
            // Cancelar compra durante selección de asientos
            if (asiento == -1) {
                // Lógica de reversión de asientos seleccionados
                int idx = indexDePrimerCero(asientosComprados) - 1;
                if (idx >= 0) {
                    // Liberar asientos en la matriz visual
                    for(int i = 0; i < asientosElegidos.length; i++){
                        if(asientosElegidos[i] != 0){
                            asientosPorPeli[el3-1][ind][(asientosElegidos[i] / 10) - 1][(asientosElegidos[i] % 10) - 1] = 0;
                        }
                    }
                    // Borrar datos temporales de los arrays
                    int endIdx = indexDePrimerCero(asientosComprados);  
                    for (int k = startIdx; k < endIdx; k++) {
                        borrarCompra(k);
                    }
                  }
                comprarEntradas();
            }
            // 0 indica que el usuario terminó de elegir asientos
            if(asiento == 0){
                flag = false;
            }
            else{
                // Validación del formato de asiento (ej: 11 a 108, evitando filas/columnas > 10 u 8)
                if (asiento < 11 || asiento > 108) {  
                    System.out.println("Formato incorrecto. Usa dos dígitos: fila + asiento (ej: 23).");
                    continue;
                }
                // Comprobar si está ocupado (valor 1)
                else if(asientosPorPeli[el3-1][ind][(asiento/10)-1][(asiento%10)-1] == 1){
                    System.out.println("El asiento ya está ocupado. Vuelve a elegirlo.");
                    continue;
                }
                else{
                    // Guardar asiento:
                    // 1. En array temporal 'asientosElegidos'
                    asientosElegidos[indexDePrimerCero(asientosElegidos)] = asiento;
                    int ind_compra = indexDePrimerCero(asientosComprados);
                    // 2. En array persistente 'asientosComprados'
                    asientosComprados[ind_compra] = asiento;
                    // 3. Marcar como ocupado en la matriz visual
                    asientosPorPeli[el3-1][ind][(asiento/10)-1][(asiento%10)-1] = 1;
                    numeroDeAsientos++;
                    horario[ind_compra] = ind; 
                    peliculas[ind_compra] = el3;
                }

            }
        }
        // Recogida de datos personales
        // Se piden nombre, apellido y correo con validación de "campo vacío"
        // También incluyen lógica de cancelación (-1) que revierte la compra
        System.out.println("Introduce nombre");
        String nombre;
        do {
            nombre = sc.nextLine();
            // Lógica de reversión
            if (nombre.equals("-1")) {
                int idx = indexDePrimerCero(asientosComprados) - 1;
                if (idx >= 0) {
                    for(int i = 0; i < asientosElegidos.length; i++){
                        if(asientosElegidos[i] != 0){
                            asientosPorPeli[el3-1][ind][(asientosElegidos[i] / 10) - 1][(asientosElegidos[i] % 10) - 1] = 0;
                        }
                    }
                    int endIdx = indexDePrimerCero(asientosComprados);  
                    for (int k = startIdx; k < endIdx; k++) {
                        borrarCompra(k);
                    }
                }
                comprarEntradas(); 
            }
            if (nombre.isEmpty()) System.out.println("Campo vacío. Inserta el nombre:");
        } while (nombre.isEmpty());

        System.out.println("Introduce apellido");
        String apellido;
        do {
            apellido = sc.nextLine();
            // Lógica de reversión
            if (apellido.equals("-1")) {
                int idx = indexDePrimerCero(asientosComprados) - 1;
                if (idx >= 0) {
                    for(int i = 0; i < asientosElegidos.length; i++){
                        if(asientosElegidos[i] != 0){
                            asientosPorPeli[el3-1][ind][(asientosElegidos[i] / 10) - 1][(asientosElegidos[i] % 10) - 1] = 0;
                        }
                    }
                    int endIdx = indexDePrimerCero(asientosComprados);  
                    for (int k = startIdx; k < endIdx; k++) {
                        borrarCompra(k);
                    }
                }
                comprarEntradas(); 
            }
            if (apellido.isEmpty()) System.out.println("Campo vacío. Inserta el apellido:");
        } while (apellido.isEmpty());
        System.out.println("Introduce correo");
        String correo;
        do {
            correo = sc.nextLine();
            // Lógica de reversión
            if (correo.equals("-1")) {
                int idx = indexDePrimerCero(asientosComprados) - 1;
                if (idx >= 0) {
                    for(int i = 0; i < asientosElegidos.length; i++){
                        if(asientosElegidos[i] != 0){
                            asientosPorPeli[el3-1][ind][(asientosElegidos[i] / 10) - 1][(asientosElegidos[i] % 10) - 1] = 0;
                        }
                    }
                    int endIdx = indexDePrimerCero(asientosComprados);  
                    for (int k = startIdx; k < endIdx; k++) {
                        borrarCompra(k);
                    }
                }
                comprarEntradas(); 
            }
            // Validación simple de correo
            if (!correo.contains("@") || correo.length() < 5) {
                System.out.println("Correo inválido. Intenta otra vez:");
                correo = "";
            }
        } while (correo.isEmpty());
        // Actualizar los arrays con los datos personales para todas las entradas de esta sesión
        for(int i = 0; i < nombres.length; i++){
            // Buscamos las entradas que acabamos de crear (que tienen null en nombre)
            if(peliculas[i] == el3 && horario[i] == ind && nombres[i] == null){
                nombres[i] = nombre;
                apellidos[i] = apellido;
                correos[i] = correo;
            }
        }
        // Cálculo de precios y promociones
        System.out.println("Introduce codigo de promocion:\nSi no lo tienes inserte 0");
        String promocion = sc.nextLine();
        
        double precioFinal = numeroDeAsientos * precioPorEntrada;
        // Aplicar descuento si coincide el código
        for(int i = 0; i < codigosDePromocion.length; i++){
            if(promocion.equals(codigosDePromocion[i])){
                precioFinal -= precioFinal * promoDescuentos[i];
            }
        }
        // Guardar el precio individual pagado en el array 'precios'
        for(int i = 0; i < nombres.length; i++){
            if(peliculas[i] == el3 && horario[i] == ind && precios[i] == 0){
                precios[i] = precioFinal / numeroDeAsientos;
            }
        }
        System.out.println("Numeros de asientos reservados " + numeroDeAsientos);
        System.out.println("Precio final pagado por entradas " + precioFinal);
        // Guardar cambios en el fichero físico
        guardarDatos();
    }
    /**
     * Imprime visualmente la matriz de asientos (sala de cine).
     */
    public static void printSeats(int[][] hall) {
        int rows = hall.length;
        int cols = hall[0].length;
        System.out.print("    "); // Espaciado inicial
        for (int j = 1; j <= cols; j++) {
            System.out.print(j + " "); // Números de columna
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + " | "); // Números de fila
            for (int j = 0; j < cols; j++) {
                System.out.print(hall[i][j] + " "); // Estado del asiento (0 o 1)
            }
            System.out.println();
        }
    }
    /**
     * Utilidad para encontrar el primer hueco vacío en un array.
     * Esencial para añadir nuevos registros.
     */
    public static int indexDePrimerCero(int arr[]){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == 0){
                return i;
            }
        }
        return -1; // Array lleno
    }
    /**
     * Borra lógicamente una compra poniendo todos sus campos a null o 0.
     */
    public static void borrarCompra(int index) {
        nombres[index] = null;
        apellidos[index] = null;
        correos[index] = null;
        peliculas[index] = 0;
        asientosComprados[index] = 0;
        horario[index] = 0;
        precios[index] = 0;
    }
    /**
     * Busca entradas compradas por nombre/apellido/email y permite eliminarlas.
     */
    public static void devolucionEntradas() {
        sc.nextLine(); // Limpieza buffer
        System.out.println("\nInserte los datos de su compra: nombre, apellidos y email con el que se realizó la compra.");

        System.out.println("Inserte su nombre:");
        String nombre = sc.nextLine().trim();

        System.out.println("Inserte sus apellidos:");
        String apellido = sc.nextLine().trim();

        System.out.println("Inserte su email:");
        String email = sc.nextLine().trim();
        // Buscar coincidencias en los arrays
        int[] indicesEncontrados = new int[nombres.length];
        int count = 0;

        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i] == null || apellidos[i] == null || correos[i] == null) continue;
            // Comparación ignorando mayúsculas/minúsculas
            if (nombres[i].trim().equalsIgnoreCase(nombre)
                && apellidos[i].trim().equalsIgnoreCase(apellido)
                && correos[i].trim().equalsIgnoreCase(email)) {
                indicesEncontrados[count] = i;
                count++;
            }
        }
        // Si no encuentra nada, da opción de reintentar
        if (count == 0) {
            System.out.println("Datos no encontrados.");
            System.out.println("Pulsa 1 para intentar otra vez, 2 para volver al menu anterior.");
            String opt = sc.nextLine().trim();
            if (opt.equals("1")) {
                devolucionEntradas();
            } else {
                cuentaInvitado();
            }
            return;
        }
        // Copiar los índices encontrados a un array ajustado
        int[] indices = new int[count];
        for (int i = 0; i < count; i++) {
            indices[i] = indicesEncontrados[i];
        }

        System.out.println("Compra(s) encontrada(s): " + count);
        confirmarDevolucion(indices);
    }


    /**
     * Calcula el dinero a devolver y elimina los registros.
     */
    public static void confirmarDevolucion(int[] arr){
        double suma = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] != -1){
                suma += precios[arr[i]]; // Sumar importe
                borrarCompra(arr[i]); // Borrar de memoria
            }
        }
        System.out.println("Devolucion efectuada con cantidad de dinero " + suma);
        guardarDatos(); // Actualizar fichero
    }
    /**
     * Acceso protegido por contraseña ("234").
     */
    public static void cuentaAdmin(){
        System.out.println("Inserte la contraseña");
        String cont = sc.nextLine();
        if (cont.equals("234")){
          adminStats();
        }
        else{
          System.out.println("Contraseña incorrecta \n");
          cuentaAdmin();
        }
    }
    /**
     * Menú de estadísticas (Entradas vendidas y Ingresos en dinero).
     */
    public static void adminStats(){
        System.out.println("\nBienvenido a las estadísticas de admin. Eliga una de las opciones: \n 1. Entradas vendidas. \n 2. Ingresos totales. \n 3. Volver");
        int stats = sc.nextInt();
        switch (stats){
          case 1:
            entradasVendidas();
            break;
          case 2:
            ingresosTotales();
            break;
          case 3:
            menuPrincipal();
            break;
          default:
            System.out.println("Solo se acepta 1, 2 o 3");
        }
    }
    // Estos métodos iteran sobre los arrays globales contando 'peliculas[i]' o sumando 'precios[i]'
      
    public static void entradasVendidas(){
        System.out.println("\nElige una de las opciones:\n1. Entradas vendidas a Pelicula 1\n2. Entradas vendidas a Pelicula 2\n3. Entradas vendidas a Pelicula 3\n4. Entradas vendidas a Pelicula 4\n5. Entradas vendidas a Pelicula 5\n6. Entradas vendidas totales\n7. Volver");
        int entradas = sc.nextInt();
        switch (entradas){
            case 1:
                ventas(0); 
                break;
            case 2:
                ventas(1); 
                break;
            case 3:
                ventas(2);
                break;
            case 4:
                ventas(3);
                break;
            case 5:
                ventas(4);
                break;
            case 6:
                ventas(5);
                break;
            case 7:
                adminStats();
            default:
                System.out.println("Solo se acepta 1, 2, 3, 4, 5, 6 o 7");
        }
    }
  
    public static void ventas(int peli){
        int entradasVenta = 0;
        if(peli == 5){
            for(int i = 0; i < peliculas.length; i++){
                if(peliculas[i] != 0){
                    entradasVenta++;
                } 
            }
            System.out.println("Entradas vendidas totales: " + entradasVenta);
        }
        else{
            for(int i = 0; i < peliculas.length; i++){
                if(peliculas[i] == peli + 1){
                    entradasVenta++;
                }
            }
            System.out.println("Entradas vendidas a Pelicula " + (peli + 1) + ": " + entradasVenta);
        }
        System.out.println("Entradas vendidas");
        System.out.println(" Pulse 1 para volver a atrás. \n Pulse 2 para volver al menú principal");
        int op = sc.nextInt();
        if (op==1)
          entradasVendidas();
        else if (op==2)
          menuPrincipal();
        else
          System.out.println("Solo se acepta 1 o 2");
    }

    public static void ingresosTotales(){
        System.out.println("\nElige una de las opciones:\n1. Ingresos de Pelicula 1\n2. Ingresos a Pelicula 2\n3. Ingresos a Pelicula 3\n4. Ingresos a Pelicula 4\n5. Ingresos a Pelicula 5\n6. Ingresos totales\n7. Volver");
        int ingreso = sc.nextInt();
        switch (ingreso){
            case 1:
                ingresosHoy(0);
                break;
            case 2:
                ingresosHoy(1); 
                break;
            case 3:
                ingresosHoy(2);
                break;
            case 4:
                ingresosHoy(3);
                break;
            case 5:
                ingresosHoy(4);
                break;
            case 6:
                ingresosHoy(5);
                break;
            case 7:
                adminStats();
            default:
                System.out.println("Solo se acepta 1, 2, 3, 4, 5, 6 o 7");
        }
    }
        
    public static void ingresosHoy(int peli){
        int ingresoTotal = 0;
        if(peli == 5){
            for(int i = 0; i < peliculas.length; i++){
                if(peliculas[i] != 0){
                    ingresoTotal += precios[i];
                } 
            }
            System.out.println("Ingresos totales: " + ingresoTotal);
        }
        else{
            for(int i = 0; i < peliculas.length; i++){
                if(peliculas[i] == peli + 1){
                    ingresoTotal += precios[i];
                }
            }
            System.out.println("Ingresos relacionado a Pelicula " + (peli + 1) + ": " + ingresoTotal);
        }
        System.out.println("Ingresos Totales");
        System.out.println(" Pulse 1 para volver a atrás. \n Pulse 2 para volver al menú principal");
        int op = sc.nextInt();
        if (op==1){
          ingresosTotales();
        }
        else if (op==2){
          menuPrincipal();
        }
        else{
          System.out.println("Solo se acepta 1 o 2");
        }
    }
    /**
     * Escribe un array 2D de Strings en un fichero de texto.
     * Usa un separador (ej: ";")
     */
    public static void escribirFichero(String[][] datos, String separadorColumnas, String nombreFichero){
        try {
            String ruta = System.getProperty("user.dir") + File.separator + nombreFichero;
            FileWriter writer = new FileWriter(ruta);

            for (int i = 0; i < datos.length; i++) {
                String[] fila = datos[i];
                for (int j = 0; j < fila.length; j++) {
                    // Si es null escribe espacio, si no escribe el dato
                    if (fila[j] == null) {
                        writer.append(" ");
                    } else {
                        writer.append(fila[j]);
                    }
                    if (j < fila.length - 1)
                        writer.append(separadorColumnas);
                }
                writer.append(System.lineSeparator()); // Salto de línea
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Lee un fichero de texto y lo convierte en una matriz de Strings.
     */
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

        } catch (FileNotFoundException e) {
            return null; // Si no existe fichero, retorna null
        }
        // Convertir lista a array 2D
        String[][] res = new String[list.size()][];
        for (int i = 0; i < res.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }
    /**
     * Compacta los arrays (elimina huecos vacíos generados por devoluciones)
     * y guarda el estado limpio en 'datos.txt'.
     */
    public static void guardarDatos() {
        int posNull = -1;
        // Algoritmo de compactación: Mueve datos válidos a las posiciones vacías (null)
        for (int i = 0; i < nombres.length; i++) {

            if (nombres[i] == null && posNull == -1) {
                posNull = i; // Encontramos el primer hueco
            }

            if (posNull != -1 && nombres[i] != null) {
                // Movemos el dato actual 'i' al hueco 'posNull'
                nombres[posNull] = nombres[i];
                apellidos[posNull] = apellidos[i];
                correos[posNull] = correos[i];
                peliculas[posNull] = peliculas[i];
                asientosComprados[posNull] = asientosComprados[i];
                horario[posNull] = horario[i];
                precios[posNull] = precios[i];

                // Borramos la posición antigua
                nombres[i] = null;
                apellidos[i] = null;
                correos[i] = null;
                peliculas[i] = 0;
                asientosComprados[i] = 0;
                horario[i] = 0;
                precios[i] = 0;

                
                posNull++; // El hueco avanza
            }
        }
        // Preparar matriz de Strings para escribir en fichero
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
    /**
     * Carga los datos del fichero al iniciar el programa y rellena los arrays en memoria.
     */
    public static void cargarDatos() {
        String[][] tabla = leerFichero(";", "datos.txt");
        if (tabla == null) return;

        for (int i = 0; i < tabla.length; i++) {
            // Evitar líneas corruptas
            if (tabla[i].length < 7) continue;
            // Convertir " " a null y Strings numéricos a int/double
            if (tabla[i][0].equals(" ")) {
                nombres[i] = null;
            } 
            else {
                nombres[i] = tabla[i][0];           
            }
            if (tabla[i][1].equals(" ")) {
                apellidos[i] = null;
            } 
            else {
                apellidos[i] = tabla[i][1];           
            }
            if (tabla[i][2].equals(" ")) {
                correos[i] = null;
            } 
            else {
                correos[i] = tabla[i][2];           
            }
            
            // Parsing de números
            peliculas[i] = Integer.parseInt(tabla[i][3]); // parseInt transforma String a int
            asientosComprados[i] = Integer.parseInt(tabla[i][4]);
            horario[i] = Integer.parseInt(tabla[i][5]);
            precios[i] = Double.parseDouble(tabla[i][6]);
        }
    }


    public static void main(String[] args){
        // 1. Cargar persistencia (datos anteriores)
        cargarDatos();
        // 2. Reconstruir el estado visual de los asientos (matriz 4D) basado en los datos cargados
        for (int i = 0; i < asientosComprados.length; i++) {
            if (asientosComprados[i] != 0) { 
                int pelicula = peliculas[i] - 1;   
                int hor = horario[i];              
                int asiento = asientosComprados[i];
                // Decodificar formato asiento (ej: 23 -> Fila 1 (índice), Asiento 2 (índice))
                int fila = (asiento / 10) - 1;
                int col = (asiento % 10) - 1;
                // Marcar como ocupado
                asientosPorPeli[pelicula][hor][fila][col] = 1; 
            }
        }
        // 3. Iniciar la interfaz de usuario
        menuPrincipal();

    }
}


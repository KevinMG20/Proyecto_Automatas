package Principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Pattern;

public class Interfaz extends javax.swing.JFrame {

    ArrayList<String> resvTiposDeDatos = new ArrayList<>(Arrays.asList("array", "const", "var", "true", "not", "false", "integer", "real", "char", "string", "byte", "boolean"));
    ArrayList<String> resvEstructDeControl = new ArrayList<>(Arrays.asList("begin", "end", "then", "to", "of", "by", "with", "module", "continue"));
    ArrayList<String> resvFuncYProc = new ArrayList<>(Arrays.asList("writeln", "readln", "exit", "return", "procedure", "function", "mod", "div", "record"));
    ArrayList<String> resvCondicionales = new ArrayList<>(Arrays.asList("if", "else", "switch", "elseif", "case", "until"));
    ArrayList<String> resvIterativas = new ArrayList<>(Arrays.asList("for", "do", "while", "loop", "break", "repeat"));
    ArrayList<String> operadoresLogicos = new ArrayList<>(Arrays.asList("and", "not", "or"));

    ArrayList<String> numerosEnteros = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
    ArrayList<String> operadoresRelacionales = new ArrayList<>(Arrays.asList("<", ">", "<=", ">=", "<>", "!="));
    ArrayList<String> signosPuntuacion = new ArrayList<>(Arrays.asList("(", ")", ";", ".", "{", "}", String.valueOf('"')));
    ArrayList<String> operadoresAritmeticos = new ArrayList<>(Arrays.asList("+", "-", "/", "*"));
    ArrayList<String> operadoresAsignacion = new ArrayList<>(Arrays.asList("="));

    ArrayList<String> palabrasReservadas = new ArrayList<>(
            Arrays.asList("array", "begin", "case", "const", "do",
                    "else", "writeln", "readln", "elseif", "end",
                    "for", "if", "loop", "module", "function",
                    "exit", "not", "of", "mod", "record",
                    "repeat", "return", "procedure", "by", "then",
                    "to", "until", "var", "while", "with",
                    "true", "false", "div", "integer", "real",
                    "char", "string", "byte", "boolean",
                    "and", "not", "or"));

    // Declaración de contadores.
    int contParentesis = 0, contComillas = 0, contTerminar = 0, bandera = 0, blancos = 0, contCom, banCor = 0;

    // Declaración de contadores para los tokens.
    int tokenAritmetico = 0, tokenAsignacion = 0, tokenRelacional = 0, tokenNumEnt = 0, tokenNumDec = 0, tokenOpLog = 0,
            tokenIdentificador = 0, tokenPuntuacion = 0,
            tokenPuntuacionFin = 0, tokenLiteral = 0, tokenComentarioM = 0, tokenComentario = 0, tokenUno = 0, tokenCorchetes = 0,
            tokenDos = 0, tokenTres = 0, tokenCuatro = 0,
            tokenCinco = 0, tokenSeis = 0, tokenSiete = 0, tokenOcho = 0, tokenNueve = 0;

    String caracter, caracter2, reservadas;
    boolean parentesisAnalizados = false;

    String pila = "", pila2 = ""; // Cada caracter se va concatenando a esta variable.
    String analisis = ""; // se va concatenando el analisis.
    String errores = ""; // se concatenan los errores detectados.
    String tipoNumero = "Entero"; // cuando hay un numero, para identificar si es entero o decimal.

    String linea[]; // guarda la cantidad de líneas de código.
    String orden[] = new String[30]; // Para verificar que la expresión este escrita de manera correcta.
    String orden2[] = new String[30]; // Para verificar que no existan datos repetidos.
    String repeticion[] = new String[30]; // Para contar el total de repeticiones de los datos.
    int apuntador = 0; // apuntador para la pila

    public Interfaz() {
        initComponents();
    }

    // si en el analisis es una plabra reservada, busca que tipo de palabra reservada es.
    public String comprobarReservadas(String pila) {

        if (resvTiposDeDatos.contains(pila)) { // busca lo que contiene la pila en el arraylist de tipo de datos
            bandera = 1; // si es u tiepo de dato, analizar que tipo de dato es.
            return "Tipo de dato";
        } else if (resvCondicionales.contains(pila)) { // si no esta en tipo de dato lo busca en condicioneles
            bandera = 0;
            return "Condicional";
        } else if (resvIterativas.contains(pila)) { // si no en iterativas
            bandera = 0;
            return "Iterativas";
        } else if (resvFuncYProc.contains(pila)) {// si no en funciones y procedimientos
            bandera = 0;
            return "Funciones y procedimientos";
        } else if (resvEstructDeControl.contains(pila)) { // si no en estructura de control
            bandera = 0;
            return "Estructura de control";
        } else if (operadoresLogicos.contains(pila)) { // si no en estructura de control
            bandera = 0;
            return "Operador lógico";
        }
        return "";
    }

    public void recorrerFila() { // Metodo principal para el analisis.
        linea = txaUserText.getText().split("\n"); // .split divide el string por espacios

        //System.out.println("Longitud de la linea 0: " + (linea[0].length()));
        // Se limpian los recuadros de la interfaz
        txaErrores.setText("");
        txaAnalisis.setText("");

        for (int x = 0; x < linea.length; x++) { // Se repite según la cantidad de lineas ingresadas
            // Se dejan los vectores sin valores con el metodo limpiarPila.
            if (linea[x].length() <= 0) {
                x++;
            }
            limpiarPila(orden);
            limpiarPila(orden2);
            pila = ""; // a la variable pila se deja vacia.
            pila2 = "";
            contComillas = 0;// El contador para las comillas se deja en 0
            contCom = 0;
            parentesisAnalizados = false; // y la bandera para el analisis se deja apagada.

            for (int y = 0; y < linea[x].length(); y++) { // Recorre la linea actual caracter por caracter
                //Caracter 2 recibe la cadena original
                caracter2 = String.valueOf(linea[x].charAt(y)); // Cala símbolo se convierte en caracter
                //caracter contiene la cadena en minusculas
                caracter = caracter2.toLowerCase();
                //pila2 contiene el caracter original
                pila2 += caracter2;
                //System.out.println("Se concateno a pila2 "+pila2);
                //pila contiene el caracter en minuscula
                pila += caracter; // y a la variable pila se le concatena ese caracter.
                //System.out.println("Se concateno a pila "+pila);                
                //se va a analizar con los caracteres en minuscula, caractery pila y se manda a analisis con los segundos.
                //System.out.println(caracter);
                System.out.println(caracter2);

                if (operadoresAritmeticos.contains(caracter)) { // si el caracter es un operador aritmetico                   
                    if (caracter.equals("/") || caracter.equals("*")) { // Si el siguiente caracter es / o *, es decir es un comentario
                        if (linea[x].contains("//") || linea[x].contains("/") || linea[x].contains("/")) {
                            int finDeComentarios[] = verificarComentarios(x, y);

                            if (finDeComentarios[0] > x || finDeComentarios[1] == linea[x].length()) {
                                x = finDeComentarios[0];
                                System.out.println("Break 1");
                                break;
                            }

                            x = finDeComentarios[0];
                            y = finDeComentarios[1];
                            System.out.println("No break");
                        }
                    } else {// Si no es un comentario se trata de un operador aritmetico.
                        System.out.println("Aritmetico");
                        tokenAritmetico++;
                        analisis += caracter2 + " → Operador Aritmético (11," + tokenAritmetico + ")\n";

                        orden2[apuntador] = caracter;
                        orden[apuntador] = "Operador Aritmético";
                        apuntador++;
                    }
                    pila = ""; // la variable pila se vuelve a dejar vacia
                    pila2 = "";

                } else if (operadoresAsignacion.contains(caracter)) {
                    System.out.println("Asignación");
                    tokenAsignacion++;
                    analisis += caracter2 + " → Operador de Asignación (10," + tokenAsignacion + ")\n";
                    orden2[apuntador] = caracter;
                    orden[apuntador] = "Operador de Asignación";
                    apuntador++;

                    pila = "";
                    pila2 = "";

                } else if (signosPuntuacion.contains(caracter)) {
                    System.out.println("Puntuación");

                    y = verificarComillas(x, y);

                    if (!parentesisAnalizados) { // si esta apagada a bandera de los parentesis
                        verificarParentesis(x); // se invoca al métoo para verificar la cantidad de parentesis
                        parentesisAnalizados = true; // se enciende la bandera.
                    }
                    pila = ""; // Comienza de nuevo
                    pila2 = "";

                } else if (operadoresRelacionales.contains(caracter)) {
                    System.out.println("Relacional");
                    tokenRelacional++;
                    analisis += caracter2 + " → Operador Relacional (12," + tokenRelacional + ")\n";

                    orden2[apuntador] = caracter;
                    orden[apuntador] = "Operador Relacional";
                    apuntador++;

                    pila = ""; // Comienza de nuevo
                    pila2 = "";

                } else if (numerosEnteros.contains(pila)) {
                    System.out.println("Numero Entero");
                    if (linea[x].length() > y) {

                        for (int z = y + 1; z < linea[x].length(); z++) { // Recorrer la cadena para leer todo el numero
                            caracter = String.valueOf(linea[x].charAt(z));
                            caracter2 = caracter;
                            if (!numerosEnteros.contains(caracter)) {
                                if (caracter.equals(".")) {
                                    tipoNumero = "Decimal";
                                } else { // Salir porque se encontró un caracter que no es un numero ni un .
                                    break;
                                }
                            }
                            pila += caracter;
                            pila2 += caracter2;
                            y++;
                        }

                        if (pila.contains(".")) {
                            if (validarDecimal(pila, x)) { // El numero decimal es valido
                                tokenNumDec++;
                                analisis += pila2 + " → Numero " + tipoNumero + " (13," + tokenNumDec + ")\n";
                            }
                            /*if (String.valueOf(pila.charAt(1)).equals(".")) {
                                
                            } else {
                                errores += "Error 513: El segundo caracter de un decimal debe ser ' . ' en la linea "
                                        + (x + 1) + "\n";
                                 tokenNumDec++;
                                  analisis += pila2 + " → Numero " + tipoNumero + " (13," + tokenNumDec + ")\n";
                            }*/
                        } else {
                            tokenNumEnt++;
                            analisis += pila2 + " → Numero " + tipoNumero + " (14," + tokenNumEnt + ")\n";
                        }

                        orden2[apuntador] = "Numero";
                        orden[apuntador] = "Numero";
                        apuntador++;
                        tipoNumero = "Entero";
                    }

                    pila = "";
                    pila2 = "";

                } else if (palabrasReservadas.contains(pila)) {
                    if (y + 1 < linea[x].length()) {
                        if (String.valueOf(linea[x].charAt(y + 1)).equals(" ")) {
                            // Despues de una palabra reservada SIEMPRE debe haber un espacio en blanco
                            System.out.println("Palabra Reservada: " + pila);

                            reservadas = comprobarReservadas(pila.toLowerCase());
                            System.out.println("La parabra reservada es: " + reservadas);

                            if (bandera == 1) {
                                tipoDato(pila.toLowerCase());
                                orden2[apuntador] = pila;
                                orden[apuntador] = "Tipo de dato";
                                apuntador++;
                            } else {
                                tokenNueve++;
                                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (20," + tokenNueve + ")\n";
                                orden2[apuntador] = pila;
                                orden[apuntador] = "Palabra Reservada";
                                apuntador++;
                            }

                            pila = "";
                            pila2 = "";
                        }
                    }

                } else if (caracter.equals(" ")) {
                    System.out.println("Blanco\n");
                    blancos++;

                    pila = ""; // Comienza de nuevo
                    pila2 = "";

                } else if (y + 1 < linea[x].length() // Si hay caracter siguiente
                        && (String.valueOf(linea[x].charAt(y + 1)).equals(" ") // Y es " "
                        || operadoresAritmeticos.contains(String.valueOf(linea[x].charAt(y + 1))) // O un
                        // operador
                        // aritmetico
                        || operadoresAsignacion.contains(String.valueOf(linea[x].charAt(y + 1))) // O un
                        // operador de
                        // asignacion
                        || signosPuntuacion.contains(String.valueOf(linea[x].charAt(y + 1))))) { // O un signo
                    // de
                    // puntuacion
                    if (!numerosEnteros.contains(String.valueOf(linea[x].charAt(y + 1)))) { // Y el siguiente no es un
                        // numero
                        System.out.println("Identificador 1");
                        tokenIdentificador++;
                        analisis += pila2 + " → Identificador (1," + tokenIdentificador + ")\n";
                        /*for (int b = 0; b < pila.length(); b++) {
                            System.out.println("Estoy en pila" + pila.substring(0, b + 1));
                        }*/
                        identificador(x);
                        pila = "";
                        pila2 = "";
                        orden2[apuntador] = "Identificador";
                        orden[apuntador] = "Identificador";
                        apuntador++;
                    } else { // Si el siguiente es un numero es porque sigue siendo parte del identificador
                        pila = " "; // Para que no entre en la condicion del numero entero
                        pila2 = "";
                    }

                } else if (y + 1 >= linea[x].length() && pila != "") {
                    // Si la linea ya llego al final y la pila no esta vacia, lo que queda en ella
                    // es un identificador
                    System.out.println("Identificador 2");
                    tokenIdentificador++;
                    analisis += pila2 + " → Identificador (1," + tokenIdentificador + ")\n";

                    identificador(x);
                    pila = "";
                    pila2 = "";

                    orden2[apuntador] = "Identificador";
                    orden[apuntador] = "Identificador";
                    apuntador++;
                }

            }

            if (blancos == linea[x].length()) {
                System.out.println("Es linea vacia");
            } else if (linea[x].endsWith("*/") && (orden[0] == null || orden[0] == "")) {
                System.out.println("Es linea de comentarios");
            } else {
                System.out.println("Analizando estructura linea " + (x + 1));
                analisis += "\n";
                //repeticion(x);
                estructura(x);
                xx = x;
                //verificarPuntoYComa(x); // metodo para ver si la linea termina con ;
                verificarGuionBajoFinal(x);
            }
            verificarPuntoYComa(x); //metodo para ver si la linea termina con ;
            blancos = 0;
        }

        // Se muestra el analisis y los errores encontrados si los hay
        txaErrores.setText(errores);
        txaAnalisis.setText(analisis);

        // Se limpian las variables
        //estructura(xx);
        errores = "";
        analisis = "";
        contadores();
    }
    int xx;

    public void identificador(int x) {
        int h = 0, j = pila.length();
        //System.out.println(pila);
        String car = String.valueOf(pila.substring(h, 1));

        if (car.equals("a") || car.equals("b") || car.equals("c") || car.equals("d") || car.equals("e") || car.equals("f") || car.equals("g") || car.equals("h") || car.equals("i")
                || car.equals("j") || car.equals("k") || car.equals("l") || car.equals("m") || car.equals("n") || car.equals("ñ") || car.equals("o") || car.equals("p") || car.equals("q")
                || car.equals("r") || car.equals("s") || car.equals("t") || car.equals("u") || car.equals("v") || car.equals("w") || car.equals("x") || car.equals("y") || car.equals("z")) {
            for (int b = 1; b <= j; b++) {
                if (b + 1 > j) {
                    System.out.println("Ya no hay más que comparar");
                } else {
                    System.out.println("Se esta comparando: " + pila.substring(h, b) + " y " + pila.substring(h + 1, b + 1));
                    if (pila.substring(h, b).equals("") && pila.substring(h + 1, b + 1).equals("")) {
                        errores += "Error 545: Hay mas de un ' _ ' seguido en la linea " + (x + 1) + "\n";
                    }
                }
                if (b == j) {
                    if (pila.substring(h, b).equals("_")) {
                        System.out.println("Error");
                        errores += "Error 546: El identificador termina en ' _ ' en la linea " + (x + 1) + "\n";
                    }
                }

                //System.out.println("Estoy en pila"+pila.substring(h, b)+"\n"); 
                h++;
            }
        } else {
            errores += "Error 548: El identificador no comienza con letra en la linea " + (x + 1) + "\n";
        }
    }

    /*
     * public void verificarPuntos(int x) {
     * // System.out.println("entra");
     * int pos = 0;
     * int contador = 0;
     * pos = linea[x].indexOf(".");
     * 
     * while (pos != -1) {
     * contador++;
     * pos = linea[x].indexOf(".", pos + 1);
     * }
     * if (contador > 1) {
     * errores += "Error 512: Hay mas de un punto en la linea " + (x + 1) + "\n";
     * }
     * }
     */
    public void verificarPuntoYComa(int y) { // lleva como parametro la linea que se esta analizando
        contTerminar = 0; // Se inicializa en 0 el contador de ;

        for (int x = linea[y].length() - 1; x > -1; x--) {// se recorre la cadena de manera inversa
            // Segun el caracter que se esta leyendo se le asigna a la variable caracter
            caracter = String.valueOf(linea[y].charAt(x));
            caracter2 = caracter;
            if (caracter.equals(";")) {// si es punto y coma se incrementa el contador
                contTerminar++;
            }
            // Si es diferente de ;, no es espacio en blanco y sigue en 0 el contador
            if (!caracter.equals(";") && !caracter.equals(" ") && contTerminar < 1) {
                // Verificar si hay comentarios hasta el final de la linea
                if (!linea[y].contains("//") && (!linea[y].contains("/") && !linea[y].contains("/"))) {
                    // Marca el error de que no termina en ;
                    errores += "Error 500: Se esperaba ' ; ' en la linea " + (y + 1) + "\n";
                    return;
                }
            }
        }
        if (contTerminar == 0) { // si no conto ningun punto y coma
            errores += "Error 501: Se esperaba ' ; ' en la linea " + (y + 1) + "\n"; // marca el error de que falta terminar
            // con ;
        } else if (contTerminar > 1) { // si hay mas de un ;
            errores += "Error 502: Hay más de un ' ; ' en la linea " + (y + 1) + "\n"; // Se marca ese error
        }

    }

    public int verificarComillas(int x, int y) {
        if (caracter.equals(String.valueOf(';'))) {
            tokenPuntuacionFin++;
            analisis += pila2 + " → Signo de puntuación (27," + tokenPuntuacionFin + ")\n";
            orden2[apuntador] = caracter;
            orden[apuntador] = "Signo de puntuación";
            apuntador++;
        } else if (caracter.equals(String.valueOf('"'))) { // Si son comillas se debe recorrer porque indica una literal
            System.out.println("Literal");
            pila = caracter; // la pila2 es igual a "
            pila2 = caracter2;
            contComillas++; // se incrementa el contador
            if (linea[x].length() > y) { // Si aun hay cadena por recorrer
                for (int z = y + 1; z < linea[x].length(); z++) {
                    caracter = String.valueOf(linea[x].charAt(z)); // se convierte a caracter el simbolo
                    caracter2 = caracter;
                    pila += caracter; // se concatena a la pila
                    pila2 += caracter2;
                    y++; // se incrementa y
                    if (caracter.equals(String.valueOf('"'))) { // Termina la literal
                        contComillas--; // se decrementa el contador
                        break; // Sale del metodo
                    }
                }
                // Verificacion de cierre de comillas
                if (contComillas != 0) { // si no termina en cero el contador entonces faltan comillas
                    errores += "Error 510: Faltan comillas en la línea: " + (x + 1) + "\n";

                    tokenPuntuacion++;
                    analisis += pila2 + " → Signo de puntuación (28," + tokenPuntuacion + ")\n";

                } else if (contComillas == 0) { // si si es cero, se trata de una literal
                    tokenLiteral++;
                    analisis += pila2 + " → Literal (15," + tokenLiteral + ")\n";
                    orden2[apuntador] = "Literal";
                    orden[apuntador] = "Literal";
                    apuntador++;
                }
            } else {
                errores += "Error 510:Faltan comillas en la linea " + x + 1 + "\n";// "Faltan comillas por cerrar en la
            }

        } else {
            y = verificarCorchetes(x, y);
            if (banCor == 0) {
                tokenPuntuacion++;
                analisis += pila2 + " → Signo de puntuación (28," + tokenPuntuacion + ")\n";
                orden2[apuntador] = caracter;
                orden[apuntador] = "Signo de puntuación";
                apuntador++;
            }

        }
        return y;
    }

    public void verificarParentesis(int x) {

        Stack<Character> p = new <Character>Stack();// Creacion de la pila
        int i = 0;
        int tam = linea[x].length();

        if (linea[x].charAt(i) == '(' || linea[x].charAt(i) == ')') {
            while (i < tam) {
                if (linea[x].charAt(i) == '(') {
                    p.push('(');
                } else if (linea[x].charAt(i) == ')') {
                    if (!p.isEmpty()) {
                        p.pop();
                    } else {
                        errores += "Error 511: Faltan parentesis por cerrar en la linea " + (x + 1) + "\n";
                        return;
                    }
                }
                i++;
            }

            if (p.isEmpty()) {// Si la pila esta vacia
                // errores += "Correcto";
            } else {
                errores += "Error 511: Faltan parentesis por cerrar en la linea " + (x + 1);
            }
        } else {
            // System.out.println(errores + " caracter diferente " + x + "\n");
        }
    }

    private boolean validarDecimal(String Entrada, int x) {
        // Expresión regular para validar números decimales con un solo punto decimal
        Pattern ExpresionPuntos = Pattern.compile("^[0-9]+(\\.[0-9]+)?$");
        if (!ExpresionPuntos.matcher(Entrada).matches()) { // No es valido
            errores += "Error 520: En el número con decimal '" + Entrada + "' en la linea " + (x + 1) + "\n";
            return false;
        }
        return true;
    }

    public int[] verificarComentarios(int x, int y) { // Debe retornar x & y
        /*Posibles casos:
        1- La linea contiene comentarios de una sola linea //
            En este caso se aumenta el token de comentario simple y se muestra en el analisis como comentario
            que ocupa todo el resto de la linea dado que los comentarions con // no se pueden cerrar.
        
        2- La linea contiene comentarios multilinea /* y cierre * / en la misma linea
            En este caso se aumenta el token de comentario multilinea y se muestra en el analisis como comentario
            desde donde se encuentra /* hasta donde se encuentra el cierre con * /.
        
        3- La linea contiene comentarios multilinea /* pero no el cierre * / en la misma linea
            En este caso se aumenta el token de comentario multilinea y se muestra en el analisis como comentario
            desde la linea donde se encuentra la apertura hasta la linea donde se encuentra el cierre si existe.
         */

        int finDeComentarios[] = new int[2]; // La linea y caracter donde terminan los comentarios multilinea
        finDeComentarios[0] = x;
        finDeComentarios[1] = y;

        if (linea[x].contains("//")) { // Caso numero 1
            tokenComentario++;
            analisis += linea[x].substring(linea[x].indexOf("//"), linea[x].length()) + " → Comentario (16," + tokenComentario + ")\n";

            orden2[apuntador] = caracter; // Se guarda en el vector el caracter del que se trata
            orden[apuntador] = "Comentarios"; // Se guarda el analisis del caracter.
            apuntador++; // Se incrementa el apuntador para las posiciones del vector

            finDeComentarios[1] = linea[x].length();
            return finDeComentarios;

        } else if (linea[x].contains("/*")) { // Caso numero 2

            if (!linea[x].contains("*/")) { // Caso numero 3

                // Hay que buscar el cierre, recorrer lo que queda de codigo hasta encontrarlo
                for (int z = x + 1; z <= linea.length - 1; z++) {

                    if (linea[z].contains("*/")) { // Se encontró
                        System.out.println("Comentarios multilinea desde linea " + (x + 1) + " hasta linea " + (z + 1) + "\n");
                        System.out.println("Reanudando analisis en linea " + (z + 1) + " posicion " + (linea[z].indexOf("*/") + 2));
                        tokenComentarioM++; // Se incrementa el contador de comentario multilinea
                        // Se muestra en el analisis que se trata de un comentario multilinea.
                        analisis += "Comentarios multilinea desde linea " + (x + 1) + " hasta linea " + (z + 1)
                                + " (17," + tokenComentarioM + ")\n";
                        //cierreEncontrado = true; // Se prende la bandera al encontrar el cierre del comentario.
                        finDeComentarios[0] = z; // Asigna a la línea donde termina el comentario
                        finDeComentarios[1] = linea[z].indexOf("*/") + 2; // Asigna el aracter donde terminan los comentarios multilinea

                        orden2[apuntador] = caracter; // Se guarda en el vector el caracter del que se trata
                        orden[apuntador] = "Comentarios"; // Se guarda el analisis del caracter.
                        apuntador++; // Se incrementa el apuntador para las posiciones del vector
                        return finDeComentarios; //Cierre encontrado, regresa al metodo principal                   
                    }

                }
                System.out.println("Cierre de comentarios no encontrados");
                // se concatena a los errores el error correspondiente
                errores += "Error 530: No se encontró el cierre de comentarios multilinea declarado en la linea " + (x + 1) + "\n";

                finDeComentarios[1] = linea[x].length(); // Indicar le final de la linea como caracter de retorno
                return finDeComentarios;
            } else { // Caso numero 2

                tokenComentarioM++;// Se incrementa el contador de tokens de error multiliena
                // Se concatena al analisis que es un comentario.
                analisis += linea[x].substring(linea[x].indexOf("/"), linea[x].indexOf("/") + 2)
                        + " → Comentarios (17, " + tokenComentarioM + ")\n";

                finDeComentarios[0] = x; // Ir a la posicion del cierre de los comentarios
                finDeComentarios[1] = linea[x].indexOf("*/") + 2;

                orden2[apuntador] = caracter; // Se guarda en el vector el caracter del que se trata
                orden[apuntador] = "Comentarios"; // Se guarda el analisis del caracter.
                apuntador++; // Se incrementa el apuntador para las posiciones del vector
                return finDeComentarios;
            }
        }//Excepcion: se utiliza el cierre de comentarios sin haberlos abierto
        else if (linea[x].contains("/") && !linea[x].contains("/")) {
            errores += "Error 531: Declaración de comentarios con '*/' en la linea " + (x + 1) + "\n";
            tokenComentarioM++;// Se incrementa el contador de tokens de error multiliena
            // Se concatena al analisis que es un comentario.
            analisis += linea[x].substring(linea[x].indexOf("*/"), linea[x].length())
                    + " → Comentarios (17, " + tokenComentarioM + ")\n";
            finDeComentarios[1] = linea[x].length();
            return finDeComentarios;
            //finDeComentarios[1] = linea[x].indexOf("*/") + 2;// Asigna el aracter donde terminan los comentarios multilinea
        }
        return finDeComentarios;
    }

    public void estructura(int x) // Verificar que la estructura este adecuadamente
    {
        /* Estructura 1:
        Tipo de dato + identificador = (valor | literal | identificador) operador (valor | literal | identificador)*/
        if (orden[0].equals("Tipo de dato")) {
            verificarEstructura1(x);
        }

        System.out.println(orden[0]);


        /*
         * if()
         * {
         * 
         * }
         * // si es un tipo de dato, un identificador, un = y posteriormente un numero o
         * literal y ; esta correcto
         * if (orden[0] == "Tipo de dato" && orden[1] == "Identificador" && orden[2] ==
         * "Operador de Asignación"
         * && (orden[3] == "Numero" || orden[3] == "Literal") && orden[4] ==
         * "Signo de puntuación") {
         * apuntador = 0;// se regresa el apuntador para sobreescribir en la pila
         * 
         * } else { //si no
         * errores += "Error 540: Esta mal estructurado en la linea " + (x + 1) + "\n";
         * //se manda el mensaje de error
         * apuntador = 0;// se regresa el apuntador para sobreescribir en la pila
         * repeticion(x); //se invoca al método para verificar que no exita repetición
         * de alguna palabra o signo.
         * 
         * }
         */
        // Se vuelven a dejar los vectores vacios.
        limpiarPila(orden);
        limpiarPila(orden2);

    }

    public void verificarEstructura1(int linea) {
        System.out.println("Verificando estructura 1");
        if (orden[0].equals("Tipo de dato")) {
            System.out.println(orden[0]);
            if (orden[1] == "Identificador") {
                System.out.println(orden[1]);
                if (orden[2] == "Operador de Asignación") {
                    System.out.println(orden[2]);
                    int x = 3;
                                                        
                            while (orden[x] != "") {
                                //Verificar operando a continuacion
                                System.out.println(orden[x]);
                                if (orden[x] != "Numero" && orden[x] != "Literal" && orden[x] != "Identificador") {
                                    //Damos error de estructura
                                    errores += "Error 544: Estructura incorrecta en la linea " + (linea + 1) + ". Se esperaba un operando.\n"; // se manda el
                                    break;
                                }
                                if (!"".equals(orden[x + 1]) && orden[x + 1] != null) {
                                    x++;
                                    System.out.println(orden[x]);
                                } else {
                                    break;
                                }
                                //Despues de un operando debe haber un operador o un punto y coma
                                if (orden[x] != "Operador Aritmético" && orden[x] != "Signo de puntuación") {
                                    //Damos error de estructura
                                    errores += "Error 544: Estructura incorrecta en la linea " + (linea + 1) + ". Se esperaba un operador.\n"; // se manda el
                                    break;
                                }
                                if (orden[x] == "Signo de puntuación") {
                                    break;
                                }
                                System.out.println("Siguiente: " + orden[x + 1]);
                                if ("".equals(orden[x + 1]) && orden[x + 1] != null) {
                                    errores += "Error 544: Estructura incorrecta en la linea " + (linea + 1) + ". Se esperaba un operando.\n"; // se manda el
                                    break;
                                }
                                x++;
                            }

                            System.out.println("Fin de la verificacion");
                            
                            apuntador = 0;// se regresa el apuntador para sobreescribir en la pila
                            repeticion(linea); // se invoca al método para verificar que no exita repetición de alguna
                            // palabra o signo.
                            
                            
                    /*if (orden[3] == "Numero" || orden[3] == "Literal" || orden[3] == "Identificador") {
                        System.out.println(orden[3]);
                        //Llegados a este punto solo puede haber un punto y coma o una operacion
                        if (orden[4] == "Signo de puntuación") {
                            System.out.println(orden[4]);
                            apuntador = 0;// se regresa el apuntador para sobreescribir en la pila
                            //Termina la linea

                        } else if (orden[4] == "Operador Aritmético") {
                            System.out.println(orden[4]);
                            //Encontrado un operador hay que seguir recorriendo los lexemas hasta encontrar el
                            //el final de la operacion 
                            
                        }

                    } else {
                        errores += "Error 543: Esta mal estructurado, No hay un valor para el identificador en la linea "
                                + (linea + 1) + " \n"; // se manda el mensaje de error
                        apuntador = 0;// se regresa el apuntador para sobreescribir en la pila
                        repeticion(linea); // se invoca al método para verificar que no exita repetición de alguna palabra
                        // o signo.
                    }*/
                } else {
                    errores += "Error 542: Esta mal estructurado, No hay un operador de asignación en la linea "
                            + (linea + 1) + "\n"; // se manda el mensaje de error
                    apuntador = 0;// se regresa el apuntador para sobreescribir en la pila
                    repeticion(linea); // se invoca al método para verificar que no exita repetición de alguna palabra
                    // o signo.
                }
            } else {
                errores += "Error 541: Esta mal estructurado, Falta un identificador en la linea " + (linea + 1) + "\n"; // se
                // error
                apuntador = 0;// se regresa el apuntador para sobreescribir en la pila
                repeticion(linea); // se invoca al método para verificar que no exita repetición de alguna palabra
                // o signo.
            }

        } else { // si no
            errores += "Error 540: Esta mal estructurado, No comienza con un tipo de dato en la linea " + (linea + 1)
                    + "\n"; // se manda el mensaje de error
            apuntador = 0;// se regresa el apuntador para sobreescribir en la pila
            repeticion(linea); // se invoca al método para verificar que no exita repetición de alguna palabra
            // o signo.
        }

    }

    public boolean comprobarOperacion(int linea, int x) {
        //Verificar operando a continuacion
        System.out.println(orden[x]);
        if (orden[x] != "Numero" && orden[x] != "Literal" && orden[x] != "Identificador") {
            //Damos error de estructura
            errores += "Error 544: Estructura incorrecta en la linea " + (linea + 1) + ". Se esperaba un operando.\n"; // se manda el
            return false;
        }
        if (x < orden.length) {
            x++;
        } else {
            return false;
        }
        //Despues de un operando debe haber un operador o un punto y coma
        if (orden[x] != "Operador Aritmético" && orden[x] != "Signo de puntuación") {
            //Damos error de estructura
            errores += "Error 544: Estructura incorrecta en la linea " + (linea + 1) + ". Se esperaba un operador.\n"; // se manda el
            return false;
        }
        return true;
    }

    public boolean comprobarOperando(int linea, int x) {

        x++;
        return comprobarOperador(linea, x);
    }

    public boolean comprobarOperador(int linea, int x) {

        return true;
    }

    public void limpiarPila(String vector[]) {// metodo para dejar los vectores vacios, lleva como parametro el vector.
        for (int a = 0; a < vector.length; a++) {
            vector[a] = "";
        }
    }

    public void repeticion(int x) { // Método para verificar repeticiones con el parametro del numero de línea.
        int r = 0; // Para contar la cantidad de repeticiones de datos que hay
        for (int a = 0; a <= orden2.length - 2; a++) { // Recorre todo el vector
            // Si no son nulos, ni "" (vacios) y el valor actual del apuntador de la pila
            // (a) y uno mas (a+1) son iguales
            if (orden2[a + 1] != null && orden2[a].equals(orden2[a + 1]) && !orden2[a + 1].equals("")) {
                repeticion[r] = orden2[a + 1]; // Entonces hay una repetición, y esa repetició se guarda en otro vector
                r++;// Se incrementa la variable r (contador de las repeticiones)
                // Si esas repeticiones se tratan de parentesis
                if (orden2[a + 1].equals("(") || orden2[a + 1].equals(")")) {
                    r--; // decrementa el contador de repeticiones ya que puede haber más de un
                    // parentesis seguido
                }
                if (orden2[a + 1].equals("}")) { // si son dos corchetes de cierre
                    r--; // también se decrement el contador, se pueden cerrar varios corchetes
                }

                if (orden2[a + 1].equals("+")) { // Si el signo repetido es un +
                    r--; // Se decrementa r porque se pueden tener dos + seguidos.
                    if (orden2[a + 2] != null && orden2[a + 2].equals("+")) { // Y en la posicion siguiente hay otro +
                        // Es error porque serian mas de tres + juntos
                        errores += "Error 545: Hay más de dos: " + "'" + orden2[a + 1] + "'" + " en la linea "
                                + (x + 1) + "\n";
                    }
                }
                if (orden2[a + 1].equals("=")) { // Si el signo repetido es un =
                    r--; // Se decrementa r porque se pueden tener dos = seguidos
                    if (orden2[a + 2] != null && orden2[a + 2].equals("=")) {/// Y si en la siguiente posición hay otro
                        /// =
                        // Es error porque serian mas de tres = juntos
                        errores += "Error 545: Hay más de dos: " + "'" + orden2[a + 1] + "'" + " en la linea " + (x
                                + 1) + "\n";
                    }
                }
                if (orden2[a + 1].equals("-")) { // Si el signo repetido es un -
                    System.out.println("Signo - repetido");
                    r--; // Se decrementa r porque es posible tener dos -- seguidos
                    if (orden2[a + 2] != null && orden2[a + 2].equals("-")) {// Y si en la siguiente posición hay otro -
                        // Es error porque serian mas de tres - juntos
                        errores += "Error 545: Hay más de dos: " + "'" + orden2[a + 1] + "'" + " en la linea " + (x
                                + 1) + "\n";
                    }
                }
                if (orden2[a + 1].equals("/")) { // Si el signo repetido es un /
                    r--; // Se decrementa r porque es posible tener dos // seguidos
                    if (orden2[a + 2] != null && orden2[a + 2].equals("/")) { // Y si en la iguiente posición hay otro /
                        // Es error porque serian mas de tres / juntos
                        errores += "Error 545: Hay más de dos: " + "'" + orden2[a + 1] + "'" + " en la linea " + (x
                                + 1) + "\n";
                    }
                }
            }
        }
        // Al final
        if (r != 0) { // si hay al más de una repetición no valida
            for (int m = 0; m <= r - 1; m++) { // se recorre un ciclo con la cantidad de repeticiones y se muestra el
                // error correspondiente
                errores += "Error 545: Hay mas de un: " + "'" + repeticion[m] + "'" + " en la linea " + (x + 1) + "\n";
            }
        }
    }

    public void tipoDato(String pila) // Método para asignar el token correspondiente segun el tipo de dato
    {// Se hace uso de un switch
        switch (pila) {
            case "const": // si es una constante se asigna el token 2
                tokenUno++; // Se incrementa el contador para estos tokens
                // Se imprime el error.
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (2," + tokenUno + ")\n";
                break;
            // Es lo mismo para cada caso.
            case "null":// Token 20
                tokenNueve++;
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (20," + tokenNueve + ")\n";
                break;
            case "short": // Token 20
                tokenNueve++;
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (20," + tokenNueve + ")\n";
                break;
            case "long": // Token 20
                tokenNueve++;
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (20," + tokenNueve + ")\n";
                break;
            case "byte":// Token 20
                tokenNueve++;
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (20," + tokenNueve + ")\n";
                break;
            case "boolean":
                tokenOcho++; // Token 8
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (8," + tokenOcho + ")\n";
                break;
            case "float":
                tokenSiete++; // Token 7
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (7," + tokenSiete + ")\n";
                break;
            case "double":// Token 6
                tokenSeis++;
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (6," + tokenSeis + ")\n";
                break;
            case "integer":// Token 3
                tokenTres++;
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (3," + tokenTres + ")\n";
                break;
            case "char":
                tokenCuatro++; // Token 4
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (4," + tokenCuatro + ")\n";
                break;
            case "string":
                tokenCinco++;// Token 5
                analisis += pila2 + " → Palabra Reservada (" + reservadas + ") (5," + tokenCinco + ")\n";
                break;

        }

    }

    public int verificarCorchetes(int x, int y) {
        if (caracter.equals(String.valueOf('{'))) { // Si son comillas se debe recorrer porque indica una literal
            System.out.println("Comentario");
            pila = caracter; // la pila es igual a "
            pila2 = caracter2;
            contCom++; // se incrementa el contador
            if (linea[x].length() > y) { // Si aun hay cadena por recorrer
                for (int z = y + 1; z < linea[x].length(); z++) {//y + 1
                    caracter = String.valueOf(linea[x].charAt(z)); // se convierte a caracter el simbolo
                    caracter2 = caracter;
                    pila += caracter; // se concatena a la pila
                    pila2 += caracter2;
                    y++; // se incrementa y
                    if (caracter.equals(String.valueOf('}'))) { // Termina la literal
                        contCom--; // se decrementa el contador
                        break; // Sale del metodo
                    }
                }
                // Verificacion de cierre de comillas
                if (contCom != 0) { // si no termina en cero el contador entonces faltan comillas
                    errores += "Error 513: Faltan corchetesen la línea: " + (x + 1) + "\n";

                    tokenPuntuacion++;
                    analisis += pila2 + " → Signo de puntuación (28," + tokenPuntuacion + ")\n";

                } else if (contCom == 0) { // si si es cero, se trata de una literal
                    tokenCorchetes++;
                    analisis += pila2 + " → Comentario (17," + tokenCorchetes + ")\n";
                    orden2[apuntador] = "Comentario";
                    orden[apuntador] = "Comentario";
                    apuntador++;
                    banCor = 1;
                }
            } else {
                errores += "Error 513: Faltan corchetes en la linea " + x + 1 + "\n";// "Faltan comillas por cerrar en la
            }

        } else {

            tokenPuntuacion++;
            analisis += pila2 + " → Signo de puntuación (28," + tokenPuntuacion + ")\n";

            orden2[apuntador] = caracter;
            orden[apuntador] = "Signo de puntuación";
            apuntador++;

        }
        return y;
    }

    public void verificarGuionBajoFinal(int y) {
        String lineaActual = linea[y].trim();
        if (lineaActual.endsWith("_")) {
            errores += "Error 546: No se permite un guion bajo al final en la línea: " + (y + 1) + "\n";
        }
    }

    public void contadores() // método para reiniciar contadores de tokens
    {
        tokenAritmetico = 0;
        tokenAsignacion = 0;
        tokenRelacional = 0;
        tokenNumEnt = 0;
        tokenNumDec = 0;
        tokenIdentificador = 0;
        tokenPuntuacion = 0;
        tokenPuntuacionFin = 0;
        tokenLiteral = 0;
        tokenUno = 0;
        tokenDos = 0;
        tokenTres = 0;
        tokenCuatro = 0;
        tokenCinco = 0;
        tokenSeis = 0;
        tokenSiete = 0;
        tokenOcho = 0;
        tokenNueve = 0;
        tokenComentario = 0;
        tokenComentarioM = 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaUserText = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaAnalisis = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaErrores = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btnAnalizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        txaUserText.setColumns(20);
        txaUserText.setRows(5);
        jScrollPane1.setViewportView(txaUserText);

        jLabel1.setText("Código del usuario");

        jLabel2.setText("Análisis Léxico");

        txaAnalisis.setColumns(20);
        txaAnalisis.setRows(5);
        jScrollPane2.setViewportView(txaAnalisis);

        txaErrores.setColumns(20);
        txaErrores.setForeground(new java.awt.Color(255, 0, 0));
        txaErrores.setRows(5);
        jScrollPane3.setViewportView(txaErrores);

        jLabel4.setText("Lista de errores");

        btnAnalizar.setText("Analizar");
        btnAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(28, 28, 28)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAnalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(30, 30, 30)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAnalizar)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarActionPerformed
        recorrerFila();
        contadores();
    }//GEN-LAST:event_btnAnalizarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JTextArea txaAnalisis;
    private javax.swing.JTextArea txaErrores;
    private javax.swing.JTextArea txaUserText;
    // End of variables declaration//GEN-END:variables
}

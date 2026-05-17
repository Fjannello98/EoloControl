package ar.edu.siglo21.eolocontrol.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConsoleView {
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Scanner scanner = new Scanner(System.in);

    public void titulo(String texto) {
        System.out.println();
        System.out.println("========================================");
        System.out.println(texto);
        System.out.println("========================================");
    }

    public int menu() {
        System.out.println();
        System.out.println("1. Registrar central");
        System.out.println("2. Listar centrales");
        System.out.println("3. Registrar turbina");
        System.out.println("4. Listar turbinas");
        System.out.println("5. Registrar telemetria");
        System.out.println("6. Reporte energia por turbina");
        System.out.println("7. Ver alertas pendientes");
        System.out.println("0. Salir");
        return leerEntero("Opcion");
    }

    public String leerTexto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return scanner.nextLine().trim();
    }

    public int leerEntero(String etiqueta) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(etiqueta));
            } catch (NumberFormatException ex) {
                error("Debe ingresar un numero entero.");
            }
        }
    }

    public BigDecimal leerDecimal(String etiqueta) {
        while (true) {
            try {
                return new BigDecimal(leerTexto(etiqueta).replace(",", "."));
            } catch (NumberFormatException ex) {
                error("Debe ingresar un numero decimal valido.");
            }
        }
    }

    public LocalDateTime leerFechaHora(String etiqueta) {
        while (true) {
            try {
                return LocalDateTime.parse(leerTexto(etiqueta), INPUT_DATE);
            } catch (Exception ex) {
                error("Formato esperado: yyyy-MM-dd HH:mm");
            }
        }
    }

    public void ok(String texto) {
        System.out.println("[OK] " + texto);
    }

    public void error(String texto) {
        System.out.println("[ERROR] " + texto);
    }

    public void linea(String texto) {
        System.out.println(texto);
    }
}

package UI;

import java.util.Scanner;

public class MenuContratacion {

	private Scanner scanner;

	public MenuContratacion() {
		this.scanner = new Scanner(System.in);
	}

	public void mostrarMenu() {
		int opcion;
		do {
			System.out.println("\n===== MENÚ PRINCIPAL =====");
			System.out.println("1. Roles faltantes de una canción");
			System.out.println("2. Roles faltantes del recital completo");
			System.out.println("3. Contratar artistas para una canción");
			System.out.println("4. Contratar artistas para todo el recital");
			System.out.println("5. Entrenar artista");
			System.out.println("6. Listar artistas contratados");
			System.out.println("7. Listar canciones y su estado");
			System.out.println("8. Consultar entrenamientos mínimos (Prolog)");
			System.out.println("9. Salir");
			System.out.print("Seleccione una opción: ");

			opcion = leerEntero();

			switch (opcion) {
			case 1:
				opcionRolesFaltantesCancion();
			case 2:
				opcionRolesFaltantesRecital();
				break;
			case 3:
				opcionContratarCancion();
				break;
			case 4:
				opcionContratarRecital();
				break;
			case 5:
				opcionEntrenarArtista();
				break;
			case 6:
				opcionListarContratados();
				break;
			case 7:
				opcionListarCanciones();
				break;
			case 8:
				opcionConsultaProlog();
				break;
			case 9:
				System.out.println("Saliendo del programa...");
				break;
			default:
				System.out.println("Opción inválida. Intente nuevamente.");
				break;
			}

		} while (opcion != 9);
	}

	private int leerEntero() {
		while (!scanner.hasNextInt()) {
			System.out.print("Ingrese un número válido: ");
			scanner.next(); // descartar entrada inválida
		}
		int valor = scanner.nextInt();
		scanner.nextLine(); // limpiar buffer
		return valor;
	}

	private void opcionRolesFaltantesCancion() {
		System.out.println("[Opción 1] Mostrar roles faltantes de una canción...");
		// TODO: implementar
	}

	private void opcionRolesFaltantesRecital() {
		System.out.println("[Opción 2] Mostrar roles faltantes del recital...");
		// TODO: implementar
	}

	private void opcionContratarCancion() {
		System.out.println("[Opción 3] Contratar artistas para una canción...");
		// TODO: implementar
	}

	private void opcionContratarRecital() {
		System.out.println("[Opción 4] Contratar artistas para todo el recital...");
		// TODO: implementar
	}

	private void opcionEntrenarArtista() {
		System.out.println("[Opción 5] Entrenar artista...");
		// TODO: implementar
	}

	private void opcionListarContratados() {
		System.out.println("[Opción 6] Listar artistas contratados...");
		// TODO: implementar
	}

	private void opcionListarCanciones() {
		System.out.println("[Opción 7] Listar canciones y su estado...");
		// TODO: implementar
	}

	private void opcionConsultaProlog() {
		System.out.println("[Opción 8] Consultar entrenamientos mínimos (Prolog)...");
		// TODO: implementar
	}

}

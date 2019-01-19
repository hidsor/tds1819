package application.model;

import java.time.LocalDate;

public class MenoresFiltro implements Filtro {

	private static final String nombre = "Menores de edad";

	@Override
	public boolean filtrarVideo(Usuario usuario, Video video) {
		if (!esMayorEdad(usuario) && video.containsEtiqueta("Adultos")) {
			return false;
		}
		return true;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

	private static boolean esMayorEdad(Usuario usuario) {
		LocalDate fechaNacimiento = usuario.getFechaNac();
		if (LocalDate.now().getYear() - fechaNacimiento.getYear() > 18) {
			return true;
		} else if (LocalDate.now().getYear() - fechaNacimiento.getYear() == 18) {
			if (LocalDate.now().getDayOfYear() - fechaNacimiento.getDayOfYear() > 0) {
				return true;
			}
		}
		return false;
	}

}

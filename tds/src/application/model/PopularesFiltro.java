package application.model;

public class PopularesFiltro implements Filtro {
	
	private static final String nombre = "Populares";
	private static final int UmbralPopularidad = 5;

	@Override
	public boolean filtrarVideo(Usuario usuario, Video video) {
		if (video.getNumReproducciones() < UmbralPopularidad) {
			return false;
		}
		return true;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

}

package application.model;

public class NoFiltro implements Filtro {
	private static final String nombre = "Sin filtro";
	
	@Override
	public boolean filtrarVideo(Usuario usuario, Video video) {
		// No hace nada, no hay que filtrar
		return true;
	}

	@Override
	public String getNombre() {
		return nombre;
	}
	
	

}

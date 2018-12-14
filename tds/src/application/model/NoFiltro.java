package application.model;

public class NoFiltro implements Filtro {

	@Override
	public boolean filtrarVideo(Usuario usuario, Video video) {
		// No hace nada, no hay que filtrar
		return true;
	}

}

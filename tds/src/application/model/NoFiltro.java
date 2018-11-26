package application.model;

public class NoFiltro implements Filtro {

	@Override
	public boolean filtrarVideo(Video video) {
		// No hace nada, no hay que filtrar
		return true;
	}

}

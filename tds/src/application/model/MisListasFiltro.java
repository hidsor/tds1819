package application.model;

import java.util.List;

public class MisListasFiltro implements Filtro {
	
	private static final String nombre = "En mis listas";

	@Override
	public boolean filtrarVideo(Usuario usuario, Video video) {
		List<ListaVideos> listasVideos = usuario.getListas();
		for (ListaVideos lv : listasVideos) {
			if (lv.containsVideo(video))
				return true;
		}
		return false;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

}

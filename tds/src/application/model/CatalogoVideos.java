package application.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CatalogoVideos {

	// ATRIBUTOS
		private Set<Video> conjuntoVideos;

		
		// CONSTRUCTOR
		public CatalogoVideos() {
			conjuntoVideos = new HashSet<Video>();
		}
		
		
		// MÉTODOS DE CONSULTA
		public Set<Video> getConjuntoVideos() {
			return Collections.unmodifiableSet(conjuntoVideos);
		}


		//FUNCIONALIDAD
		boolean addVideo(Video video) {
			return conjuntoVideos.add(video);
		}
		
		boolean removeVideo(Video video) {
			return conjuntoVideos.remove(video);
		}
		
		Set<Video> buscarVideo(String cadena, Filtro filtro) {
			if (filtro == null) {
				System.err.println("Debe pasar un filtro como parámetro");
				throw new IllegalArgumentException();
			}
			Set<Video> videosEncontrados = new HashSet<Video>();
			for (Video i : conjuntoVideos) {
				if (i.getTitulo().contains(cadena) && filtro.filtrarVideo(i)) {
					videosEncontrados.add(i);
				}
			}
			return Collections.unmodifiableSet(videosEncontrados);
		}
		
		Set<Video> buscarVideo(String cadena) {
			return this.buscarVideo(cadena, new NoFiltro());
		}


}

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

}

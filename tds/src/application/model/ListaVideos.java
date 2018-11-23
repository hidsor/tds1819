package application.model;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ListaVideos {
	// ATRIBUTOS
	private int codigo;
	private String nombreLista;
	// private int numVideos;	<- Atributo calculado
	private List<Video> videos;	
	
	
	// CONSTRUCTORES
	public ListaVideos(String nombreLista) {
		this.nombreLista = nombreLista;
	}
	
	


	// MÉTODOS DE CONSULTA Y MODIFICADO
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getNombreLista() {
		return nombreLista;
	}

	public void setNombreLista(String nombreLista) {
		this.nombreLista = nombreLista;
	}

	public int getNumVideos() {
		return videos.size();
	}

	public List<Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}
	
	
	// FUNCIONALIDAD
	boolean addVideo(Video video) {
		return videos.add(video);
	}
	
	boolean removeVideo(Video video) {
		return videos.remove(video);
	}
	
	void reproducirLista() {
		for (Video i : videos) {
			// TODO
			// reproducirVideo();
		}
	}
	


}

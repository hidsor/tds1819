package application.model;

import java.util.Collections;
import java.util.List;

public class ListaVideos {
	// ATRIBUTOS
	private int codigo;
	private String nombre;
	// private int numVideos;	<- Atributo calculado
	private List<Video> videos;	
	
	
	// CONSTRUCTORES
	public ListaVideos(String nombre) {
		this.nombre = nombre;
	}
	
	


	// MÉTODOS DE CONSULTA Y MODIFICADO
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNumVideos() {
		return videos.size();
	}

	public List<Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}
	
	
	// FUNCIONALIDAD
	public boolean addVideo(Video video) {
		return videos.add(video);
	}
	
	public Video removeVideo(int index) {
		return videos.remove(index);
	}
	
	public boolean removeVideo(Video video) {
		return videos.remove(video);
	}
	
	public boolean containsVideo(Video video) {
		return videos.contains(video);
	}
	
	public void reproducirLista() {
		for (Video i : videos) {
			// TODO
			// reproducirVideo();
		}
	}
	


}

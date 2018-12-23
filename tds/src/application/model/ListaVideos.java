package application.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ListaVideos {
	
	// Atributos
	private int codigo;
	private String nombre;
	// private int numVideos;	<- Atributo calculado
	private List<Video> videos;	
	
	// Constructor
	public ListaVideos(String nombre) {
		this.nombre = nombre;
	}
	
	// M�todos de consulta y modificado
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
		if (videos == null) {
			videos = new LinkedList<Video>();
		}
		return Collections.unmodifiableList(videos);
	}

	// Funcionalidad
	public boolean addVideo(Video video) {
		if (videos == null) {
			videos = new LinkedList<Video>();
		}
		return videos.add(video);
	}
	
	public Video removeVideo(int index) {
		if (videos == null) {
			videos = new LinkedList<Video>();
		}
		return videos.remove(index);
	}
	
	public boolean removeVideo(Video video) {
		if (videos == null) {
			videos = new LinkedList<Video>();
		}
		return videos.remove(video);
	}
	
	public boolean containsVideo(Video video) {
		if (videos == null) {
			videos = new LinkedList<Video>();
		}
		return videos.contains(video);
	}
	
	public void reproducirLista() {
		//for (Video i : videos) {
			// TODO <--- �? Vlad no entender este m�todo
			// reproducirVideo(); 
		//}
	}
}

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
		this.videos = new LinkedList<Video>();
	}
	
	// Métodos de consulta y modificado
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

	// Funcionalidad
	public boolean addVideo(Video video) {
		if (videos.contains(video)) return false;
		return videos.add(video);
	}
	
	public boolean addVideo(int index, Video video) {
		if (videos.contains(video)) return false;
		videos.add(index, video);
		return true;
	}
	
	public Video removeVideo(int index) {
		return videos.remove(index);
	}
	
	public boolean removeVideo(Video video) {
		return videos.remove(video);
	}

	public boolean removeVideo(String videoURL) {
		return removeVideo(new Video(videoURL, "", 0));
	}

	public boolean containsVideo(Video video) {
		return videos.contains(video);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("Título de la lista: " + nombre + "\n");
		for (Video i : videos) {
			buffer.append("\t" + i.toString() + "\n");
		}
		return buffer.toString();
	}

}

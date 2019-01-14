package application.model;

public interface Filtro {
	public boolean filtrarVideo(Usuario usuario, Video video);
	public String getNombre();
}

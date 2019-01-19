package application.persistence;

import java.util.List;

import application.model.Video;

public interface IAdaptadorVideoDAO {

	public void registrarVideo(Video video);

	public void borrarVideo(Video video);

	public void modificarVideo(Video video);

	public Video recuperarVideo(int codigo);

	public List<Video> recuperarTodosVideos();
}

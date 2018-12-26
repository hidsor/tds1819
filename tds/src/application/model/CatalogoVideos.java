package application.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.persistence.DAOException;
import application.persistence.FactoriaDAO;
import application.persistence.IAdaptadorVideoDAO;

public class CatalogoVideos {

	// Atributos
	private Map<String, Video> videos;
	static private CatalogoVideos unicaInstancia = null;
	
	// Necesarios para la persistencia
	private FactoriaDAO dao;
	private IAdaptadorVideoDAO adaptadorVideo;
	
	// Patrón singleton
	public static CatalogoVideos getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new CatalogoVideos();
		}
		return unicaInstancia;
	}
	
	// Constructor
	private CatalogoVideos() {
		try {
			dao = FactoriaDAO.getInstancia();
			adaptadorVideo = dao.getVideoDAO();
			videos = new HashMap<String, Video>();
			cargarCatalogo();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	// Métodos de consulta
	public Map<String, Video> getVideos() {
		return Collections.unmodifiableMap(videos);
	}
	
	public Video getVideo(String URL) {
		return videos.get(URL);
	}

	// Funcionalidad
	public boolean addVideo(Video video) {
		// Imitamos un set; si el vídeo no estaba ya, lo metemos
		if (videos.get(video.getURL()) != null) return false;
		videos.put(video.getURL(), video);
		
		return true;
	}
	
	public boolean addAllVideos(List<Video> videos) {
		for (Video v : videos) {
			addVideo(v);
		}
		return true;
	}
	
	public boolean borrarVideo(Video video) {
		return videos.remove(video.getURL()) != null;
	}

	// Carga los videos de la base de datos en el conjunto;
	private void cargarCatalogo() throws DAOException {
		List<Video> videosBD = adaptadorVideo.recuperarTodosVideos();
		for (Video v : videosBD)
			videos.put(v.getURL(), v);
	}


}

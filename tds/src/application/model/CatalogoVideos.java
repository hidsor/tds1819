package application.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import application.persistence.DAOException;
import application.persistence.FactoriaDAO;
import application.persistence.IAdaptadorVideoDAO;

public class CatalogoVideos {

	// ATRIBUTOS�
	private Map<String, Video> videos;
	static private CatalogoVideos unicaInstancia = null;
	
	// necesarios para la persistencia
	private FactoriaDAO dao;
	private IAdaptadorVideoDAO adaptadorVideo;
	
	
	public static CatalogoVideos getUnicaInstancia() {
		if (unicaInstancia == null)
			return new CatalogoVideos();
		else
			return unicaInstancia;
	}
	
	// CONSTRUCTOR
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
	
	
	// M�TODOS DE CONSULTA
	public Map<String, Video> getVideos() {
		return Collections.unmodifiableMap(videos);
		
		//return new HashMap<String, Video>(videos);
	}


	//FUNCIONALIDAD
	public boolean addVideo(Video video) {
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
	
	boolean removeVideo(Video video) {
		return videos.remove(video.getURL()) != null;
	}
	
	
	// Carga los videos de la base de datos en el conjunto;
	private void cargarCatalogo() throws DAOException {
		List<Video> videosBD = adaptadorVideo.recuperarTodosVideos();
		for (Video v : videosBD)
			videos.put(v.getURL(), v);
		
		//hocus pocus
		Video v1 = new Video("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "Rick Astley - Never Gonna Give You Up (Video)", 1337);
		Video v2 = new Video("https://www.youtube.com/watch?v=QiFBgtgUtfw", "KOLM TRIIPU / THREE STRIPES", 1238);
		Video v3 = new Video("https://www.youtube.com/watch?v=FTrxDBDBOHU", "Obi-Wan says \"Hello There\" 67 million times", 1339);
		Video v4 = new Video("https://www.youtube.com/watch?v=FTQbiNvZqaY", "Toto - Africa (Official Music Video)", 1344);
		Video v5 = new Video("https://www.youtube.com/watch?v=KsomXlyTyaQ", "Matt & Kim - Yea Yeah", 1123);
		
		addVideo(v1);
		addVideo(v2);
		addVideo(v3);
		addVideo(v4);
		addVideo(v5);
	}


}

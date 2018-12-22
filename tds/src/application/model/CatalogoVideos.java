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

	// ATRIBUTOS
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
	
	
	// MÉTODOS DE CONSULTA
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
		v1.addEtiqueta(new Etiqueta("Music"));
		v1.addEtiqueta(new Etiqueta("Meme"));
		v1.addEtiqueta(new Etiqueta("Classic"));
		
		Video v2 = new Video("https://www.youtube.com/watch?v=QiFBgtgUtfw", "KOLM TRIIPU / THREE STRIPES", 1238);
		v2.addEtiqueta(new Etiqueta("Music"));
		v2.addEtiqueta(new Etiqueta("Meme"));
		
		Video v3 = new Video("https://www.youtube.com/watch?v=FTrxDBDBOHU", "Obi-Wan says \"Hello There\" 67 million times", 1339);
		v3.addEtiqueta(new Etiqueta("Meme"));
		v3.addEtiqueta(new Etiqueta("Movie"));
		
		Video v4 = new Video("https://www.youtube.com/watch?v=wGjtv2jHKrg", "Toto - Africa", 42);
		v4.addEtiqueta(new Etiqueta("Music"));
		v4.addEtiqueta(new Etiqueta("Meme"));
		v4.addEtiqueta(new Etiqueta("Classic"));
		
		Video v5 = new Video("https://www.youtube.com/watch?v=KsomXlyTyaQ", "Matt & Kim - Yea Yeah", 1123);
		v5.addEtiqueta(new Etiqueta("Music"));
		v5.addEtiqueta(new Etiqueta("Indie"));
		
		addVideo(v1);
		addVideo(v2);
		addVideo(v3);
		addVideo(v4);
		addVideo(v5);
	}


}

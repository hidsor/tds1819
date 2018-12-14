package application.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import application.persistence.DAOException;
import application.persistence.FactoriaDAO;
import application.persistence.IAdaptadorVideoDAO;

public class CatalogoVideos {

	// ATRIBUTOSç
	private Set<Video> conjuntoVideos;
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
			conjuntoVideos = new HashSet<Video>();
			cargarCatalogo();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	
	// MÉTODOS DE CONSULTA
	public Set<Video> getConjuntoVideos() {
		return Collections.unmodifiableSet(conjuntoVideos);
	}


	//FUNCIONALIDAD
	boolean addVideo(Video video) {
		return conjuntoVideos.add(video);
	}
	
	public boolean addAllVideos(List<Video> videos) {
		for (Video v : videos) {
			addVideo(v);
		}
		return true;
	}
	
	boolean removeVideo(Video video) {
		return conjuntoVideos.remove(video);
	}
	
	Set<Video> buscarVideo(Usuario usuario, String cadena, Filtro filtro) {
		if (filtro == null) {
			System.err.println("Debe pasar un filtro como parámetro");
			throw new IllegalArgumentException();
		}
		Set<Video> videosEncontrados = new HashSet<Video>();
		for (Video i : conjuntoVideos) {
			if (i.getTitulo().contains(cadena) && filtro.filtrarVideo(usuario, i)) {
				videosEncontrados.add(i);
			}
		}
		return Collections.unmodifiableSet(videosEncontrados);
	}
	
	Set<Video> buscarVideo(Usuario usuario, String cadena) {
		return this.buscarVideo(usuario, cadena, new NoFiltro());
	}
	
	
	// Carga los videos de la base de datos en el conjunto;
	private void cargarCatalogo() throws DAOException {
		List<Video> videosBD = adaptadorVideo.recuperarTodosVideos();
		for (Video u : videosBD)
			conjuntoVideos.add(u);
	}


}

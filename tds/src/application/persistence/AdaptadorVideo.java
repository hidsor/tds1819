package application.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import application.model.*;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorVideo implements IAdaptadorVideoDAO {
	
	// Constantes y atributos
	public static final String propURL = "URL";
	public static final String propTitulo = "titulo";
	public static final String propNumReproducciones = "num de reproducciones";
	public static final String propEtiquetas = "etiquetas";
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorVideo unicaInstancia;

	// Patrón singleton
	public static AdaptadorVideo getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorVideo();
		}
		return unicaInstancia;
	}

	private AdaptadorVideo() { 
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public void registrarVideo(Video video) {
		// Cuando se registra un vídeo se le asigna un identificador único
		Entidad eVideo;
		
		// Si la entidad está registrada no se registra de nuevo
		boolean existe = true; 
		try {
			eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;
		
		eVideo = new Entidad();
		eVideo.setNombre("video");
		eVideo.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad(propURL, video.getURL()),
						new Propiedad(propTitulo, video.getTitulo()),
						new Propiedad(propNumReproducciones, String.valueOf(video.getNumReproducciones())),
						new Propiedad(propEtiquetas, obtenerStringEtiquetas(video.getEtiquetas()))
				)
		));
		
		eVideo = servPersistencia.registrarEntidad(eVideo);
		
		// Se aprovecha el identificador único que genera el servicio de persistencia
		video.setCodigo(eVideo.getId()); 	
	}

	public void borrarVideo(Video video) {	
		Entidad eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
		servPersistencia.borrarEntidad(eVideo);
	}

	public void modificarVideo(Video video) {
		Entidad eVideo;
		eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
		actualizarPropiedadEntidad(eVideo, propURL, video.getURL());
		actualizarPropiedadEntidad(eVideo, propTitulo, video.getTitulo());
		actualizarPropiedadEntidad(eVideo, propNumReproducciones, String.valueOf(video.getNumReproducciones()));
		actualizarPropiedadEntidad(eVideo, propEtiquetas, obtenerStringEtiquetas(video.getEtiquetas()));
	}

	public Video recuperarVideo(int codigo) {
		// Si la entidad está en el pool se devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Video) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// Si no, se recupera de la base de datos
		Entidad eVideo = servPersistencia.recuperarEntidad(codigo);

		// Recuperar propiedades que no son objetos
		String URL = servPersistencia.recuperarPropiedadEntidad(eVideo, propURL);
		String titulo = servPersistencia.recuperarPropiedadEntidad(eVideo, propTitulo);
		int numReproducciones = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eVideo, propTitulo));
		
		Video video = new Video(URL, titulo, numReproducciones);
		video.setCodigo(codigo);
		Set<Etiqueta> etiquetas = obtenerEtiquetasDesdeString(servPersistencia.recuperarPropiedadEntidad(eVideo, propEtiquetas));
		for (Etiqueta i : etiquetas) {
			video.addEtiqueta(i);
		}	
		
		PoolDAO.getUnicaInstancia().addObjeto(codigo, video);
		
		return video;
	}

	public List<Video> recuperarTodosVideos() {
		List<Video> listasVideos = new LinkedList<Video>();
		List<Entidad> eVideos = servPersistencia.recuperarEntidades("video");

		for (Entidad eVideo : eVideos) {
			listasVideos.add(recuperarVideo(eVideo.getId()));
		}
		return listasVideos;
	}

	// Funcionalidad auxiliar
	private String obtenerStringEtiquetas(Set<Etiqueta> etiquetas) {
		String lineas = "";
		for (Etiqueta i : etiquetas) {
			lineas += i.getNombre() + " ";
		}
		return lineas.trim();
	}
	
	private Set<Etiqueta> obtenerEtiquetasDesdeString(String lineas) {
		Set<Etiqueta> etiquetas = new HashSet<Etiqueta>();
		StringTokenizer strTok = new StringTokenizer(lineas, " ");
		while (strTok.hasMoreTokens()) {
			etiquetas.add( new Etiqueta((String) strTok.nextElement()));
		}
		return etiquetas;
	}
	
	private void actualizarPropiedadEntidad(Entidad entidad, String propiedad, String nuevoValor) {
		servPersistencia.eliminarPropiedadEntidad(entidad, propiedad);
		servPersistencia.anadirPropiedadEntidad(entidad, propiedad, nuevoValor);
	}

}

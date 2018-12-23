package application.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import application.model.*;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorListaVideos implements IAdaptadorListaVideosDAO {
	
	// Constantes y atributos
	private static final String propNombre = "nombre";
	private static final String propNum = "numero de videos";
	private static final  String propVideos = "videos";
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorListaVideos unicaInstancia;

	// Patrón singleton
	public static AdaptadorListaVideos getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorListaVideos();
		}
		return unicaInstancia;
	}

	private AdaptadorListaVideos() { 
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public void registrarListaVideos(ListaVideos listaVideos) {
		// Cuando se registra una listaVideos se le asigna un identificador único
		Entidad eListaVideos;
		// Si la entidad está registrada no la registra de nuevo
		boolean existe = true; 
		try {
			eListaVideos = servPersistencia.recuperarEntidad(listaVideos.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;
		
		// Crear entidad listaVideos
		eListaVideos = new Entidad();

		eListaVideos.setNombre("listaVideos");
		eListaVideos.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad(propNombre, listaVideos.getNombre()),
						new Propiedad(propNum, String.valueOf(listaVideos.getNumVideos())),
						new Propiedad(propVideos, obtenerCodigosVideos(listaVideos.getVideos())))));
		
		// Registrar entidad listaVideos
		eListaVideos = servPersistencia.registrarEntidad(eListaVideos);
		
		// Se aprovecha el identificador único que genera el servicio de persistencia
		listaVideos.setCodigo(eListaVideos.getId()); 	
	}

	public void borrarListaVideos(ListaVideos listaVideos) {		
		// No hay que borrar los videos de la lista		
		Entidad eListaVideos = servPersistencia.recuperarEntidad(listaVideos.getCodigo());
		
		servPersistencia.borrarEntidad(eListaVideos);

	}

	public void modificarListaVideos(ListaVideos listaVideos) {
		Entidad eListaVideos;
		eListaVideos = servPersistencia.recuperarEntidad(listaVideos.getCodigo());
		
		actualizarPropiedadEntidad(eListaVideos, propNombre,listaVideos.getNombre());
		actualizarPropiedadEntidad(eListaVideos, propNum, String.valueOf(listaVideos.getNumVideos()));
		actualizarPropiedadEntidad(eListaVideos, propVideos, obtenerCodigosVideos(listaVideos.getVideos()));
	}

	public ListaVideos recuperarListaVideos(int codigo) {
		// Si la entidad está en el pool se devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (ListaVideos) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// Si no, se recupera de la base de datos
		Entidad eListaVideos = servPersistencia.recuperarEntidad(codigo);

		// Recuperar propiedades que no son objetos
		String nombre = servPersistencia.recuperarPropiedadEntidad(eListaVideos, propNombre);

		ListaVideos listaVideos = new ListaVideos(nombre);
		listaVideos.setCodigo(codigo);

		// IMPORTANTE: añadir la lista de vídeos al pool antes de llamar a otros adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, listaVideos);

		// Recuperar propiedades que son objetos llamando a adaptadores
		List<Video> videos = obtenerVideosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eListaVideos, propVideos));
		for (Video video : videos)
			listaVideos.addVideo(video);

		return listaVideos;
	}

	public List<ListaVideos> recuperarTodasListasVideos() {
		List<ListaVideos> listasVideos = new LinkedList<ListaVideos>();
		List<Entidad> eListaVideoss = servPersistencia.recuperarEntidades("listaVideos");

		for (Entidad eListaVideos : eListaVideoss) {
			listasVideos.add(recuperarListaVideos(eListaVideos.getId()));
		}
		return listasVideos;
	}

	// Funcionalidad auxiliar
	private String obtenerCodigosVideos(List<Video> videos) {
		String lineas = "";
		for (Video i : videos) {
			lineas += i.getCodigo() + " ";
		}
		return lineas.trim();

	}

	private List<Video> obtenerVideosDesdeCodigos(String lineas) {
		List<Video> videos = new LinkedList<Video>();
		StringTokenizer strTok = new StringTokenizer(lineas, " ");
		AdaptadorVideo adaptadorV = AdaptadorVideo.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			videos.add(adaptadorV.recuperarVideo(Integer.valueOf((String) strTok.nextElement())));
		}
		return videos;
	}
	
	private void actualizarPropiedadEntidad(Entidad entidad, String propiedad, String nuevoValor) {
		servPersistencia.eliminarPropiedadEntidad(entidad, propiedad);
		servPersistencia.anadirPropiedadEntidad(entidad, propiedad, nuevoValor);
	}

}

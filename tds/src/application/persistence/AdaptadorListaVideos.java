package application.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import application.model.*;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorListaVideos implements IAdaptadorListaVideosDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorListaVideos unicaInstancia;

	public static AdaptadorListaVideos getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorListaVideos();
		else
			return unicaInstancia;
	}

	private AdaptadorListaVideos() { 
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/* cuando se registra una listaVideos se le asigna un identificador unico */
	public void registrarListaVideos(ListaVideos listaVideos) {
		Entidad eListaVideos;
		// Si la entidad está registrada no la registra de nuevo
		boolean existe = true; 
		try {
			eListaVideos = servPersistencia.recuperarEntidad(listaVideos.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;

		// TODO ¿Hace falta hacer esto?
		// registrar primero los atributos que son objetos
		// registrar lineas de listaVideos
		/*
		AdaptadorLineaListaVideos adaptadorLV = AdaptadorLineaListaVideos.getUnicaInstancia();
		for (LineaListaVideos ldv : listaVideos.getLineasListaVideos())
			adaptadorLV.registrarLineaListaVideos(ldv);
		// registrar cliente
		AdaptadorClienteTDS adaptadorCliente = AdaptadorClienteTDS.getUnicaInstancia();
		adaptadorCliente.registrarCliente(listaVideos.getCliente());
		 */
		
		// Crear entidad listaVideos
		eListaVideos = new Entidad();

		eListaVideos.setNombre("listaVideos");
		eListaVideos.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("nombre", listaVideos.getNombre()),
						new Propiedad("numero de video", String.valueOf(listaVideos.getNumVideos())),
						new Propiedad("videos", obtenerCodigosVideos(listaVideos.getVideos())))));
		// registrar entidad listaVideos
		eListaVideos = servPersistencia.registrarEntidad(eListaVideos);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		listaVideos.setCodigo(eListaVideos.getId()); 	
	}

	
	public void borrarListaVideos(ListaVideos listaVideos) {
		
		// Esto no hace falta hacerlo, porque al eliminar una lista, no se borran los videos que la componen
		/*
		AdaptadorVideo adaptadorV = AdaptadorVideo.getUnicaInstancia();

		for (Video video : listaVideos.getVideos()) {
			adaptadorV.borrarVideo(video);
		}
		*/
		
		Entidad eListaVideos = servPersistencia.recuperarEntidad(listaVideos.getCodigo());
		servPersistencia.borrarEntidad(eListaVideos);

	}

	public void modificarListaVideos(ListaVideos listaVideos) {
		Entidad eListaVideos;

		eListaVideos = servPersistencia.recuperarEntidad(listaVideos.getCodigo());
		actualizarPropiedadEntidad(eListaVideos, "nombre",listaVideos.getNombre());
		actualizarPropiedadEntidad(eListaVideos, "numero de videos", String.valueOf(listaVideos.getNumVideos()));
		actualizarPropiedadEntidad(eListaVideos, "videos", obtenerCodigosVideos(listaVideos.getVideos()));
	}

	public ListaVideos recuperarListaVideos(int codigo) {
		// Si la entidad estï¿½ en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (ListaVideos) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		// recuperar entidad
		Entidad eListaVideos = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades que no son objetos
		// nombre
		String nombre = servPersistencia.recuperarPropiedadEntidad(eListaVideos, "nombre");
		
		
		ListaVideos listaVideos = new ListaVideos(nombre);
		listaVideos.setCodigo(codigo);

		// IMPORTANTE: anadir la lista de videos al pool antes de llamar a otros adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, listaVideos);

		// recuperar propiedades que son objetos llamando a adaptadores
		// videos
		List<Video> videos = obtenerVideosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eListaVideos, "videos"));
		for (Video video : videos)
			listaVideos.addVideo(video);

		// devolver el objeto listaVideos
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

	// -------------------Funciones auxiliares-----------------------------
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

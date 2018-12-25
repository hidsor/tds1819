package application.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import application.model.*;
import application.persistence.*;
import umu.tds.videos.Videos;
import umu.tds.videos.VideosEvent;
import umu.tds.videos.VideosListener;

public class AppVideo implements VideosListener {
	// Atributos
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorVideoDAO adaptadorVideo;
	private IAdaptadorListaVideosDAO adaptadorListaVideos;

	private CatalogoUsuarios catalogoUsuarios;
	private CatalogoVideos catalogoVideos;
	private ListaVideos topten;
	private Set<Etiqueta> listaEtiquetas;
	
	private Set<Etiqueta> etiquetasBusqueda;

	private Usuario usuarioActual; // Para saber qui�n est� usando la aplicaci�n

	// Patr�n singleton
	private static AppVideo unicaInstancia = null;

	// Constructor
	private AppVideo() {
		// Cargamos los adaptadores
		FactoriaDAO factoria;
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuario = factoria.getUsuarioDAO();
			adaptadorVideo = factoria.getVideoDAO();
			adaptadorListaVideos = factoria.getListaVideosDAO();
		} catch (DAOException e) {
			e.printStackTrace();
		}

		// Cargamos todos los elementos del controlador
		catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
		catalogoVideos = CatalogoVideos.getUnicaInstancia();

		// Obtenemos una lista con todos los v�deos del sistema
		List<Video> videos = new LinkedList<Video>(catalogoVideos.getVideos().values());

		// Inicializaci�n de los 10 v�deos m�s visualizados del sistema
		topten = new ListaVideos("topten");
		// Ordenamos los v�deos por n�mero de reproducciones
		// TODO: Por qu� pones un - a las reproducciones ????
		videos.sort((v1, v2) -> {
			return ((Integer) (-v1.getNumReproducciones())).compareTo(-v2.getNumReproducciones());
		});

		// A�adimos los 10 v�deos m�s reproducidos
		for (int i = 0; i < 10 && i < videos.size(); i++)
			catalogoVideos.addVideo(videos.get(i));

		// Conjunto de etiquetas
		// Recorremos todos los videos a�adiendo todas las etiquetas de cada uno al
		// conjunto
		listaEtiquetas = new HashSet<Etiqueta>();
		for (Video i : videos) {
			listaEtiquetas.addAll(i.getEtiquetas());
		}
		
		
		etiquetasBusqueda = new HashSet<Etiqueta>();
	}

	// Patr�n singleton
	public static AppVideo getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AppVideo();
		}
		return unicaInstancia;
	}

	// M�todos de consulta
	public CatalogoUsuarios getCatalogoUsuarios() {
		return catalogoUsuarios;
	}

	public CatalogoVideos getCatalogoVideos() {
		return catalogoVideos;
	}

	public ListaVideos getTopten() {
		return topten;
	}

	public Set<Etiqueta> getListaEtiquetas() {
		return Collections.unmodifiableSet(listaEtiquetas);
	}
	
	public Set<Etiqueta> getEtiquetasBusqueda() {
		return Collections.unmodifiableSet(etiquetasBusqueda);
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	// Funcionalidad
	public boolean verificarUsuario(String login, String password) {
		// Buscamos el usuario en el cat�logo
		Usuario usuario = catalogoUsuarios.getUsuario(login);

		// Si el usuario no est� registrado, el login es v�lido
		if (usuario == null) {
			return false;
		}

		// Si est� registrado y la contrase�a es correcta, el login es v�lido
		if (usuario.getPassword().equals(password)) {
			usuarioActual = usuario;
			return true;
		}
		// Si est� registrado, pero la contrase�a no es correcta, el login es inv�lido
		return false;
	}
	
	
	
	// Devuelve verdadero si se ha podido registrar.
	// Devuelve falso si no (ya hay alguien con ese login en el sistema)
	public boolean registrarUsuario(String login, String password, String nombre, String apellidos, LocalDate fechaNac,
			String email) {
		Usuario usuario = new Usuario(login, password, nombre, apellidos, fechaNac, email);
		if (catalogoUsuarios.addUsuario(usuario)) {
			adaptadorUsuario.registrarUsuario(usuario);
			usuarioActual = usuario;
			return true;
		}
		return false;
	}
	
	public boolean registrarVideo(Video video) {
		if (catalogoVideos.addVideo(video)) {
			adaptadorVideo.registrarVideo(video);
			return true;
		}
		return false;
	}
	
	public void salirUsuario() {
		usuarioActual = null;
	}
	
	public boolean addEtiqueta(Etiqueta etiqueta) {
		return listaEtiquetas.add(etiqueta);
	}
	
	public boolean addEtiquetaBusqueda(String nombre) {
		Etiqueta etiqueta = new Etiqueta(nombre);
		return etiquetasBusqueda.add(etiqueta);
	}
	
	public boolean removeEtiquetaBusqueda(String nombre) {
		Etiqueta etiqueta = new Etiqueta(nombre);
		return etiquetasBusqueda.remove(etiqueta);
	}
	
	public boolean isEtiquetasBusquedaEmpty() {
		return etiquetasBusqueda.isEmpty();
	}
	

	// Buscar un v�deo que contenga la cadena pasada de par�metro (case insensitive)
	public Set<Video> buscarVideos(String cadena) {
		Set<Video> resultados = new HashSet<>();
		Filtro filtro = usuarioActual.getFiltro();

		// Recorremos todos los videos. Si el video contiene la cadena que buscamos
		// y la condici�n del filtro se cumple, es un posible resultado.
		for (Video i : catalogoVideos.getVideos().values()) {
			if (i.contieneTitulo(cadena) && filtro.filtrarVideo(usuarioActual, i) 
					&& ( etiquetasBusqueda.isEmpty() || i.containsAllEtiquetas(etiquetasBusqueda) )) {
				resultados.add(i);
			}
		}
		return resultados;
	}

	// Modifica los campos del usuarioActual pasados de par�metro
	// Solo se modifican los campos que no sean nulos o vac�os
	public void modificarUsuarioActual(String email, String password, LocalDate fechaNac) {
		boolean cambiado = false;

		if (email != null && !email.equals("")) {
			usuarioActual.setEmail(email);
			cambiado = true;
		}
		if (password != null && !password.equals("")) {
			usuarioActual.setPassword(password);
			cambiado = true;
		}
		if (fechaNac != null) {
			usuarioActual.setFechaNac(fechaNac);
			cambiado = true;
		}
		if (cambiado)
			adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	// Convertir un usuario en premium
	public void obtenerPremium() {
		usuarioActual.setPremium();
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	public boolean addEtiquetaVideo(Etiqueta etiqueta, Video video) {
		video.addEtiqueta(etiqueta);
		listaEtiquetas.add(etiqueta);
		adaptadorVideo.modificarVideo(video);
		return true;
	}

	public void crearListaVideos(String nombre) {
		ListaVideos listaVideos = new ListaVideos(nombre);
		usuarioActual.addListaVideos(listaVideos);
		adaptadorListaVideos.registrarListaVideos(listaVideos);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	public void removeListaVideos(ListaVideos listaVideos) {
		usuarioActual.removeListaVideos(listaVideos);
		adaptadorListaVideos.borrarListaVideos(listaVideos);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}

	public boolean addVideoALista(Video video, ListaVideos listaVideos) {
		listaVideos.addVideo(video);
		adaptadorListaVideos.modificarListaVideos(listaVideos);
		return true;
	}

	public boolean removeVideoDeLista(Video video, ListaVideos listaVideos) {
		listaVideos.removeVideo(video);
		adaptadorListaVideos.modificarListaVideos(listaVideos);
		return true;
	}

	public void reproducir(Video video) {
		// Simplemente incrementamos el n�mero de visitas del v�deo y lo persistimos
		video.reproducir();
		adaptadorVideo.modificarVideo(video);

	}
	
	public void crearPDF(String nombre){
		try {
			FileOutputStream archivo = new FileOutputStream(nombre);
			Document documento = new Document();
			PdfWriter.getInstance(documento, archivo);
		    documento.open();
		    documento.add(new Paragraph(nombre + "\n\n"));
		    documento.add(new Paragraph(usuarioActual.infoListasVideos()));
		    documento.close();
			
		} catch (FileNotFoundException e) {			
			System.err.println("No se pudo crear el archivo " + nombre);
		}
		catch (DocumentException e) {
			System.err.println("No se pudo escribir en el documento");
		}
	}

	@Override
	public void nuevosVideos(VideosEvent evento) {
		List<Video> videos = adaptarVideos(evento.getVideos());
		for (Video i : videos) {
			registrarVideo(i);
			for (Etiqueta j : i.getEtiquetas()) {
				listaEtiquetas.add(j);
			}
		}
	}
	
	private List<Video> adaptarVideos(Videos videos) {
		List<Video> videosAdaptados = new LinkedList<Video>();
		for (umu.tds.videos.Video i : videos.getVideo()) {
			Video video = new Video(i.getUrl(), i.getTitulo(), 0);
			for (umu.tds.videos.Etiqueta j : i.getEtiqueta()) {
				video.addEtiqueta(new Etiqueta(j.getNombre()));
			}
			videosAdaptados.add(video);			
		}
		
		return videosAdaptados;		
	}
	
	
}

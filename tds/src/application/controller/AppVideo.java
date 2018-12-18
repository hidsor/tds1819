package application.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
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
	
	private Usuario usuarioActual; // Para saber quién está usando la aplicación
	
	// Patrón singleton
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
		
		// Obtenemos una lista con todos los vídeos del sistema
		List<Video> videos = new LinkedList<Video>(catalogoVideos.getVideos().values());
		
		// Topten videos
		topten = new ListaVideos("topten");
		videos.sort( (v1, v2) -> { return ((Integer)(-v1.getNumReproducciones())).compareTo(-v2.getNumReproducciones()); } );
		for (int i = 0; i < 10 && i < videos.size(); i++)
			catalogoVideos.addVideo(videos.get(i));
		

		// Conjunto de etiquetas
		// Recorremos todos los videos añadiendo todas las etiquetas de cada uno al conjunto
		listaEtiquetas = new HashSet<Etiqueta>();
		for (Video i : videos) {
			listaEtiquetas.addAll(i.getEtiquetas());
		}
		
		
	}
	
	// Patrón singleton
	public static AppVideo getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AppVideo();
			return unicaInstancia;
		} else {
			return unicaInstancia;
		}
	}
	
	
	
	
	// Métodos get
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
		return listaEtiquetas;
	}
	
	public Usuario getUsuarioActual() {
		return usuarioActual;
	}
	
	
	
	
	// Funcionalidad
	public boolean verificarUsuario(String login, String password) {
		Usuario usuario = catalogoUsuarios.getUsuario(login);
		// Si el usuario no está registrado, devuelve falso
		if (usuario == null)
			return false;
		
		// Si está registrado y la contraseña es correcta, devuelve verdadero
		if (usuario.getPassword().equals(password)) {
			usuarioActual = usuario;
			return true;
		}
		
		// Si está registrado, pero la contraseña no es correcta, devuelve falso.
		else
			return false;
	}
	
	
	
	// Devuelve verdadero si se ha podido registrar.
	// Devuelve falso si no (ya hay alguien con ese login en el sistema)
	public boolean registrarUsuario(String login, String password, String nombre, String apellidos, LocalDate fechaNac, String email) {
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
	
	// Busca un video
	public Set<Video> buscar(String cadena) {
		Set<Video> resultados = new HashSet<>();
		Filtro filtro = usuarioActual.getFiltro();
		
		// Recorremos todos los videos. Si el video contiene la cadena que buscamos 
		// y la condición del filtro se cumple, es un posible resultado.
		for (Video i : catalogoVideos.getVideos().values()) {
			if (i.getTitulo().contains(cadena) && filtro.filtrarVideo(usuarioActual, i)) {
				resultados.add(i);
			}
		}	
		return resultados;
	}

	
	// Ahora el usuario será premium
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
	
	public void borrarListaVideos(ListaVideos listaVideos) {
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
		// Aquí, con el componente que debemos disenar, reproduciremos el video
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

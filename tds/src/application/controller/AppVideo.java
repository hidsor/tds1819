package application.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	private Map<Etiqueta, Integer> listaEtiquetas;
	
	private Set<Etiqueta> etiquetasBusqueda;

	private Usuario usuarioActual; // Para saber quiï¿½n estï¿½ usando la aplicaciï¿½n

	// Patrï¿½n singleton
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

		// Obtenemos una lista con todos los vï¿½deos del sistema
		List<Video> videos = new LinkedList<Video>(catalogoVideos.getVideos().values());

		// Inicializaciï¿½n de los 10 vï¿½deos mï¿½s visualizados del sistema
		topten = new ListaVideos("topten");
		// Ordenamos los vï¿½deos por nï¿½mero de reproducciones
		// TODO: Por quï¿½ pones un - a las reproducciones ????
		videos.sort((v1, v2) -> {
			return ((Integer) (-v1.getNumReproducciones())).compareTo(-v2.getNumReproducciones());
		});

		// Aï¿½adimos los 10 vï¿½deos mï¿½s reproducidos
		for (int i = 0; i < 10 && i < videos.size(); i++)
			catalogoVideos.addVideo(videos.get(i));

		// Actualizamos la "lista" de etiquetas global con el número de referencias de cada etiqueta para luego poder borrar
		// correctamente
		listaEtiquetas = new HashMap<Etiqueta, Integer>();
		for (Video video : videos) {
			for (Etiqueta etiqueta : video.getEtiquetas()) {
				incrementarReferenciasEtiqueta(etiqueta);
			}
		}
		
		
		etiquetasBusqueda = new HashSet<Etiqueta>();
	}

	// Patrón singleton
	public static AppVideo getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AppVideo();
		}
		return unicaInstancia;
	}

	// Métodos de consulta
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
		return Collections.unmodifiableSet(listaEtiquetas.keySet());
	}
	
	public boolean containsEtiqueta(Etiqueta etiqueta) {
		Integer contadorReferencias = listaEtiquetas.get(etiqueta);
		if (contadorReferencias == null) return false;
		return true;
	}
	public Set<Etiqueta> getEtiquetasBusqueda() {
		return Collections.unmodifiableSet(etiquetasBusqueda);
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}
	
	public Video getVideo(String URL) {
		return catalogoVideos.getVideo(URL);
	}

	// Funcionalidad
	public boolean verificarUsuario(String login, String password) {
		// Buscamos el usuario en el catálogo
		Usuario usuario = catalogoUsuarios.getUsuario(login);

		// Si el usuario no está registrado, el login es inválido
		if (usuario == null) {
			return false;
		}

		// Si está registrado y la contraseña es correcta, el login es válido
		if (usuario.getPassword().equals(password)) {
			usuarioActual = usuario;
			return true;
		}
		// Si está registrado, pero la contraseña no es correcta, el login es inválido
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
	
	public boolean registrarVideo(String URL, String titulo) {
		Video video = new Video(URL, titulo, 0);
		return registrarVideo(video);
	}
	
	public boolean borrarVideo(String URL) {
		Video video = catalogoVideos.getVideo(URL);
		if (video == null)
			return false;
		if (!catalogoVideos.borrarVideo(video))
			return false;
		adaptadorVideo.borrarVideo(video);
		return true;
		
	}
	
	public void salirUsuario() {
		usuarioActual = null;
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
	

	// Buscar un vï¿½deo que contenga la cadena pasada de parï¿½metro (case insensitive)
	public Set<Video> buscarVideos(String cadena) {
		if (usuarioActual == null) return null;
		
		Set<Video> resultados = new HashSet<>();
		Filtro filtro = usuarioActual.getFiltro();

		// Recorremos todos los videos. Si el video contiene la cadena que buscamos
		// y la condiciï¿½n del filtro se cumple, es un posible resultado.
		for (Video i : catalogoVideos.getVideos().values()) {
			if (i.contieneTitulo(cadena) && filtro.filtrarVideo(usuarioActual, i) 
					&& ( etiquetasBusqueda.isEmpty() || i.containsAllEtiquetas(etiquetasBusqueda) )) {
				resultados.add(i);
			}
		}
		return resultados;
	}

	// Modifica los campos del usuarioActual pasados de parï¿½metro
	// Solo se modifican los campos que no sean nulos o vacï¿½os
	public boolean modificarUsuarioActual(String email, String password, LocalDate fechaNac) {
		if (usuarioActual == null) return false;
		
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
		return true;
	}

	// Convertir un usuario en premium
	public boolean obtenerPremium() {
		if (usuarioActual == null) return false;
		
		usuarioActual.setPremium();
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return true;
	}

	// Añade una etiqueta al vídeo, si este existe en el catálogo
	// Consideramos oportuno trabajar directamente con una etiqueta en vez de crearla porque es una clase contenedor
	public boolean addEtiquetaVideo(Etiqueta etiqueta, String URL) {
		Video video = catalogoVideos.getVideo(URL);
		if (video == null) return false;
		
		if (!video.addEtiqueta(etiqueta)) return false;
		incrementarReferenciasEtiqueta(etiqueta);
		adaptadorVideo.modificarVideo(video);
		return true;
	}
	
	public boolean borrarEtiquetaVideo(Etiqueta etiqueta, String URL) {
		Video video = catalogoVideos.getVideo(URL);
		if (video == null) return false;
		
		if (!video.removeEtiqueta(etiqueta)) return false;
		decrementarReferenciasEtiqueta(etiqueta);
		adaptadorVideo.modificarVideo(video);
		return true;
	}

	public boolean crearListaVideos(String titulo) {
		if (usuarioActual == null) return false;
		
		ListaVideos listaVideos = new ListaVideos(titulo);
		if (!usuarioActual.addListaVideos(listaVideos)) return false;
		
		adaptadorListaVideos.registrarListaVideos(listaVideos);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return true;
	}

	public boolean removeListaVideos(String titulo) {
		ListaVideos listaVideos = usuarioActual.getListaVideos(titulo);
		if (!usuarioActual.borrarListaVideos(titulo)) return false;
		
		adaptadorListaVideos.borrarListaVideos(listaVideos);
		adaptadorUsuario.modificarUsuario(usuarioActual);
		return true;
	}
	
	public ListaVideos getListaVideos(String titulo) {
		return usuarioActual.getListaVideos(titulo);
	}

	// Añade un vídeo, si existe en el catálogo, a la lista especificada, si existe en el usuario actual
	public boolean addVideoALista(String videoURL, String tituloLista) {
		if (usuarioActual == null) return false;
		
		Video video = catalogoVideos.getVideo(videoURL);
		if (video == null) return false;
		
		ListaVideos listaVideos = usuarioActual.getListaVideos(tituloLista);
		if (listaVideos == null) return false;	
		
		listaVideos.addVideo(video);
		adaptadorListaVideos.modificarListaVideos(listaVideos);
		return true;
	}
	
	// Borra un vídeo, si existe en el catálogo, de la lista especificada, si existe en el usuario actual
	public boolean removeVideoDeLista(String videoURL, String tituloLista) {
		if (usuarioActual == null) return false;
		
		Video video = catalogoVideos.getVideo(videoURL);
		if (video == null) return false;
		
		ListaVideos listaVideos = usuarioActual.getListaVideos(tituloLista);
		if (listaVideos == null) return false;	
		
		listaVideos.removeVideo(video);
		adaptadorListaVideos.modificarListaVideos(listaVideos);
		return true;
	}

	// Reproduce un vídeo del catálogo actual, si está
	public boolean reproducir(String URL) {
		Video video = catalogoVideos.getVideo(URL);
		if (video == null) return false;
		
		video.reproducir();
		adaptadorVideo.modificarVideo(video);
		return true;

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
		for (Video video : videos) {
			registrarVideo(video);
			for (Etiqueta etiqueta : video.getEtiquetas()) {
				incrementarReferenciasEtiqueta(etiqueta);
			}
		}
	}
	
	// Funcionalidad auxiliar
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
	
	private void incrementarReferenciasEtiqueta(Etiqueta etiqueta) {
		Integer contadorReferencias = listaEtiquetas.get(etiqueta);
		if (contadorReferencias == null) contadorReferencias = 0;
		listaEtiquetas.put(etiqueta, ++contadorReferencias);
	}
	
	private void decrementarReferenciasEtiqueta(Etiqueta etiqueta) {
		Integer contadorReferencias = listaEtiquetas.get(etiqueta);
		if (contadorReferencias == null) return;
		if (contadorReferencias == 1) {
			listaEtiquetas.remove(etiqueta);
		} else {
			listaEtiquetas.put(etiqueta, --contadorReferencias);
		}
		
	}
}

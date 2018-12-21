package application.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import application.model.*;
import application.persistence.*;

public class AppVideo {
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
	
	// Modifica los campos del usuarioActual pasados de parámetro 
	// Solo se modifican los campos que no sean nulos o vacíos
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
		if (cambiado) adaptadorUsuario.modificarUsuario(usuarioActual);
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
	
	public void crearPDF() {
		// TODO
		// Como ostias se usa iText???
		// Nombro rey de toda Inglaterra a quien lo consiga
	}
	
	
}

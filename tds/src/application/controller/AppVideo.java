package application.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import application.model.*;

public class AppVideo {
	// Atributos
	private CatalogoUsuarios catalogoUsuarios;
	private CatalogoVideos catalogoVideos;
	private ListaVideos topten;
	private Set<Etiqueta> listaEtiquetas;
	
	private Usuario usuarioActual; // Para saber quién está usando la aplicación
	
	// Patrón singleton
	private static AppVideo unicaInstancia = null;
	
	// Constructor
	private AppVideo() {
		// Cargamos todos los elementos del controlador
		catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
		catalogoVideos = CatalogoVideos.getUnicaInstancia();
		
		// Obtenemos una lista con todos los vídeos del sistema
		List<Video> videos = (List<Video>) catalogoVideos.getVideos().values();
		
		// Topten videos
		topten = new ListaVideos("topten");
		videos.sort( (v1, v2) -> { return ((Integer)(-v1.getNumReproducciones())).compareTo(-v2.getNumReproducciones()); } );
		for (int i = 0; i < 10; i++)
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
			usuarioActual = usuario;
			return true;
		}
		return false;
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
	}
	
	public boolean addEtiquetaVideo(Etiqueta etiqueta, Video video) {
		return video.addEtiqueta(etiqueta);
	}
	
	
	
	
}

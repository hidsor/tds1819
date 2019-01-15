package application.JUnitTests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import application.model.Filtro;
import application.model.ListaVideos;
import application.model.MisListasFiltro;
import application.model.NoFiltro;
import application.model.Usuario;
import application.model.Video;

public class TestUsuario {
	// Clase a testear
	private Usuario usuario;
	
	// Para mayor legibilidad, declaramos constantes
	private final String login = "EnriTDS1998";
	private final String password = "12345";
	private final String nombre = "Enrique";
	private final String apellidos = "Valero Leal";
	private final String email = "enrique.valerol@um.es";
	private final String fechaNac = "1998-05-29";
	

	@Before
	public void inicializarUsuario() {
		usuario = new Usuario(login, password, nombre, apellidos, LocalDate.parse(fechaNac), email);
	}
	
	@Test
	public void testCreacion() {
		// Comprobamos que el usuario se ha creado correctamente (con los parámetros especificados y sin ser premium)
		assertEquals("Login", login, usuario.getLogin());
		assertEquals("Contraseña", password, usuario.getPassword());
		assertEquals("Nombre", nombre, usuario.getNombre());
		assertEquals("Nombre", apellidos, usuario.getApellidos());
		assertEquals("E-mail", email, usuario.getEmail());
		assertEquals("Fecha de nacimiento", fechaNac, usuario.getFechaNac().toString());
		assertEquals("Premium", false, usuario.isPremium());				
	}
	
	@Test
	public void testPremium() {
		assertEquals("Filtro antes de pasar a premium", NoFiltro.class.getName(), usuario.getFiltro().getClass().getName());
		
		// Pasamos a premium
		usuario.setPremium(true);
		assertEquals("Pasar a premium", true, usuario.isPremium());
		assertEquals("Filtro tras pasar a premium",  NoFiltro.class.getName(), usuario.getFiltro().getClass().getName());
		
		// Eligimos un filtro
		Filtro filtro = new MisListasFiltro();
		usuario.setFiltro(filtro);
		assertEquals("Filtro seleccionado", filtro.getClass().getName(), usuario.getFiltro().getClass().getName());
		
		// Salimos de premium
		usuario.setPremium(false);
		assertEquals("Salir de premium", false, usuario.isPremium());
		assertEquals("Filtro tras salir de premium",  NoFiltro.class.getName(), usuario.getFiltro().getClass().getName());
		
		// Intentamos elegir un filtro
		boolean intento = usuario.setFiltro(filtro);
		assertEquals("Intentar elegir filtro sin ser premium", false, intento);
	}
	
	@Test
	public void testRecientes() {
		Video video1 = new Video("1", "1", 0);
		assertEquals("Lista recientes al comenzar", 0, usuario.getListaRecientes().getNumVideos());
		usuario.reproducirVideo(video1);
		assertEquals("1 video reciente", 1, usuario.getListaRecientes().getNumVideos());
		usuario.reproducirVideo(video1);		
		assertEquals("Video aparece una sola vez en recientes", 1, usuario.getListaRecientes().getNumVideos());
		
		// Comprobamos si al reproducir mas de 5 videos, solo se almacenen 5 en recientes
		for (int i = 2; i < 7; i++) {
			Video videoi = new Video(Integer.toString(i), Integer.toString(i), 0);
			usuario.reproducirVideo(videoi);
			assertEquals(i+" videos reciente", Math.min(i, 5), usuario.getListaRecientes().getNumVideos());
		}
	}
	
	@Test
	public void testCrearListasVideos() {
		assertEquals("Numero de listas iniciales", 0, usuario.getListas().size());
		
		usuario.addListaVideos( new ListaVideos("Mi Lista"));
		assertEquals("Añadida primera lista", 1 , usuario.getListas().size());
		
		usuario.addListaVideos(new ListaVideos("Mi lista"));
		assertEquals("Añadir dos listas con el mismo nombre", false , usuario.addListaVideos(new ListaVideos("Mi lista")));
		
		assertEquals("Añadir una lista llamada Recientes", false , usuario.addListaVideos(new ListaVideos(usuario.getListaRecientes().getNombre())));
		assertEquals("Borrar una lista existente", true, usuario.borrarListaVideos("Mi Lista"));	
		assertEquals("Intentar borrar una lista inexistente", false, usuario.borrarListaVideos("Mi Lista"));
	}
	
	
	

}

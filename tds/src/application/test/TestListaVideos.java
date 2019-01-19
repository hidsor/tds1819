package application.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import application.model.ListaVideos;
import application.model.Video;

public class TestListaVideos {
	private ListaVideos lista;

	private final String nombre = "Mi lista";

	@Before
	public void inicializarListaVideos() {
		lista = new ListaVideos(nombre);
	}

	@Test
	public void testCreacion() {
		assertEquals("Nombre", nombre, lista.getNombre());
		assertEquals("N�mero de v�deos", 0, lista.getNumVideos());
	}

	@Test
	public void testAddVideos() {
		assertEquals("A�adir primer v�deo", true, lista.addVideo(new Video("1", "1", 0)));
		assertEquals("A�adido primer video", 1, lista.getNumVideos());
		assertEquals("A�adir dos videos iguales (Misma URL)", false, lista.addVideo(new Video("1", "1b", 5)));
		assertEquals("A�adir un video diferente", true, lista.addVideo(new Video("2", "2", 0)));
		assertEquals("A�adir video en una posicion x", true, lista.addVideo(1, new Video("3", "3", 0)));
		assertEquals("A�adir video repetido en una posicion x", false, lista.addVideo(1, new Video("3", "3b", 7)));
	}

	@Test
	public void deleteVideos() {
		assertEquals("Eliminar video con lista vac�a", false, lista.removeVideo(new Video("Video", "video", 0)));
		for (int i = 0; i < 10; i++) {
			lista.addVideo(new Video("Video" + Integer.toString(i), Integer.toString(i), 0));
		}
		Video inexistente = new Video("inexistenteURL", "inexistenteTitulo", 0);
		assertEquals("Eliminar video que no est� en la lista", false, lista.removeVideo(inexistente));
		assertEquals("Eliminar video (URL)", true, lista.removeVideo("Video8"));
		assertEquals("Eliminar video (objeto)", true, lista.removeVideo(new Video("Video5", "some", 1337)));
		assertEquals("Eliminar video (�ndice)", new Video("Video4", "some", 1337), lista.removeVideo(4));
		assertEquals("Videos al final de borrar", 7, lista.getNumVideos());

	}

}

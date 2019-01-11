package application.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.persistence.*;

public class CatalogoUsuarios {

	// Atributos
	private Map<String, Usuario> usuarios;
	private static CatalogoUsuarios unicaInstancia = null;
	private Map<String, Filtro> filtros;

	// Necesarios para la persistencia
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;

	// Patrón singleton
	public static CatalogoUsuarios getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new CatalogoUsuarios();
		}
		return unicaInstancia;
	}

	// Constructor
	private CatalogoUsuarios() {
		try {
			dao = FactoriaDAO.getInstancia();
			adaptadorUsuario = dao.getUsuarioDAO();
			usuarios = new HashMap<String, Usuario>();
			inicializarFiltros();
			this.cargarCatalogo();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}

	// Métodos de consulta
	public Usuario getUsuario(String login) {
		return usuarios.get(login);
	}

	// Funcionalidad
	public boolean addUsuario(Usuario usuario) {
		// Imitamos un set
		if (usuarios.get(usuario.getLogin()) != null) return false;
		usuarios.put(usuario.getLogin(), usuario);
		
		return true;
	}

	public void removeUsuario(Usuario usuario) {
		usuarios.remove(usuario.getLogin());
	}

	private void cargarCatalogo() throws DAOException {
		List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		for (Usuario u : usuariosBD)
			usuarios.put(u.getLogin(), u);
	}
	
	// Funcionalidad auxiliar
	private void inicializarFiltros() {
		// Lo separamos en un método auxiliar para aumentar legibilidad
		// Asimismo, si se desean añadir nuevos filtros, basta con agregar un elemento al enumerado e inicializarlo aquí
		filtros = new HashMap<String, Filtro>();
		filtros.put("NoFiltro", new NoFiltro());
		filtros.put("MisListasFiltro", new MisListasFiltro());
		
		// TODO: Añadir un filtro más
	}

}

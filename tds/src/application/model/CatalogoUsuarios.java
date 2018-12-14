package application.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import application.persistence.*;

public class CatalogoUsuarios {
	// ATRIBUTOS
	private Set<Usuario> conjuntoUsuarios;
	private static CatalogoUsuarios unicaInstancia = null;
	
	// necesarios para la persistencia
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;

	
	public static CatalogoUsuarios getUnicaInstancia() {
		if (unicaInstancia == null)
			return new CatalogoUsuarios();
		else
			return unicaInstancia;
	}
	
	// CONSTRUCTOR
	private CatalogoUsuarios() {
		try {
			dao = FactoriaDAO.getInstancia();
			adaptadorUsuario = dao.getUsuarioDAO();
			conjuntoUsuarios = new HashSet<Usuario>();
			this.cargarCatalogo();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	// MÉTODOS GET
	public Set<Usuario> getConjuntoUsuarios() {
		return Collections.unmodifiableSet(conjuntoUsuarios);
	}


	//FUNCIONALIDAD
	boolean addUsuario(Usuario usuario) {
		return conjuntoUsuarios.add(usuario);
	}
	
	boolean removeUsuario(Usuario usuario) {
		return conjuntoUsuarios.remove(usuario);
	}
	
	private void cargarCatalogo() throws DAOException {
		List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
		for (Usuario u : usuariosBD)
			conjuntoUsuarios.add(u);
	}
	

}

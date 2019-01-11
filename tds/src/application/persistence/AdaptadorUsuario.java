package application.persistence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import application.model.*;
import beans.*;
import tds.driver.*;

public class AdaptadorUsuario implements IAdaptadorUsuarioDAO {
	
	// Constantes y atributos
	public static final String propLogin = "login";
	public static final String propPassword = "password";
	public static final String propNombre = "nombre";
	public static final String propApellidos = "apellidos ";
	public static final String propFecNac = "fecha nacimiento";
	public static final String propEmail = "email";
	public static final String propPremium = "premium";
	public static final String propListas = "listas";
	public static final String propRecientes = "lista recientes";
	public static final String propFiltro = "filtro";
	
	private final static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuario unicaInstancia = null;

	// Patrón singleton
	public static AdaptadorUsuario getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorUsuario();
		}
		return unicaInstancia;
	}

	private AdaptadorUsuario() { 
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia(); 
		/*
		//TODO: EL MEGA FLUSH POR SI LA VUELVO A LIAR
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		for (Entidad e : eUsuarios) {
			servPersistencia.borrarEntidad(e);
		}
		*/
	}

	public void registrarUsuario(Usuario usuario) {
		// Cuando se registra un usuario se le asigna un identificador único
		Entidad eUsuario;
		boolean existe = true; 
		
		// Si la entidad está registrada no se registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;

		// Registrar primero los atributos que son objetos	
		AdaptadorListaVideos adaptadorLV = AdaptadorListaVideos.getUnicaInstancia();
		for (ListaVideos lv : usuario.getListas())
			adaptadorLV.registrarListaVideos(lv);
		
		adaptadorLV.registrarListaVideos(usuario.getListaRecientes());
		
		// Crear entidad Usuario
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad(propLogin, usuario.getLogin()),
						new Propiedad(propPassword, usuario.getPassword()),
						new Propiedad(propNombre, usuario.getNombre()),
						new Propiedad(propApellidos, usuario.getApellidos()),
						new Propiedad(propFecNac, localDateToString(usuario.getFechaNac())),
						new Propiedad(propEmail, usuario.getEmail()),
						new Propiedad(propPremium, String.valueOf(usuario.isPremium())),
						new Propiedad(propListas, obtenerCodigosListasVideos(usuario.getListas())),
						new Propiedad(propRecientes, String.valueOf(usuario.getListaRecientes().getCodigo()))
					)));
		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		
		// Se aprovecha el identficador único que genera el servicio de persistencia
		usuario.setCodigo(eUsuario.getId()); 
	}

	public void borrarUsuario(Usuario usuario) {
		// Eliminamos las listas de reproducción del usuario.
		AdaptadorListaVideos adaptadorLV = AdaptadorListaVideos.getUnicaInstancia();
		for (ListaVideos LV : usuario.getListas()) {
			adaptadorLV.borrarListaVideos(LV);
		}
		
		// Eliminamos también su lista de vídeos recientes
		adaptadorLV.borrarListaVideos(usuario.getListaRecientes());
		
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		servPersistencia.borrarEntidad(eUsuario);
	}

	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

		String premium = String.valueOf(usuario.isPremium());
		String listas = obtenerCodigosListasVideos(usuario.getListas());
		String listaRecientes = String.valueOf(usuario.getListaRecientes().getCodigo());
		
		actualizarPropiedadEntidad(eUsuario, propLogin, usuario.getLogin());
		actualizarPropiedadEntidad(eUsuario, propPassword, usuario.getPassword());		
		actualizarPropiedadEntidad(eUsuario, propNombre, usuario.getNombre());		
		actualizarPropiedadEntidad(eUsuario, propApellidos, usuario.getApellidos());		
		actualizarPropiedadEntidad(eUsuario, propFecNac, localDateToString(usuario.getFechaNac()));		
		actualizarPropiedadEntidad(eUsuario, propEmail, usuario.getEmail());
		actualizarPropiedadEntidad(eUsuario, propPremium, premium);
		actualizarPropiedadEntidad(eUsuario, propListas, listas);		
		actualizarPropiedadEntidad(eUsuario, propRecientes, listaRecientes);
	}

	public Usuario recuperarUsuario(int codigo) {
		// Si la entidad está en el pool se devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// Si no, se recupera de la base de datos
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);

		// Recuperar propiedades que no son objetos
		String login = servPersistencia.recuperarPropiedadEntidad(eUsuario, propLogin);
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, propPassword);		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, propNombre);		
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, propApellidos);		
		LocalDate fechaNac = stringToLocalDate(servPersistencia.recuperarPropiedadEntidad(eUsuario, propFecNac));	
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, propEmail);
		
		Usuario usuario = new Usuario(login, password, nombre, apellidos, fechaNac, email);
		usuario.setCodigo(codigo);
		
		// Configuramos si es premium
		usuario.setPremium(Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, propPremium)));
		

		// IMPORTANTE: añadir el usuario al pool antes de llamar a otros adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		// Recuperar propiedades que son objetos llamando a adaptadores
		List<ListaVideos> listas = obtenerListasVideosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, propListas));
		for (ListaVideos l : listas)
			usuario.addListaVideos(l);
		
		AdaptadorListaVideos adaptadorLV = AdaptadorListaVideos.getUnicaInstancia();
		int codigoLR = Integer.valueOf(servPersistencia.recuperarPropiedadEntidad(eUsuario, propRecientes));
		usuario.setListaRecientes(adaptadorLV.recuperarListaVideos(codigoLR));
		
		return usuario;
	}

	public List<Usuario> recuperarTodosUsuarios() {
		List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
		List<Usuario> usuarios = new LinkedList<Usuario>();

		for (Entidad eUsuario : eUsuarios) {
			usuarios.add(recuperarUsuario(eUsuario.getId()));
		}
		return usuarios;
	}

	// Funcionalidad auxiliar
	private String obtenerCodigosListasVideos(List<ListaVideos> listasVideos) {
		String aux = "";
		for (ListaVideos lv : listasVideos) {
			aux += lv.getCodigo() + " ";
		}
		return aux.trim();
	}
	
	private List<ListaVideos> obtenerListasVideosDesdeCodigos(String listas) {
		List<ListaVideos> listasVideos = new LinkedList<ListaVideos>();
		StringTokenizer strTok = new StringTokenizer(listas, " ");
		AdaptadorListaVideos adaptadorLV = AdaptadorListaVideos.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listasVideos.add(adaptadorLV.recuperarListaVideos(Integer.valueOf((String) strTok.nextElement())));
		}
		return listasVideos;
	}
	
	private LocalDate stringToLocalDate(String fecha) {
		return LocalDate.parse(fecha, formatoFecha);
	}
	
	private String localDateToString(LocalDate fecha) {
		return fecha.format(formatoFecha);
	}
	
	private void actualizarPropiedadEntidad(Entidad entidad, String propiedad, String nuevoValor) {
		servPersistencia.eliminarPropiedadEntidad(entidad, propiedad);
		servPersistencia.anadirPropiedadEntidad(entidad, propiedad, nuevoValor);
	}
	
}

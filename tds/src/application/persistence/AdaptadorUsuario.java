package application.persistence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import application.model.ListaVideos;
import application.model.RolPremium;
import application.model.Usuario;
import beans.*;
import tds.driver.*;

public class AdaptadorUsuario implements IAdaptadorUsuarioDAO {
	// CONSTANTE
	private final static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuario unicaInstancia = null;

	public static AdaptadorUsuario getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorUsuario();
		else
			return unicaInstancia;
	}

	private AdaptadorUsuario() { 
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia(); 
	}

	/* cuando se registra un usuario se le asigna un identificador único */
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario;
		boolean existe = true; 
		
		// Si la entidad está registrada no la registra de nuevo
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;

		// registrar primero los atributos que son objetos
		// TODO ¿Esto es necesario hacerlo? Por que el usuario se creo sin una lista
		/*
		AdaptadorVentaTDS adaptadorVenta = AdaptadorVentaTDS.getUnicaInstancia();
		for (Venta v : usuario.getVentas())
			adaptadorVenta.registrarVenta(v);
		*/

		// crear entidad Usuario
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(new Propiedad("login", usuario.getLogin()), new Propiedad("password", usuario.getPassword()),
						new Propiedad("nombre", usuario.getNombre()), new Propiedad("apellidos", usuario.getApellidos()),
						new Propiedad("fecha nacimiento", localDateToString(usuario.getFechaNac())),
						new Propiedad("email", usuario.getEmail()), new Propiedad("premium", null),
						new Propiedad("listas", ""), new Propiedad("lista recientes", "")
						)));
		
		// registrar entidad usuario
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		// asignar identificador unico
		// Se aprovecha el que genera el servicio de persistencia
		usuario.setCodigo(eUsuario.getId()); 
	}

	public void borrarUsuario(Usuario usuario) {
		// No se comprueban restricciones de integridad con las listas
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		servPersistencia.borrarEntidad(eUsuario);
	}

	public void modificarUsuario(Usuario usuario) {

		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

		actualizarPropiedadEntidad(eUsuario, "login", usuario.getLogin());
		actualizarPropiedadEntidad(eUsuario, "password", usuario.getPassword());		
		actualizarPropiedadEntidad(eUsuario, "nombre", usuario.getNombre());		
		actualizarPropiedadEntidad(eUsuario, "apellidos", usuario.getApellidos());		
		actualizarPropiedadEntidad(eUsuario, "fecha nacimiento", localDateToString(usuario.getFechaNac()));		
		actualizarPropiedadEntidad(eUsuario, "email", usuario.getEmail());
		
		String premium = getCodigoSiPremium(usuario);
		actualizarPropiedadEntidad(eUsuario, "premium", premium);

		String listas = obtenerCodigosListasVideos(usuario.getListas());
		actualizarPropiedadEntidad(eUsuario, "listas", listas);
		
		String listaRecientes = obtenerCodigosListasVideos(usuario.getListaRecientes());
		actualizarPropiedadEntidad(eUsuario, "lista recientes", listaRecientes);
	}

	public Usuario recuperarUsuario(int codigo) {

		// Si la entidad está en el pool la devuelve directamente
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);

		// si no, la recupera de la base de datos
		Entidad eUsuario;
		String login;
		String password;
		String nombre;
		String apellidos;
		LocalDate fechaNac;
		String email;
		RolPremium premium;
		List<ListaVideos> listas = new LinkedList<ListaVideos>();
		List<ListaVideos> listaRecientes = new LinkedList<ListaVideos>();

		// recuperar entidad
		eUsuario = servPersistencia.recuperarEntidad(codigo);

		// recuperar propiedades para construir el usuario
		login = servPersistencia.recuperarPropiedadEntidad(eUsuario, "login");
		password = servPersistencia.recuperarPropiedadEntidad(eUsuario, "password");		
		nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");		
		apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");		
		fechaNac = stringToLocalDate(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha nacimiento"));	
		email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");

		Usuario usuario = new Usuario(login, password, nombre, apellidos, fechaNac, email);
		usuario.setCodigo(codigo);

		// IMPORTANTE:añadir el usuario al pool antes de llamar a otros
		// adaptadores
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		// recuperar propiedades que son objetos llamando a adaptadores
		// premium 
		premium = getRolPremium(servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium"));
		usuario.setPremium(premium);
		
		// listas
		listas = obtenerListasVideosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "listas"));
		for (ListaVideos l : listas)
			usuario.addListaVideos(l);
		
		// lista de recientes
		listaRecientes = obtenerListasVideosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "lista recientes"));
		for (ListaVideos l : listaRecientes)
			usuario.addListaVideos(l);

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

	// -------------------Funciones auxiliares-----------------------------
	
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
	
	private String getCodigoSiPremium(Usuario usuario) {
		String premium = null;
		if (usuario.getPremium() != null) {
			premium = "";
			premium += usuario.getPremium().getCodigo();
		}
		return premium;
	}
	
	private RolPremium getRolPremium(String premium) {
		// si no es premium (es null), devolvemos null
		if (premium == null) return null;
		
		// Si no, buscamos el RolPremium en el servidor de persistencia y lo devolvemos
		int codigoPremium = Integer.parseInt(premium);
		return recuperarRolPremium(codigoPremium);
	
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

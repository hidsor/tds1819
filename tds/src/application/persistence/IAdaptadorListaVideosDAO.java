package application.persistence;

import java.util.List;

import application.model.ListaVideos;

public interface IAdaptadorListaVideosDAO {

	public void registrarListaVideos(ListaVideos listaVideos);
	public void borrarListaVideos(ListaVideos listaVideos);
	public void modificarListaVideos(ListaVideos listaVideos);
	public ListaVideos recuperarListaVideos(int codigo);
	public List<ListaVideos> recuperarTodasListasVideos();
}

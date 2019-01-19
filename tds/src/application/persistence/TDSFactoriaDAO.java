package application.persistence;

public class TDSFactoriaDAO extends FactoriaDAO {
	public TDSFactoriaDAO() {
	}

	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuario.getUnicaInstancia();
	}

	@Override
	public IAdaptadorListaVideosDAO getListaVideosDAO() {
		return AdaptadorListaVideos.getUnicaInstancia();
	}

	@Override
	public IAdaptadorVideoDAO getVideoDAO() {
		return AdaptadorVideo.getUnicaInstancia();
	}

}

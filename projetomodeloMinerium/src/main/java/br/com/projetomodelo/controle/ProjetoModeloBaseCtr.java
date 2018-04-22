package br.com.projetomodelo.controle;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import br.com.projetomodelo.entidades.ProjetoModeloBaseVO;
import br.com.projetomodelo.entidades.aluno.UsuarioVO;
import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodigio.controle.ProCtr;

public class ProjetoModeloBaseCtr<ENTITY extends ProjetoModeloBaseVO> extends ProCtr<ProjetoModeloBaseVO> {

	@Override
	protected void configuraUnidadeLogada(Component comp) {
		//sobrescrito para evitar que seja obrigatório a existencia de uma unidade na sessão		
//		super.configuraUnidadeLogada(comp);
	}
	
	@Override
	public void doAfterCompose(Component tela) throws Exception {
		try {
			//Verifica se o menu está sendo montado corretamente
			tela.getPage().getFellow("principal");
		} catch (Exception e) {
			tela.setVisible(false);
			Executions.sendRedirect("/");
		}
		
		UsuarioVO usuario = (UsuarioVO)getTela().getPage().getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
		if (usuario!=null && usuario.getDateSessionValid()!=null && usuario.getDateSessionValid().after(new Date())) {
			//Adiciona 30 minutos na sessão do usuário
			Calendar agora = new GregorianCalendar();
			agora.add(Calendar.MINUTE, 30);
			usuario.setDateSessionValid(agora.getTime());
		} else {
			tela.setVisible(false);
			Executions.getCurrent().setVoided(true); //Para o carregamento da pagina
			Executions.sendRedirect("/");
		}
		super.doAfterCompose(tela);
	}
	
	//Lógica para validar se a funcionalidade (papel) pode ser visualizada pelo usuário
	public Boolean possuiAutorizacao (String papel) {
		UsuarioVO usuario = (UsuarioVO)getTela().getPage().getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
		if (usuario!=null && usuario.getTipoConta()!=null) {
			return usuario.getTipoConta().possuiAutorizacao(papel);
		} else {
			return false;
		}
	}
	
	public void abrirTelaLogin() {
		Window window = (Window)Executions.createComponents("/auth/loginWindow.zul", null, null);
        window.doModal();
	}
}

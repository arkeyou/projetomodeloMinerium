package br.com.projetomodelo.controle.auth;

import org.zkoss.zk.ui.Component;

import br.com.projetomodelo.entidades.aluno.UsuarioVO;
import br.gov.prodemge.ssc.comuns.constantes.Constantes;

public class SegurancaCtr {
	
	//Lógica para validar se a funcionalidade (papel) pode ser visualizada pelo usuário
		public Boolean possuiAutorizacao(String papel, Component comp) {
			UsuarioVO usuario = (UsuarioVO)comp.getPage().getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
			if (usuario!=null && usuario.getTipoConta()!=null) {
				return usuario.getTipoConta().possuiAutorizacao(papel);
			} else {
				return false;
			}
		}
	
}
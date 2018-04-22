package br.com.projetomodelo.controle.auth;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodigio.controle.WindowPrincipalUnsecured;

public class LogoutCtr extends WindowPrincipalUnsecured {
	private static final long serialVersionUID = 1L;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		getTela().setAttribute(Constantes.USUARIO_AUTENTICADO, null, Component.SESSION_SCOPE);
		getTela().setAttribute(Constantes.UNIDADE_AUTENTICADA, null, Component.SESSION_SCOPE);
		Sessions.getCurrent().setAttribute(Constantes.USUARIO_AUTENTICADO, null);
		Executions.sendRedirect("/");
		Sessions.getCurrent().invalidate();
//		HttpSession s = (HttpSession) Sessions.getCurrent().getNativeSession();
//		s.invalidate();
		
	}

}
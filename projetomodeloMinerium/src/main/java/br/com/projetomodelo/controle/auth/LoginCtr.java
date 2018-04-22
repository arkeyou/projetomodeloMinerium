package br.com.projetomodelo.controle.auth;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import br.com.projetomodelo.entidades.aluno.UsuarioVO;
import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.controle.WindowPrincipalUnsecured;
import br.gov.prodigio.controle.componente.ProAnnotateDataBinder;

public class LoginCtr extends WindowPrincipalUnsecured {
	private static final long serialVersionUID = 1L;

//	@Override
//	public void doAfterCompose(Component comp) throws Exception {
//		super.doAfterCompose(comp);
//		//Se o usu√°rio est√° logado, n√£o exibe a tela de login
//		UsuarioVO usuario = (UsuarioVO)getTela().getPage().getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
//		if (usuario!=null && usuario.getDateSessionValid()!=null && usuario.getDateSessionValid().after(new Date())) {
//			configuraTelaUsuarioLogado(getTela(), usuario);
//		} 
//	}
	
	public void onOK() throws Exception {
//		this.doLogin();
    }
	
	public void doLogin() throws Exception {
		AbstractComponent tela = (AbstractComponent)getTela();
		Label mensagem = (Label)tela.getFellow("message");
		mensagem.setValue("...");
		new ProAnnotateDataBinder(tela).loadComponent(mensagem);
		
		Textbox login = (Textbox)tela.getFellow("username");
		Textbox senha = (Textbox)tela.getFellow("password");

		IProFacade fachadaDeNegocio = (IProFacade) tela.getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO, Component.APPLICATION_SCOPE);
		UsuarioVO usuarioParam = new UsuarioVO();
		usuarioParam.setEmail(login.getValue());
		
		try {
			UsuarioVO usuario = fachadaDeNegocio.recuperaPorChaveNatural(usuarioParam, "email");
			if (usuario!=null && usuario.getSenha().equals(senha.getValue())) {
				mensagem.setValue("Bem-vindo "+usuario.getNome()+"!");

				//Adiciona 30 minutos na sess√£o do usu√°rio
				Calendar agora = new GregorianCalendar();
				agora.add(Calendar.MINUTE, 30);
				usuario.setDateSessionValid(agora.getTime());
				
				configuraUsuarioUnidadeLogada();
				tela.setAttribute(Constantes.USUARIO_AUTENTICADO, usuario, Component.SESSION_SCOPE);

//				configuraTelaUsuarioLogado(tela, usuario);
//				setTela(janelaMenu);
//				incluiComponenteNaPagina();
				Executions.sendRedirect("/");
				return;
			} else {
				mensagem.setValue("Usu·rio ou senha inv·lido!");
			}
			new ProAnnotateDataBinder(tela).loadComponent(mensagem);
		} catch (Exception e) {
			throw e;
		}
	}

	public void doLoginAuthenticate() throws Exception {
		
		AbstractComponent tela = (AbstractComponent)getTela();
		Textbox login = (Textbox)tela.getFellow("j_username");
		Textbox senha = (Textbox)tela.getFellow("j_password");
		try {
			//Preenche o login do usu·rio para ser recuperado na classe UserDetailsService
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					 new MyUser(login.getValue(), "", new String[]{}), senha.getValue());
//	        token.setDetails(new WebAuthenticationDetails(request));
            DaoAuthenticationProvider authenticator = new DaoAuthenticationProvider();
            authenticator.setUserDetailsService(new MyUserDetailsService());
//            authenticator.setPasswordEncoder(new Md5PasswordEncoder());
            Authentication authentication = authenticator.authenticate(token);
			
            //Se passou deste ponto, a autenticaÁ„o 
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
    		IProFacade fachadaDeNegocio = (IProFacade) tela.getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO, Component.APPLICATION_SCOPE);
    		UsuarioVO usuarioParam = new UsuarioVO();
    		usuarioParam.setEmail(login.getValue());
    		UsuarioVO dadosUsuario = fachadaDeNegocio.recuperaPorChaveNatural(usuarioParam, "email");
    		
			//Adiciona 30 minutos na sess„o do usu·rio
			Calendar agora = new GregorianCalendar();
			agora.add(Calendar.MINUTE, 30);
			dadosUsuario.setDateSessionValid(agora.getTime());
			
			configuraUsuarioUnidadeLogada();
			tela.setAttribute(Constantes.USUARIO_AUTENTICADO, dadosUsuario, Component.SESSION_SCOPE);

//			configuraTelaUsuarioLogado(tela, usuario);
//			setTela(janelaMenu);
//			incluiComponenteNaPagina();
			Executions.sendRedirect("/");
			return;
		} catch (Exception e) {
			throw e;
		}
	}

//	private void configuraTelaUsuarioLogado(AbstractComponent tela, UsuarioVO usuario) {
//		tela.setVisible(false);
//		Window janelaMenu = (Window)tela.getPage().getFellow("index");
//		Label bemVindoMsg = (Label)janelaMenu.getFellow("bemVindoMsg");
//		bemVindoMsg.setValue("Bem vindo "+usuario.getNome()+" ("+usuario.getTipoConta()+")");
//		janelaMenu.setVisible(true);
//	}
//	
//	protected void incluiComponenteNaPagina() {
//		AbstractComponent tela = getTela();
//		Component loginWindow = tela;
//
//		String urlcasodeuso = "introducao.zul";
//		// redireciona o meio da pagina
//		Page page = null;
//		page = loginWindow.getPage();
////		if (loginWindow != null) {
////			loginWindow.detach();
////		}
////		loginWindow.getChildren().clear();
//
//		Map attributes = tela.getAttributes(Component.SESSION_SCOPE);
//		attributes.put("casodeusoatual", urlcasodeuso);
//
//		Component[] component = Executions.createComponents(urlcasodeuso,
//				page, null, attributes);
////		page.s;
//	}
//	
//	protected void exibeTelaDeIntroducao() {
//		AbstractComponent tela = getTela();
//
//		Component includeIntroducao = tela.getFellow("introducao");
//		String urlcasodeuso = "introducao.zul";
//		// redireciona o meio da pagina
//		Component component = null;
//		component = includeIntroducao.getFirstChild();
//		if (component != null) {
//			component.detach();
//		}
//		includeIntroducao.getChildren().clear();
//
//		Map attributes = tela.getAttributes(Component.SESSION_SCOPE);
//		attributes.put("casodeusoatual", urlcasodeuso);
//
//		component = Executions.createComponents(urlcasodeuso,
//				includeIntroducao, attributes);
//		includeIntroducao.appendChild(component);
//	}

}
package br.com.projetomodelo.controle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.LabelElement;

import br.com.projetomodelo.controle.auth.ApplicationContextProvider;
import br.gov.prodemge.ssc.comuns.constantes.Constantes;
import br.gov.prodemge.ssc.interfaces.IPapel;
import br.gov.prodemge.ssc.interfaces.IPermissao;
import br.gov.prodemge.ssc.interfaces.IPermissaoEspecie;
import br.gov.prodemge.ssc.interfaces.IRecurso;
import br.gov.prodemge.ssc.interfaces.IRecursoOperacao;
import br.gov.prodemge.ssc.interfaces.IUnidade;
import br.gov.prodemge.ssc.interfaces.IUnidadeEspecie;
import br.gov.prodemge.ssc.interfaces.IUsuario;
import br.gov.prodemge.ssc.interfaces.IUsuarioUnidade;
import br.gov.prodemge.ssc.interfaces.IUsuarioUnidadePapel;
import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.controle.WindowIntroducao;
import br.gov.prodigio.visao.helper.ProHelperView;
import br.gov.prodigio.visao.helper.ProMessageHelper;

public class WindowIntroducaoModelo extends WindowIntroducao {

	private static final long serialVersionUID = -5908637925748107094L;
	private static final Logger log = LoggerFactory.getLogger(WindowIntroducaoModelo.class);

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		//Controla o evento avan�ar e voltar do browser history
		desktop.setBookmark("/");
		page.addEventListener(Events.ON_BOOKMARK_CHANGE, e -> { 
		               	 if (getTela().getDesktop().getBookmark()!=null && !getTela().getDesktop().getBookmark().equals("/")) {
		               		validateSpringPermission(new String(Base64.decodeBase64(getTela().getDesktop().getBookmark())));
		                	Map attributes = getTela().getAttributes(Component.SESSION_SCOPE);
		             		ProHelperView.insereNovoConteudoNoCentroDaJanela(new String(Base64.decodeBase64(getTela().getDesktop().getBookmark())), getTela(), attributes);
		            	 } else {
		            		 Executions.sendRedirect("/");
		            	 }
		         	});
	}
	
	@Deprecated
	public void abrir(Menuitem itemDeMenuClicado) {
//		page.getDesktop().getExecution();
//		page.getDesktop().getSession();
//		page.getDesktop().getWebApp().getNativeContext();
//		page.getDesktop().getFirstPage().getRequestPath().equals("/index.zul");
		validateSpringPermission("/"+(String) itemDeMenuClicado.getAttribute("urlcasodeuso"));
		abrir((AbstractComponent)itemDeMenuClicado);
		
		//Browser history
		desktop.setBookmark(Base64.encodeBase64String((("/"+(String) itemDeMenuClicado.getAttribute("urlcasodeuso")).getBytes())));
		abrir((AbstractComponent)itemDeMenuClicado);
	}
	
	private void validateSpringPermission(String url){
	    FilterSecurityInterceptor fsi = ApplicationContextProvider.getApplicationContext().getBean(org.springframework.security.web.access.intercept.FilterSecurityInterceptor.class);
	    FilterInvocationSecurityMetadataSource sms = fsi.getSecurityMetadataSource();

	    Iterator it = ApplicationContextProvider.getApplicationContext().getBeansOfType(WebInvocationPrivilegeEvaluator.class).values().iterator();
	    
	    while (it.hasNext()) {
	    	WebInvocationPrivilegeEvaluator inv = (WebInvocationPrivilegeEvaluator)it.next();	    	
	        if (!inv.isAllowed(page.getDesktop().getFirstPage().getRequestPath(),url, null,SecurityContextHolder.getContext().getAuthentication())) {
	        	throw new AccessDeniedException("Acesso negado");
	        }
	    }
	}

	protected void insereItemDeMenu(AbstractComponent menu) {
		// TODO alterar processo de montagem do menu
		ProMessageHelper controllerMessagesHelper = ProMessageHelper.getInstance();
		IProFacade fachadaDeNegocio = (IProFacade) getTela().getAttribute(ContextParameters.INTERFACE_DE_NEGOCIO, Component.APPLICATION_SCOPE);

		try{

			IUsuario usuario = (IUsuario) getTela().getAttribute(Constantes.USUARIO_AUTENTICADO, Component.SESSION_SCOPE);
			IUnidade unidade = (IUnidade) getTela().getAttribute(Constantes.UNIDADE_AUTENTICADA, Component.SESSION_SCOPE);
			// Se n�o selecionou a unidade nao monta o menu
			if(unidade == null){
				return;
			}

			List<LabelElement> listaMenuInseridos = new ArrayList<LabelElement>();
			Set<IUsuarioUnidade> acessosUsuarioUnidade = usuario.getListaUsuarioUnidade();
			List<String> itensMenu = new ArrayList<String>();
			for(IUsuarioUnidade acesso : acessosUsuarioUnidade){				
				// S� deve filtrar pela unidade selecionada
				if(acesso.getUnidade().getId().longValue() != unidade.getId().longValue()){
					continue;
				}
				
				Set<IUsuarioUnidadePapel> usuarioUnidadePapel = acesso.getUsuarioUnidadePapel();

				for(IUsuarioUnidadePapel uup : usuarioUnidadePapel){					

					IPapel papel = uup.getUnidadePapel().getPapel();
					IPapel papelPai = null;
					
					Set<IPermissao> listaPermissoes = new HashSet<IPermissao>();					
					if (papel != null) {
						listaPermissoes = papel.getListaPermissoes();
						papelPai = uup.getUnidadePapel().getPapel().getPapelPai();
					}
					
					Set<IPermissao> listaPermissoesPai = new HashSet<IPermissao>();					
					if (papelPai != null) {
						listaPermissoesPai = papelPai.getListaPermissoes();
					}				
										
					Set<IPermissao> permissoes = new HashSet<IPermissao>();
					
					if (listaPermissoes != null && !listaPermissoes.isEmpty()) {					
						permissoes.addAll(listaPermissoes);
					}
					
					if (listaPermissoesPai != null && !listaPermissoesPai.isEmpty()) {
						permissoes.addAll(listaPermissoesPai);
					}
					
					if(permissoes.isEmpty())
						continue;
					for(IPermissao permissao : permissoes){
						IRecursoOperacao recursoOperacao = permissao.getRecursoOperacao();
						LabelElement menuPai = null;
//						if(menu instanceof Nav){
//							menuPai =  (LabelElement) menu;
//						}
//						else{
							menuPai =  (LabelElement) menu.getParent();
//						}
						IRecurso recurso = recursoOperacao.getRecurso();
						boolean possuiFuncao = true;
						// System.out.print(menu.getLabel() + "  => ");
						// System.out.println(recurso.getChaveMenu());
						if(recurso.getChaveMenu() != null && recurso.getChaveMenu().equalsIgnoreCase(menuPai.getLabel())){
							List<IPermissaoEspecie> listaPermissaoEspecie = recursoOperacao.getListaPermissaoEspecie();
							// Caso exista Especie deve verificar se a especie �
							// da Unidade do Usuario
							if(listaPermissaoEspecie != null && !listaPermissaoEspecie.isEmpty()){
								possuiFuncao = false;
								forEspecie: for(IPermissaoEspecie iPermissaoEspecie : listaPermissaoEspecie){
									Set<IUnidadeEspecie> listaEspecieUnidade = iPermissaoEspecie.getEspecie().getUnidadeEspecie();
									for(IUnidadeEspecie especieUnidade : listaEspecieUnidade){
										IUnidade unidadeEspecie = especieUnidade.getUnidade();
										if(acesso.getUnidade().getId().longValue() == unidadeEspecie.getId().longValue()){
											possuiFuncao = true;
											break forEspecie;
										}
									}
								}

							}

							String itemMenuString = recurso.getChaveMenu() + recurso.getUrl();
							boolean itemJaInserido = itensMenu.contains(itemMenuString);
							// Se a especie nao liberar nao exibe o menu
							// Nao insere item de menu repetido
							if(!possuiFuncao || itemJaInserido){
								continue;
							}
							menuPai.setVisible(true);
							LabelElement itemMenu = null;

//							if(menu instanceof Nav){
//								itemMenu = new Navitem();
//							}else{
								itemMenu = new Menuitem();
//							}
							
							itemMenu.setAttribute("urlcasodeuso", recurso.getUrl());
							itemMenu.addEventListener("onClick", evento());
							itemMenu.setLabel(recurso.getNome());
							itensMenu.add(itemMenuString);
							itemMenu.setParent(menu);
							// menupopup.appendChild(itemMenu);
							// Somente obtem a lista dos menus a serem inseridos
							listaMenuInseridos.add(itemMenu);
						}
					}
				}
			}

			ordenaMenu(listaMenuInseridos);
			// Insere os itens de menu ordenados
			for(LabelElement itemMenu : listaMenuInseridos){
				menu.appendChild(itemMenu);
			}

		}catch(Exception e){
			log.error("Erro ao inserir item de menu ", e);
			controllerMessagesHelper.emiteMensagemErro(e.getMessage());
		}
	}
	
	@Secured({"isAnonymous()"})
	public void abrirTelaLogin() {
		Window window = (Window)Executions.createComponents("/auth/loginModal.zul", null, null);
        window.doModal();
	}
	
	public void montaGrafico() throws Exception {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("C/C++", new Double(17.5));
		pieDataset.setValue("PHP", new Double(32.5));
		pieDataset.setValue("Java", new Double(43.2));
		pieDataset.setValue("Visual Basic", new Double(10));
		
		JFreeChart chart = ChartFactory.createPieChart3D("Sample Pie Chart 3D", pieDataset,true,true,true);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setForegroundAlpha(0.5f);
		BufferedImage bi = chart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT , null);
		byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
		
		Component meio = getTela().getFellow("meio");
		AImage image = new AImage("Pie Chart", bytes);
		Image myimage = new Image();
		myimage.setContent(image);
		
		meio.appendChild(myimage);
	}

	
	@Override
	public void logout() {
		ProMessageHelper controllerMessagesHelper = ProMessageHelper.getInstance();
		//Com o ZK EE a linha abaixo interrompe a execu��o da thread com um messagebox
//		if(controllerMessagesHelper.desejaRealmente("Deseja realmente sair?")){
//			Executions.sendRedirect("/timeout.zul");
//		}
		EventListener<Event> ev = new EventListener<Event>(){
            public void onEvent(Event e){
                switch (e.getName()) {
                case Messagebox.ON_OK: //OK is clicked
                	Executions.sendRedirect("/logout.zul");
                	break;
                case Messagebox.ON_CANCEL: //Cancel is clicked
                	break;
                default: //if the Close button is clicked, e.getButton() returns null
                }
            }
		};
		Messagebox.show("Deseja realmente sair?", "Aten��o!", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, ev);
				
	}
}

/**
 * 
 */
package br.com.projetomodelo.controle.auth;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.projetomodelo.entidades.aluno.UsuarioVO;
import br.gov.prodigio.comuns.IProFacade;
import br.gov.prodigio.comuns.utils.ProConfiguracao;
import br.gov.prodigio.persistencia.ProDAOHelper;


/**
 * A custom UserDetailsService implementation to adapt to extended User class.<br>
 * 
 * If you have your own user credential model and don't want to change it even a bit, 
 * then instead of swapping the UserDetailService of default AuthenticationProvider, 
 * you have to swap the AuthenticationProvider itself, and you'll have to deal with 
 * password hashing and salting by your own.<br>
 * 
 * 
 * @author Ian YT Tsai (zanyking)
 *
 */

@Service
public class MyUserDetailsService implements UserDetailsService {

	private static final Map<String, MyUser> USERS = new HashMap<String,MyUser>();
	private static void add(MyUser mu){
		USERS.put(mu.getUsername(), mu);
	}
	
	static{
		
		add(new MyUser("rod","81dc9bdb52d04dc20036dbd8313ed055", //password:1234 
			new String[]{"ROLE_USER", "ROLE_EDITOR"} ));
		
		add(new MyUser("dianne","81dc9bdb52d04dc20036dbd8313ed055", 
			new String[]{"ROLE_USER", "ROLE_EDITOR"} ));
		
		add(new MyUser("scott","81dc9bdb52d04dc20036dbd8313ed055", 
			new String[]{"ROLE_USER"} ));
		
		add(new MyUser("peter","81dc9bdb52d04dc20036dbd8313ed055", 
			new String[]{"ROLE_USER"} ));
	}
	
	// must return a value or throw UsernameNotFoundException
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			IProFacade fachadaDeNegocio = recuperaInterfacePersistencia();
			UsuarioVO usuarioParam = new UsuarioVO();
			usuarioParam.setEmail(username);
			UsuarioVO usuario = null;
			usuario = fachadaDeNegocio.recuperaPorChaveNatural(usuarioParam, "email");
		
			if(usuario==null){
				System.out.println(">>> cannot find user: "+username);
				throw new UsernameNotFoundException("cannot found user: "+username);
			}
			//Return a clone object to avoid a user's credentials data being erased after success authentication.
			//This behavior only happens from Spring Security 3.1 onwards.
			//please refer to http://static.springsource.org/spring-security/site/docs/3.1.x/reference/core-services.html#core-services-erasing-credentials
			return new MyUser(usuario.getEmail(), usuario.getSenha(), usuario.getTipoConta().getPapeis());
		} catch (Exception e) {
			throw new UsernameNotFoundException("cannot found user: "+e.getMessage());
		}
	}


	private IProFacade recuperaInterfacePersistencia() throws Exception {
		ProConfiguracao config = new ProConfiguracao();
		//String lookupJndiNameRemote2 = config.getPropriedade("projetomodeloMineriu.ejbFacadeJndiLookupRemote2");

		String loader = ProDAOHelper.class.getClassLoader().toString();
		String nomeCompletoDaAplicacao = loader.split("deployment.")[1];
		String nomePrincipal = nomeCompletoDaAplicacao.contains(".ear:main") ? nomeCompletoDaAplicacao.split(".ear:main")[0] : nomeCompletoDaAplicacao.split(".war:main")[0];
		
		String lookupJndiNameRemote2 = config.getPropriedade(nomePrincipal.substring(0, nomePrincipal.length()-1)+".ejbFacadeJndiLookupRemote2");

		Context context = new InitialContext();

		Hashtable props = new Hashtable();
		props.put("jboss.naming.client.ejb.context", true);
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		context = new InitialContext(props);


		return 	(IProFacade) context.lookup(lookupJndiNameRemote2);
	}
}

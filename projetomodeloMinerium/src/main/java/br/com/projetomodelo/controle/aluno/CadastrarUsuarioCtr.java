package br.com.projetomodelo.controle.aluno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;

import br.com.projetomodelo.controle.ProjetoModeloBaseCtr;
import br.com.projetomodelo.entidades.aluno.UsuarioVO;
import br.com.projetomodelo.enums.TipoContaEnum;
import br.gov.prodigio.comum.ContextParameters;
import br.gov.prodigio.controle.componente.Bandboxbind;

public class CadastrarUsuarioCtr extends ProjetoModeloBaseCtr<UsuarioVO> {

	@Override
	public void doAfterCompose(Component tela) throws Exception{
		super.doAfterCompose(tela);
		pesquisar();
	}
	//Necessário para bandboxbind
	public List<TipoContaEnum> listarTipoContaEnum(Bandboxbind bandbox) {
		TipoContaEnum[] values = TipoContaEnum.values();
		ArrayList<TipoContaEnum> arrayList = new ArrayList<TipoContaEnum>(Arrays.asList(values));
		return arrayList;
	}

	@Override
	protected void configuraTelaDeEntrada(Component comp) throws Exception {
		super.configuraTelaDeEntrada(comp);
		//Necessário para campo radiogroupbind
		getWindowAtual().setAttribute(ContextParameters.PREFIX_ENUM+TipoContaEnum.class.getSimpleName(), new ArrayList<TipoContaEnum>(Arrays.asList(TipoContaEnum.values())), getWindowAtual().APPLICATION_SCOPE);

	}
	
}

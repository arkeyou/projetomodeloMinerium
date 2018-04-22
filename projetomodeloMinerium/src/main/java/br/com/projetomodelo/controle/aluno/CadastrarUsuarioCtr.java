package br.com.projetomodelo.controle.aluno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;

import br.com.projetomodelo.controle.ProjetoModeloBaseCtr;
import br.com.projetomodelo.entidades.aluno.UsuarioVO;
import br.com.projetomodelo.enums.TipoContaEnum;

public class CadastrarUsuarioCtr extends ProjetoModeloBaseCtr<UsuarioVO> {

	@Override
	public void doAfterCompose(Component tela) throws Exception{
		super.doAfterCompose(tela);
		pesquisar();
	}
	
	public List<TipoContaEnum> getListaTipoContaEnum() {
		TipoContaEnum[] values = TipoContaEnum.values();
		ArrayList<TipoContaEnum> arrayList = new ArrayList<TipoContaEnum>(Arrays.asList(values));
		return arrayList;
	}
	
}

package br.com.projetomodelo.entidades.aluno;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.projetomodelo.enums.TipoContaEnum;
import br.gov.prodigio.entidades.ProBaseVO;

@Entity
@Table(name = "tb_assoc_endereco_pessoa")
public class AssocEnderecoPessoaVO extends ProBaseVO {

	private static final long serialVersionUID = 4237843165013293291L;
	private Long id;
	private String endereco;
	private UsuarioVO usuario;
	private TipoContaEnum tipoConta;
	
	@Id
	@SequenceGenerator(name = "sq_assoc_endereco_pessoa", sequenceName = "sq_assoc_endereco_pessoa", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_assoc_endereco_pessoa")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public TipoContaEnum getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoContaEnum tipoConta) {
		this.tipoConta = tipoConta;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public UsuarioVO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

}
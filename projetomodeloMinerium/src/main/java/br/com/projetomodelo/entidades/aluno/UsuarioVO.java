package br.com.projetomodelo.entidades.aluno;

import java.beans.Transient;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.projetomodelo.entidades.ProjetoModeloBaseVO;
import br.com.projetomodelo.enums.TipoContaEnum;
import br.gov.prodemge.ssc.interfaces.base.IUsuarioBase;

@Entity
@Table(name = "tb_usuario")
public class UsuarioVO extends ProjetoModeloBaseVO implements IUsuarioBase {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String email;
	private String senha;
	private String nome;
	private String cpf;
	private String telefone;
	private TipoContaEnum tipoConta;
	private String periodo;
	private Date dateSessionValid = null;
	private Set<AssocEnderecoPessoaVO> enderecos = new LinkedHashSet<AssocEnderecoPessoaVO>();
	
	@Id
	@SequenceGenerator(name = "sq_usuario", sequenceName = "sq_usuario", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_usuario")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "usuario", orphanRemoval = true)
	@OrderBy("id")
	public Set<AssocEnderecoPessoaVO> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(Set<AssocEnderecoPessoaVO> enderecos) {
		this.enderecos = enderecos;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	@Enumerated(EnumType.STRING)
	public TipoContaEnum getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoContaEnum tipoConta) {
		this.tipoConta = tipoConta;
	}

	/*
	 * Auxiliares
	 */
	@Transient
	public String toString(){
		return this.nome;
	}

	@Transient
	public String getLogin() {
		return getEmail();
	}
	
	public void setLogin(String login) {
//		setEmail(login);
	}

	@Transient
	public Date getDateSessionValid() {
		return dateSessionValid;
	}

	public void setDateSessionValid(Date dateSessionValid) {
		this.dateSessionValid = dateSessionValid;
	}
	
	
	
}
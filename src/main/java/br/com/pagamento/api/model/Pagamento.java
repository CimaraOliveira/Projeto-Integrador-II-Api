package br.com.pagamento.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.pagamento.api.enums.Status;
import br.com.pagamento.api.enums.TipoPagamento;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "compras")
public class Pagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idCompra;

	@ManyToOne
	@JoinColumn(name = "id_Cartao")
	private Cartao cartao;

	@ManyToOne
	@JoinColumn(name = "id_Boleto")
	private Boleto boleto;

	private double valor;

	private int quantidade=1;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataCompra;

	private TipoPagamento tipoPagamento;

	private Status status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id")
	// @JsonIgnore
	public User usuario;

	@OneToOne(mappedBy = "compra", fetch = FetchType.LAZY)
	@JsonIgnore
	private LogRegister logRegister;

	private String token;

	private String origin;

	public Pagamento() {
		super();
	}

	public Pagamento(Long idCompra, Cartao cartao, Boleto boleto, double valor, int quantidade, Date dataCompra,
			TipoPagamento tipoPagamento, Status status, User usuario) {
		super();
		this.idCompra = idCompra;
		this.cartao = cartao;
		this.boleto = boleto;
		this.valor = valor;
		this.quantidade = quantidade;
		this.dataCompra = dataCompra;
		this.tipoPagamento = tipoPagamento;
		this.status = status;
		this.usuario = usuario;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Long getId() {
		return idCompra;
	}

	public void setId(Long idCompra) {
		this.idCompra = idCompra;
	}

	public Long getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(Long idCompra) {
		this.idCompra = idCompra;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LogRegister getLogRegister() {
		return logRegister;
	}

	public void setLogRegister(LogRegister logRegister) {
		this.logRegister = logRegister;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

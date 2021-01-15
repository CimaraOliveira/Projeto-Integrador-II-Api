package br.com.pagamento.api.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "boleto")
public class Boleto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idBoleto;
	private String numeroBoleto;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataCompra;
	
	private LocalDate dataVencimento;
	
	public Long getIdBoleto() {
		return idBoleto;
	}
	
	public void setIdBoleto(Long idBoleto) {
		this.idBoleto = idBoleto;
	}
	
	public String getNumeroBoleto() {
		return numeroBoleto;
	}
	
	public void setNumeroBoleto(String numeroBoleto) {
		this.numeroBoleto = numeroBoleto;
	}
	
	public Date getDataCompra() {
		return dataCompra;
	}
	
	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}
	
	public  LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento( LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	@Override
	public String toString() {
		return "Boleto [idBoleto=" + idBoleto + ", numeroBoleto=" + numeroBoleto + ", dataVencimento=" + dataCompra
				+ "]";
	}
	
	
}


package br.com.pagamento.api.model;



import javax.persistence.Column;






import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
public class Role implements GrantedAuthority{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
    private Long id; 
    
    @Column(unique = true)
    private String nome;
    
   	@Override
	public String getAuthority() {
		return nome;
	}

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
}

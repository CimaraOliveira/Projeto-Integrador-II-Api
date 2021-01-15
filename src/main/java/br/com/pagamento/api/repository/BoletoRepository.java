package br.com.pagamento.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.pagamento.api.model.Boleto;
import br.com.pagamento.api.model.Cartao;


public interface BoletoRepository extends JpaRepository<Boleto, Long>{

	@Query(value="select * from compras cs \n" + 
			"inner join boleto b on(cs.id_boleto = b.id_boleto) \n" + 
			"where b.id_boleto = :id_boleto", nativeQuery = true)
	public Cartao idBoleto(@Param("id_boleto")Long id);

}

package br.com.pagamento.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.pagamento.api.model.Pagamento;


public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

	@Query(value = "select * from compras c\n" + "inner join usuario u on(c.usuario_id = u.id)\n"
			+ "where u.id = :id && c.status is not null", nativeQuery = true)
	public List<Pagamento> findAllByIdUser(@Param("id") Long id);

	Pagamento findByIdCompra(Long id);

	@Query(value = "select * from compras c\n" + "inner join boleto b on(c.id_boleto = b.id_boleto) \n"
			+ "where b.id_boleto = :id_boleto", nativeQuery = true)
	public List<Pagamento> findByIdBoleto(@Param("id_boleto") Long id_boleto);

	@Query(value = "select * from compras c\n" + "inner join cartao b on(c.id_cartao = b.id_cartao) \n"
			+ "where b.id_cartao = :id_cartao", nativeQuery = true)
	public List<Pagamento> findByIdCartao(@Param("id_cartao") Long id_cartao);

	@Query(value = "select * from compras c\n" + "inner join usuario u on(c.usuario_id = u.id)\n"
			+ "where u.email = :email", nativeQuery = true)
	public List<Pagamento> findByEmail(@Param("email") String email);
	
	@Query(value = "select * from compras c\n"
			+ "where c.token = :token", nativeQuery = true)
	public Pagamento findByPagamento(@Param("token") String token);

}

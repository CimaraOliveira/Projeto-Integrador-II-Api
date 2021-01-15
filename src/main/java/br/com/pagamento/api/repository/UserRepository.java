package br.com.pagamento.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pagamento.api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByNome(String username);

	@Query(value = "select * from usuario u\n" + 
			"inner join compras c on(u.id = c.usuario_id)\n" + 
			"where c.token = :token ", nativeQuery = true)
	User tokenPagamento(@Param("token") String token);
	
	
	@Query(value = "select * from usuario u\n" + 
			"where u.token = :token && u.id =:id ", nativeQuery = true)
	User tokenId(@Param("token") String token, @Param("id")Long id);

}

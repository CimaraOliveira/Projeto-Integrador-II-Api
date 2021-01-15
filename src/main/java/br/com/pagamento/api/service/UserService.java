package br.com.pagamento.api.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.api.model.Role;
import br.com.pagamento.api.model.User;
import br.com.pagamento.api.repository.UserRepository;



@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional(readOnly = false)
	public void salvar(User usuario) {
		repository.save(usuario);
	}
	
	@Transactional(readOnly = false)
	public void salvarCadastro(User user){
		String crypt = new BCryptPasswordEncoder().encode(user.getSenha());
		user.setSenha(crypt);
		repository.save(user);		
	}
	
	public User findById(Long id) {
		return repository.findById(id).get();  
	}
	
	public User findByEmail(String email) {
		return repository.findByEmail(email);  //aqui
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public List<User> findAll() {
		return repository.findAll();
	}
		
	@Transactional(readOnly = false)
	public void editar(User id) {
     id.setId(id.getId());
     repository.saveAndFlush(id);
     
	}
		
	
	@Transactional(readOnly = false)
	public void excluir(Long id) {
        repository.deleteById(id);
		
	}
	
	@Transactional(readOnly = true)
	public User getEmail(String email) {
		return repository.findByEmail(email);
	}
	
	@Transactional(readOnly = true)
	public User findByNome(String username) {
		return repository.findByNome(username);
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(username);
		UserDetails user = repository.findByEmail(username);
		org.springframework.security.core.userdetails.User userFinal = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getPermissoes(user));
        System.out.println(userFinal.getAuthorities());
       
		return userFinal;
	}

	private Collection<? extends GrantedAuthority> getPermissoes(UserDetails user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		
		Set<Role> permissoes =  ((User) user).getRole();
		for(Role r : permissoes ) {
			 authorities.add(new SimpleGrantedAuthority(r.getNome().toUpperCase()));
		}
		
		return authorities;
	}
	
	@Transactional(readOnly = false)
	public void alterarSenha(User user, String senha) {
		user.setSenha(new BCryptPasswordEncoder().encode(senha));
		repository.save(user);
	}

	public User tokenPagamento(@Param("token") String token) {
		return repository.tokenPagamento(token);
	}
	public User tokenId(@Param("id") Long id,@Param("token") String token) {
		return repository.tokenId(token, id);
		
	}
	
} 
	

package br.com.pagamento.api.json;


import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.pagamento.api.service.EmailService;

import br.com.pagamento.api.jwt.JwtComponent;
import br.com.pagamento.api.model.Email;
import br.com.pagamento.api.model.Role;
import br.com.pagamento.api.model.User;
import br.com.pagamento.api.service.RoleService;
import br.com.pagamento.api.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/usuarios")
@Api(value="API REST Usuarios")
@CrossOrigin()
public class UsuarioJson {
	@Autowired
	private JwtComponent jwtComponent;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService serviceUsuario;
	
	@Autowired
	private UserService service;
	
	@Autowired
	private EmailService sendEmail;

	@Autowired
	private RoleService roleService;
	
	@PostMapping(value = "/login")
	@ApiOperation(value="Usuario faz login")
	public ResponseEntity<?> login(@RequestParam("email") String email,
			@RequestParam("senha") String senha) {
		if (email.trim().isEmpty() && senha.trim().isEmpty()) {
			return ResponseEntity.status(400).build();
		}
		try {
			authenticate(email, senha);
			UserDetails userDB = serviceUsuario.loadUserByUsername(email);
			
			if(userDB != null) { 
				User user2 = serviceUsuario.findByEmail(email);
				user2.setToken(jwtComponent.generateToken(userDB));
				serviceUsuario.salvar(user2);
				return ResponseEntity.ok(user2);
			}
			 
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	/*@PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<User> salvar(@Valid User user) {
		User u = serviceUsuario.getEmail(user.getEmail());
		String senha = user.getSenha();
		user.setSenha(new BCryptPasswordEncoder().encode(senha));
		serviceUsuario.salvar(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}*/

	
	@GetMapping("/findByEmail/{email}")
	@ApiOperation(value="Detalhes do usuário pelo email")
	public ResponseEntity<User> detalhePorId(@PathVariable("email") String email,
			@RequestHeader(value = "Authorization", required = false) String Authorization) {
        System.out.println(Authorization); 
		try {
			System.out.println(email);
			
			boolean isValid = jwtComponent.isTokenExpired(Authorization.substring(7));
			if (!isValid) { 
				User user = serviceUsuario.findByEmail(email);
				if (user != null) {
					return ResponseEntity.ok(user);
				}
				return ResponseEntity.notFound().build();
			}
		} catch (ExpiredJwtException | SignatureException e) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.status(400).build();
	}

	@PutMapping(value = "/updateUser/{id}")
	public ResponseEntity<User> update(
			@RequestParam("nome") String nome,
			@RequestParam("senha") String senha,
 			@RequestParam("email") String email, @PathVariable("id") Long id) {
		User user = serviceUsuario.findById(id);
		if (user != null && (!email.trim().isEmpty() && !nome.trim().isEmpty() && !senha.trim().isEmpty())) {
			user.setEmail(email);
			user.setNome(nome);
			user.setSenha(new BCryptPasswordEncoder().encode(senha));
			serviceUsuario.editar(user);
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.status(400).build();
	}
	
	@PostMapping(value = "/trocarSenha")
	@ApiOperation(value="Enviar senha para email")
	public ResponseEntity <User> trocarSenha(@RequestParam("email") String email) {
			
		User user2 = serviceUsuario.getEmail(email);
		ModelAndView view = new ModelAndView("login");
		if(user2 == null) {
			
				view.addObject("error", "Email não está cadastrado no sistema!");
				return ResponseEntity.status(404).build();
		}else {
			Random r = new Random();
			String novaSenhaGerada = String.valueOf(Math.abs(r.nextInt()));
			System.out.println(novaSenhaGerada);
			//user2.setSenha(novaSenhaGerada);			
			user2.setSenha(new BCryptPasswordEncoder().encode(novaSenhaGerada));			
			serviceUsuario.salvar(user2);
			Email email2 = new Email();
			email2.setTo(user2.getEmail());
			sendEmail.sendNovaSenhaEmail(email2, novaSenhaGerada);
			view.addObject("mensagem", "Nova senha gerada!!!");
			
			
		}
		
		return ResponseEntity.ok(user2);
		
		
	}
	
	@GetMapping("/tokenPagamento/{token}")
	public ResponseEntity<User> tokenPagamento(@PathVariable("token") String token){
		return ResponseEntity.ok(serviceUsuario.tokenPagamento(token));
	}

	@PostMapping(value = "/save")
	@ApiOperation(value="Criando um novo usuário")
	public ResponseEntity<User> salvarNovo(@RequestParam("nome") String nome, @RequestParam("email") String email,
			@RequestParam("senha") String senha) {
		
		User user = new User();
		user.setNome(nome);
		user.setEmail(email);
		user.setSenha(senha);
		Role role = roleService.getNome("USER");
		
		if (role != null) {
			user.getRole().add(role);
		}
		serviceUsuario.salvarCadastro(user);

		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	

}
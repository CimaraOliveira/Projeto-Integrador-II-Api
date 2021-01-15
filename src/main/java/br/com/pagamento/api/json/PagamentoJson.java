package br.com.pagamento.api.json;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pagamento.api.enums.Status;
import br.com.pagamento.api.enums.TipoPagamento;
import br.com.pagamento.api.jwt.JwtComponent;
import br.com.pagamento.api.model.Boleto;
import br.com.pagamento.api.model.Cartao;
import br.com.pagamento.api.model.LogRegister;
import br.com.pagamento.api.model.Pagamento;
import br.com.pagamento.api.model.User;
import br.com.pagamento.api.service.BoletoService;
import br.com.pagamento.api.service.CartaoService;
import br.com.pagamento.api.service.LogRegisterService;
import br.com.pagamento.api.service.PagamentoService;
import br.com.pagamento.api.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

class TestBoleto { // Classe Auxiliar
	public Boleto boleto;
	public Long idCompra;
}

class TestCartao { // Classe Auxiliar
	public Cartao cartao;
	public Long idCompra;
}

class Link {
	public String link;

	public Link(String token) {
		//this.link = "http://localhost:8080/compras/comprar/" + token;
		this.link = "https://projeto-pag-web.herokuapp.com/compras/comprar/" + token;
		//this.link = "https://projeto-pag-web.herokuapp.com//api/compras/gerarLink" + token;
	}
}

@RestController
@RequestMapping("/api/compras")
@Api(value = "API REST Pagamento")
@CrossOrigin(origins = { "*" })
public class PagamentoJson {
	@Autowired
	private JwtComponent jwtComponent;

	@Autowired
	private PagamentoService compraService;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private CartaoService cartaoService;

	@Autowired
	private UserService serviceUsuario;

	@Autowired
	private LogRegisterService logService;

	@GetMapping("/pagamento/{token}")
	@ApiOperation(value = "Retorna Pagamento")
	public ResponseEntity<Pagamento> pagamento(@PathVariable("token") String token,
			@RequestHeader(value = "Authorization", required = false) String Authorization) {
		try {

			boolean isValid = jwtComponent.isTokenExpired(Authorization.substring(7));
			if (!isValid) {
				Pagamento compras = compraService.findByPagamento(token);

				if (compras != null) {
					return ResponseEntity.ok(compras);
				}
				return ResponseEntity.notFound().build();
			}
		} catch (ExpiredJwtException | SignatureException e) {
			return ResponseEntity.status(403).build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).build();
		}
		return ResponseEntity.status(400).build();
	}

	@PostMapping("/gerarLink")
	public ResponseEntity<?> saveAndGerarLink(@RequestParam("valor") Double valor, @RequestParam("data") String data,
			@RequestParam("id") Long id, @RequestHeader(required = true, value = "Origem") String origin,
			@RequestHeader(required = true, value = "Authorization") String Authorization) {
		/*
		 * Esse metodo vai ser util para salvar a instancia do pagamento e depois ser
		 * usado para vincular o tipo de pagamento (boleto ou cartao)
		 */
		try {
			boolean isValid = jwtComponent.isTokenExpired(Authorization.substring(7));
			System.out.println(Authorization.substring(7));
			if (!isValid) {
				User user = serviceUsuario.tokenId(id,Authorization.substring(7)); // verifica se existe na base de dados o usuario com o id
				
				if (user != null) { // se ele for diferente de null, é pq existe
					user.setToken(Authorization.substring(7)); // seta o token no usuario
					serviceUsuario.salvar(user); // atualiza o usuario na base de dados
					Pagamento pagamento = new Pagamento(); // cria um pagamento
					pagamento.setValor(valor); // seta o valor
					pagamento.setUsuario(user); // seta o usuario
					pagamento.setToken(jwtComponent.generateToken(user)); // seta o token para ser usado para o link
					pagamento.setOrigin(origin);
					compraService.salvarCompra(pagamento);// salva o pagamento
					Link link = new Link(pagamento.getToken()); // cria um objeto link, e informa o token
					return ResponseEntity.ok(link); // retorna um objeto que contem o link
				}
				else {
					return ResponseEntity.status(404).build();
				}
			}
		} catch (ExpiredJwtException | SignatureException e) {
			// se o token for invalido e expirado, retorna 403
			return ResponseEntity.status(403).build();
		} catch (EntityNotFoundException e) {
			// se o usuario é nulo, retorna 404
			return ResponseEntity.status(404).build();
		} // a requisição precisa ser informada corretamente
		return ResponseEntity.status(400).build();
	}

	@PostMapping("/saveBoleto")
	public ResponseEntity<Boleto> salvarBoleto(HttpServletRequest request, @RequestBody TestBoleto test) {
		Pagamento compra = compraService.findByIdCompra(test.idCompra);

		String origin = request.getHeader("Origin");
		LogRegister logRegister = new LogRegister();
		logRegister.setHostOrigin(origin);
		logRegister.setDate(new Date());
		logService.save(logRegister);

		if (compra != null) {
			boletoService.salvarBoleto(test.boleto);
			test.boleto.setDataCompra(new Date());
			compra.setBoleto(test.boleto);
			compraService.salvarCompra(compra);
			return ResponseEntity.status(HttpStatus.CREATED).body(test.boleto);
		}

		return ResponseEntity.status(400).build();
	}

	@PostMapping("/saveCartao")
	public ResponseEntity<Cartao> salvarCartao(@RequestBody TestCartao test) {
		Pagamento compra = compraService.findByIdCompra(test.idCompra);

		if (compra != null) {
			cartaoService.salvarCartao(test.cartao);
			compra.setCartao(test.cartao);
			compraService.salvarCompra(compra);
			return ResponseEntity.status(HttpStatus.CREATED).body(test.cartao);
		}

		return ResponseEntity.status(400).build();
	}

	@GetMapping("/detalhesCompra/{id}")
	@ApiOperation(value = "Retorna os Pagamentos pelo id do usuario")
	public ResponseEntity<Pagamento> detalhePorId(@PathVariable("id") Long id,
			@RequestHeader(value = "Authorization", required = false) String Authorization) {
		try {
			boolean isValid = jwtComponent.isTokenExpired(Authorization.substring(7));
			if (!isValid) {
				Pagamento compras = compraService.findByIdCompra(id);

				if (compras != null) {
					return ResponseEntity.ok(compras);
				}
				return ResponseEntity.notFound().build();
			}
		} catch (ExpiredJwtException | SignatureException e) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.status(400).build();
	}

	@GetMapping("/detalhesCompraIdCartao/{id_cartao}")
	public ResponseEntity<List<Pagamento>> detalhesCompraCartao(@PathVariable("id_cartao") Long id_cartao,
			@RequestHeader(value = "Authorization", required = false) String Authorization) {
		try {
			boolean isValid = jwtComponent.isTokenExpired(Authorization.substring(7));
			if (!isValid) {
				return ResponseEntity.ok(compraService.findByIdcartao(id_cartao));
			}
		} catch (ExpiredJwtException | SignatureException e) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.status(400).build();

	}

	@GetMapping("/detalhesCompraIdBoleto/{id_boleto}")
	public ResponseEntity<List<Pagamento>> detalhesBoleto(@PathVariable("id_boleto") Long id_boleto,
			@RequestHeader(value = "Authorization", required = false) String Authorization) {
		try {
			boolean isValid = jwtComponent.isTokenExpired(Authorization.substring(7));
			if (!isValid) {
				return ResponseEntity.ok(compraService.findByIdBoleto(id_boleto));
			}
		} catch (ExpiredJwtException | SignatureException e) {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.status(400).build();
	}

	@GetMapping("/detalhes/{email}")
	@ApiOperation(value = "Retorna todos os Pagamentos do Usuário")
	public ResponseEntity<List<Pagamento>> detalhes(@PathVariable("email") String email,
			@RequestHeader(value = "Authorization", required = false) String Authorization) {
		try {
			System.out.println(email);

			boolean isValid = jwtComponent.isTokenExpired(Authorization.substring(7));
			if (!isValid) {
				Long id_compra = serviceUsuario.getEmail(email).getId();
				List<Pagamento> compras = compraService.findAllByIdUser(id_compra);

				if (!compras.isEmpty()) {
					return ResponseEntity.ok(compras);
				}
				return ResponseEntity.notFound().build();
			}
		} catch (ExpiredJwtException | SignatureException e) {
			return ResponseEntity.status(403).build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(404).build();
		}
		return ResponseEntity.status(400).build();
	}

	private double calPMT(double pv, int n, String i) {/// aquiiiiiiiiiiiiii

		// System.out.println( decimalFormat.format(valor) );
		String porcent[] = i.split("%");
		double taxa = Double.parseDouble(porcent[0]) / 100;
		double resultOne = (Math.pow((1 + taxa), n) - 1);
		double resultTwo = ((Math.pow(1 + taxa, n) * taxa));
		double resultThree = resultOne / resultTwo;
		// DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		// decimalFormat.setRoundingMode(RoundingMode.DOWN);
		BigDecimal bigDecimal = new BigDecimal(pv / resultThree, MathContext.DECIMAL32).setScale(2, BigDecimal.ROUND_HALF_EVEN);;


		// String valor[]=decimalFormat.format(pv/resultThree).split(",");

		/*
		 * if(Double.parseDouble(valor[0]) >=1.000) { return
		 * Double.parseDouble(valor[0]+valor[1]); }
		 */
		// return Double.parseDouble(valor[0]+"."+ valor[1]);
		return bigDecimal.doubleValue();
	}

	@PostMapping("/saveCompra")
	@ApiOperation(value = "Salva uma compra no cartao ou boleto se usuario for autenticado")
	public ResponseEntity<Pagamento> salvarCompra(@RequestBody Pagamento pagamento,
			@RequestHeader(value = "Authorization", required = true) String Authorization) {

		if (Authorization == null) { // verifica se na requisição tem o token no header
			return ResponseEntity.status(400).build();
		} else if (Authorization.trim().isEmpty()) { // verifica se o token no header não é vazio
			return ResponseEntity.status(400).build();
		}

		try {
			boolean isValid = jwtComponent.isTokenExpired(Authorization.substring(7));
			if (!isValid) { // verifica se o token é valido e não expirou
				if (pagamento != null) { // verifica se o pagamento é diferente de nulo. Se for verdadeiro o fluxo //
											// continua...
					if (pagamento.getTipoPagamento() != null) { // verifica se o tipo de pagamento existe, caso seja
																// verdadeiro ele continua o fluxo...
						User user = serviceUsuario.findByEmail(pagamento.getUsuario().getEmail());
						if (user != null) { // verifica se o usuario passado na requisição existe na base de dados. Se
											// for verdadeiro o fluxo continua...
							if (pagamento.getTipoPagamento().equals(TipoPagamento.BOLETO)) {
								if (pagamento.getBoleto() != null) { // verifica se existe o boleto na requisição
									Boleto boleto = pagamento.getBoleto();
									pagamento.setStatus(Status.ANDAMENTO);
									boleto.setDataCompra(new Date());
									Random r = new Random();
									String codigo = String.valueOf(Math.abs(r.nextInt() * 100000000));
									System.out.println(codigo);
									boleto.setNumeroBoleto(codigo);
									boleto.setDataVencimento(LocalDate.now().plusDays(5));
									boletoService.salvarBoleto(boleto);
									pagamento.setBoleto(boleto);
								} else { // se não existe deve informar
									return ResponseEntity.status(400).build();
								}
							} else if (pagamento.getTipoPagamento().equals(TipoPagamento.CARTAO)) {
								if (pagamento.getCartao() != null) {// verifica se existe o cartao na requisição
									double valor = pagamento.getQuantidade() > 0
											? pagamento.getValor() * pagamento.getQuantidade()
											: pagamento.getValor();
									Cartao cartao = pagamento.getCartao();
									cartao.setValor_parcelado(calPMT(valor, cartao.getQtd_parcelas(), "2%"));
									pagamento.setStatus(Status.CONCLUÍDA);
									cartaoService.salvarCartao(cartao);
									pagamento.setCartao(cartao);
								} else {// se não existe deve informar
									return ResponseEntity.status(400).build();
								}
							}
							// caso existe o boleto ou cartão.
							pagamento.setDataCompra(new Date());
							if (pagamento.getQuantidade() > 0) {
								pagamento.setValor(pagamento.getValor() * pagamento.getQuantidade());
							} else {
								pagamento.setValor(pagamento.getValor());
							}
							pagamento.setUsuario(user);
							pagamento.setOrigin(pagamento.getOrigin());
							compraService.salvarCompra(pagamento);
							return new ResponseEntity<Pagamento>(pagamento, HttpStatus.CREATED);
						}
					}
				}
			}
		} catch (ExpiredJwtException | SignatureException e) {
			return ResponseEntity.status(403).build();// retorna caso o token não seja valido
		} catch (NoSuchElementException e) {// retorna caso o id não se encontra na base de dados
			return ResponseEntity.status(404).build();
		}
		return ResponseEntity.status(400).build();
	}

}
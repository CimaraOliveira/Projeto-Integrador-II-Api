package br.com.pagamento.api.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pagamento.api.model.Cartao;
import br.com.pagamento.api.service.CartaoService;

@RestController
@RequestMapping("/api/cartao")
@CrossOrigin(origins = {"*"})
public class CartaoJson {
	@Autowired
	private CartaoService cartaoService;

	/*criar um metodo que salve o cartao com relacionamento do usuario*/
}

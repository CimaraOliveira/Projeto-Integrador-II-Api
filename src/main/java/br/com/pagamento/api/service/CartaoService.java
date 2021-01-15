package br.com.pagamento.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pagamento.api.model.Cartao;
import br.com.pagamento.api.repository.CartaoRepository;


@Service
public class CartaoService {

	@Autowired
	private CartaoRepository repository;

	public Cartao salvarCartao(Cartao cartao) {
		return repository.save(cartao);
	}

	public Cartao idCartao(Long id) {
		return repository.idCartao(id);
	}

}

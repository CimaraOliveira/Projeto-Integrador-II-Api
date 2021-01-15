package br.com.pagamento.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.api.model.Pagamento;
import br.com.pagamento.api.repository.PagamentoRepository;

@Service
public class CompraService {

	@Autowired
	private PagamentoRepository repository;

	@Transactional(readOnly = false)
	public void salvarCompra(Pagamento compra) {

		repository.save(compra);
	}

	public Pagamento findByIdCompra(Long id) {
		return repository.findByIdCompra(id);
	}

	public List<Pagamento> findAllByIdUser(Long id) {
		return repository.findAllByIdUser(id);
	}

	public List<Pagamento> findByIdBoleto(Long id_boleto) {
		return repository.findByIdBoleto(id_boleto);
	}

	public List<Pagamento> findByIdcartao(Long id_cartao) {
		return repository.findByIdCartao(id_cartao);
	}

}

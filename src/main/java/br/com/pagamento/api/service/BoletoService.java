package br.com.pagamento.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pagamento.api.model.Boleto;
import br.com.pagamento.api.repository.BoletoRepository;



@Service
public class BoletoService {
 
	@Autowired   
	private  BoletoRepository repository;
	
	@Transactional(readOnly = false)
	public Boleto salvarBoleto(Boleto boleto) {
	 
		return repository.save(boleto);
		 
	}
	
		
}

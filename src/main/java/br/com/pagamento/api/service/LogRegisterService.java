package br.com.pagamento.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pagamento.api.model.LogRegister;
import br.com.pagamento.api.repository.LogRegisterRepository;

@Service
public class LogRegisterService {

	@Autowired
	private LogRegisterRepository repository;

	public void save(LogRegister logRegister) {
		repository.saveAndFlush(logRegister);
	}

	public LogRegister getHostOrigin(String hostOrigin) {
		return repository.findByHostOrigin(hostOrigin);
	}

}

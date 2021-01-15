package br.com.pagamento.api.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService<T> {

	@Autowired
	private HttpSession session;
	@Autowired
	private HttpServletRequest request;

	public void criarSession(String name, T objeto) {
		if (request.getSession().getAttribute(name) == null) {
			request.getSession().setAttribute(name, objeto);
		}
	}

	public T getSession(String name) {
		return (T) session.getAttribute(name);
	}

	public void clearSession() {
		request.getSession().invalidate();
	}

}

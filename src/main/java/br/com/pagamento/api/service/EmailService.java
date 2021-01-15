package br.com.pagamento.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.pagamento.api.model.Email;
import br.com.pagamento.api.model.User;


@Service
public class EmailService { 
		
	@Autowired
	private UserService serviceUsuario;
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	public void sendNovaSenhaEmail(Email email, String novaSenha) {
		try {
			User usuario = serviceUsuario.getEmail(email.getTo());
			email.setFrom("suporte.livraria123@gmail.com");
			email.getMap().put("name", usuario.getNome());
			email.setSubject("Olá " + usuario.getNome() + " Confira sua nova senha!");
			
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email.getTo());
			message.setSubject(email.getSubject());
			message.setText("Bem vindo ao sistema de Pagamento \n"+usuario.getNome()+"\n Sua nova senha: " + novaSenha);
			message.setFrom(email.getFrom());
			
		    mailSender.send(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
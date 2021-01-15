package br.com.pagamento.api.enums;

public enum Exemple {
	MODULO_AREA("http://moduloarea.com.br"),
	MODULO_HOSP("http://hospedacao.com.br"),
	MODULO_VEICULO("http://veiculo.com.br");
	
	private String value;
	
	private Exemple(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}

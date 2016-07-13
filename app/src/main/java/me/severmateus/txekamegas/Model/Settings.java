package me.severmateus.txekamegas.Model;

public class Settings {
	
	private int codSettings;
	private String operadora;
	private boolean corte, mensagens;
	
	public Settings() {
		super();
		corte = true;
		mensagens = true;
	}

	public Settings(int codSettings, String operadora, boolean corte, boolean mensagens) {
		super();
		this.codSettings = codSettings;
		this.corte = corte;
		this.mensagens = mensagens;
		this.operadora = operadora;
	}

	public int getCodSettings() {
		return codSettings;
	}

	public void setCodSettings(int codSettings) {
		this.codSettings = codSettings;
	}

	public boolean isCorte() {
		return corte;
	}

	public void setCorte(boolean corte) {
		this.corte = corte;
	}

	public String getOperadora() {
		return operadora;
	}

	public void setOperadora(String operadora) {
		this.operadora= operadora;
	}
	
	public boolean isMensagens() {
		return mensagens;
	}

	public void setMensagens(boolean mensagens) {
		this.mensagens = mensagens;
	}

	@Override
	public String toString() {
		return "Settings [codSettings=" + codSettings + ", operadora="
				+ operadora + ", corte=" + corte + ", mensagens="
				+ mensagens + "]";
	}

}

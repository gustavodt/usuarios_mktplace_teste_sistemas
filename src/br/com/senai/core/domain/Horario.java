package br.com.senai.core.domain;

import java.time.LocalTime;
import java.util.Objects;

public class Horario {
	
	private int id;
	
	private String diaDaSemana;
	
	private LocalTime horaAbertura;
	
	private LocalTime horaFechamento;
	
	private Restaurante restaurante;

	public Horario(String diaDaSemana, LocalTime horaAbertura, LocalTime horaFechamento,
			Restaurante restaurante) {
		this.diaDaSemana = diaDaSemana;
		this.horaAbertura = horaAbertura;
		this.horaFechamento = horaFechamento;
		this.restaurante = restaurante;
	}

	public Horario(int id, String diaDaSemana, LocalTime horaAbertura, LocalTime horaFechamento,
			Restaurante restaurante) {
		this(diaDaSemana, horaAbertura, horaFechamento, restaurante);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDiaDaSemana() {
		return diaDaSemana;
	}

	public void setDiaDaSemana(String diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public LocalTime getHoraAbertura() {
		return horaAbertura;
	}

	public void setHoraAbertura(LocalTime horaAbertura) {
		this.horaAbertura = horaAbertura;
	}

	public LocalTime getHoraFechamento() {
		return horaFechamento;
	}

	public void setHoraFechamento(LocalTime horaFechamento) {
		this.horaFechamento = horaFechamento;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Horario other = (Horario) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return horaAbertura + " : " + horaFechamento;
	}	
	
	
}

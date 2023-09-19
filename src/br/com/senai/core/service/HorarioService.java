package br.com.senai.core.service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Horario;

public class HorarioService {

	private DaoHorario dao;
	
	public HorarioService() {
		this.dao = FactoryDao.getInstance().getDaoHorario();
	}
	
	public void salvar(Horario horario) throws IOException {
		this.validar(horario);
		
		boolean isPersistido = horario.getId() > 0;
		
		if (isPersistido) {
			this.dao.alterar(horario);
		}else {
			this.dao.inserir(horario);
		}
	}
	
	private void validar(Horario horario) {
		if (horario != null) {
		
			List<Horario> horarios = new ArrayList<Horario>();
			
			horarios.addAll(listarPorId(horario.getRestaurante().getId()));
			horarios.remove(horario);
			
			
			boolean isConflito = horarios
					.stream()
					.anyMatch(horari -> horari.getDiaDaSemana().equals(horario.getDiaDaSemana())
							&& horario.getHoraAbertura().isBefore(horari.getHoraFechamento())
							&& horario.getHoraFechamento().isAfter(horari.getHoraAbertura()));
			
			if (isConflito) {
				throw new IllegalArgumentException("O horário informado está conflitando com outro cadastrado no banco");
			}
			
			
			boolean isCamposInvalidos = horario.getDiaDaSemana().isBlank()
					|| horario.getDiaDaSemana() == null
					|| horario.getHoraAbertura() == null
					|| horario.getHoraFechamento() == null
					|| horario.getRestaurante() == null;
			
			if (isCamposInvalidos) {
				throw new IllegalArgumentException("Todos os campos são obrigatórios");
			}
			
			LocalTime tempoMaximo = LocalTime.of(23, 59);
			LocalTime tempoMin = LocalTime.of(00, 00);
			
			boolean isHorarioInvalido = horario.getHoraAbertura().isAfter(tempoMaximo)
					||  horario.getHoraAbertura().isBefore(tempoMin) 
					|| 	horario.getHoraFechamento().isAfter(tempoMaximo)
					||  horario.getHoraFechamento().isBefore(tempoMin);
			
			if (isHorarioInvalido) {
				throw new IllegalArgumentException("Horario inválido");
			}
			
			boolean isHoraInicialMaiorFinal = horario.getHoraAbertura().equals(horario.getHoraFechamento())
					|| horario.getHoraAbertura().isAfter(horario.getHoraFechamento());
			
			if (isHoraInicialMaiorFinal) {
				throw new IllegalArgumentException("O horario inicial não pode ser maior ");
			}
			
		} else {
			throw new NullPointerException("O horario não pode ser nula");
		}
	}
	
	public void removerPor (int id) {
		if(id > 0 ) {
			this.dao.excluirPor(id);
		}else {
			throw new IllegalArgumentException("O id da Horario deve ser maior que zero ");
		}
	}
	
	public Horario buscarPor(int id) {
		if (id > 0) {
			Horario HorarioEncontrado = this.dao.buscarPor(id);
			if (HorarioEncontrado == null) {
				throw new IllegalArgumentException("Não foi encontrado Horario para o código informado");
			}
			return HorarioEncontrado;
		} else {
			throw new IllegalArgumentException("O id da Horario deve ser maior que 0");
		}
	}

	public List<Horario> listarPorId(int id) {
		
		if ( id > 0 ) {
			return dao.listarPor(id);
		}
		throw new IllegalArgumentException("O id do restaurante obrigatório");
	}
}

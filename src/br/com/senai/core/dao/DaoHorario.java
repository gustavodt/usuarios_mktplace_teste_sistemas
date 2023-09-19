package br.com.senai.core.dao;

import java.util.List;

import br.com.senai.core.domain.Horario;

public interface DaoHorario {

	public void inserir(Horario horario);

	public void alterar(Horario horario);

	public void excluirPor(int id);
	
	public Horario buscarPor(int id);
	
	public List<Horario> listarPor(int id);
	
	public int contarPor(int idRestaurante);
}

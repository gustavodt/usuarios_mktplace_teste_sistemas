package br.com.senai.view.componentes.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.senai.core.domain.Horario;

public class HorarioTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int QTDE_COLUNAS = 3;
	
	private List<Horario> horarios;
	
	public HorarioTableModel(List<Horario> horarios) {
		this.horarios = horarios;
	}
	
	public Horario getPorLinha(int rowIndex) {
		return horarios.get(rowIndex);
	}
	
	public String getColumnName(int column){
		switch (column) {
		
		case 0:
			return "Dia da Semana";
		case 1:
			return "Abertura";
		case 2:
			return "Fechamento";
		default:
			throw new IllegalArgumentException("Ocorreu um erro ao pegar as colunas da tabela");
		}
	}

	@Override
	public int getRowCount() {
		return horarios.size();
	}

	@Override
	public int getColumnCount() {
		return QTDE_COLUNAS;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return horarios.get(rowIndex).getDiaDaSemana();
		case 1:
			return horarios.get(rowIndex).getHoraAbertura();
		case 2:
			return horarios.get(rowIndex).getHoraFechamento();
		default:
			throw new IllegalArgumentException("Ocorreu um erro ao pegar os valores da coluna");
		}
	}
	
	public void removerPor(int rowIndex) {
		this.horarios.remove(rowIndex);
	}
	
	public boolean isVazio() {
		return horarios.isEmpty();
	}

	
}

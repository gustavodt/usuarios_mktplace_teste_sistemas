package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.Horario;
import br.com.senai.core.domain.Restaurante;

public class DaoPostgresHorario implements DaoHorario{
	
	private final String INSERT = "INSERT INTO horarios_atendimento"
			+ " (dia_semana, hora_abertura,"
			+ " hora_fechamento, id_restaurante)"
			+ " VALUES (?, ?, ?, ?)";
	
	private final String UPDATE = "UPDATE horarios_atendimento SET"
			+ " dia_semana = ?, hora_abertura = ?,"
			+ " hora_fechamento = ?, id_restaurante = ?"
			+ " WHERE id = ?";
	
	private final String DELETE = "DELETE FROM horarios_atendimento WHERE id = ?";
	
	private final String SELECT_BY_ID_RESTAURANTE = "SELECT h.id, h.dia_semana,"
			+ " h.hora_abertura, h.hora_fechamento, r.id id_restaurante, r.nome nome_restaurante,"
			+ " r.descricao , r.cidade , r.logradouro," 
			+ " r.bairro , r.complemento"
			+ " FROM horarios_atendimento h , restaurantes r"
			+ " WHERE h.id_restaurante = r.id"
			+ " AND r.id = ?"
			+ " ORDER BY"
			+ " CASE dia_semana"
			+ " WHEN 'DOMINGO' THEN 1"
			+ " WHEN 'SEGUNDA' THEN 2"
			+ " WHEN 'TERCA' THEN 3"
			+ " WHEN 'QUARTA' THEN 4"
			+ " WHEN 'QUINTA' THEN 5"
			+ " WHEN 'SEXTA' THEN 6"
			+ " WHEN 'SABADO' THEN 7"
			+ " END;";
	
	private final String COUNT_BY_REST = "SELECT COUNT(*) qtde"
			+ " FROM horarios_atendimento h"
			+ " WHERE h.id_restaurante = ?";
	
	private Connection conexao;
	
	public DaoPostgresHorario() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}

	@Override
	public void inserir(Horario horario) {
		PreparedStatement ps = null;
		try {
			ps = conexao.prepareStatement(INSERT);
			ps.setString(1, horario.getDiaDaSemana());
			ps.setTime(2, Time.valueOf(horario.getHoraAbertura()));
			ps.setTime(3, Time.valueOf(horario.getHoraFechamento()));
			ps.setInt(4, horario.getRestaurante().getId());
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao inserir o horario. Motivo:" + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}
	}

	@Override
	public void alterar(Horario horario) {
		PreparedStatement ps = null;
		
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);	
			ps = conexao.prepareStatement(UPDATE);
			ps.setString(1, horario.getDiaDaSemana());
			ps.setTime(2, Time.valueOf(horario.getHoraAbertura()));
			ps.setTime(3, Time.valueOf(horario.getHoraFechamento()));
			ps.setInt(4, horario.getRestaurante().getId());
			ps.setInt(5, horario.getId());
			boolean isAlteracaoOK = ps.executeUpdate() == 1;
			if (isAlteracaoOK) {
				this.conexao.commit();
			}else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public void excluirPor(int id) {
		PreparedStatement ps = null ;
		
		try {
		
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
			
			ps = conexao.prepareStatement(DELETE);
			ps.setInt(1, id );
			
			Boolean isDeleteok = ps.executeUpdate() == 1;

			if (isDeleteok) {
				this.conexao.commit();
			} else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);

			
		} catch (Exception e) {
			
			throw new RuntimeException("erro ao alterar . motivo :" + e.getMessage() );
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}
	
	@Override
	public Horario buscarPor(int id) {
		PreparedStatement ps = null ;
		ResultSet rs = null ;
		
		try {
			
			ps = conexao.prepareStatement(SELECT_BY_ID_RESTAURANTE);	
			ps.setInt(1,id);
			rs = ps.executeQuery();
		
			if(rs.next()) {
				return extrairDo(rs);
			}
			 
			return null ;
			
		} catch (Exception e) {
			
			throw new RuntimeException("ocorreu um erro ao buscar o horario" + e.getMessage()) ;
	
		}finally {
			
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
			
		}
	}
	
	private Horario extrairDo(ResultSet rs) {
		
		try {
			
			int id_horario = rs.getInt("id");
			String dia_semana = rs.getString("dia_semana");
			LocalTime hora_abertura = rs.getTime("hora_abertura").toLocalTime();
			LocalTime hora_fechamento = rs.getTime("hora_fechamento").toLocalTime();
			
			int idDoRestaurante = rs.getInt("id_restaurante");
			String nomeRestaurante = rs.getString("nome_restaurante");
			String descricao = rs.getString("descricao");
			String cidade = rs.getString("cidade");
			String logradouro = rs.getString("logradouro");
			String bairro = rs.getString("bairro");
			String complemento = rs.getString("complemento");
			
			
			Endereco endereco = new Endereco(cidade, logradouro, bairro, complemento);
			Restaurante restaurante = new Restaurante(idDoRestaurante, nomeRestaurante, descricao, endereco, null);
			
			return new Horario(id_horario, dia_semana, hora_abertura, hora_fechamento, restaurante);
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao extrair do horario. Motivo:" + e.getMessage());
		}
	}

	@Override
	public List<Horario> listarPor(int id) {
		List<Horario> horarios = new ArrayList<Horario>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_BY_ID_RESTAURANTE);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				horarios.add(extrairDo(rs));
			}
		} catch (Exception e) {
			throw new RuntimeException("Ocorre um erro ao listar os horarios. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
		return horarios;
	}

	@Override
	public int contarPor(int idRestaurante) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(COUNT_BY_REST);
			ps.setInt(1, idRestaurante);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("qtde");
			}
			return 0;
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao contar os horarios. Motivo:" + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}
}

package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoCategoria;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Categoria;

public class DaoPostgresCategoria implements DaoCategoria {

	private final String INSERT = "INSERT INTO categorias (nome) VALUES (?)";

	private final String UPDATE = "UPDATE categorias SET nome = ? WHERE id = ?";

	private final String DELETE = "DELETE FROM categorias WHERE id = ?";

	private final String SELECT_BY_ID = "SELECT c.id, c.nome "
									  + "FROM categorias c "
									  + "WHERE c.id = ?";

	private final String SELECT_BY_NAME = "SELECT c.id, c.nome "
										+ "FROM categorias c "
										+ "WHERE Upper(c.nome) LIKE (?)"
										+ "ORDER BY c.nome";
	
	private final String SELECT_ALL = "SELECT c.id, c.nome"
			+ " FROM categorias c "
			+ " ORDER BY c.nome";
	
	private Connection conexao;
	
	public DaoPostgresCategoria() {
		this.conexao = ManagerDb.getInstance().getConexao();
		System.out.println("Otimização da classe");
	}

	@Override
	public void inserir(Categoria categoria) {
		PreparedStatement ps = null;
		try {
			ps = conexao.prepareStatement(INSERT);
			ps.setString(1, categoria.getNome());
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao inserir a categoria. "
									 + "Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}

	}

	@Override
	public void alterar(Categoria categoria) {
		PreparedStatement ps = null;
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);	
			ps = conexao.prepareStatement(UPDATE);
			ps.setString(1, categoria.getNome());
			ps.setInt(2, categoria.getId());
			boolean isAlteracaoOK = ps.executeUpdate() == 1;
			if (isAlteracaoOK) {
				this.conexao.commit();
			}else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao alterar a categoria. "
					 + "Motivo: " + e.getMessage());
			
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}
	}

	@Override
	public void excluirPor(int id) {
		PreparedStatement ps = null;
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
			ps = conexao.prepareStatement(DELETE);
			ps.setInt(1, id);
			boolean isExclusaoOK = ps.executeUpdate() == 1;
			if (isExclusaoOK) {
				this.conexao.commit();				
			}else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "excluir a categoria. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}

	}
	
	private Categoria extrairDo(ResultSet rs) {
		try {
			
			int id = rs.getInt("id");
			String nome = rs.getString("nome");
			
			return new Categoria(id, nome);					
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "extrair a categoria. Motivo: " + e.getMessage());
		}
	}

	@Override
	public Categoria buscarPor(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_BY_ID);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				return extrairDo(rs);
			}
			return null;
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "buscar a categoria. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

	@Override
	public List<Categoria> listarPor(String nome) {
		List<Categoria> categorias = new ArrayList<Categoria>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_BY_NAME);
			ps.setString(1, nome);
			rs = ps.executeQuery();
			while (rs.next()) {
				categorias.add(extrairDo(rs));
			}
			return categorias;
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "listar as categorias. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

	@Override
	public List<Categoria> listarTodas() {
		List<Categoria> categorias = new ArrayList<Categoria>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_ALL);
			rs = ps.executeQuery();
			while (rs.next()) {
				categorias.add(extrairDo(rs));
			}
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro na listagem de categorias. Motivo "
					+ ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
		return categorias;
	}
}

package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.Restaurante;

public class DaoPostgresRestaurante implements DaoRestaurante {
	
	private final String INSERT = "INSERT INTO restaurantes (nome, descricao, cidade,"
								+ " logradouro, bairro, complemento, id_categoria) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

	private final String UPDATE = "UPDATE restaurantes SET nome = ?, descricao = ?, cidade = ?,"
								+ " logradouro = ?, bairro = ?, complemento = ?, id_categoria = ?  "
								+ "WHERE id = ?";

	private final String DELETE = "DELETE FROM restaurantes WHERE id = ?";

	private final String SELECT_BY_ID = "SELECT r.id id_restaurante, r.nome nome_restaurante, r.descricao,"
									  + " r.cidade, r.logradouro, r.bairro, r.complemento, "
									  + "c.id id_categoria, c.nome nome_categoria"
									  + " FROM restaurantes r,"
									  + " categorias c"
									  + " WHERE r.id_categoria = c.id"
									  + " AND r.id = ?";

	private final String SELECT_BY_NAME_CATEG = "SELECT r.id, r.nome, r.descricao,"
			  								  + " r.cidade, r.logradouro, r.bairro, r.complemento,"
			  								  + " c.id id_categoria, c.nome nome_categoria"
			  								  + " FROM restaurantes r,"
			  								  + " categorias c"
			  								  + " WHERE r.id_categoria = c.id";
	
	private final String SELECT_ALL = "SELECT r.id , r.nome , r.descricao,"
			  + " r.cidade, r.logradouro, r.bairro, r.complemento,"
			  + " c.id id_categoria, c.nome nome_categoria"
			  + " FROM restaurantes r, categorias c"
			  + " WHERE r.id_categoria = c.id"
			  + " ORDER BY r.nome";
	
	private final String COUNT_BY_CATEG = "SELECT COUNT(id) qtde"
			+ " FROM restaurantes r"
			+ " WHERE r.id_categoria = ?";
	
	private Connection conexao;
	
	public DaoPostgresRestaurante() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}

	@Override
	public void inserir(Restaurante restaurante) {
		PreparedStatement ps = null;
		try {
			ps = conexao.prepareStatement(INSERT);
			ps.setString(1, restaurante.getNome());
			ps.setString(2, restaurante.getDescricao());
			ps.setString(3, restaurante.getEndereco().getCidade());
			ps.setString(4, restaurante.getEndereco().getLogradouro());
			ps.setString(5, restaurante.getEndereco().getBairro());
			ps.setString(6, restaurante.getEndereco().getComplemento());
			ps.setInt(7, restaurante.getCategoria().getId());
			ps.execute();
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "inserir o restaurante. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}

	}

	@Override
	public void alterar(Restaurante restaurante) {
		PreparedStatement ps = null;
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);			
			ps = conexao.prepareStatement(UPDATE);
			ps.setString(1, restaurante.getNome());
			ps.setString(2, restaurante.getDescricao());
			ps.setString(3, restaurante.getEndereco().getCidade());
			ps.setString(4, restaurante.getEndereco().getLogradouro());
			ps.setString(5, restaurante.getEndereco().getBairro());
			ps.setString(6, restaurante.getEndereco().getComplemento());
			ps.setInt(7, restaurante.getCategoria().getId());
			ps.setInt(8, restaurante.getId());
			boolean isAlteracaoOK = ps.executeUpdate() == 1;
			if (isAlteracaoOK) {
				this.conexao.commit();
			}else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "alterar o restaurante. Motivo: " + e.getMessage());
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
					+ "excluir o restaurante. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
		}

	}
	
	private Restaurante extrairDo(ResultSet rs) {
		try {
			
			int idDoRestaurante = rs.getInt("id");
			String nomeDoRestaurante = rs.getString("nome");
			String descricao = rs.getString("descricao");
			String cidade = rs.getString("cidade");
			String logradouro = rs.getString("logradouro");
			String bairro = rs.getString("bairro");
			String complemento = rs.getString("complemento");
			
			int idDaCategoria = rs.getInt("id_categoria");
			String nomeDaCategoria = rs.getString("nome_categoria");
			
			Endereco endereco = new Endereco(cidade, logradouro, bairro, complemento);
			Categoria categoria = new Categoria(idDaCategoria, nomeDaCategoria);
			
			return new Restaurante(idDoRestaurante, nomeDoRestaurante, descricao, endereco, categoria);
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "extrair o projeto. Motivo: " + e.getMessage());
		}
	}

	@Override
	public Restaurante buscarPor(int id) {
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
					+ "buscar o restaurante. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

	@Override
	public List<Restaurante> listarPor(String nome, Categoria categoria) {
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuilder consulta = new StringBuilder(SELECT_BY_NAME_CATEG);
			
			if (categoria != null) {
				consulta.append(" AND c.id = ?");
			}
			if (nome != null && !nome.isBlank()) {
				consulta.append(" AND Upper(r.nome) LIKE Upper(?)");
			}
			
			consulta.append(" ORDER BY r.nome ");
			
			ps = conexao.prepareStatement(consulta.toString());
			
			int indice = 1;
			
			if (categoria != null) {
				ps.setInt(indice, categoria.getId());
				indice++;
			}
			if (nome != null && !nome.isBlank()) {
				ps.setString(indice, nome);
			}
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				restaurantes.add(extrairDo(rs));
			}
			return restaurantes;
		}catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao "
					+ "listar os restaurantes. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

	@Override
	public List<Restaurante> listarTodas() {
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(SELECT_ALL);
			rs = ps.executeQuery();
			while (rs.next()) {
				restaurantes.add(extrairDo(rs));
			}
		} catch (Exception ex) {
			throw new RuntimeException("Ocorreu um erro na listagem de restaurantes. Motivo: "
					+ ex.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
		return restaurantes;
	}
	
	@Override
	public int contarPor(int idCategoria) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conexao.prepareStatement(COUNT_BY_CATEG);
			ps.setInt(1, idCategoria);
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

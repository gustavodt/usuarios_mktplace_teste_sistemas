package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoCategoria;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;

public class CategoriaService {
	
	private DaoCategoria dao;
	
	private DaoRestaurante daoRestaurante;
	
	public CategoriaService() {
		this.dao = FactoryDao.getInstance().getDaoCategoria();
		this.daoRestaurante = FactoryDao.getInstance().getDaoRestaurante();
	}

	public void salvar(Categoria categoria) {
		
		this.validar(categoria);
		
		boolean isPersistido = categoria.getId() > 0;
		
		if (isPersistido) {
			this.dao.alterar(categoria);
		}else {
			this.dao.inserir(categoria);
		}
		
	}
	
	private void validar(Categoria categoria) {
		if (categoria != null) {
			
			boolean isNomeInvalido = categoria.getNome() == null 
					|| categoria.getNome().isBlank() 
					|| categoria.getNome().length() < 3 
					|| categoria.getNome().length() > 100;
			
			if (isNomeInvalido) {
				throw new IllegalArgumentException("O nome é obrigatório "
						+ "e deve possuir entre 3 e 100 caracteres");
			}				
					
		}else {
			throw new NullPointerException("A categoria não pode ser nula");
		}
	}
	
	public void removerPor(int idDaCategoria) {
		if (idDaCategoria > 0) {
			
			int qtdeRestaurantes = daoRestaurante.contarPor(idDaCategoria);
			
			if (qtdeRestaurantes > 0) {
				throw new IllegalArgumentException(
						"A categoria não pode ser excluída porque contém restaurantes vinculados a ele");
			} else {
				this.dao.excluirPor(idDaCategoria);
			}
		} else {
			throw new IllegalArgumentException(
					"O id para remoção da categoria deve ser maior que zero");
		}
	}
	
	public Categoria buscarPor(int idDaCategoria) {
		if (idDaCategoria > 0) {
			Categoria categoriaEncontrada = this.dao.buscarPor(idDaCategoria);
			if (categoriaEncontrada == null) {
				throw new IllegalArgumentException("Não foi encontrada categoria para o código informado");
			}
			return categoriaEncontrada;
		}else {
			throw new IllegalArgumentException("O id para remoção da categoria deve ser maior que zero");
		}
	}
	
	public List<Categoria> listarPor(String nome){
		if (nome != null && !nome.isBlank() && nome.length() >= 3) {
			return dao.listarPor("%" + nome + "%");		
		}else {
			throw new IllegalArgumentException("O filtro para listagem é obrigatório e deve conter mais de 2 caracteres");
		}
	}
	
	public List<Categoria> listarTodas() {
		return dao.listarTodas();
	}
}

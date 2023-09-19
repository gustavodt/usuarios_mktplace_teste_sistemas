package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;

public class RestauranteService {

	private DaoRestaurante dao;
	
	private DaoHorario daoHorario;

	public RestauranteService() {
		this.dao = FactoryDao.getInstance().getDaoRestaurante();
		this.daoHorario = FactoryDao.getInstance().getDaoHorario();	
	}

	public void salvar(Restaurante restaurante) {
		
		this.validar(restaurante);

		boolean isPersistido = restaurante.getId() > 0;

		if (isPersistido) {
			this.dao.alterar(restaurante);
		} else {
			this.dao.inserir(restaurante);
		}
	}

	private void validar(Restaurante restaurante) {
		
		if (restaurante != null) {
			
			if (restaurante.getEndereco() != null) {
				
				if (restaurante.getCategoria() != null && restaurante.getCategoria().getId() > 0) {
					
					boolean isNomeInvalido = restaurante.getNome() == null 
							|| restaurante.getNome().isBlank()
							|| restaurante.getNome().length() < 3 
							|| restaurante.getNome().length() > 250;
							
					if (isNomeInvalido) {
						throw new IllegalArgumentException(
								"O nome é obrigatório e deve possuir entre 3 e 250 caracteres");
					}		
					
					boolean isDescricaoInvalida = restaurante.getDescricao() == null 
							|| restaurante.getDescricao().isBlank()
							|| restaurante.getDescricao().length() < 10;
					
					if (isDescricaoInvalida) {
						throw new IllegalArgumentException(
								"A descrição é obrigatória e deve possuir no mínimo 10 caracteres");
					}
					
					boolean isLogradouroInvalido = restaurante.getEndereco().getLogradouro() == null
							|| restaurante.getEndereco().getLogradouro().isBlank()
							|| restaurante.getEndereco().getLogradouro().length() < 3
							|| restaurante.getEndereco().getLogradouro().length() > 200;
							
					if (isLogradouroInvalido) {
						throw new IllegalArgumentException(
								"O logradouro do endereço é obrigatório e não deve possuir mais de 200 caracteres");
					}
					
					boolean isCidadeInvalida = restaurante.getEndereco().getCidade() == null 
							|| restaurante.getEndereco().getCidade().isBlank()
							|| restaurante.getEndereco().getCidade().length() < 3
							|| restaurante.getEndereco().getCidade().length() > 80;
							
					if (isCidadeInvalida) {
						throw new IllegalArgumentException(
								"A cidade do endereço é obrigatório e não deve possuir mais de 80 caracteres");
					}
					
					boolean isBairroInvalido = restaurante.getEndereco().getBairro() == null 
							|| restaurante.getEndereco().getBairro().isBlank()
							|| restaurante.getEndereco().getBairro().length() < 3
							|| restaurante.getEndereco().getBairro().length() > 250;
							
					if (isBairroInvalido) {
						throw new IllegalArgumentException(
								"O bairro do endereço é obrigatório enão deve possuir entre 3 e 250 caracteres");
					}
					
				} else {
					throw new IllegalArgumentException("A categoria do restaurante é obrigatória");
				}
			} else {
				throw new IllegalArgumentException("O endereço do restaurante não pode ser nulo");
			}
		} else {
			throw new NullPointerException("O restaurante não pode ser nulo");
		}

	}

	public void removerPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			
			int qtdeHorarios = daoHorario.contarPor(idDoRestaurante);
			
			if (qtdeHorarios > 0) {
				throw new IllegalArgumentException(
						"O restaurante não pode ser excluído porque contém horários cadastrados nele");
			} else {
				this.dao.excluirPor(idDoRestaurante);
			}
		} else {
			throw new IllegalArgumentException(
					"O id para remoção do restaurante deve ser maior que zero");
		}
	}

	public Restaurante buscarPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			Restaurante restauranteEncontrado = dao.buscarPor(idDoRestaurante);
			if (restauranteEncontrado == null) {
				throw new IllegalArgumentException(
						"Não foi encontrado restaurante para o código informado");
			}
			return restauranteEncontrado;
		} else {
			throw new IllegalArgumentException(
					"O id para busca do restaurante deve ser maior que zero");
		}
	}

	public List<Restaurante> listarPor(String nome, Categoria categoria) {
		
		boolean isCategoriaInformada = categoria != null && categoria.getId() > 0;
		boolean isNomeInformado = nome != null && !nome.isBlank();
				
		String filtro = "";
		if (!isNomeInformado && !isCategoriaInformada) {
			throw new IllegalArgumentException(
					"Informar o nome ou categoria para listagem");
		}
		
		if (isCategoriaInformada) {
			filtro = nome + "%";
		} else {
			filtro = "%" + nome + "%";
		}
		return dao.listarPor(filtro, categoria);
	}

	public List<Restaurante> listarTodas() {
		return dao.listarTodas();
	}
}

package br.com.senai.core.service;

import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.dao.postgres.DaoPostgresCarregaConexao;

public class CarregaConexaoService {

	DaoPostgresCarregaConexao dao;
	
	public CarregaConexaoService() {
		dao = FactoryDao.getInstance().getDaoConexao();
	}
}

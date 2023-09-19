package br.com.senai.core.dao.postgres;

import br.com.senai.core.dao.ManagerDb;

public class DaoPostgresCarregaConexao {
	
	public DaoPostgresCarregaConexao() {
		ManagerDb.getInstance().getConexao();
	}

}

package br.com.senai.view.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CarregarPropriedades {

	private static final String CONFIG_FILE = "config.properties";
	
	private static Properties properties;
	
	static {
		try (InputStream input = CarregarPropriedades.class.getClassLoader().getResourceAsStream(CONFIG_FILE)){
			properties = new Properties();
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Ocorreu um erro ao carregar os valores da properties. Motivo: " + e.getMessage());
		}
	}
	
	public static String getPropertyOf(String value) {
		return properties.getProperty(value);
	}
}

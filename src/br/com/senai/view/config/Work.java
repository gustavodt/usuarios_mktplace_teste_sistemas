package br.com.senai.view.config;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

public class Work {
	

	public Work() {
		
	}
	
	public static <T> void chamarAssincrono(Callable<T> chamada, Consumer<T> consome) {
		SwingWorker<T, Void> worker = new SwingWorker<T, Void>(){

			@Override
			protected T doInBackground() throws Exception {
				if (chamada != null) {
					return chamada.call();
				} else {
					return null;
				}
			}
			
			@Override
			protected void done() {
				try {
					T t = get();
					consome.accept(t);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}			
		};
		worker.execute();
	}
}

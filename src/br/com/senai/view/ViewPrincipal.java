package br.com.senai.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.service.CarregaConexaoService;
import br.com.senai.view.categoria.ViewConsultaCategoria;
import br.com.senai.view.config.Work;
import br.com.senai.view.horario.ViewCadastroHorario;
import br.com.senai.view.restaurante.ViewConsultaRestaurante;

public class ViewPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;

	private JMenu menuConfiguracoes;
	private JMenu menuCadastros;
	JProgressBar progressBar;

	/**
	 * Create the frame.
	 */
	public ViewPrincipal() {

		setResizable(false);
		setTitle("Tela Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 649, 378);
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		progressBar = new JProgressBar(0,100);
		progressBar.setBounds(50, 150, 500, 50);
		progressBar.setIndeterminate(true);
		contentPane.add(progressBar, BorderLayout.SOUTH);
		
		JMenuBar barraPrincipal = new JMenuBar();
		barraPrincipal.setBounds(0, 0, 784, 22);
		contentPane.add(barraPrincipal);
		
		menuCadastros = new JMenu("Cadastros");
		barraPrincipal.add(menuCadastros);
		
		JMenuItem opcaoCategorias = new JMenuItem("Categorias");		
		opcaoCategorias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
   
				ViewConsultaCategoria view = new ViewConsultaCategoria();
				view.setVisible(true);		
			}
		});
		menuCadastros.add(opcaoCategorias);
		
		JMenuItem opcaoRestaurantes = new JMenuItem("Restaurantes");
		opcaoRestaurantes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewConsultaRestaurante view = new ViewConsultaRestaurante();
				view.setVisible(true);
			}
		});
		menuCadastros.add(opcaoRestaurantes);
		
		menuConfiguracoes = new JMenu("Configurações");
		barraPrincipal.add(menuConfiguracoes);
		
		JMenuItem opcaoHorarios = new JMenuItem("Horários");		
		opcaoHorarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewCadastroHorario view = new ViewCadastroHorario();
				view.setVisible(true);
			}
		});
		menuConfiguracoes.add(opcaoHorarios);
		
		JMenuItem opcaoSair = new JMenuItem("Sair");
		opcaoSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		barraPrincipal.add(opcaoSair);
		
		Work.chamarAssincrono(() -> {
			desabilitarCampos(false);
			return new CarregaConexaoService();
		}, (T) -> {
			desabilitarCampos(true);
			contentPane.remove(progressBar);
			contentPane.updateUI();
		});
	}
	
	private void desabilitarCampos(boolean valor) {
		this.menuCadastros.setEnabled(valor);
		this.menuConfiguracoes.setEnabled(valor);
	}
}

package br.com.senai.view.categoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.service.CategoriaService;

public class ViewCadastroCategoria extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField edtNome;

	private CategoriaService service;
	private Categoria categoria;

	/**
	 * Create the frame.
	 */
	public ViewCadastroCategoria() {
		this.service = new CategoriaService();
		setResizable(false);
		setTitle("Gerenciar Categoria - Cadastro");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 639, 153);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewConsultaCategoria view = new ViewConsultaCategoria();
				view.setVisible(true);
				dispose();
			}
		});
		btnPesquisar.setBounds(512, 11, 101, 23);
		contentPane.add(btnPesquisar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edtNome.setText("");
				categoria = null;
			}
		});
		btnCancelar.setBounds(524, 76, 89, 23);
		contentPane.add(btnCancelar);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = edtNome.getText();
					if (nome.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Nome é obrigatório!");
					} else {
						if (categoria == null) {
							categoria = new Categoria(nome);
							service.salvar(categoria);
							JOptionPane.showMessageDialog(contentPane, "Categoria salva com sucesso!");
							categoria = null;
						} else {
							categoria.setNome(nome);
							service.salvar(categoria);
							JOptionPane.showMessageDialog(contentPane, "Categoria alterada com sucesso!");
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnSalvar.setBounds(425, 76, 89, 23);
		contentPane.add(btnSalvar);

		edtNome = new JTextField();
		edtNome.setBounds(54, 45, 559, 20);
		contentPane.add(edtNome);
		edtNome.setColumns(10);

		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(10, 48, 46, 14);
		contentPane.add(lblNome);
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
		this.edtNome.setText(categoria.getNome());
	}
}

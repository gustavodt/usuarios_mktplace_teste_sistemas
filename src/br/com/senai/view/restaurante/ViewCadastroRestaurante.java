package br.com.senai.view.restaurante;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;

public class ViewCadastroRestaurante extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField edtNome;
	private JTextField edtLogradouro;
	private JTextField edtCidade;
	private JTextField edtBairro;
	private JTextField edtComplemento;
	private JTextArea taDescricao;
	private RestauranteService restauranteService;
	private CategoriaService categoriaService;
	private JComboBox<Categoria> cbCategoria;
	private Restaurante restaurante;

	public void carregarComboCategoria() {
		cbCategoria.addItem(null);
		List<Categoria> categorias = categoriaService.listarTodas();
		for (Categoria ca : categorias) {
			cbCategoria.addItem(ca);
		}
	}

	/**
	 * Create the frame.
	 */
	public ViewCadastroRestaurante() {
		this.restauranteService = new RestauranteService();
		setTitle("Gerenciar Restaurante - Cadastro");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 625, 385);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewConsultaRestaurante view = new ViewConsultaRestaurante();
				view.setVisible(true);
				dispose();
			}
		});
		btnPesquisar.setBounds(492, 11, 107, 23);
		contentPane.add(btnPesquisar);

		cbCategoria = new JComboBox<Categoria>();
		cbCategoria.setBounds(401, 45, 198, 22);
		contentPane.add(cbCategoria);

		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setBounds(328, 49, 63, 14);
		contentPane.add(lblCategoria);

		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(38, 49, 46, 14);
		contentPane.add(lblNome);

		edtNome = new JTextField();
		edtNome.setBounds(76, 46, 242, 20);
		contentPane.add(edtNome);
		edtNome.setColumns(10);

		JLabel lblDescrição = new JLabel("Descrição");
		lblDescrição.setBounds(10, 83, 67, 14);
		contentPane.add(lblDescrição);

		taDescricao = new JTextArea();
		taDescricao.setBounds(86, 78, 513, 94);
		contentPane.add(taDescricao);

		JLabel lblLogradouro = new JLabel("Logradouro");
		lblLogradouro.setBounds(10, 191, 74, 14);
		contentPane.add(lblLogradouro);

		edtLogradouro = new JTextField();
		edtLogradouro.setBounds(96, 188, 503, 20);
		contentPane.add(edtLogradouro);
		edtLogradouro.setColumns(10);

		JLabel lblCidade = new JLabel("Cidade");
		lblCidade.setBounds(25, 230, 46, 14);
		contentPane.add(lblCidade);

		edtCidade = new JTextField();
		edtCidade.setBounds(75, 227, 255, 20);
		contentPane.add(edtCidade);
		edtCidade.setColumns(10);

		JLabel lblBairro = new JLabel("Bairro");
		lblBairro.setBounds(340, 230, 51, 14);
		contentPane.add(lblBairro);

		edtBairro = new JTextField();
		edtBairro.setBounds(376, 227, 223, 20);
		contentPane.add(edtBairro);
		edtBairro.setColumns(10);

		JLabel lblComplemento = new JLabel("Complemento");
		lblComplemento.setBounds(0, 265, 84, 14);
		contentPane.add(lblComplemento);

		edtComplemento = new JTextField();
		edtComplemento.setBounds(85, 262, 514, 20);
		contentPane.add(edtComplemento);
		edtComplemento.setColumns(10);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});
		btnCancelar.setBounds(510, 300, 89, 23);
		contentPane.add(btnCancelar);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = edtNome.getText();
					String bairro = edtBairro.getText();
					String logradouro = edtLogradouro.getText();
					String cidade = edtCidade.getText();
					String complemento = edtComplemento.getText();
					String detalhamento = taDescricao.getText();
					Categoria categoria = (Categoria) cbCategoria.getSelectedItem();

					if (nome.isEmpty() || bairro.isEmpty() || logradouro.isEmpty() || cidade.isEmpty() || detalhamento.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Todos os campos são obrigatórios!");
					} else {
						if (restaurante == null) {
			                restaurante = new Restaurante(nome, detalhamento, new Endereco(cidade, logradouro, bairro, complemento), categoria);
							restauranteService.salvar(restaurante);
							JOptionPane.showMessageDialog(contentPane, "Restaurante inserido com sucesso!");
							clearFields();
							restaurante = null;

						} else {
							restaurante.setNome(nome);
							restaurante.setDescricao(detalhamento);
							restaurante.setEndereco(new Endereco(cidade, logradouro, bairro, complemento));
							restaurante.setCategoria(categoria);
							restauranteService.salvar(restaurante); 
							JOptionPane.showMessageDialog(contentPane, "Restaurante alterado com sucesso!");
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
					if (restaurante.getId() <= 0) {
						restaurante = null;
					}
				}
			}
		});
		btnSalvar.setBounds(411, 300, 89, 23);
		contentPane.add(btnSalvar);
		this.categoriaService = new CategoriaService();
		this.carregarComboCategoria();
	}

	public void clearFields() {
		edtNome.setText("");
		edtBairro.setText("");
		edtLogradouro.setText("");
		edtCidade.setText("");
		edtComplemento.setText("");
		taDescricao.setText("");
		cbCategoria.setSelectedIndex(0);
	}
	
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
		this.edtNome.setText(restaurante.getNome());
		this.taDescricao.setText(restaurante.getDescricao());
		this.edtBairro.setText(restaurante.getEndereco().getBairro());
		this.edtLogradouro.setText(restaurante.getEndereco().getLogradouro());
		this.edtCidade.setText(restaurante.getEndereco().getCidade());
		this.edtComplemento.setText(restaurante.getEndereco().getComplemento());
		this.cbCategoria.setSelectedItem(restaurante.getCategoria());
	}
}

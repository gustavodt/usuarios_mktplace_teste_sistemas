package br.com.senai.view.restaurante;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.view.componentes.table.RestauranteTableModel;

public class ViewConsultaRestaurante extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField edtFiltro;
	private JTable tableRestaurante;
	
	private RestauranteService restauranteService;
	private CategoriaService categoriaService;
	private JComboBox<Categoria> cbCategoria;
	
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
	public ViewConsultaRestaurante() {
		this.restauranteService = new RestauranteService();
		RestauranteTableModel model = new RestauranteTableModel(new ArrayList<Restaurante>());
		setTitle("Gerenciar Restaurante - Listagem");
		this.tableRestaurante = new JTable(model);
		tableRestaurante.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroRestaurante view = new ViewCadastroRestaurante();
				view.setVisible(true);
				dispose();
			}
		});
		btnNovo.setBounds(585, 11, 89, 23);
		contentPane.add(btnNovo);
		
		JButton btnListar = new JButton("Listar");
		btnListar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String filtroInformado = edtFiltro.getText().toUpperCase();
					Categoria categoriaInformada = (Categoria) cbCategoria.getSelectedItem();
					List<Restaurante> restaurantesEncontrados = restauranteService.listarPor(filtroInformado, categoriaInformada);
					if (restaurantesEncontrados.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane,
								"Não foi encontrado nenhum restaurante");
					} else {
						RestauranteTableModel model = new RestauranteTableModel(restaurantesEncontrados);
						tableRestaurante.setModel(model);
						tamanhoColuna();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnListar.setBounds(585, 45, 89, 23);
		contentPane.add(btnListar);
		
		JLabel lblFiltros = new JLabel("Filtros");
		lblFiltros.setBounds(10, 15, 46, 14);
		contentPane.add(lblFiltros);
		
		cbCategoria = new JComboBox<Categoria>();
		cbCategoria.setBounds(424, 45, 151, 22);
		contentPane.add(cbCategoria);
		
		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setBounds(356, 49, 70, 14);
		contentPane.add(lblCategoria);
		
		edtFiltro = new JTextField();
		edtFiltro.setBounds(57, 46, 289, 20);
		contentPane.add(edtFiltro);
		edtFiltro.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(20, 49, 46, 14);
		contentPane.add(lblNome);
		
		JLabel lblRestaurantesEncontrados = new JLabel("Restaurantes Encontrados");
		lblRestaurantesEncontrados.setBounds(10, 77, 172, 14);
		contentPane.add(lblRestaurantesEncontrados);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Ações", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(480, 328, 200, 45);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int linhaSelecionada = tableRestaurante.getSelectedRow();
				RestauranteTableModel model = (RestauranteTableModel) tableRestaurante.getModel();
				if (linhaSelecionada >= 0 && !model.isVazio()) {
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente remover!?", "Remoção",
							JOptionPane.YES_NO_OPTION);
					if (opcao == 0) {

						Restaurante restauranteSelecionado = model.getPor(linhaSelecionada);
						try {
							restauranteService.removerPor(restauranteSelecionado.getId());
							model.removerPor(linhaSelecionada);
							tableRestaurante.updateUI();
							JOptionPane.showMessageDialog(contentPane, "Restaurante removido com sucesso!");
						} catch (IndexOutOfBoundsException iobe) {
							JOptionPane.showMessageDialog(contentPane, iobe.getMessage());
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(contentPane, ex.getMessage());
						}
					}
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para remoção.");
				}
			}
		});
		btnExcluir.setBounds(105, 16, 89, 23);
		panel.add(btnExcluir);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableRestaurante.getSelectedRow();
				if (linhaSelecionada >= 0) {
					RestauranteTableModel model = (RestauranteTableModel) tableRestaurante.getModel();
					Restaurante restauranteSelecionado = model.getPor(linhaSelecionada);
					ViewCadastroRestaurante view = new ViewCadastroRestaurante();
					view.setRestaurante(restauranteSelecionado);
					view.setVisible(true);
					dispose();

				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição.");
				}
			}
		});
		btnEditar.setBounds(6, 16, 89, 23);
		panel.add(btnEditar);
		
		JScrollPane scrollPane = new JScrollPane(tableRestaurante);
		tamanhoColuna();
		scrollPane.setBounds(10, 114, 664, 203);
		contentPane.add(scrollPane);
		
		this.categoriaService = new CategoriaService();
		this.carregarComboCategoria();
	}
	
	public void tamanhoColuna() {
		tableRestaurante.getColumnModel().getColumn(1).setPreferredWidth(500);
	}

}

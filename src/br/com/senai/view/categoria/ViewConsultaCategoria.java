package br.com.senai.view.categoria;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.view.componentes.table.CategoriaTableModel;

public class ViewConsultaCategoria extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField edtFiltro;
	private JTable tableCategoria;

	private CategoriaService service;

	/**
	 * Create the frame.
	 */
	public ViewConsultaCategoria() {
		this.service = new CategoriaService();
		CategoriaTableModel model = new CategoriaTableModel(new ArrayList<Categoria>());
		setTitle("Gerenciar Categoria - Listagem");
		this.tableCategoria = new JTable(model);
		tableCategoria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 703, 424);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblFiltros = new JLabel("Filtros");
		lblFiltros.setBounds(10, 11, 46, 14);
		contentPane.add(lblFiltros);

		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(52, 45, 46, 14);
		contentPane.add(lblNome);

		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroCategoria view = new ViewCadastroCategoria();
				view.setVisible(true);
				dispose();
			}
		});
		btnNovo.setBounds(588, 7, 89, 23);
		contentPane.add(btnNovo);

		JButton btnListar = new JButton("Listar");
		btnListar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String filtroInformado = edtFiltro.getText().toUpperCase();
					List<Categoria> categoriasEncontrados = service.listarPor(filtroInformado);
					if (categoriasEncontrados.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane,
								"Não foi encontrado nenhuma categoria com " + " esse nome.");
					} else {
						CategoriaTableModel model = new CategoriaTableModel(categoriasEncontrados);
						tableCategoria.setModel(model);
						configurarTabela();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnListar.setBounds(588, 41, 89, 23);
		contentPane.add(btnListar);

		edtFiltro = new JTextField();
		edtFiltro.setBounds(108, 42, 470, 20);
		contentPane.add(edtFiltro);
		edtFiltro.setColumns(10);

		JLabel lblCategoriasEncontradas = new JLabel("Categorias Encontradas");
		lblCategoriasEncontradas.setBounds(10, 84, 147, 16);
		contentPane.add(lblCategoriasEncontradas);

		JPanel panelAcoes = new JPanel();
		panelAcoes.setToolTipText("");
		panelAcoes.setBorder(new TitledBorder(null, "Ações", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAcoes.setBounds(466, 330, 216, 49);
		contentPane.add(panelAcoes);
		panelAcoes.setLayout(null);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableCategoria.getSelectedRow();
				CategoriaTableModel model = (CategoriaTableModel) tableCategoria.getModel();
				if (linhaSelecionada >= 0 && !model.isVazio()) {
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente remover!?", "Remoção",
							JOptionPane.YES_NO_OPTION);
					if (opcao == 0) {

						Categoria categoriaSelecionado = model.getPor(linhaSelecionada);
						try {
							service.removerPor(categoriaSelecionado.getId());
							model.removerPor(linhaSelecionada);
							tableCategoria.updateUI();
							JOptionPane.showMessageDialog(contentPane, "Categoria removida com sucesso!");
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
		btnExcluir.setBounds(113, 18, 98, 26);
		panelAcoes.add(btnExcluir);

		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableCategoria.getSelectedRow();
				if (linhaSelecionada >= 0) {
					CategoriaTableModel model = (CategoriaTableModel) tableCategoria.getModel();
					Categoria categoriaSelecionado = model.getPor(linhaSelecionada);
					ViewCadastroCategoria view = new ViewCadastroCategoria();
					view.setCategoria(categoriaSelecionado);
					view.setVisible(true);
					dispose();

				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição.");
				}
			}
		});
		btnEditar.setBounds(5, 18, 98, 26);
		panelAcoes.add(btnEditar);

		JScrollPane scrollPane = new JScrollPane(tableCategoria);
		scrollPane.setBounds(12, 114, 665, 204);
		contentPane.add(scrollPane);
	}
	
	private void configurarColuna(int indice, int largura) {
		this.tableCategoria.getColumnModel().getColumn(indice).setResizable(true);
		this.tableCategoria.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	
	private void configurarTabela() {
		final int COLUNA_ID = 0;
		final int COLUNA_NOME = 1;
		this.tableCategoria.getTableHeader().setReorderingAllowed(false);
		this.configurarColuna(COLUNA_ID, 90);
		this.configurarColuna(COLUNA_NOME, 250);
	}
}

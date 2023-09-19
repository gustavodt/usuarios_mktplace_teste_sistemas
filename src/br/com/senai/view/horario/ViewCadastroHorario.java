package br.com.senai.view.horario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import br.com.senai.core.domain.Horario;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.HorarioService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.view.componentes.table.HorarioTableModel;
import br.com.senai.view.config.Utilitario;
import br.com.senai.view.config.Work;

public class ViewCadastroHorario extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableHorario;
	private RestauranteService restauranteService;
	private HorarioService horarioService;
	private JComboBox<String> cbDiaDaSemana;
	private JComboBox<Restaurante> cbRestaurante;
	private Horario horario;
	private JFormattedTextField txtAbertura;
	private JFormattedTextField txtFechamento;
	private Utilitario util;
	boolean isEditando = false;
	

	public void carregarComboRestaurante() {
		this.horarioService = new HorarioService(); 
		cbRestaurante = new JComboBox<Restaurante>();
		List<Restaurante> restaurantes = restauranteService.listarTodas();
		carregarValoresComboRestaurante(restaurantes);
		cbRestaurante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Restaurante restauranteInformado = (Restaurante) cbRestaurante.getSelectedItem();
				try {
					if (restauranteInformado != null) {
						mostrarLista(restauranteInformado);
						limparCampos();
					} else {

						HorarioTableModel model = new HorarioTableModel(new ArrayList<Horario>());
						tableHorario.setModel(model);
					}
				} catch (Exception ex) {
					throw new IllegalArgumentException(
							"Ocorreu um erro na listagem dos horarios. Motivo:" + ex.getMessage());
				}
			}
		});
	}

	private void setHorario(Horario horario) {
		this.horario = horario;
		this.txtAbertura.setText(horario.getHoraAbertura().toString());
		this.txtFechamento.setText(horario.getHoraFechamento().toString());
		this.cbDiaDaSemana.setSelectedItem(horario.getDiaDaSemana());
	}

	/**
	 * Create the frame.
	 */
	public ViewCadastroHorario() {
		util = new Utilitario();
		this.restauranteService = new RestauranteService();
		setTitle("Gerenciar Horários - Cadastro");
		HorarioTableModel model = new HorarioTableModel(new ArrayList<Horario>());
		this.tableHorario = new JTable(model);
		this.tableHorario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 673, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));	

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblRest = new JLabel("Restaurante");
		lblRest.setBounds(10, 27, 80, 14);
		contentPane.add(lblRest);
		
		carregarComboRestaurante();
		cbRestaurante.setBounds(100, 23, 547, 22);
		contentPane.add(cbRestaurante);
		
		JLabel lblDiaSemana = new JLabel("Dia da Semana");
		lblDiaSemana.setHorizontalAlignment(SwingConstants.LEFT);
		lblDiaSemana.setBounds(10, 80, 90, 14);
		contentPane.add(lblDiaSemana);
		
		carregarComboDiaSemana();
		cbDiaDaSemana.setBounds(100, 76, 111, 22);
		contentPane.add(cbDiaDaSemana);
		
		JLabel lblAbertura = new JLabel("Abertura");
		lblAbertura.setHorizontalAlignment(SwingConstants.LEFT);
		lblAbertura.setBounds(221, 80, 52, 14);
		contentPane.add(lblAbertura);
		
		JLabel lblFechamento = new JLabel("Fechamento");
		lblFechamento.setHorizontalAlignment(SwingConstants.LEFT);
		lblFechamento.setBounds(372, 80, 72, 14);
		contentPane.add(lblFechamento);
		
		try {
			MaskFormatter maskFormatterAbre = new MaskFormatter("##:##");
			txtAbertura = new JFormattedTextField(maskFormatterAbre);
			txtAbertura.setBounds(282, 77, 80, 20);
			contentPane.add(txtAbertura);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			MaskFormatter maskFormatterFecha = new MaskFormatter("##:##");
			txtFechamento = new JFormattedTextField(maskFormatterFecha);
			txtFechamento.setBounds(454, 77, 83, 20);
			contentPane.add(txtFechamento);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		txtFechamento.addFocusListener((FocusListener) new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            	txtFechamento.setCaretPosition(0);
            }
        });
		
		txtAbertura.addFocusListener((FocusListener) new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            	txtAbertura.setCaretPosition(0);
            }
        });

		JButton btnAdic = new JButton("Adicionar");
		btnAdic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {	
					
					DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
					
					LocalTime horaInicial = LocalTime.parse(txtAbertura.getText().formatted(horaFormatter)); 
					LocalTime horaFinal = LocalTime.parse(txtFechamento.getText().formatted(horaFormatter));
					String diaDaSemana = (String) cbDiaDaSemana.getSelectedItem();
					Restaurante restaurante = (Restaurante) cbRestaurante.getSelectedItem();
	
					if (horario == null) {
						horario = new Horario(diaDaSemana, horaInicial, horaFinal, restaurante);
						horarioService.salvar(horario);
						JOptionPane.showMessageDialog(contentPane, "Horário inserido com sucesso!");
						limparCampos();
						mostrarLista(restaurante);
						horario = null;

					} else {
						
						isEditando = false;
						horario.setDiaDaSemana(diaDaSemana);
						horario.setHoraAbertura(horaInicial);
						horario.setHoraFechamento(horaFinal);
						horario.setRestaurante(restaurante);
						horarioService.salvar(horario);
						mostrarLista(restaurante);

						JOptionPane.showMessageDialog(contentPane, "Horário alterado com sucesso!");
						
						Work.chamarAssincrono(() -> {
							return horarioService.listarPorId(restaurante.getId());
						}, (List<Horario> horarios) -> {
							String tabela = criarTabela(horarios);
							try {
								util.enviarEmail(tabela, horario.getRestaurante().getNome());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						});
					}
				} catch (DateTimeParseException e2) {
					JOptionPane.showMessageDialog(contentPane, "O horário é obrigatório e precisa estar no formato 24h");
				} catch (NullPointerException e3) {
					JOptionPane.showMessageDialog(contentPane, "Todos os campos são obrigatórios");
				} catch (Exception e3) {
					JOptionPane.showMessageDialog(contentPane, e3.getMessage());
				}
			}
		});
		btnAdic.setBounds(558, 76, 89, 23);
		contentPane.add(btnAdic);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {

	public void actionPerformed(ActionEvent e) {
		limparTodosOsCampos();
			}
		});
		btnCancelar.setBounds(499, 319, 101, 22);
		contentPane.add(btnCancelar);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Ações", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(454, 145, 179, 110);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isEditando = true;
				
				int linhaSelecionada = tableHorario.getSelectedRow();
				
				if (linhaSelecionada >= 0) {
					
					HorarioTableModel model = (HorarioTableModel) tableHorario.getModel();
					Horario horarioSelecionado = model.getPorLinha(linhaSelecionada);
					setHorario(horarioSelecionado);
					
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição");
				}
			}
		});
		btnEditar.setBounds(10, 20, 159, 34);
		panel.add(btnEditar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (isEditando == true) {
					JOptionPane.showMessageDialog(contentPane, "Termine ou cancele a edição para poder excluir!");
				} else {
					int linhaSelecionada = tableHorario.getSelectedRow();
					HorarioTableModel model = (HorarioTableModel) tableHorario.getModel();
					if (linhaSelecionada >= 0 && !model.isVazio()) {
						int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente remover!?", "Remoção",
								JOptionPane.YES_NO_OPTION);
						if (opcao == 0) {

							Horario horarioSelecionado = model.getPorLinha(linhaSelecionada);
							try {
								horarioService.removerPor(horarioSelecionado.getId());
								model.removerPor(linhaSelecionada);
								mostrarLista(horarioSelecionado.getRestaurante());
								JOptionPane.showMessageDialog(contentPane, "Horário removido com sucesso!");
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
			}
		});
		btnExcluir.setBounds(10, 65, 159, 34);
		panel.add(btnExcluir);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 138, 415, 182);
		contentPane.add(scrollPane);
		
		scrollPane.setViewportView(tableHorario);
		
		JLabel lblHor = new JLabel("Horários");
		lblHor.setBounds(10, 117, 52, 14);
		contentPane.add(lblHor);
		
	}
	
	private void carregarValoresComboRestaurante(List<Restaurante> restaurantes) {
		cbRestaurante.addItem(null);
		for (Restaurante ca : restaurantes) {
			cbRestaurante.addItem(ca);
		}
	}

	public void carregarComboDiaSemana() {
		cbDiaDaSemana = new JComboBox<>();
		cbDiaDaSemana.addItem(null);

		String[] diasDaSemana = { "DOMINGO", "SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO" };
		for (String dia : diasDaSemana) {
			cbDiaDaSemana.addItem(dia);
		}
	}

	private void limparCampos() {
		this.horario = null;
		txtAbertura.setText("");
		txtFechamento.setText("");
		cbDiaDaSemana.setSelectedIndex(0);
	}
	
	private void limparTodosOsCampos() {
		limparCampos();
		cbRestaurante.setSelectedIndex(0);
	}

	private void mostrarLista(Restaurante restauranteInformado) {
	                
        List<Horario> horariosEncontrados = horarioService.listarPorId(restauranteInformado.getId());

		HorarioTableModel model = new HorarioTableModel(horariosEncontrados);
		tableHorario.setModel(model);
	}
	
	private static String criarTabela(List<Horario> horarios) {
		StringBuilder builder = new StringBuilder();
		builder.append("<table style='width:30%; border-collapse:collapse; text-align:center; border:1px solid black; '>");
		builder.append("<tr><th style='background-color:green; color:white; border:1px; border:1px solid black; '>Dia da Semana</th>");
		builder.append("<th style='background-color:green; color:white; border:1px; border:1px solid black; '>Hora inicial</th>");
		builder.append("<th style='background-color:green; color:white; border:1px; border:1px solid black; '>Hora Final</th></tr>");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");		
		
		String validacao = null;
		
		for (Horario horario : horarios) {
			builder.append("<tr>");
			
			if (validacao == null || !validacao.equals(horario.getDiaDaSemana())) {
				int quantidadeRowspan = contarLinhasDoMesmoDia(horarios, horario.getDiaDaSemana());
				builder
				.append("<td style='border:1px solid black;' rowspan=" + quantidadeRowspan + " >")
				.append(horario.getDiaDaSemana())
				.append("</td>");
				
				validacao = horario.getDiaDaSemana();
			}
			builder.append("<td style='border:1px solid black; '>" + horario.getHoraAbertura().format(formatter) + "</td>");
			builder.append("<td style='border:1px solid black; '>" + horario.getHoraFechamento().format(formatter) + "</td>");
			builder.append("</tr>");
		}
		builder.append("</table>");
		
		return builder.toString();
	}
	
	private static int contarLinhasDoMesmoDia(List<Horario> horarios, String diaDaSemana) {		
		int contador = 0;
		for (Horario horario : horarios) {
			if (horario.getDiaDaSemana().equals(diaDaSemana)) {
				contador += 1;
			}
		}
		return contador;
	}
}

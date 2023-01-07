package com.mvc.comercialplus.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.MaskFormatter;

import org.apache.logging.log4j.message.MapMessage.MapFormat;

import com.mvc.comercialplus.GerenciamentoComercialSpringApplication.MenuPrincipal;
import com.mvc.comercialplus.config.Configuracao;
import com.mvc.comercialplus.controller.CaixaController;
import com.mvc.comercialplus.model.Cliente;
import com.mvc.comercialplus.model.FormaPagamento;
import com.mvc.comercialplus.service.ClienteService;
import com.mvc.comercialplus.service.VendaService;

@SuppressWarnings("serial")
public class JanelaPagamento extends JDialog{
	
	private JTextField campoValorTotal;
	private JFormattedTextField campoDesconto;
	
	private final BigDecimal valorProdutos;
	private BigDecimal valorFinal;
	private BigDecimal valorTaxaCartao;
	private BigDecimal valorTaxaParcelamento;
	
	private JInternalFrame internalFrame;
	
	private JDesktopPane desktopPane;
	
	private JButton btFinalizar;
	private JButton btSelCliente;
	private JButton btCadCliente;
	
	private Cliente cliente;
	
	private CaixaController caixaController;
	
	public JanelaPagamento(MenuPrincipal pai, BigDecimal valorProdutos, 
			CaixaController caixaController) {
		
		this.caixaController = caixaController;
		
		this.valorProdutos = valorProdutos;
		
		setarValoresPagamento();
		
		this.setSize(new Dimension(1100,730));
		this.setTitle("Janela pagamento");
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(pai);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		adicionarComponentes(pai);
		cliente = null;
		//this.pack();
	}
	
	private void setarValoresPagamento() {
		//inicialmente se presume que a venda sera em dinheiro
		//logo nao havera taxas e o valor final antes de qualquer
		//desconto (opcional)seria igual ao valor dos produtos.
		valorFinal = valorProdutos;
		valorTaxaCartao = BigDecimal.ZERO.setScale(2);
		valorTaxaParcelamento = BigDecimal.ZERO.setScale(2);
	}
	
	
	private void adicionarComponentes(MenuPrincipal pai) {
		/*PARTE SUPERIOR DA JANELA*/
		var radioDinheiro = new JRadioButton("DINHEIRO");
		radioDinheiro.addActionListener(evento -> {
			if(internalFrame != null) {
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
			}
			setarValoresPagamento();
			internalFrame = criarFrameInternoDinheiro();
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);
			
		});
		var radioPix = new JRadioButton("PIX");
		radioPix.addActionListener(eventoClique -> {
			if(internalFrame != null) {
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
			}
			setarValoresPagamento();
			internalFrame = criarFrameInternoPix();
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);	
		});
		var radioQRPix = new JRadioButton("QR CODE PIX");
		radioQRPix.addActionListener(eventoMarcacao -> {
			if(internalFrame != null) {
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
			}
			setarValoresPagamento();
			internalFrame = criarFrameInternoQRPix();
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);	
		});
		var radioVale = new JRadioButton("CARTÃO VALE ALIMENTAÇÃO");
		radioVale.addItemListener(eventoMarcado -> {
			if(internalFrame != null) {
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
			}
			setarValoresPagamento();
			internalFrame = criarFrameInternoCartao(FormaPagamento.CARTAO_VALE);
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);	
		});
		var radioDebito = new JRadioButton("CARTÃO DE DÉBITO");
		radioDebito.addItemListener(eventoMarcado -> {
			if(internalFrame != null) {
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
			}
			setarValoresPagamento();
			internalFrame = criarFrameInternoCartao(FormaPagamento.CARTAO_DEBITO);
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);	
		});
		var radioCredito = new JRadioButton("CARTÃO DE CRÉDITO");
		radioCredito.addItemListener(evento -> {
			if(internalFrame != null) {
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
			}
			setarValoresPagamento();
			internalFrame = criarFrameInternoCartao(FormaPagamento.CARTAO_CREDITO);
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);	
		});
		var radioPendura = new JRadioButton("FIADO");
		radioPendura.addActionListener(eventoMarcacao -> {
			if(internalFrame != null) {
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
			}
			setarValoresPagamento();
			internalFrame = criarFrameInternoFiado(null);
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);
		});
		var btGrupo = new ButtonGroup();
		//adicionando ao grupo para que apenas um fique selecionado
		//por vez
		btGrupo.add(radioDinheiro);
		btGrupo.add(radioPix);
		btGrupo.add(radioQRPix);
		btGrupo.add(radioVale);
		btGrupo.add(radioDebito);
		btGrupo.add(radioCredito);
		btGrupo.add(radioPendura);
		
		var lbEscolha = new JLabel("Escolha a forma de pagamento");
		lbEscolha.setFont(lbEscolha.getFont().deriveFont(30f).deriveFont(Font.BOLD));
		
		var pBotoesRadio = new JPanel(new GridLayout(4,2));
		pBotoesRadio.add(radioDinheiro);
		pBotoesRadio.add(radioDebito);
		pBotoesRadio.add(radioPix);
		pBotoesRadio.add(radioVale);
		pBotoesRadio.add(radioQRPix);
		pBotoesRadio.add(radioCredito);
		pBotoesRadio.add(radioPendura);
		
		/*Selecionando dinheiro por padrao*/
		radioDinheiro.setSelected(true);
		
		JPanel pSuperior = new JPanel(new WrapLayout(FlowLayout.LEFT));
		
		pSuperior.add(lbEscolha);
		pSuperior.add(pBotoesRadio);
		
		/*LATERAL DIREITA DA JANELA*/
		
		/*PARTE CENTRAL DA JANELA*/
		desktopPane = new JDesktopPane();
		internalFrame = criarFrameInternoDinheiro();
		desktopPane.add(internalFrame);
		//desktopPane.getDesktopManager();
		internalFrame.setVisible(true);
		
		/*PARTE INFERIOR DA JANELA*/
		var lbValorTotal = new JLabel("VALOR TOTAL:");
		lbValorTotal.setFont(lbValorTotal.getFont().deriveFont(38f).deriveFont(Font.BOLD));
		campoValorTotal = new JTextField();
		campoValorTotal.setFont(campoValorTotal.getFont().deriveFont(38f).deriveFont(Font.BOLD));
		campoValorTotal.setPreferredSize(new Dimension(200,55));
		campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
		
		var btCancelar = new JButton("CANCELAR");
		btCancelar.setPreferredSize(new Dimension(110,55));
		btCancelar.addActionListener(clicado -> {
			pai.setVisible(true);
			this.dispose();
		});
		btFinalizar = new JButton("CONFIRMAR");
		btFinalizar.setPreferredSize(new Dimension(110,55));
		btFinalizar.addActionListener(clicado -> {
			/* TODO 
			 * tratar a venda aqui
			 * adicionar ela ao BD e
			 * imprimir comprovante*/
			pai.setVisible(true);
			this.dispose();
		});
		
		var pInferior = new JPanel(new WrapLayout());
		pInferior.add(lbValorTotal);
		pInferior.add(campoValorTotal);
		pInferior.add(btCancelar);
		pInferior.add(btFinalizar);
		
		/*ADICIONANDO COMPONENTES A JANELA*/
		this.add(pSuperior, BorderLayout.NORTH);
		this.add(desktopPane, BorderLayout.CENTER);
		this.add(pInferior, BorderLayout.SOUTH);
	}

	private ImageIcon carregarImagemQRPix(int altura, int largura) {
		byte[] imagemBytes = null;
		
		try {
			imagemBytes = Files.readAllBytes(Paths.get("./qrcode.jpeg"));
		} catch (IOException e1) {
			e1.getCause().getMessage();
		}
		var icone = new ImageIcon(imagemBytes);
		var imagemRedimensionada = icone.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
		icone.setImage(imagemRedimensionada);
		return icone;
	}
	
	private String carregarTextoChavePix() {
		byte[] textoBytes = null;
		
		try {
			textoBytes = Files.readAllBytes(Paths.get("./chavepix.txt"));
		} catch (IOException eio) {
			eio.getCause().getMessage();
		}
		
		return new String(textoBytes);
	}
	
	private JInternalFrame criarFrameInternoFiado(Cliente talvezCliente) {
		var frame = criarFrameInternoBasico();
		frame.setTitle("Adicionar valor à conta do cliente - Fiado");
		frame.setSize(640,530);
		//deixar pra depois
		var boxDinheiro = new JCheckBox("Pagar parte da compra no dinheiro?");
		
		var pCentral = new JPanel(new WrapLayout(FlowLayout.LEFT));
		pCentral.setSize(new Dimension(300,600));

		btSelCliente = new JButton("Selecionar");
		btCadCliente = new JButton("Cadastrar");
		
		btSelCliente.addActionListener(eventoClique -> {
			var frameSel = criarFrameInternoSelecionarCliente(frame.getWidth());
			frameSel.setVisible(true);
			btCadCliente.setEnabled(false);
			btSelCliente.setEnabled(false);
		});
		btCadCliente.addActionListener(eventoClique -> {
			var frameCad = criarFrameInternoCadastrarCliente(frame.getWidth());
			frameCad.setVisible(true);
			btCadCliente.setEnabled(false);
			btSelCliente.setEnabled(false);
		});
		
		if(Optional.ofNullable(talvezCliente).isPresent()) {
			var pInfo = new JPanel(new WrapLayout(FlowLayout.LEFT));
			pInfo.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
			
			var lbInfo = new JLabel("Informações do cliente:");
			lbInfo.setFont(lbInfo.getFont().deriveFont(23f).deriveFont(Font.BOLD));
			var lbNome = new JLabel(cliente.getNome());
			lbNome.setFont(lbNome.getFont().deriveFont(23f));
			var lbEmail = new JLabel(cliente.getEmail());
			lbEmail.setFont(lbEmail.getFont().deriveFont(23f));
			var lbEndereco = new JLabel(cliente.getEndereco());
			lbEndereco.setFont(lbEndereco.getFont().deriveFont(23f));
			var lbTelefone = new JLabel(cliente.getTelefone());
			lbTelefone.setFont(lbEndereco.getFont().deriveFont(23f));
			var lbCPF = new JLabel(cliente.getCpf());
			lbCPF.setFont(lbEndereco.getFont().deriveFont(23f));
			
			pInfo.add(lbInfo);
			pInfo.add(lbNome);
			pInfo.add(lbEndereco);
			pInfo.add(lbTelefone);
			pInfo.add(lbCPF);
			pInfo.add(lbEmail);
			
			var pBotoes = new JPanel(new GridLayout(3,1));
			var lbInfoBt = new JLabel("Não é esse o cliente que procura?");
			btSelCliente.setText("Selecionar outro");
			btCadCliente.setText("Cadastrar novo");
			//lbInfoBt.setFont(lbInfoBt.getFont().deriveFont(23f));
			pBotoes.add(lbInfoBt);
			pBotoes.add(btSelCliente);
			pBotoes.add(btCadCliente);
			
			pCentral.add(pInfo);
			pCentral.add(pBotoes);
		} else {
			var lbFiado = new JLabel("<html>Escolha o cliente que<br> está fazendo a compra:");
			lbFiado.setFont(lbFiado.getFont().deriveFont(23f).deriveFont(Font.BOLD));
			
			var pBotoes = new JPanel(new GridLayout(4,1));
			var lbCadastrado = new JLabel("Cliente já cadastrado?");
			lbCadastrado.setFont(lbCadastrado.getFont().deriveFont(23f));
			lbCadastrado.setLabelFor(btSelCliente);
			var lbNovo = new JLabel("Cliente novo?");
			lbNovo.setFont(lbNovo.getFont().deriveFont(23f));
			lbNovo.setLabelFor(btCadCliente);

			pBotoes.add(lbCadastrado);
			pBotoes.add(btSelCliente);
			pBotoes.add(lbNovo);
			pBotoes.add(btCadCliente);
			
			pCentral.add(lbFiado);
			pCentral.add(pBotoes);
		}
		
		
		frame.add(pCentral, BorderLayout.WEST);
		
		return frame;
	}
	
	private JInternalFrame criarFrameInternoCadastrarCliente(int larguraFrameFiado) {
		var frame = new JInternalFrame("Cadastro de novo cliente");
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(360,520));
		frame.setResizable(true);
		frame.setLocation(larguraFrameFiado+10, 0);
		
		desktopPane.add(frame);
		
		var btFechar = new JButton("Cadastrar");
		
		var lbNome = new JLabel("Nome (obrigatório):");
		var campoNome = new JTextField();
		lbNome.setLabelFor(campoNome);
		
		var lbEmail = new JLabel("Email:");
		var campoEmail = new JTextField();
		lbEmail.setLabelFor(campoEmail);
		
		var lbEndereco = new JLabel("Endereço:");
		var campoEndereco = new JTextField();
		lbEndereco.setLabelFor(campoEndereco);
		
		var lbTelefone = new JLabel("Telefone:");
		var campoTelefone = new JFormattedTextField();
		MaskFormatter mascaraTel;
		try {
			mascaraTel = new MaskFormatter("(##)# ########");
			mascaraTel.setPlaceholder(" ");
			mascaraTel.setPlaceholderCharacter('_');
			mascaraTel.install(campoTelefone);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		lbTelefone.setLabelFor(campoTelefone);
		
		var lbCPF = new JLabel("CPF:");
		var campoCPF = new JFormattedTextField();
		MaskFormatter mascaraCPF;
		try {
			mascaraCPF = new MaskFormatter("###.###.###-##");
			//mascaraCPF.setPlaceholder(" ");
			mascaraCPF.setPlaceholderCharacter('_');
			mascaraCPF.install(campoCPF);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		lbCPF.setLabelFor(campoCPF);
		
		btFechar.addActionListener(eventoClique -> {
			
			if(!campoNome.getText().equals("")) {
				this.cliente = caixaController.salvarCliente(campoNome.getText(), campoEmail.getText(),
						campoEndereco.getText(), campoTelefone.getText(), campoCPF.getText());
				//pai.add(criarPainelInfoCliente(cliente));
				
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
				setarValoresPagamento();
				internalFrame = criarFrameInternoFiado(cliente);
				desktopPane.add(internalFrame);
				internalFrame.setVisible(true);
			}

			btCadCliente.setEnabled(true);
			btSelCliente.setEnabled(true);
			
			frame.dispose();
		});
		
		var pForm = new JPanel(new GridLayout(10,1));
		pForm.add(lbNome);
		pForm.add(campoNome);
		pForm.add(lbEmail);
		pForm.add(campoEmail);
		pForm.add(lbEndereco);
		pForm.add(campoEndereco);
		pForm.add(lbTelefone);
		pForm.add(campoTelefone);
		pForm.add(lbCPF);
		pForm.add(campoCPF);
		
		frame.add(pForm, BorderLayout.NORTH);
		frame.add(btFechar, BorderLayout.SOUTH);
		return frame;
	}
	

	
	private JInternalFrame criarFrameInternoSelecionarCliente(int larguraFrameFiado) {
		var frame = new JInternalFrame("Busca de cliente");
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(320,520));
		frame.setResizable(true);
		frame.setLocation(larguraFrameFiado+10, 0);
		
		desktopPane.add(frame);
		
		var btFechar = new JButton("Fechar");
		
		btFechar.addActionListener(eventoClique -> {
			btCadCliente.setEnabled(true);
			btSelCliente.setEnabled(true);
			frame.dispose();
		});
		
		frame.add(btFechar, BorderLayout.SOUTH);
		return frame;
	}
	
	private JInternalFrame criarFrameInternoQRPix() {
		var frame = criarFrameInternoBasico();
		frame.setTitle("Pagamento por código QR Pix");

		var lbMensagem = new JLabel(
				"<html>Após o pagamento do QRCode<br>na maquininha, clique em CONFIRMAR, abaixo.");
		lbMensagem.setFont(lbMensagem.getFont().deriveFont(45f).deriveFont(Font.BOLD));
		
		frame.add(lbMensagem, BorderLayout.CENTER);
		
		return frame;
	}
	
	private JInternalFrame criarFrameInternoPix() {
		var frame = criarFrameInternoBasico();
		frame.setTitle("Pagamento por transferência PIX");
		frame.setSize(new Dimension(900, 535));
		
		var lbQrCode = new JLabel();
		var icone = carregarImagemQRPix(320, 320);
		lbQrCode.setIcon(icone);
		
		var lbChavePix = new JLabel();
		var chavePix = carregarTextoChavePix();
		lbChavePix.setText(chavePix);
		lbChavePix.setFont(lbChavePix.getFont().deriveFont(45f).deriveFont(Font.BOLD));
		
		frame.add(lbQrCode, BorderLayout.WEST);
		frame.add(lbChavePix, BorderLayout.CENTER);
		
		return frame;
	}
	
	private JInternalFrame criarFrameInternoDinheiro() {
		var frame = criarFrameInternoBasico();
		frame.setTitle("Pagamento em dinheiro");
		frame.setSize(new Dimension(740,450));
		
		var lbDinheiro = new JLabel("Dinheiro recebido:");
		lbDinheiro.setFont(lbDinheiro.getFont().deriveFont(23f).deriveFont(Font.BOLD));
		var campoDinheiro = new JFormattedTextField();
		campoDinheiro.setFont(campoDinheiro.getFont().deriveFont(25f));
		campoDinheiro.setColumns(9);
		campoDinheiro.setFormatterFactory(new FormatadorDinheiroFactory());
		campoDinheiro.setText(Configuracao.PREFIXO_MONETARIO.get()+"0");
		
		var lbTroco = new JLabel("Troco:");
		lbTroco.setFont(lbTroco.getFont().deriveFont(23f).deriveFont(Font.BOLD));
		var campoTroco = new JTextField();
		campoTroco.setFont(campoTroco.getFont().deriveFont(25f));
		campoTroco.setColumns(10);
		campoTroco.setEditable(false);
		campoTroco.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) { btFinalizar.requestFocusInWindow(); }
			public void focusLost(FocusEvent e) {}
		});
		
		campoDinheiro.addFocusListener(new EscutadorFoco(campoDinheiro));
		campoDinheiro.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {}
			public void focusLost(FocusEvent e) {
				var dinheiroRecebido = CaixaController.converterTextoEmMonetario(campoDinheiro.getText());
				var troco = dinheiroRecebido.subtract(valorFinal);
				campoTroco.setText(CaixaController.converterMonetarioEmTexto(troco));
			}
		});
		
		campoDinheiro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					var dinheiroRecebido = CaixaController.converterTextoEmMonetario(campoDinheiro.getText());
					var troco = dinheiroRecebido.subtract(valorFinal);
					campoTroco.setText(CaixaController.converterMonetarioEmTexto(troco));
					btFinalizar.requestFocusInWindow();
				}
			}
		});
		
		var pDinheiro = new JPanel(new WrapLayout(FlowLayout.LEFT));
		pDinheiro.setPreferredSize(new Dimension(310,60));
		var pTroco = new JPanel(new WrapLayout(FlowLayout.LEFT));
		pTroco.setPreferredSize(new Dimension(300,60));
		
		pDinheiro.add(lbDinheiro);
		pDinheiro.add(campoDinheiro);
		pTroco.add(lbTroco);
		pTroco.add(campoTroco);
		
		
		JPanel pCentral = new JPanel(new GridLayout(2,1));
		pCentral.add(pDinheiro);
		pCentral.add(pTroco);
		
		frame.add(pCentral, BorderLayout.WEST);
		
		return frame;
	}
	
	private JInternalFrame criarFrameInternoCartao(FormaPagamento tipoCartao) {
		var frame = criarFrameInternoBasico();
		
		var pEsquerdo = new JPanel(new WrapLayout(FlowLayout.LEFT));
		pEsquerdo.setSize(new Dimension(200,550));
		
		var lbTaxa = new JLabel("Taxa do cartão:");
		lbTaxa.setFont(lbTaxa.getFont().deriveFont(23f).deriveFont(Font.BOLD));
		var campoTaxa = new JTextField();
		campoTaxa.setEditable(false);
		campoTaxa.setColumns(12);
		campoTaxa.setFont(campoTaxa.getFont().deriveFont(28f));
		
		var boxTaxa = new JCheckBox("Aplicar taxa");
		boxTaxa.addItemListener(quandoClicado -> {
			if(boxTaxa.isSelected()) {
				campoTaxa.setEnabled(true);
				var valorComTaxa = CaixaController.aplicarTaxaCartao(valorProdutos, tipoCartao);
				valorTaxaCartao = valorComTaxa.subtract(valorProdutos);
				valorFinal = valorProdutos.add(valorTaxaParcelamento.add(valorTaxaCartao));
				
				campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
				campoTaxa.setText(CaixaController.converterMonetarioEmTexto(valorTaxaCartao));
			} else {
				valorTaxaCartao = BigDecimal.ZERO.setScale(2);
				valorFinal = valorProdutos.add(valorTaxaParcelamento.add(valorTaxaCartao));
				
				campoTaxa.setText("Taxa não aplicada");
				campoTaxa.setEnabled(false);
				campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
			}
		});
		
		if(tipoCartao == FormaPagamento.CARTAO_DEBITO) {
			frame.setTitle("Pagamento com cartão de débito");
		} else if(tipoCartao == FormaPagamento.CARTAO_CREDITO) {
			frame.setTitle("Pagamento com cartão de crédito");
			var pDireito = new JPanel(new WrapLayout(FlowLayout.LEFT));
			var boxParcelamento = new JCheckBox("Parcelar?");

			var lbParcelas = new JLabel("Parcelamento em:");
			lbParcelas.setFont(lbParcelas.getFont().deriveFont(23f).deriveFont(Font.BOLD));
			lbParcelas.setVisible(false);
			var comboParcelas = new JComboBox<String>(
					new String[]{"1x","2x","3x","4x","5x","6x","7x","8x","9x","10x","11x","12x"});
			comboParcelas.setEnabled(false);
			comboParcelas.setVisible(false);
			
			var campoValorTaxaParcelamento = new JTextField();
			campoValorTaxaParcelamento.setEditable(false);
			campoValorTaxaParcelamento.setColumns(10);
			campoValorTaxaParcelamento.setFont(campoValorTaxaParcelamento.getFont().deriveFont(25f));
			campoValorTaxaParcelamento.setVisible(false);
			
			var boxAplicarTaxa = new JCheckBox("Aplicar taxa");
			boxAplicarTaxa.setVisible(false);
			boxAplicarTaxa.addItemListener(quandoClicado -> {
				if(boxAplicarTaxa.isSelected()) {
					campoValorTaxaParcelamento.setText("Selecione o Nº de parcelas");
				} else {
					comboParcelas.setSelectedIndex(0);
					valorTaxaParcelamento = BigDecimal.ZERO.setScale(2);
					valorFinal = valorProdutos.add(valorTaxaParcelamento.add(valorTaxaCartao));
					campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
				}
			});
			boxAplicarTaxa.setSelected(true);
			
			
			boxParcelamento.addItemListener(eventoSelecao -> {
				if(boxParcelamento.isSelected()) {
					lbParcelas.setVisible(true);
					comboParcelas.setVisible(true);
					comboParcelas.setEnabled(true);
					campoValorTaxaParcelamento.setVisible(true);
					boxAplicarTaxa.setVisible(true);
				} else {
					lbParcelas.setVisible(false);
					comboParcelas.setSelectedIndex(0);
					comboParcelas.setVisible(false);
					comboParcelas.setEnabled(false);
					campoValorTaxaParcelamento.setVisible(false);
					boxAplicarTaxa.setVisible(false);
					valorTaxaParcelamento = BigDecimal.ZERO.setScale(2);
					valorFinal = valorProdutos.add(valorTaxaParcelamento.add(valorTaxaCartao));
					campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
				}
			});
			comboParcelas.addActionListener(eventoSelecao -> {
				
				BigDecimal valorComParcelamento;
				
				switch((String)comboParcelas.getSelectedItem()) {
					case "1x":
						valorComParcelamento = valorProdutos;
						break;
					case "2x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.0459);
						break;
					case "3x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.0597);
						break;
					case "4x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.0733);
						break;
					case "5x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.0866);
						break;
					case "6x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.0996);
						break;
					case "7x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.1124);
						break;
					case "8x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.1250);
						break;
					case "9x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.1373);
						break;
					case "10x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.1493);
						break;
					case "11x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.1612);
						break;
					case "12x":
						valorComParcelamento = CaixaController
							.aplicarTaxaParcelamentoCartao(valorProdutos, 1.1728);
						break;
					default:
						valorComParcelamento = valorProdutos;
				}
				
				if(boxAplicarTaxa.isSelected()) {
					valorTaxaParcelamento = valorComParcelamento.subtract(valorProdutos);
					
					valorFinal = valorProdutos.add(valorTaxaParcelamento.add(valorTaxaCartao));
					campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
				}
			});
			pDireito.add(boxParcelamento);
			pDireito.add(lbParcelas);
			pDireito.add(comboParcelas);
			pDireito.add(campoValorTaxaParcelamento);
			pDireito.add(boxAplicarTaxa);
			frame.add(pDireito, BorderLayout.EAST);
		} else {
			frame.setTitle("Pagamento com cartão vale-alimentação");
		}
		
		boxTaxa.setSelected(true);
		
		pEsquerdo.add(lbTaxa);
		pEsquerdo.add(campoTaxa);
		pEsquerdo.add(boxTaxa);
		
		frame.add(pEsquerdo, BorderLayout.WEST);
		return frame;
	}
	
	private JInternalFrame criarFrameInternoBasico() {
		var frame = new JInternalFrame("Janela interna base");
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(740,430));
		frame.setResizable(true);
		
		var lbValor = new JLabel("Valor dos produtos:");
		lbValor.setFont(lbValor.getFont().deriveFont(23f).deriveFont(Font.BOLD));
		var campoValorVenda = new JTextField(CaixaController.converterMonetarioEmTexto(valorProdutos));
		campoValorVenda.setFont(campoValorVenda.getFont().deriveFont(28f));
		campoValorVenda.setEditable(false);
		campoValorVenda.setPreferredSize(new Dimension(150,45));
		
		JPanel pValor = new JPanel(new WrapLayout(FlowLayout.LEFT));
		pValor.add(lbValor);
		
		JPanel pCampoVenda = new JPanel(new WrapLayout(FlowLayout.LEFT));
		pCampoVenda.add(campoValorVenda);
		
		campoDesconto = new JFormattedTextField();
		campoDesconto.setFont(campoDesconto.getFont().deriveFont(28f));
		campoDesconto.setPreferredSize(new Dimension(150,45));
        campoDesconto.setFormatterFactory(new FormatadorDinheiroFactory());
        campoDesconto.setText(Configuracao.PREFIXO_MONETARIO.get()+"0");
        campoDesconto.addFocusListener(new EscutadorFoco(campoDesconto));
        campoDesconto.setEnabled(false);
        campoDesconto.setVisible(false);
        
		var btAplicarDesconto = new JButton("Aplicar");
		btAplicarDesconto.addActionListener( quandoClicado -> {
			var textoDesconto = campoDesconto.getText();
			BigDecimal desconto = CaixaController.converterTextoEmMonetario(textoDesconto);
			var subtotal = valorProdutos.add(valorTaxaParcelamento.add(valorTaxaCartao));
			valorFinal = subtotal.subtract(desconto);
			
			campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
		});
		btAplicarDesconto.setPreferredSize(new Dimension(85,45));
		btAplicarDesconto.setEnabled(false);
		btAplicarDesconto.setVisible(false);
		
        JCheckBox boxDesconto = new JCheckBox("Desconto?");
        boxDesconto.addActionListener(clicado -> {
        	if(boxDesconto.isSelected()) {
        		campoDesconto.setEnabled(true);
        		campoDesconto.setVisible(true);
        		btAplicarDesconto.setEnabled(true);
        		btAplicarDesconto.setVisible(true);
        	}        		
        	else {
        		campoDesconto.setEnabled(false);
        		campoDesconto.setVisible(false);
        		btAplicarDesconto.setEnabled(false);
        		btAplicarDesconto.setVisible(false);
        	}
        		
        });
		
		JPanel pInfoDesconto = new JPanel(new WrapLayout(FlowLayout.LEFT));
		pInfoDesconto.add(boxDesconto);
		
		JPanel pAdicionarDesconto = new JPanel(new WrapLayout(FlowLayout.LEFT));
		pAdicionarDesconto.add(campoDesconto);
		pAdicionarDesconto.add(btAplicarDesconto);
		
		JPanel pSuperior = new JPanel(new GridLayout(2,1));
		pSuperior.add(pValor);
		pSuperior.add(pCampoVenda);
		
		JPanel pInferior = new JPanel(new GridLayout(2,1));
		pInferior.add(pInfoDesconto);
		pInferior.add(pAdicionarDesconto);
		
		frame.add(pSuperior, BorderLayout.NORTH);
		frame.add(pInferior, BorderLayout.SOUTH);
				
		return frame;
	}
	
	/**
	 * Fonte:
	 https://stackoverflow.com/questions/18102740/only-numbers-and-one-decimal-point-allow-on-jtextfield-in-java
	 * */
	public class FormatadorDinheiroFactory extends AbstractFormatterFactory {
        @Override
        public AbstractFormatter getFormatter(JFormattedTextField tf) {
            Locale local = getLocale();
        	
        	var formatoNumero = (DecimalFormat)DecimalFormat.getInstance(local);
        	
            formatoNumero.setMinimumFractionDigits(2);
            formatoNumero.setMaximumFractionDigits(2);
            formatoNumero.setCurrency(Currency.getInstance(local));
            formatoNumero.setGroupingUsed(false);
            formatoNumero.setPositivePrefix(Configuracao.PREFIXO_MONETARIO.get());
            
            InternationalFormatter formatador = new InternationalFormatter();
            formatador.setFormat(formatoNumero);
            formatador.setAllowsInvalid(false);
            formatador.setCommitsOnValidEdit(true);
            //System.out.println(formatador.getOverwriteMode());
            
            return formatador;
        }
    }
	
	protected class EscutadorFoco implements FocusListener {
		
		JFormattedTextField ftf;
		
		public EscutadorFoco(JFormattedTextField ftf) {
			this.ftf = ftf;
		}
		
		@Override
		public void focusGained(FocusEvent e) {
		    SwingUtilities.invokeLater(() -> {
		        ftf.select(3,4);
		    });
		}
		@Override
		public void focusLost(FocusEvent e) {}
	}
	
//	MaskFormatter mascaraDin;
//	try {
//		mascaraDin = new MaskFormatter("R$##########");
//		mascaraDin.setPlaceholder(" ");
//		mascaraDin.setPlaceholderCharacter(' ');
//		mascaraDin.install(campoDesconto);
//	} catch (ParseException e) {
//		e.printStackTrace();
//	}
}

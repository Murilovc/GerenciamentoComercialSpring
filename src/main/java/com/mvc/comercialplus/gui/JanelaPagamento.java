package com.mvc.comercialplus.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.IllegalComponentStateException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.InternationalFormatter;

import com.mvc.comercialplus.GerenciamentoComercialSpringApplication.MenuPrincipal;
import com.mvc.comercialplus.config.Configuracao;
import com.mvc.comercialplus.controller.CaixaController;
import com.mvc.comercialplus.model.FormaPagamento;

@SuppressWarnings("serial")
public class JanelaPagamento extends JDialog{
	
	private JTextField campoValorTotal;
	private JFormattedTextField campoDesconto;
	
	private BigDecimal valorProdutos;
	private BigDecimal valorFinal;
	
	private JInternalFrame internalFrame;
	
	private JDesktopPane desktopPane;
	
	private JButton btFinalizar;
	
	public JanelaPagamento(MenuPrincipal pai, BigDecimal valorProdutos) {
		
		//pai.setVisible(false);
		
		this.valorProdutos = valorProdutos;
		//inicialmente se presume que a venda sera em dinheiro
		//logo nao havera taxas e o valor final antes de qualquer
		//desconto (opcional)seria igual ao valor dos produtos.
		valorFinal = valorProdutos;
		
		this.setSize(new Dimension(1100,730));
		this.setTitle("Janela pagamento");
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(pai);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		adicionarComponentes(pai);
		//this.pack();
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
			internalFrame = criarFrameInternoDinheiro();
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);
			
		});
		var radioPix = new JRadioButton("PIX");
		var radioQRPix = new JRadioButton("QR CODE PIX");
		var radioVale = new JRadioButton("CARTÃO VALE ALIMENTAÇÃO");
		radioVale.addItemListener(eventoMarcado -> {
			if(internalFrame != null) {
				desktopPane.remove(internalFrame);
				internalFrame.dispose();
				desktopPane.repaint();
			}
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
			internalFrame = criarFrameInternoCartao(FormaPagamento.CARTAO_CREDITO);
			desktopPane.add(internalFrame);
			internalFrame.setVisible(true);	
		});
		var radioPendura = new JRadioButton("FIADO");
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
		
		var pCentral = new JPanel(new WrapLayout(FlowLayout.LEFT));
		
		var lbTaxa = new JLabel("Taxa do cartão:");
		var campoTaxa = new JTextField();
		campoTaxa.setEditable(false);
		
		if(tipoCartao == FormaPagamento.CARTAO_DEBITO) {
			frame.setTitle("Pagamento com cartão de crédito");
		} else if(tipoCartao == FormaPagamento.CARTAO_DEBITO) {
			frame.setTitle("Pagamento com cartão de débito");
		} else {
			frame.setTitle("Pagamento com cartão vale-alimentação");
		}
		
		var boxTaxa = new JCheckBox("Aplicar taxa");
		boxTaxa.addItemListener(quandoClicado -> {
			if(boxTaxa.isSelected()) {
				campoTaxa.setEnabled(true);
				var valorComTaxa = CaixaController.aplicarTaxaCartao(valorProdutos, tipoCartao);
				valorFinal = valorComTaxa;
				campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
				campoTaxa.setText(CaixaController.converterMonetarioEmTexto(valorComTaxa));
			} else {
				campoTaxa.setText("");
				campoTaxa.setEnabled(false);
				valorFinal = valorProdutos;
				campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorProdutos));
			}
		});
		boxTaxa.setSelected(true);
		
		pCentral.add(lbTaxa);
		pCentral.add(campoTaxa);
		pCentral.add(boxTaxa);
		

		
		frame.add(pCentral, BorderLayout.CENTER);
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
		
		var lbDesconto = new JLabel("Desconto?");
		lbDesconto.setFont(lbDesconto.getFont().deriveFont(23f).deriveFont(Font.BOLD));
		
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
			valorFinal = valorProdutos.subtract(desconto);
			
			campoValorTotal.setText(CaixaController.converterMonetarioEmTexto(valorFinal));
		});
		btAplicarDesconto.setPreferredSize(new Dimension(85,45));
		btAplicarDesconto.setEnabled(false);
		btAplicarDesconto.setVisible(false);
		
        JCheckBox boxDesconto = new JCheckBox("Sim");
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
		pInfoDesconto.add(lbDesconto);
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
            //XXX
            System.out.println(formatador.getOverwriteMode());
            //formatador.setOverwriteMode(true);
            
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

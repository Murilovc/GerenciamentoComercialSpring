package com.mvc.comercialplus.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.mvc.comercialplus.GerenciamentoComercialSpringApplication.MenuPrincipal;

@SuppressWarnings("serial")
public class JanelaPagamento extends JDialog{
	public JanelaPagamento(MenuPrincipal pai) {
		this.setSize(new Dimension(750,590));
		this.setTitle("Janela pagamento");
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(pai);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setResizable(false);
	}
	
	private void adicionarComponentes() {
		/*PARTE SUPERIOR DA JANELA*/
		var radioDinheiro = new JRadioButton("DINHEIRO");
		var radioPix = new JRadioButton("PIX");
		var radioQRPix = new JRadioButton("QR CODE PIX");
		var radioVale = new JRadioButton("CARTÃO VALE ALIMENTAÇÃO");
		var radioDebito = new JRadioButton("CARTÃO DE DÉBITO");
		var radioCredito = new JRadioButton("CARTÃO DE CRÉDITO");
		var radioPendura = new JRadioButton("PENDURA");
		var btGrupo = new ButtonGroup();
		btGrupo.add(radioDinheiro);
		btGrupo.add(radioPix);
		btGrupo.add(radioQRPix);
		btGrupo.add(radioVale);
		btGrupo.add(radioDebito);
		btGrupo.add(radioCredito);
		btGrupo.add(radioPendura);
		
		var lbEscolha = new JLabel("Escolha a forma de pagamento");
		JPanel pSuperior = new JPanel();
		pSuperior.add(lbEscolha);
		pSuperior.add(radioDinheiro);
		pSuperior.add(radioPix);
		pSuperior.add(radioQRPix);
		pSuperior.add(radioVale);
		pSuperior.add(radioDebito);
		pSuperior.add(radioCredito);
		pSuperior.add(radioPendura);
		
		/*LATERAL DIREITA DA JANELA*/
		
		/*PARTE CENTRAL DA JANELA*/
		
		/*PARTE INFERIOR DA JANELA*/
		
		/*ADICIONANDO COMPONENTES A JANELA*/
		this.add(pSuperior, BorderLayout.CENTER);
	}
}

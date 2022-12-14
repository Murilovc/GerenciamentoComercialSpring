package com.mvc.comercialplus;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.aspectj.weaver.patterns.HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.mvc.comercialplus.config.Configuracao;
import com.mvc.comercialplus.controller.CaixaController;
import com.mvc.comercialplus.controller.ProdutoController;
import com.mvc.comercialplus.gui.JanelaPagamento;
import com.mvc.comercialplus.gui.Visualizacao;
import com.mvc.comercialplus.gui.WrapLayout;
import com.mvc.comercialplus.model.Categoria;
import com.mvc.comercialplus.model.Produto;
import com.mvc.comercialplus.service.ClienteService;
import com.mvc.comercialplus.service.EstoqueService;
import com.mvc.comercialplus.service.ProdutoService;
import com.mvc.comercialplus.service.UnidadeService;
import com.mvc.comercialplus.service.VendaService;

@SpringBootApplication
public class GerenciamentoComercialSpringApplication {
	
	@Autowired
	public GerenciamentoComercialSpringApplication(CaixaController caixaController, ProdutoController produtoController) {
		

		
		//FlatDarculaLaf.setup();
		FlatIntelliJLaf.setup();
		
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() { 
				MenuPrincipal menu = new MenuPrincipal(produtoController, caixaController);
			} 
		});
	}
	
	public static class MenuPrincipal extends JFrame{
		
		private ProdutoController produtoController;
		private CaixaController caixaController;
		
		private JButton btPagamento;
		private JButton botaoRemover;
		private JButton botaoCancelar;
		
		private JTable tabela;
		
		private JScrollPane jcp;
		
		private ProdutoService service;
		
		private Visualizacao<Produto> visualizacao;
		
		private JTextField campoQuantidade;
		private JTextField campoPreco;
		private JTextField campoNomeProduto;
		private JTextField campoTotal;
		
		private JLabel lbCodBarras;
		private JLabel lbDesconto;
		private JLabel lbLeituraCodBarras;
		
		private Produto ultimoProdutoAdicionado;
		
		private JPanel pLateralEsq;
		
		private JLabel lbCategoria;
		
		private final static short TEMA_CLARO = 1;
		private final static short TEMA_ESCURO = 2;
		private static short temaEscolhido = TEMA_CLARO;
		
		
		public MenuPrincipal(ProdutoController produtoController, CaixaController caixaController) {
			
			this.produtoController = produtoController;
			this.caixaController = caixaController;
			
			//this.service = repo;
			/* Como este ?? o primeiro menu, precisa
			 * explicitamente ser tornado vis??vel
			 * 
			 * NOTA: Se nenhum menu estiver vis??vel,
			 * a JVM encerra o programa
			 * */
			this.setVisible(true);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setSize(new Dimension(1280,736));
			this.setExtendedState(MAXIMIZED_BOTH);
			//this.pack();
			
			this.adicionarComponentes();

		}
		
		private ImageIcon carregarImagemCategoria(int largura, int altura) {
			
			ImageIcon imagem;
			
			if(ultimoProdutoAdicionado != null) {
				Categoria categoria = ultimoProdutoAdicionado.getCategoria();
				imagem = carregarImagem(categoria.getArquivoCorrespondente(), 120, 175);
			} else {
				imagem = carregarImagem("ovos.png", 120, 175);
			}
			return imagem;
		}
		
		private ImageIcon carregarImagem(String pathArquivo, int altura, int largura) {
			byte[] imagemBytes = null;
			
			try {
				imagemBytes = Files.readAllBytes(Paths.get(pathArquivo));
			} catch (IOException e1) {
				e1.getCause().getMessage();
			}
			var icone = new ImageIcon(imagemBytes);
			var imagemRedimensionada = icone.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
			icone.setImage(imagemRedimensionada);
			return icone;
		}
		
		private void adicionarComponentes() {

			/*BARRA DE MENU*/
			JMenuBar menuBar = new JMenuBar();
			//seletor de cliente cadastrado como
			//item de menu no menu arquivo?
			var menuArquivo = new JMenu("Arquivo");
			var menuOpcoes = new JMenu("Op????es");
			var menuAjuda = new JMenu("Ajuda");
			var menuSobre = new JMenu("Sobre");
			
			menuBar.add(menuArquivo);
			menuBar.add(menuOpcoes);
			menuBar.add(menuAjuda);
			menuBar.add(menuSobre);
			
			
			/*LATERAL ESQUERDA DA JANELA*/
			ImageIcon iconeCat = carregarImagemCategoria(120, 175);
			
			lbCategoria = new JLabel(iconeCat);
			var pImagem = new JPanel();
			pImagem.setSize(new Dimension(130,185));
			pImagem.add(lbCategoria);
			lbCodBarras = new JLabel("Nenhum produto escaneado");
			lbDesconto = new JLabel("Desconto: ");

			lbLeituraCodBarras = new JLabel("Pronto para ler");
			
			var icone = carregarImagem("barcode.png", 20, 66);
			lbLeituraCodBarras.setIcon(icone);
			
			var pInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pInfo.add(lbCodBarras);
			pInfo.add(lbDesconto);
			
			pLateralEsq = new JPanel(new BorderLayout());
			pLateralEsq.setPreferredSize(new Dimension(200,700));
			
			pLateralEsq.add(pImagem, BorderLayout.NORTH);
			pLateralEsq.add(pInfo, BorderLayout.CENTER);
			pLateralEsq.add(lbLeituraCodBarras, BorderLayout.SOUTH);
			
			
			/*PARTE SUPERIOR DA JANELA*/
			campoNomeProduto = new JTextField("Nenhum produto adicionado na lista");
			campoNomeProduto.setEditable(false);
			campoNomeProduto.setFont(getFont().deriveFont(36f));
			campoNomeProduto.setPreferredSize(new Dimension(750,50));
			campoNomeProduto.setFocusable(false);
			var lbQuantidade = new JLabel("X");
			lbQuantidade.setPreferredSize(new Dimension(15,50));
			campoQuantidade = new JTextField("0");
			campoQuantidade.setFont(getFont().deriveFont(32f));
			campoQuantidade.setEditable(false);
			campoQuantidade.setFocusable(false);
			var lbUnidade = new JLabel("UN.");
			lbUnidade.setPreferredSize(new Dimension(20,50));
			var lbSeta = new JLabel(carregarImagem("right-arrow.png", 45, 75));
			
			var lbPreco = new JLabel("PRE??O: ");
			lbPreco.setFont(getFont().deriveFont(Font.BOLD).deriveFont(36f));
			campoPreco = new JTextField(Configuracao.PREFIXO_MONETARIO.get()+"0,00");
			campoPreco.setEditable(false);
			campoPreco.setFont(getFont().deriveFont(36f));
			campoPreco.setPreferredSize(new Dimension(220,50));
			campoPreco.setFocusable(false);
			
			JPanel pPrimeiraLinha = new JPanel(new WrapLayout((FlowLayout.LEFT)));
			pPrimeiraLinha.setSize(new Dimension(755,115));
			pPrimeiraLinha.add(campoNomeProduto);
			
			var pSegundaLinha = new JPanel(new WrapLayout(FlowLayout.LEFT));
			pSegundaLinha.setSize(new Dimension(755,60));
			pSegundaLinha.add(lbQuantidade);
			pSegundaLinha.add(campoQuantidade);
			pSegundaLinha.add(lbUnidade);
			pSegundaLinha.add(lbSeta);
			pSegundaLinha.add(lbPreco);
			pSegundaLinha.add(campoPreco);
			
			//painel para listar as linhas igualmente
			var pInfoProduto = new JPanel(new GridLayout(2,1));
			pInfoProduto.add(pPrimeiraLinha);
			pInfoProduto.add(pSegundaLinha);
			
			var btTema = new JButton();
			btTema.setIcon(carregarImagem("lua.png", 50, 50));
			//deixando o botao transparente
			btTema.setContentAreaFilled(false);
			btTema.setBorderPainted(false);
			btTema.addActionListener( e -> {
				if(temaEscolhido == TEMA_CLARO) {
					btTema.setIcon(carregarImagem("sol.png", 50, 50));
					FlatDarculaLaf.setup();
					SwingUtilities.updateComponentTreeUI(MenuPrincipal.this);
					temaEscolhido = TEMA_ESCURO;
				} else {
					btTema.setIcon(carregarImagem("lua.png", 50, 50));
					FlatIntelliJLaf.setup();
					SwingUtilities.updateComponentTreeUI(MenuPrincipal.this);
					temaEscolhido = TEMA_CLARO;
				}
				MenuPrincipal.this.requestFocusInWindow();
			});
			
			var btConf = new JButton("<html>??rea <br>administrativa");
			btConf.setPreferredSize(new Dimension(100,50));
			//deixando o botao transparente
			btConf.setContentAreaFilled(false);
			btConf.setBorderPainted(false);
			btConf.addActionListener(e -> {
				MenuPrincipal.this.requestFocusInWindow();
			});
			
			var pInfoProduto2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
			pInfoProduto2.add(pInfoProduto);
			
			var pSuperior = new JPanel(new BorderLayout());
			pSuperior.add(btConf, BorderLayout.WEST);
			pSuperior.add(pInfoProduto2, BorderLayout.CENTER);
			pSuperior.add(btTema, BorderLayout.EAST);
			
			
			/*CENTRO DA JANELA*/
			List<Produto> produtos = new ArrayList<>();
			Class<?>[] classes = {Long.class, String.class, String.class,
					String.class, BigDecimal.class, BigDecimal.class};
			
			visualizacao = new Visualizacao<Produto>(
					produtos,
					new String[] {"ID","CATEGORIA","C??DIGO DE BARRAS","NOME","PRE??O", "DESCONTO"},
					classes,
					6);
			
			tabela = visualizacao.getTable();
			tabela.addMouseListener(new EscutadorMouse());
			jcp = new JScrollPane(tabela);
			
			var lbTituloTabela = new JLabel("Produtos adicionados:");
			lbTituloTabela.setFont(getFont().deriveFont(25f).deriveFont(Font.BOLD));
			
			JPanel pCentral = new JPanel(new BorderLayout());
			pCentral.add(jcp, BorderLayout.CENTER);
			pCentral.add(lbTituloTabela, BorderLayout.NORTH);
			
			
			/* PARTE INFERIOR DA JANELA */
			var lbTotal = new JLabel("TOTAL:");
			lbTotal.setFont(getFont().deriveFont(Font.BOLD).deriveFont(36f));
			campoTotal = new JTextField(Configuracao.PREFIXO_MONETARIO.get()+"0,00");
			campoTotal.setFont(getFont().deriveFont(Font.BOLD).deriveFont(36f));
			campoTotal.setEditable(false);
			campoTotal.setFocusable(false);
			campoTotal.setPreferredSize(new Dimension(180, 50));
			
			btPagamento = new JButton(new AcaoPagamento(caixaController));
			btPagamento.setPreferredSize(new Dimension(120,50));
			
			JPanel pInferior = new JPanel(new WrapLayout());
			pInferior.add(lbTotal);
			pInferior.add(campoTotal);
			pInferior.add(btPagamento);
			
			/* LATERAL DIREITA DA JANELA */
			botaoRemover = new JButton("Remover");
			//botaoRemover.setPreferredSize(new Dimension(90,240));
			botaoRemover.setEnabled(false);
			
			botaoRemover.addActionListener(e -> {
				if(tabela.getSelectedRowCount() > 1) {
					
					int[] indices = tabela.getSelectedRows();
					List<Produto> listaProdutos = new ArrayList<>();
					
					/* O resto da funcao assume que a ordem das linhas
					 * no JTable eh a mesma ordem dos produtos na lista de produtos
					 * da Visualizacao */
					for(int i = 0; i < tabela.getSelectedRowCount(); i++) {
						Produto p = visualizacao.listaTipo.get(indices[i]);
						listaProdutos.add(p);
					}
					visualizacao.removerElementos(listaProdutos);
					
					var opProduto = Optional.ofNullable(visualizacao.pegarUltimoElemento());
					opProduto.ifPresentOrElse(this::atualizarInfoCaixa, () -> {
						//caixa volta ao estado inicial se n??o houver produtos na lista
						ultimoProdutoAdicionado = null;
						atualizarInfoCaixa();
					});
					
					exibirDialog("Sucesso!", ModalityType.APPLICATION_MODAL, "Produtos removidos da lista");
				} else {
					int indice = tabela.getSelectedRow();
					if(indice != -1) {
						Produto p = visualizacao.listaTipo.get(indice);
						visualizacao.removerElemento(p);
						
						var opProduto = Optional.ofNullable(visualizacao.pegarUltimoElemento());
						opProduto.ifPresentOrElse(this::atualizarInfoCaixa, () -> {
							//caixa volta ao estado inicial se n??o houver produtos na lista
							ultimoProdutoAdicionado = null;
							atualizarInfoCaixa();
						});
						
						exibirDialog("Sucesso!", ModalityType.APPLICATION_MODAL, "Produto removido da lista");
					}
				}
				
				botaoRemover.setEnabled(false);
				botaoCancelar.setEnabled(false);
			});
			
			
			botaoCancelar = new JButton("Cancelar");
			//botaoCancelar.setPreferredSize(new Dimension(90,240));
			botaoCancelar.setEnabled(false);
			
			botaoCancelar.addActionListener(e -> {
				exibirDialog("OK", ModalityType.APPLICATION_MODAL,
						"<html>Sele????o cancelada, nenhuma<br>altera????o na lista de produtos");
				botaoRemover.setEnabled(false);
				botaoCancelar.setEnabled(false);
				visualizacao.getTable().clearSelection();
			});
			
			var pBotoes = new JPanel(new GridLayout(2,1));
			pBotoes.add(botaoRemover);
			pBotoes.add(botaoCancelar);
			
			pCentral.add(pBotoes, BorderLayout.EAST);
			
			
			
			/*ADICAO DOS PAINEIS E COMPONENTES NA JANELA*/
			this.setJMenuBar(menuBar);
			
			this.add(pLateralEsq, BorderLayout.WEST);
			this.add(pSuperior, BorderLayout.NORTH);
			this.add(pCentral, BorderLayout.CENTER);
			this.add(pInferior, BorderLayout.SOUTH);
			
			this.addKeyListener(new EscutadorTeclado());
			this.setFocusable(true);
			this.setAutoRequestFocus(true);
			this.requestFocusInWindow();

		}
		
		protected class EscutadorMouse extends MouseAdapter{
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (tabela.getSelectedRow() >= 0) {
					botaoRemover.setEnabled(true);
					botaoCancelar.setEnabled(true);
					lbLeituraCodBarras.setText("Leitura indispon??vel");
					lbLeituraCodBarras.setIcon(carregarImagem("errado.png", 25, 25));
					
				}

			}

		}
				
		protected class EscutadorTeclado extends KeyAdapter {
			
			//buffer de caracteres com tamanho maximo suficiente
			//para acomodar um codigo de barras padrao EAN-13
			char[] buffer = new char[13];
			short contador = 0;
			
			//metodo utilizado caso o usuario cole um codigo
			//de barras copiado de outra aplicacao
			@Override
			public void keyPressed(KeyEvent e) {
				if((e.isControlDown() || e.isMetaDown()) && e.getKeyCode() == KeyEvent.VK_V) {

					var areaTransferencia = Toolkit.getDefaultToolkit().getSystemClipboard();
					String str = "1234567890123";
					//lendo da area de tranferencia
					try {
						str = (String)areaTransferencia.getData(DataFlavor.stringFlavor);
					} catch (UnsupportedFlavorException | IOException e1) {
						e1.getCause().getMessage();
					}
					
					buffer = str.toCharArray();
					tratarEntrada(buffer);
					contador = 0;

				}

			}
						
			@Override
			public void keyTyped(KeyEvent e) {
				buffer[contador] = e.getKeyChar();
				contador++;
				
				if(contador == 13) {
					tratarEntrada(buffer);
					contador = 0;
				}
				
			}
		}
		
		private void tratarEntrada(char[] buffer) {
			var codBarras = new String(buffer);
			
			if(produtoController.validarEAN13(codBarras) == true) {
				Optional<Produto> op = Optional.ofNullable(produtoController.getByCodigoBarras(codBarras));
				
				if(op.isPresent()) {
										
					Produto p = op.get();
					visualizacao.adicionarElemento(p);
					ultimoProdutoAdicionado = p;
					var icone = carregarImagemCategoria(120,175);
					lbCategoria.setIcon(icone);
					
					atualizarInfoCaixa(p);
				} else {
					exibirDialog("Alerta", ModalityType.APPLICATION_MODAL, "Produto ainda n??o cadastrado");
				}

			} else {
				
				exibirDialog("Alerta", ModalityType.APPLICATION_MODAL, "C??digo de barras inv??lido");

			}
			
		}
		
		/**
		 * Metodo chamado quando nao existem produtos na lista
		 * de compra do cliente.
		 */
		private void atualizarInfoCaixa() {
			campoPreco.setText(Configuracao.PREFIXO_MONETARIO.get()+"0,00");
			campoTotal.setText(Configuracao.PREFIXO_MONETARIO.get()+"0,00");
			campoNomeProduto.setText("Nenhum produto adicionado na lista");
			campoQuantidade.setText("0");
			var icone = carregarImagemCategoria(175, 125);
			lbCategoria.setIcon(icone);
		}
		
		private void atualizarInfoCaixa(Produto produtoEscaneado) {
			int quantidade = 0;
			
			BigDecimal valorUnidades = new BigDecimal(0);
			BigDecimal valorTotal = new BigDecimal(0);
			
			/* TODO Salvar o total em uma variavel global neste arquivo,
			 * para evitar te de somar todos os produtos sempre que um
			 * novo produto e adicionado na lista*/
			for(Produto produto : visualizacao.listaTipo) {
				valorTotal = produto.getPreco().add(valorTotal);
				if(produtoEscaneado.equals(produto)) {
					valorUnidades = produto.getPreco().add(valorUnidades);
					quantidade++;
				}
			}
			campoQuantidade.setText(String.valueOf(quantidade));
			campoPreco.setText(CaixaController.converterMonetarioEmTexto(valorUnidades));
			campoNomeProduto.setText(produtoEscaneado.getNome());
			campoTotal.setText(CaixaController.converterMonetarioEmTexto(valorTotal));
			lbCodBarras.setText("<html>C??digo de barras escaneado:<br>"+produtoEscaneado.getCodigoBarras());
			lbDesconto.setText("<html>Desconto:<br>Nenhum");
		}
		
		private void exibirDialog(String titulo, ModalityType tipoModal, String mensagem) {
			var dialogo = new JDialog(MenuPrincipal.this);
			dialogo.setSize(new Dimension(250,150));
			dialogo.setTitle(titulo);
			dialogo.setLayout(new BorderLayout());
			dialogo.setLocationRelativeTo(MenuPrincipal.this);
			dialogo.setModalityType(tipoModal);
			dialogo.setResizable(false);
			
			var lbMensagem = new JLabel(mensagem);
			var btFechar = new JButton("Fechar");
			btFechar.addActionListener( evento -> {
				dialogo.dispose();
				MenuPrincipal.this.requestFocusInWindow();
				lbLeituraCodBarras.setText("Pronto para ler");
				lbLeituraCodBarras.setIcon(carregarImagem("barcode.png", 20, 66));
			});

			//KeyboardFocusManager.clearGlobalFocusOwner();
			var painel = new JPanel(new BorderLayout());
			painel.add(btFechar, BorderLayout.SOUTH);
			painel.add(lbMensagem, BorderLayout.CENTER);
			
			dialogo.add(painel, BorderLayout.CENTER);
			
			dialogo.setVisible(true);
			dialogo.pack();
		}
		
		protected class AcaoPagamento extends AbstractAction {

			CaixaController c;
			
			public AcaoPagamento(CaixaController c) {
				super("PAGAMENTO");
				putValue(MNEMONIC_KEY, KeyEvent.VK_T);
				putValue(SHORT_DESCRIPTION, "Visualizar turma selecionada");
				this.c = c;
			}
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuPrincipal.this.requestFocusInWindow();
				
				SwingUtilities.invokeLater(() -> {
					JanelaPagamento jp;

					jp = new JanelaPagamento(MenuPrincipal.this,
							CaixaController.converterTextoEmMonetario(campoTotal.getText()), c);
					jp.setVisible(true);

					
				});
				
			}
			
		}

	}
	
	public static void main(String[] args) {
		
		//Headless mode setado para falso, para permitir
		//a execu????o da GUI Swing
		System.setProperty("java.awt.headless", "false");
		
		SpringApplication.run(GerenciamentoComercialSpringApplication.class, args);
		
		/* Para usar os look and feel do Java, dispon??veis na JVM.
		
		//Imprime todos os look feels disponiveis
		for(var info : UIManager.getInstalledLookAndFeels()) {
			System.out.println(info+"\n");
		}
		
	    try {
	    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        	if ("Nimbus".equals(info.getName())) {
	                UIManager.setLookAndFeel(info.getClassName());
	                break;
	            }
	        }
	    } catch (ClassNotFoundException ex) {
	        System.out.println("Error seting Look and Feel: ClassNotFoundException "+ex.getCause());
	    } catch (InstantiationException e) {
	    	System.out.println("Error seting Look and Feel: InstantiationException "+e.getCause());
		} catch (IllegalAccessException e) {
			System.out.println("Error seting Look and Feel: IllegalAccessException "+e.getCause());
		} catch (UnsupportedLookAndFeelException e) {
			System.out.println("Error seting Look and Feel: UnsupportedLookAndFeelException "+e.getCause());
		}
		
		*/
		
		/*Para usar a dependencia FlatLaf, que traz um look and feel avan??ado,
		 *podedendo escolher entre diferentes temas */
	    //FlatLightLaf.setup();
	    
		/** XXX
		 * kkkkkkkkkkkkk
		 * perola encontrada em 17/12/2022
		 * HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor*/
	}
	
}



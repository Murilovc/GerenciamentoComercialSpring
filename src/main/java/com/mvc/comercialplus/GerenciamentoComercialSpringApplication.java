package com.mvc.comercialplus;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.mvc.comercialplus.gui.Visualizacao;
import com.mvc.comercialplus.gui.WrapLayout;
import com.mvc.comercialplus.model.Categoria;
import com.mvc.comercialplus.model.Produto;
import com.mvc.comercialplus.service.ProdutoService;

@SpringBootApplication
public class GerenciamentoComercialSpringApplication {

	private ProdutoService produtoService;
	
	@Autowired
	public GerenciamentoComercialSpringApplication(ProdutoService repo) {
		this.produtoService = repo;
		
		FlatDarculaLaf.setup();
		
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() { 
				//testeRuntime();
				MenuPrincipal menu = new MenuPrincipal(repo);
			} 
		});
	}
	
	static class MenuPrincipal extends JFrame{
		
		private JButton btPagamento;
		
		private JTable tabela;
		
		private JScrollPane jcp;
		
		private ProdutoService service;
		
		private Visualizacao<Produto> visualizacao;
		
		private JTextField campoQuantidade;
		
		private Produto ultimoProdutoAdicionado;
		
		private JPanel pLeteralEsq;
		
		private ImageIcon imagem;
		
		private JLabel lbCategoria;
		
		
		public MenuPrincipal(ProdutoService repo) {
			
			this.service = repo;
			/* Como este é o primeiro menu, precisa
			 * explicitamente ser tornado visível,
			 * já aque a o método da superclasse 
			 * por padrão torna os menus invisíveis.
			 * 
			 * NOTA: Se nenhum menu estiver visível,
			 * a JVM encerra o programa
			 * */
			this.setVisible(true);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setSize(new Dimension(1280,736));
			this.setExtendedState(MAXIMIZED_BOTH);
			//this.pack();
			
			this.adicionarComponentes();

		}
		
		private void carregarImagemCategoria(int largura, int altura) {
			
			byte[] imagemBytes = null;
			try {
				if(ultimoProdutoAdicionado != null) {
					Categoria categoria = ultimoProdutoAdicionado.getCategoria();
					imagemBytes = Files.readAllBytes(Paths.get(categoria.getArquivoCorrespondente()));
				} else {
					imagemBytes = Files.readAllBytes(Paths.get("ovos.png"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			var icone = new ImageIcon(imagemBytes);
			var imagemOriginal = icone.getImage();
			icone.setImage(imagemOriginal.getScaledInstance(largura, altura, Image.SCALE_DEFAULT));
			
			imagem = icone;
		}
		
		
		private void adicionarComponentes() {

			/*LATERAL ESQUERDA DA JANELA*/
			carregarImagemCategoria(120, 175);
			
			lbCategoria = new JLabel(imagem);
			//labelCategoria.setBounds(new Rectangle(new Dimension(220,298)));
			var lbCodBarras = new JLabel("Código de barras do produto atual");
			var lbDesconto = new JLabel("Desconto em R$");
			
			
			pLeteralEsq = new JPanel(new FlowLayout());
			pLeteralEsq.setPreferredSize(new Dimension(200,700));
			
			
			pLeteralEsq.add(lbCategoria);
			pLeteralEsq.add(lbCodBarras);
			pLeteralEsq.add(lbDesconto);
			
			
			/*PARTE SUPERIOR DA JANELA*/
		
			var campoNomeProduto = new JTextField("Produto Z");
			campoNomeProduto.setEditable(false);
			campoNomeProduto.setFont(getFont().deriveFont(36f));
			campoNomeProduto.setPreferredSize(new Dimension(650,50));
			campoNomeProduto.setFocusable(false);
			//campoNomeProduto.addKeyListener(new EscutadorTeclado());
			var lbQuantidade = new JLabel("X");
			lbQuantidade.setPreferredSize(new Dimension(15,50));
			campoQuantidade = new JTextField("1");
			campoQuantidade.setFont(getFont().deriveFont(32f));
			campoQuantidade.setEditable(false);
			campoQuantidade.setFocusable(false);
			var lbUnidade = new JLabel("UN.");
			lbUnidade.setPreferredSize(new Dimension(20,50));
			
			byte[] arquivoSeta = null;
			try {
				arquivoSeta = Files.readAllBytes(Paths.get("right-arrow.png"));
			} catch (IOException e) {
				e.getCause().getMessage();
			}
			var iconSeta = new ImageIcon(arquivoSeta);
			var imgParaConverter = iconSeta.getImage();
			iconSeta.setImage(imgParaConverter.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			var lbImgSeta = new JLabel(iconSeta);
			lbImgSeta.setPreferredSize(new Dimension(50,50));
			var lbPreco = new JLabel("PREÇO:");
			lbPreco.setFont(getFont().deriveFont(Font.BOLD).deriveFont(36f));
			//lbPreco.setPreferredSize(new Dimension(100,50));
			var campoPreco = new JTextField("R$ 2,90");
			campoPreco.setEditable(false);
			campoPreco.setFont(getFont().deriveFont(36f));
			campoPreco.setPreferredSize(new Dimension(200,50));
			campoPreco.setFocusable(false);
			
			var pSegundaLinha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			pSegundaLinha.setPreferredSize(new Dimension(700,60));
			pSegundaLinha.add(lbImgSeta);
			pSegundaLinha.add(lbPreco);
			pSegundaLinha.add(campoPreco);
			
			JPanel pSuperior = new JPanel(new WrapLayout());
			pSuperior.setPreferredSize(new Dimension(950,120));
			pSuperior.add(campoNomeProduto);
			pSuperior.add(lbQuantidade);
			pSuperior.add(campoQuantidade);
			pSuperior.add(lbUnidade);
			pSuperior.add(pSegundaLinha);
			
			
			/*CENTRO DA JANELA*/
			
			List<Produto> clientes = service.getAll();
			Class<?>[] classes = {Long.class, String.class, String.class,
					String.class, BigDecimal.class, BigDecimal.class};
			
			visualizacao = new Visualizacao<Produto>(
					clientes,
					new String[] {"id","categoria","codigoBarras","nome","preco", "desconto"},
					classes,
					6);
			
			tabela = visualizacao.getTable();
			tabela.addMouseListener(new HabilitarEdicaoExclusao());
			jcp = new JScrollPane(tabela);
			
			var lbTituloTabela = new JLabel("Produtos adicionados:");
			lbTituloTabela.setFont(getFont().deriveFont(25f).deriveFont(Font.BOLD));
			
			JPanel pCentral = new JPanel(new BorderLayout());
			pCentral.add(jcp, BorderLayout.CENTER);
			pCentral.add(lbTituloTabela, BorderLayout.NORTH);
			
			
			/* PARTE INFERIOR DA JANELA */
			
			var lbTotal = new JLabel("TOTAL:");
			lbTotal.setFont(getFont().deriveFont(Font.BOLD).deriveFont(36f));
			var campoTotal = new JTextField("R$ 6,00");
			campoTotal.setFont(getFont().deriveFont(Font.BOLD).deriveFont(36f));
			campoTotal.setEditable(false);
			campoTotal.setFocusable(false);
			campoTotal.setPreferredSize(new Dimension(180, 50));
			
			btPagamento = new JButton(new AcaoPagamento());
			btPagamento.setPreferredSize(new Dimension(120,50));
			
			JPanel pInferior = new JPanel(new WrapLayout());
			pInferior.add(lbTotal);
			pInferior.add(campoTotal);
			pInferior.add(btPagamento);

			
			/*ADICAO DOS PAINEIS NA JANELA*/
			this.add(pLeteralEsq, BorderLayout.WEST);
			this.add(pSuperior, BorderLayout.NORTH);
			this.add(pCentral, BorderLayout.CENTER);
			this.add(pInferior, BorderLayout.SOUTH);
			
			this.addKeyListener(new EscutadorTeclado());
			this.setFocusable(true);
			this.setAutoRequestFocus(true);
			this.requestFocusInWindow();
			
//			desativarBotoes();
		}
		
//		private void desativarBotoes() {
//			btPagamento.setEnabled(false);
//		}
//		
//		private void ativarBotoes() {
//			btPagamento.setEnabled(true);
//		}
		
		protected class HabilitarEdicaoExclusao extends MouseAdapter{
			
			public void mousePressed(MouseEvent e) {
				if (tabela.getSelectedRow() >= 0) {
								
					/*Quero pegar o Id (está na coluna 0) da linha selecionada*/				
					long idSelecionado = (long)tabela.getValueAt(tabela.getSelectedRow(), 0);
					System.out.println(tabela.getSelectedRow());
					//ativarBotoes();
				
				}

			}

		}
				
		protected class EscutadorTeclado extends KeyAdapter {
			
			//buffer de caracteres com tamanho maximo suficiente
			//para acomodar um codigo de barras padrao EAN-13
			char[] buffer = new char[13];
			short contador = 0;
			
			public void keyTyped(KeyEvent e) {
				buffer[contador] = e.getKeyChar();
				contador++;
				
				if(contador == 13) {
					var codBarras = new String(buffer);
					
					if(service.validarEAN13(codBarras) == true) {
						Produto p = service.getByCodigoBarras(codBarras);
						
						int quantidade = 0;
						
						visualizacao.adicionarElemento(p);
						ultimoProdutoAdicionado = p;
						carregarImagemCategoria(120,175);
						lbCategoria.setIcon(imagem);
						for(Produto produto : visualizacao.listaTipo) {
							if(p.equals(produto)) {
								quantidade++;
							}
						}
						campoQuantidade.setText(String.valueOf(quantidade));
					} else {
						
						var dialogo = new JDialog(MenuPrincipal.this);
						dialogo.setSize(new Dimension(250,150));
						dialogo.setTitle("Alerta");
						dialogo.setLayout(new BorderLayout());
						dialogo.setLocationRelativeTo(MenuPrincipal.this);
						dialogo.setModalityType(ModalityType.APPLICATION_MODAL);
						dialogo.setResizable(false);
						
						var lbMensagem = new JLabel("Código de barras inválido.");
						var btFechar = new JButton("Fechar");
						btFechar.addActionListener((evento) -> dialogo.dispose());
						
						var painel = new JPanel(new BorderLayout());
						painel.add(btFechar, BorderLayout.SOUTH);
						painel.add(lbMensagem, BorderLayout.CENTER);
						
						dialogo.add(painel, BorderLayout.CENTER);
						
						dialogo.setVisible(true);
						dialogo.pack();
					}
					contador = 0;
				}
			}
		}
		
		protected class AcaoPagamento extends AbstractAction {

			public AcaoPagamento() {
				super("PAGAMENTO");
				putValue(MNEMONIC_KEY, KeyEvent.VK_T);
				putValue(SHORT_DESCRIPTION, "Visualizar turma selecionada");
			}
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//desativarBotoes();
				
				long valor = Long.valueOf(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 0)));
				
				MenuPrincipal.this.setVisible(false);
			}
			
		}

	}
	
	public static void main(String[] args) {
		
		//Headless mode setado para falso, para permitir
		//a execução da GUI Swing
		System.setProperty("java.awt.headless", "false");
		
		SpringApplication.run(GerenciamentoComercialSpringApplication.class, args);
		
		/* Para usar os look and feel do Java, disponíveis na JVM.
		
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
		
		/*Para usar a dependencia FlatLaf, que traz um look and feel avançado,
		 *podedendo escolher entre diferentes temas */
	    //FlatLightLaf.setup();
	    
	}
	
}



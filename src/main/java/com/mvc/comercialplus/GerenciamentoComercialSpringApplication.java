package com.mvc.comercialplus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.mvc.comercialplus.gui.Visualizacao;
import com.mvc.comercialplus.model.Cliente;
import com.mvc.comercialplus.repository.ClienteRepository;

@SpringBootApplication
public class GerenciamentoComercialSpringApplication {

	ClienteRepository repo;
	
	@Autowired
	public GerenciamentoComercialSpringApplication(ClienteRepository repo) {
		this.repo = repo;
		
		FlatDarculaLaf.setup();
		
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() { 
				//testeRuntime();
				MenuPrincipal menu = new MenuPrincipal(repo);
			} 
		});
	}
	
	static class MenuPrincipal extends JFrame{
		
		private JTextArea areaLembrete;
		
		private JButton btPagamento;
		
		private JTable tabela;
		
		private JScrollPane jcp;
		
		public ClienteRepository repo;
		
		
		
		public MenuPrincipal(ClienteRepository repo) {
			
			this.repo = repo;
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
			this.setPreferredSize(new Dimension(1280,736));
			this.setExtendedState(MAXIMIZED_BOTH);
			this.pack();
			
			this.adicionarComponentes();
			
			//MenuTurma mt = new MenuTurma(this);
		}
		
		
		
		private void adicionarComponentes() {


			/*LATERAL ESQUERDA DA JANELA*/
			byte[] imagem = null;
			try {
				imagem = Files.readAllBytes(Paths.get("teste.gif"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JLabel lbCategoria = new JLabel(new ImageIcon(imagem));
			//labelCategoria.setBounds(new Rectangle(new Dimension(220,298)));
			JLabel lbCodBarras = new JLabel("Código de barras do produto atual");
			JLabel lbDesconto = new JLabel("Desconto em R$");
			
			
			JPanel pLeteralEsq = new JPanel(new FlowLayout());
			pLeteralEsq.setPreferredSize(new Dimension(200,700));
			
			
			pLeteralEsq.add(lbCategoria);
			pLeteralEsq.add(lbCodBarras);
			pLeteralEsq.add(lbDesconto);
			
			
			

			
			/*CENTRO DA JANELA*/
			
			List<Cliente> clientes = repo.findAll();
			Class<?>[] classes = {Long.class, String.class, String.class, String.class, String.class};
			
			Visualizacao<Cliente> visualizacao = new Visualizacao<Cliente>(
					clientes,
					new String[] {"id","email","endereco","telefone","cpf"},
					classes,
					clientes.size(),
					5);
			
			tabela = visualizacao.getTable();
			tabela.addMouseListener(new HabilitarEdicaoExclusao());
			jcp = new JScrollPane(tabela);
			
			btPagamento = new JButton(new AcaoVerTurma());
			
			areaLembrete = new JTextArea("Lembrete sobre a turma selecionada...");
			areaLembrete.setPreferredSize(new Dimension(800, 60));
			areaLembrete.setFont(new Font("Arial", Font.BOLD+Font.ITALIC, 20));

			
			/*ADICAO DOS COMPONENTES NA JANELA*/
			this.add(pLeteralEsq, BorderLayout.WEST);
			this.add(jcp, BorderLayout.CENTER);
			this.add(areaLembrete, BorderLayout.SOUTH);
			
			
			desativarBotoes();
		}
		
		private void desativarBotoes() {
			btPagamento.setEnabled(false);
		}
		
		private void ativarBotoes() {
			btPagamento.setEnabled(true);
		}
		
		protected class HabilitarEdicaoExclusao extends MouseAdapter{
			
			public void mousePressed(MouseEvent e) {
				if (tabela.getSelectedRow() >= 0) {
								
					/*Quero pegar o Id (está na coluna 0) da linha selecionada*/				
					long idSelecionado = (long)tabela.getValueAt(tabela.getSelectedRow(), 0);
					System.out.println(tabela.getSelectedRow());
					ativarBotoes();
				
				}

			}

		}
		
		protected class AcaoVerTurma extends AbstractAction {

			public AcaoVerTurma() {
				super("Ver turma");
				putValue(MNEMONIC_KEY, KeyEvent.VK_T);
				putValue(SHORT_DESCRIPTION, "Visualizar turma selecionada");
			}
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				desativarBotoes();
				
				long valor = Long.valueOf(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 0)));
				
				MenuPrincipal.this.setVisible(false);
			}
			
		}
		
		protected class AcaoNovaTurma extends AbstractAction {

			
			public AcaoNovaTurma() {
				super("Criar nova turma");
				putValue(MNEMONIC_KEY, KeyEvent.VK_B);
				putValue(SHORT_DESCRIPTION, "Criar uma nova turma");
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("AcaoNovaTurma()");
				
			}
			
		}
		
		protected class AcaoEditarTurma extends AbstractAction {

			
			public AcaoEditarTurma() {
				super("Editar turma");
				putValue(MNEMONIC_KEY, KeyEvent.VK_E);
				putValue(SHORT_DESCRIPTION, "Editar turma selecionada");
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				long valor = Long.valueOf(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 0)));
				
				desativarBotoes();
				
				System.out.println("AcaoEditarTurma()");
				
			}
			
		}
		
		protected class AcaoApagarTurma extends AbstractAction {

			
			public AcaoApagarTurma() {
				super("Apagar turma");
				putValue(MNEMONIC_KEY, KeyEvent.VK_B);
				putValue(SHORT_DESCRIPTION, "Apaga a turma selecionada");
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				desativarBotoes();
				
				long valor = Long.valueOf(String.valueOf(tabela.getValueAt(tabela.getSelectedRow(), 0)));

			}
			
		}
		
		protected class AcaoAbrirMenuAtividades extends AbstractAction {
			
			public AcaoAbrirMenuAtividades() {
				super("Atividades");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		}
		
		protected class AcaoAbrirMenuAlunos extends AbstractAction {
			
			public AcaoAbrirMenuAlunos() {
				super("Alunos");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		}
		
		protected class AcaoAbrirMenuFrequencias extends AbstractAction {
			
			public AcaoAbrirMenuFrequencias() {
				super("Frequências");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
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



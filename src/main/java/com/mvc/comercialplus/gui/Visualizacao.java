package com.mvc.comercialplus.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class Visualizacao<T> extends JTable{
	
	JTable tabela;
	DefaultTableCellRenderer df;
	DefaultTableCellRenderer headerRenderer;
	DefaultTableColumnModel columnModel;
	AbstractTableModel tableModel;
	
	public List<T> listaTipo;
	String[] nomeColunas;
	Class<?>[] classesCampos;
	
	int numLinhas, numColunas;
	
	public Visualizacao(List<T> listaTipo, String[] nomeColunas, Class<?>[] classesCampos, int colunas) {
		
		this.tabela = new JTable();
		
		this.listaTipo = listaTipo;
		this.numLinhas = listaTipo.size();
		this.numColunas = colunas;
		this.nomeColunas = nomeColunas;
		this.classesCampos = classesCampos;
		
		df = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable tabela, 
		            Object valor, boolean isSelected, boolean hasFocus, int linha, int coluna){
		    	JLabel label = (JLabel)super.getTableCellRendererComponent(tabela, valor, isSelected, hasFocus, 
		                linha, coluna);

		    	
		    	if(isSelected == true){
		            
		    		//label.setForeground(Color.BLACK);
		            //label.setBackground(corTema);
		            //label.setFont(new Font("Arial", Font.BOLD, tamanhoLetra));
		            this.setBorder(noFocusBorder);    
		            //Border b = new LineBorder(Color.BLACK, 3, false);
		                 
		            //label.setBorder(b);

		              
		        }else{
		        	/*TODO 
		        	 * colocar aqui alguma lógica de deixar em vermelho valores negativos*/
		        	label.setForeground(Color.BLACK);
		            label.setBackground(Color.WHITE);
		            //label.setFont(new Font("Arial", Font.BOLD, tamanhoLetra));
		            
		        }


		       //lembrando que vc pode obter o objeto da linhas correspondente assim
		       //Pessoa pessoa = ((PessoasTableModel)table.getModel()).getValoresPessoa(row);
		       //e fazer os if's direto nos valores do objeto, nesse caso para colorir a linha inteira!!
		        return label; 
			}
		};
		
		headerRenderer = new DefaultTableCellRenderer() {
		    @Override
			public Component getTableCellRendererComponent(JTable tabela, 
		            Object valor, boolean isSelected, boolean hasFocus, int linha, int coluna){
		        
		        JLabel label = (JLabel)super.getTableCellRendererComponent(tabela, valor, isSelected, hasFocus, 
		                linha, coluna);
		        
		        //label.setBackground(Color.BLACK));
		        //label.setFont(new Font("Arial", Font.BOLD, 20));
		        
		        
		        label.setHorizontalAlignment(SwingConstants.CENTER);
		        label.setBorder(BorderFactory.createEtchedBorder());
		        
		        
		        return label;       
		    }
		};
		

		
		tableModel = new AbstractTableModel() {

			@Override
			public int getRowCount() {
				return numLinhas;
			}

			@Override
			public int getColumnCount() {
				return colunas;
			}

			@Override
			public Object getValueAt(int indiceLinha, int indiceColuna) {
				
				Object dado = null;
				
				T tipo = listaTipo.get(indiceLinha);
				
				/* XXX Usando reflexao para descobrir o campo correspondente ao
				 * nome da coluna XXX */
				try {
					Field f = tipo.getClass().getDeclaredField(nomeColunas[indiceColuna]);
					f.setAccessible(true);
					dado = f.get(tipo);
				} catch (NoSuchFieldException | SecurityException e) {
					e.getCause();
					return "Erro";
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return dado;
			}
			
			@Override
			public Class<?> getColumnClass(int indiceColuna) {
				
				return classesCampos[indiceColuna];
				
			}
			
			@Override
			public String getColumnName(int indiceColuna) {
				return nomeColunas[indiceColuna];
				
			}
			
		};
		columnModel = new ViewColumnModel();

		
		tabela.setColumnModel(columnModel);
		tabela.setModel(tableModel);
		
	}
	
	public void adicionarElemento(T t) {
		listaTipo.add(t);
		numLinhas++;
		tableModel.fireTableDataChanged();
	}
	
	public void removerElemento(T t) {
		listaTipo.remove(t);
		numLinhas--;
		tableModel.fireTableDataChanged();
	}
	
	public void removerElementos(List<T> listaGenerica) {
		for(T t : listaGenerica) {
			removerElemento(t);
		}
	}
	
	public T pegarUltimoElemento() {
		return listaTipo.isEmpty() ? null : listaTipo.get(listaTipo.size()-1);
	}
	
	protected class ViewColumnModel extends DefaultTableColumnModel {
		
		public ViewColumnModel() {

		    for(int i=0; i < numColunas; i++) {
		    	TableColumn coluna = new TableColumn(i);
		    	
		    	
		    	//CellRendererDoProf acr = new CellRendererDoProf(tamanhoFonte, corCelulas, alinhamento);
		    	
		        
		    	JTextField campoTexto = new JTextField();
		        coluna.setCellRenderer(df);
		        coluna.setCellEditor(new DefaultCellEditor(campoTexto));
		    	coluna.setHeaderRenderer(headerRenderer);
		        coluna.setHeaderValue(tableModel.getColumnName(i));
		        
		        coluna.setResizable(true);
		        
		        addColumn(coluna);
		    }
		}
	}
	
	
	public JTable getTable() {
		return tabela;
	}
	

}

package com.mvc.comercialplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.comercialplus.model.Produto;
import com.mvc.comercialplus.repository.ProdutoRepository;

@Service
public class ProdutoService implements BdService<Produto>{

	private ProdutoRepository repo;
	
	@Autowired
	public ProdutoService(ProdutoRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public List<Produto> getAll() {
		return repo.findAll();
	}

	@Override
	public Produto getById(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Produto save(Produto objeto) {
		return repo.save(objeto);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
	}
	
	public Produto getByCodigoBarras(String codBarras) {
		return repo.findByCodigoBarras(codBarras);
	}
	
	/**
	 *  Retirado e adaptado do StackoOverflow,
	 *  respondida 7/03/2016 as 3:17.
	 *  Disponivel em:
	 *  https://pt.stackoverflow.com/questions/116712/como-validar-um-c%C3%B3digo-de-barras-padr%C3%A3o-ean-13
	 *  
	 *  @author utluiz
	 */
	public boolean validarEAN13(String codBarras) {
        
		//expressao regular para checar se o codigo e numerico
		//e tem 13 digitos
		if (!codBarras.matches("^[0-9]{13}$")) {
            return false;
        }
		
        int[] numeros = codBarras.chars().map(Character::getNumericValue).toArray();
        int somaPares = numeros[1] + numeros[3] + numeros[5] + numeros[7] + numeros[9] + numeros[11];
        int somaImpares = numeros[0] + numeros[2] + numeros[4] + numeros[6] + numeros[8] + numeros[10];
        int resultado = somaImpares + somaPares * 3;
        int digitoVerificador = 10 - resultado % 10;
        
        return digitoVerificador == numeros[12];
	}

}

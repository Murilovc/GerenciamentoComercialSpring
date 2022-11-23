package com.mvc.comercialplus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mvc.comercialplus.model.Usuario;
import com.mvc.comercialplus.service.UsuarioService;

@Service
public class PerfilUsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioService servico;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = servico.getByNomeUsuario(username);
        return new PerfilUsuario(usuario);
    }
    
}

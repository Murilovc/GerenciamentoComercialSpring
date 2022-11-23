package com.mvc.comercialplus.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mvc.comercialplus.model.Usuario;

public class PerfilUsuario implements UserDetails {

    private Usuario usuario;

    public PerfilUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Talvez seja possivel usar o papel do Usuario criado pelo professor
        //como o role do nosso projeto.

        //SimpleGrantedAuthority auth = new SimpleGrantedAuthority(usuario.getPapel().name());
        //return Arrays.asList(auth);
        
        return null;//Essa linha vai causar erros
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getNomeUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.isAtivo();
    }
    
}

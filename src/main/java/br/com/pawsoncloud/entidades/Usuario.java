package br.com.pawsoncloud.entidades;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade JPA que representa um usuário no banco de dados.
 * 
 * @author Edielson Assis
 */
@JsonIgnoreProperties(value = { "nivelAcesso", "enabled", "accountNonLocked", "authorities", "password", "username", "credentialsNonExpired", "accountNonExpired" })
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Setter(AccessLevel.NONE)
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String telefone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_endereco", referencedColumnName = "id")
    private Endereco endereco;

    @ManyToOne
    @Setter(AccessLevel.NONE)
    @JoinColumn(name = "id_nivel_acesso")
    private NivelAcesso nivelAcesso;

    private boolean ativo;

    /**
     * Retorna as autorizações associadas a este usuário.
     *
     * @return Uma coleção de autorizações concedidas ao usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(nivelAcesso.getNome()));
    }

    /**
     * Retorna a senha associada a este usuário.
     *
     * @return A senha do usuário.
     */
    @Override
    public String getPassword() {
        return senha;
    }

    /**
     * Retorna o username associado a este usuário.
     *
     * @return Username do usuário.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indica se a conta do usuário está expirada.
     *
     * @return Sempre retorna true, indicando que a conta não está expirada.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se a conta do usuário está bloqueada.
     *
     * @return Sempre retorna true, indicando que a conta não está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se as credenciais do usuário estão expiradas.
     *
     * @return Sempre retorna true, indicando que as credenciais não estão
     *         expiradas.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se o usuário está habilitado.
     *
     * @return O estado de ativação do usuário.
     */
    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
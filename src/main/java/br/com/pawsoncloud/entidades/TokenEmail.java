package br.com.pawsoncloud.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "token_email")
public class TokenEmail implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(name = "criado_as")
    private LocalDateTime criadoAs;

    @Column(name = "expirado_as")
    private LocalDateTime expiradoAs;

    @Column(name = "confirmado_as")
    private LocalDateTime confirmadoAs;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public TokenEmail(String token, LocalDateTime criadoAs, LocalDateTime expiradoAs, Usuario usuario) {
        this.token = token;
        this.criadoAs = criadoAs;
        this.expiradoAs = expiradoAs;
        this.usuario = usuario;
    }
}
package com.proz.projetointegrador.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.proz.projetointegrador.dto.UsuarioDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

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

    @Transient
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private final List<Animais> animaisTutorado = new ArrayList<>();

    public Usuario(UsuarioDto usuarioDto) {
        this.nome = usuarioDto.nome();
        this.email = usuarioDto.email();
        this.senha = usuarioDto.senha();
        this.dataNascimento = usuarioDto.dataNascimento();
        this.cpf = usuarioDto.cpf();
        this.telefone = usuarioDto.telefone();
        this.endereco = new Endereco(usuarioDto.enderecoDto());
        this.nivelAcesso = new NivelAcesso(1L);
    }
}
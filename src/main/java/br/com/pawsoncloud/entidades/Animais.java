package br.com.pawsoncloud.entidades;

import java.io.Serializable;

import br.com.pawsoncloud.entidades.enums.StatusAdocao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidade JPA que representa um pet no banco de dados.
 * 
 * @author Edielson Assis
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Animais implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String porte;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false)
    private String especie;
    private String pelagem;

    @Column(nullable = false)
    private Double peso;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_adocao")
    private StatusAdocao status;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private boolean adotado;

    /**
     * Define o identificador (ID) associado a esta instância.
     *
     * @param id O identificador a ser atribuído a esta instância.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Define o status de adoção associado a esta instância.
     *
     * @param status O status de adoção a ser atribuído a esta instância.
     */
    public void setStatus(StatusAdocao status) {
        this.status = status;
    }

    /**
     * Define o estado de adoção associado a esta instância.
     *
     * @param adotado O estado de adoção a ser atribuído a esta instância.
     */
    public void setAdotado(boolean adotado) {
        this.adotado = adotado;
    }
}
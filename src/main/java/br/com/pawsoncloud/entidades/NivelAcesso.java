package br.com.pawsoncloud.entidades;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Entidade JPA que representa um nível de acesso no banco de dados. A classe {@link NivelAcesso} define o nivel de acesso do usuário. Por padrão, o nível de acesso definido é <b>ROLE_USER</b>.
 * isso limita o acesso do usuário a certas partes do sistema. Foi utilizado o padrão singleton na implementação da calsse.
 * Isso faz com que seja gerado apenas uma única instância do construtor.
 * 
 * @author Edielson Assis
 */
@Entity
@Getter
@EqualsAndHashCode(of = "id")
@Table(name = "nivel_acesso")
public class NivelAcesso implements Serializable {

    private static final NivelAcesso INSTANCE = new NivelAcesso(1L, "ROLE_USER");
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Transient
    @OneToMany(mappedBy = "id_nivel_acesso", fetch = FetchType.LAZY)
    private final Set<Usuario> usuarios = new HashSet<>();

    private NivelAcesso(){}

    private NivelAcesso(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    
    /** 
     * Retorna uma instância do nível de acesso. 
     * @return NivelAcesso
     */
    public static NivelAcesso getInstance() {
        return INSTANCE;
    }
}
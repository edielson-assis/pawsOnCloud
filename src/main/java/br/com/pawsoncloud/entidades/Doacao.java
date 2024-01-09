package br.com.pawsoncloud.entidades;

import java.io.Serializable;
import java.time.LocalDate;

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

@Entity
@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Doacao implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.NONE)
    @Column(name = "data_doacao")
    private LocalDate dataDoacao;

    @Setter(AccessLevel.NONE)
    @Column(name = "data_cadastramento")
    private LocalDate dataCadastramento;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_animais", referencedColumnName = "id")
    private Animais pet;
    
    @ManyToOne
    @Setter(AccessLevel.NONE)
    @JoinColumn(name = "id_usuario")
    private Usuario doador;

    @Column(name = "confirmar_doacao")
    private boolean confirmarDoacao;

    private void setDataDoacao() {
        this.dataDoacao = LocalDate.now();
    }

    public void getDataDoacao() {
        setDataDoacao();
    }
}
package models;

import play.db.jpa.GenericModel;

import javax.persistence.*;
@Entity
@Table(schema = "licenciamento", name = "envio_informativo_email")
public class EnvioEmailInfo extends GenericModel {

    private static final String SEQ = "licenciamento.envio_informativo_email_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    public Long id;

    @Column(name = "cpf_cnpj")
    public String cpfCnpj;

    @Transient
    public boolean emailEnviado;

    public EnvioEmailInfo(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public static Boolean findByCpfCnpj(String cpfCnpj){
        return !find("byCpfCnpj", cpfCnpj).fetch().isEmpty();
    }


}

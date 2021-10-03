package models.caracterizacao;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import exceptions.ValidacaoException;
import play.db.jpa.Model;
import utils.WebServiceSefaz;

@Entity
@Table(schema = "licenciamento", name = "taxa_administrativa_dae")
public class TaxaAdministrativaDae extends Model {

    @Column(name = "ano")
    public Integer ano;

    @Column(name = "valor")
    public Double valor;

    @Column(name = "isento")
    public Boolean isento;

    @Column(name = "atividade_dispensavel")
    public Boolean atividadeDispensavel;

    @Column(name = "atividade_licenciavel")
    public Boolean atividadeLicenciavel;

    @Column(name = "link_taxas_licenciamento")
    public String linkTaxasLicenciamento;

    public static TaxaAdministrativaDae getTaxaAtual(Caracterizacao caracterizacao) {

        // TODO: AJUSTAR NO CONFIGURADOR O VALOR DA TAXA
        int ano = Calendar.getInstance().get(Calendar.YEAR);

        TaxaAdministrativaDae taxa = find("ano = :ano")
                .setParameter("ano", ano)
                .first();

        if (taxa == null) {

             throw new ValidacaoException("A taxa administrativa "
                     + "para o ano de " + ano + " não está cadastrada.");

        }

        boolean licencasPermitidas = WebServiceSefaz.islicencasPermitidas(caracterizacao);

        if (caracterizacao.tipoLicenca.id.equals(TipoLicenca.DISPENSA_LICENCA_AMBIENTAL)) {
            taxa.valor = 30.0;
        } else if (licencasPermitidas) {
            taxa.valor = 100.0;
        }

        caracterizacao.valorTaxaAdministrativa = taxa.valor;
//        caracterizacao._save();

        return taxa;
    }

    public static Double findValorAtual(Caracterizacao caracterizacao) {

        TaxaAdministrativaDae taxa = getTaxaAtual(caracterizacao);

        return taxa.valor;
    }

}
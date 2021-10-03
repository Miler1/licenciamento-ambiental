package models.caracterizacao;

import play.db.jpa.Model;

import javax.persistence.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(schema = "licenciamento", name = "taxa_licenciamento")
public class TaxaLicenciamento extends Model {

    @ManyToOne
    @JoinColumn(name = "id_porte_empreendimento")
    public PorteEmpreendimento porteEmpreendimento;

    @ManyToOne
    @JoinColumn(name = "id_potencial_poluidor")
    public PotencialPoluidor potencialPoluidor;

    @ManyToOne
    @JoinColumn(name = "id_tipo_licenca")
    public TipoLicenca tipoLicenca;

    @Column(name = "codigo")
    public Integer codigo;

    @Column(name = "valor")
    public String valor;

    public static Double getTaxaLicenca(Caracterizacao caracterizacao, TipoLicenca tipoLicenca) {

        AtividadeCaracterizacao atividadeCaracterizacao = caracterizacao.getAtividadeCaracterizacaoMaiorPotencialPoluidorEPorte();

        List<TaxaLicenciamento> taxa = atividadeCaracterizacao.atividade.taxasLicenca.stream().
                filter(tx -> tx.filterCurrentTaxa(tipoLicenca, atividadeCaracterizacao)).collect(Collectors.toList());

        if (taxa.isEmpty()) {
            throw new RuntimeException("Não há taxa de licenciamento vinculada à caracterização");
        }
        if (taxa.size() > 1) {
            throw new RuntimeException("Permitida apenas uma licença por caracterização");
        }

        return resolverFormula(taxa.get(0).valor, atividadeCaracterizacao.atividadeCaracterizacaoParametros);

    }

    private boolean filterCurrentTaxa(TipoLicenca tipoLicenca, AtividadeCaracterizacao ac) {
        return this.tipoLicenca.id.equals(tipoLicenca.id) &&
                this.porteEmpreendimento.id.equals(ac.porteEmpreendimentoParaCalculoDoPorte.id) &&
                this.potencialPoluidor.id.equals(ac.atividade.potencialPoluidor.id);
    }

    private static Double resolverFormula(String formula, List<AtividadeCaracterizacaoParametros> parametros) {

        String[] elementos = formula.split(" ");
        String[] novosElementos = elementos.clone();

        for (int i = 0; i < elementos.length; i++) {
            for (AtividadeCaracterizacaoParametros acp : parametros) {
                if (acp.parametroAtividade.codigo.equals(elementos[i])) {
                    novosElementos[i] = acp.valorParametro.toString();
                }
            }
        }

        return resolveTaxa(novosElementos);
    }

    private static Double resolveTaxa(String[] elementos) {

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

        try {
            return (Double) scriptEngine.eval(String.join("", elementos));
        } catch (ScriptException se) {
            throw new RuntimeException(se.getMessage(), se.getCause());
        }
    }

    public static Double calcular(Caracterizacao caracterizacao, TipoLicenca tipoLicenca) {

        Double taxaLicenca;

        if (caracterizacao.valorTaxaLicenciamento > 0) {
            taxaLicenca = caracterizacao.valorTaxaLicenciamento;
        } else {
            taxaLicenca = getTaxaLicenca(caracterizacao, tipoLicenca);
        }

        return taxaLicenca + caracterizacao.valorTaxaAdministrativa;

    }

}
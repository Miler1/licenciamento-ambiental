package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class TipoLicencaBuscaVOSerializer {

    public static JSONSerializer findTipoLicenca = SerializerUtil.create(
            "tipoLicencaSolicitacao.id",
            "tipoLicencaSolicitacao.validadeEmAnos",
            "tipoLicencaSolicitacao.nome",
            "tipoLicencaSolicitacao.sigla",
            "tipoLicencaSolicitacao.finalidade",
            "tipoLicencaSolicitacao.isento",
            "tipoLicencaSolicitacao.selecionado",
            "tipoLicencaSolicitacao.licencasFilhas.id",
            "tipoLicencaSolicitacao.licencasFilhas.validadeEmAnos",
            "tipoLicencaSolicitacao.licencasFilhas.nome",
            "tipoLicencaSolicitacao.licencasFilhas.sigla",
            "tipoLicencaSolicitacao.licencasFilhas.finalidade",
            "tipoLicencaSolicitacao.licencasFilhas.isento",
            "tipoLicencaSolicitacao.licencasFilhas.selecionado",

            "tipoLicencaRenovacao.id",
            "tipoLicencaRenovacao.validadeEmAnos",
            "tipoLicencaRenovacao.nome",
            "tipoLicencaRenovacao.sigla",
            "tipoLicencaRenovacao.finalidade",
            "tipoLicencaRenovacao.isento",
            "tipoLicencaRenovacao.selecionado",
            "tipoLicencaRenovacao.licencasFilhas.id",
            "tipoLicencaRenovacao.licencasFilhas.validadeEmAnos",
            "tipoLicencaRenovacao.licencasFilhas.nome",
            "tipoLicencaRenovacao.licencasFilhas.sigla",
            "tipoLicencaRenovacao.licencasFilhas.finalidade",
            "tipoLicencaRenovacao.licencasFilhas.isento",
            "tipoLicencaRenovacao.licencasFilhas.selecionado",

            "tipoLicencaAtualizacao.id",
            "tipoLicencaAtualizacao.validadeEmAnos",
            "tipoLicencaAtualizacao.nome",
            "tipoLicencaAtualizacao.sigla",
            "tipoLicencaAtualizacao.finalidade",
            "tipoLicencaAtualizacao.isento",
            "tipoLicencaAtualizacao.selecionado",
            "tipoLicencaAtualizacao.licencasFilhas.id",
            "tipoLicencaAtualizacao.licencasFilhas.validadeEmAnos",
            "tipoLicencaAtualizacao.licencasFilhas.nome",
            "tipoLicencaAtualizacao.licencasFilhas.sigla",
            "tipoLicencaAtualizacao.licencasFilhas.finalidade",
            "tipoLicencaAtualizacao.licencasFilhas.isento",
            "tipoLicencaAtualizacao.licencasFilhas.selecionado",

            "tipoLicencaCadastro.id",
            "tipoLicencaCadastro.validadeEmAnos",
            "tipoLicencaCadastro.nome",
            "tipoLicencaCadastro.sigla",
            "tipoLicencaCadastro.finalidade",
            "tipoLicencaCadastro.isento",
            "tipoLicencaCadastro.selecionado"
    ).exclude("*");
}

# --- !Ups

ALTER TABLE licenciamento.codigos_taxa_licenciamento ADD COLUMN id_usuario_licenciamento INTEGER;
ALTER TABLE licenciamento.codigos_taxa_licenciamento ADD COLUMN data_cadastro timestamp without time zone;
ALTER TABLE licenciamento.codigos_taxa_licenciamento ADD COLUMN ativo BOOLEAN DEFAULT 'true';

COMMENT ON COLUMN licenciamento.codigos_taxa_licenciamento.id_usuario_licenciamento IS 'Identificador único da entidade usuario_licenciamento.';
COMMENT ON COLUMN licenciamento.codigos_taxa_licenciamento.data_cadastro IS 'Data de cadastro do codigos_taxa_licenciamento.';
COMMENT ON COLUMN licenciamento.codigos_taxa_licenciamento.ativo IS 'Determina se um item codigos_taxa_licenciamento está ativo ou inativo';

ALTER TABLE licenciamento.codigos_taxa_licenciamento ADD COLUMN codigo VARCHAR(40);

COMMENT ON COLUMN licenciamento.codigos_taxa_licenciamento.codigo IS 'Código da tabela de valores.';

UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-A' WHERE descricao = 'Tabela A - Anexo II';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-B' WHERE descricao = 'Tabela B - Anexo III';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-C' WHERE descricao = 'Tabela C - Anexo III';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-D' WHERE descricao = 'Tabela D - Anexo III';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-E' WHERE descricao = 'Tabela E - Anexo III';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-F' WHERE descricao = 'Tabela F - Anexo III';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-G' WHERE descricao = 'Tabela G - Anexo IV';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-H' WHERE descricao = 'Tabela H - Anexo IV';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-I' WHERE descricao = 'Tabela I - Anexo IV';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-J' WHERE descricao = 'Tabela J - Anexo V';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-K' WHERE descricao = 'Tabela K - Anexo VI';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-L' WHERE descricao = 'Tabela L - Anexo VIII';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-M' WHERE descricao = 'Tabela M - Anexo VIII';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-N' WHERE descricao = 'Tabela N - Anexo VIII';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-ISENTA' WHERE descricao = 'Tabela de isenção';
UPDATE licenciamento.codigos_taxa_licenciamento SET codigo = 'TAB-EXCEPCIONAL' WHERE descricao = 'TABELA de códigos excepcionais';

# --- !Downs

ALTER TABLE licenciamento.rel_tipo_licenca_grupo_documento DROP COLUMN codigo;

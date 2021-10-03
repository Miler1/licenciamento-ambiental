# --- !Ups

UPDATE licenciamento.tipo_documento 
SET nome = 'DAR - Documento de Arrecadação Avulso.', caminho_pasta = 'dar', prefixo_nome_arquivo = 'dar' 
WHERE nome = 'DAE - Documento de Arrecadação Estadual.';


# --- !Downs

UPDATE licenciamento.tipo_documento 
SET nome = 'DAE - Documento de Arrecadação Estadual.', caminho_pasta = 'dae', prefixo_nome_arquivo = 'dae' 
WHERE nome = 'DAR - Documento de Arrecadação Avulso.';

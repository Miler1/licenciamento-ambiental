# PLAY SHELL UTILS

Play-shell-utils é um projeto que busca oferecer diversas funções utilitárias
de shell script, comumente utilizadas nos projetos PlayFramework para automação 
de tarefas. Dentre os recursos oferecidos estão desde funções utilizadas para 
exibir mensagens ou capturar entradas do usuário no terminal até procedimentos 
mais complexos, como o de deploy da aplicação.


# 1 - Configuração


## 1.1 - Adicionando o play-shell-utils à um projeto

O play-shell-utils deve ser adicionado à outro projeto como um submódulo. Um 
submódulo funciona como se você importasse um projeto para dentro de outro, 
porém continua sendo um projeto separado, com seu próprio repositório.

Para configurar play-shell-utils, primeiramente acesse a pasta na qual deseja
importar o submódulo (recomenda-se a pasta "raiz_do_projeto/scripts"). Em 
seguida e execute o comando:

    git submodule add git@gitlab.ti.lemaf.ufla.br:utils/play-shell-utils.git

Após a execução é possível observar que o diretório "play-shell-utils" foi 
criado com os códigos fonte deste projeto. Em seguida, entre na pasta 
"play-shell-utils" e execute o comando:

    git submodule init

Este comando criará uma pasta oculta ".git" na pasta do submódulo e ele será
tratado como submódulo pelo projeto.

Em seguida, retorne a pasta raiz de seu projeto e efetue o commit e push. Apesar
da pasta do submódulo ser listado no commit, o seu conteúdo não será "commitado".
O seu projeto passará a referenciar o módulo adicionado.

Para saber mais sobre submódulos acesse [https://git-scm.com/book/pt-br/v1/Ferramentas-do-Git-Subm%C3%B3dulos](https://git-scm.com/book/pt-br/v1/Ferramentas-do-Git-Subm%C3%B3dulos).


## 1.2 - Efetuando o clone de um projeto que já utiliza o play-shell-utils

Se você acabou de clonar um projeto que já utiliza o play-shell-utils como 
submódulo, a pasta "play-shell-utils" estará vazia. Para inicializar o submódulo 
para poder utilizá-lo, acesse a pasta "play-shell-utils" do seu projeto e 
execute os comandos abaixo:

    git submodule init
    git submodule update

Em seguida, verifique que a pasta "play-shel-utils" foi preenchida com os 
arquivos deste submódulo.


## 1.3 - Atualizando o submódulo play-shell-utils de um projeto

Caso o play-shell-utils tenha evoluído, com novas funcionalidades e correções, 
para atualizá-lo em um projeto basta entrar na pasta "play-shell-utils", onde
o submódulo foi importado, e executar:

    git pull


# 2 - Funcionalidades


## 2.1 - Script de deploy de aplicações

Para o play-shell-utils é necessário que, em sua aplicação, seja criado um 
arquivo "deploy.sh". (recomenda-sea  criação na pasta $ROOT_FOLDER/scripts, mas 
pode ser usado outra pasta, como detalhado no --help-deploy)

Nesse script "deploy.sh" é setado as configurações específicas de sua aplicação. 
Esse arquivo deve importar o arquivo "play-shell-utils.sh" e chamar a execução 
da function `dp_execute_deploy_procedure "$1"` que lê as configurações 
específicas da sua aplicação e executa o procedimento de deploy. Se necessário, 
configurar a variável "ROOT_FOLDER".

Segue um exemplo simples:

    #!/bin/bash

    # Caso o play-shell-utils não esteja no caminho raiz_do_projeto/scripts/play-shell-utils, antes de de 
    # tudo deve-se configurar a variável "ROOT_FOLDER" com o caminho relativo para a pasta raiz do projeto 
    # a partir da pasta "play-shell-utils". Exemplo: 
    # ROOT_FOLDER="caminho/relativo/para/pasta/raiz"

    source "play-shell-utils/play-shell-utils.sh"
    
    config_project_name="analise-pa"
    config_play_version='1.2.7'
    # Demais opções de customização disponíveis no --help-deploy do script play-shell-utils.sh, vide tópico abaixo

    ### Deploy
    dp_execute_deploy_procedure "$1"

[Exemplo de arquivo deploy.sh (Receptor do CAR - Pará)](http://gitlab.ti.lemaf.ufla.br/car-pa/receptor/blob/master/scripts/deploy.sh)

Além do arquivo deploy.sh a ser criado, é necessário setar as configurações 
específicas de cada ambiente de deploy (teste, homologação, produção, etc..). 
Para tal, crie um arquivo [AMBIENTE].sh para cada ambiente dentro da pasta 
$ROOT_FOLDER/scripts/conf (recomendado a criação nesse caminho, mas também pode 
ser usado outro caminho e sobrescrita a configuração).

[Exemplo destes arquivos de configuração (Receptor do CAR - Pará).](http://gitlab.ti.lemaf.ufla.br/car-pa/receptor/tree/master/scripts/conf)    


# 3 - Ajuda

O arquivo [play-shell-utils.sh](http://gitlab.ti.lemaf.ufla.br/utils/play-shell-utils/blob/master/play-shell-utils.sh) possui um help. Execute o comando `./play-shell-utils.sh --help` para acesso a ajuda.
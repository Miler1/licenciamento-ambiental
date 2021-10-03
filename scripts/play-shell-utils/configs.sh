#!/bin/bash


### USER INTERFACE

config_msg_prefix=" >> "
config_msg_prefix_color=$color_green


### PROJETO


# @config_project(config_project_name, type: string) 
# Nome do projeto a ser utilizado para gerar o nome do arquivo zip da release. 
# Evite utilizar espaços e caracteres especiais.
config_project_name=""

# @config_project(config_path_dist, type: string, default: "$ROOT_FOLDER/dist")
# Caminho para a pasta onde será gerada a release para deploy. De preferência 
# deve ser uma pasta ignorada pelo git. 
config_path_dist="$ROOT_FOLDER/dist"

# @config_project(config_path_backend, type: string, default:"$ROOT_FOLDER/backend")
# Caminho para a pasta onde se encontra o projeto do Play! (backend)
config_path_backend="$ROOT_FOLDER/backend"

# @config_project(config_path_frontend, type: string, default: "$ROOT_FOLDER/frontend")
# Caminho para a pasta onde se encontra o projeto frontend.
config_path_frontend="$ROOT_FOLDER/frontend"

# @config_project(config_environment_config_path, type: string, default: "$ROOT_FOLDER/scripts/conf/<env>.sh")
# Padrão de caminho para encontrar os arquivos de configuração relacionados aos 
# ambientes. Deve-se utilizar o padrão <env> para inserir o nome do ambiente em 
# questão. Por exemplo: caso execute o comando de deploy passando o ambiente 
# "homolog", por padrão, deve existir um arquivo de configuração 
# "$ROOT_FOLDER/scripts/conf/homolog.sh".
config_environment_config_path="$ROOT_FOLDER/scripts/conf/<env>.sh"


### BUILD DA APLICAÇÃO


# @config_build(config_backend_precompile, type: boolean, default: false)
# Indica se ao executar o deploy o código fonte deve ser pré-compilado. Caso 
# configurado como true, o script de deploy executará "play precompile" para o 
# projeto backend e o código fonte não será inserido na release gerada.
config_backend_precompile=false

# @config_build(config_backend_encrypt_code, type: boolean, default: false)
# Indica se o código fonte, após pré-compilado, também será criptografado. Caso 
# seja habilitado, deve-se configurar também o valor de 
# "config_backend_code_encryption_key". Para utilizar a criptografia, o projeto 
# backend deve possuir um arquivo <backend>/conf/play.plugins e também uma 
# classe implementando um classloader customizado em <backend>/plugins/a.java. 
# Observar a implementação no módulo de Análise do CAR.
config_backend_encrypt_code=false

# @config_build(config_backend_code_encryption_key, type: string)
# Chave de criptografia em caso de utilização da criptografia do código fonte. 
# Ver a configuração "config_backend_encrypt_code".
config_backend_code_encryption_key=""

# @config_build(config_backend_include_app_conf, type: boolean, default: true)
# Indica se deve ser incluído no arquivo application.conf da aplicação Play um 
# arquivo específico para o ambiente. Ver também a configuração 
# "config_backend_app_conf_include_path".
config_backend_include_app_conf=true

# @config_build(config_backend_app_conf_include_path, type: string, default: "application_<env>.conf")
# Caminho para o arquivo de configuração do ambiente, sendo este caminho 
# relativo à pasta "conf" do projeto. Pode-se utilizar o padrão <env> para 
# inserir o nome do ambiente no caminho. Ex.: utilizando o valor padrão 
# "application_<env>.conf", o arquivo de configuração do ambiente "producao" 
# deve estar contido em "conf/application_producao.conf", e será inserido no 
# application.conf do projeto a linha:
#     @include.production=application_producao.conf
config_backend_app_conf_include_path="application_<env>.conf"

# @config_build(config_backend_static_folders, type: array, default: ("conf" "lib" "public" "modules"))
# Array contendo quais são as pastas de arquivos estáticos do projeto 
# (arquivos que não são do formato .java), que serão apenas copiadas para a 
# release.
config_backend_static_folders=("conf" "lib" "public" "modules")

# @config_build(config_backend_copy_modules_source_code, type: boolean, default: true)
# Quando um módulo do Play é instalado em uma aplicação, em alguns casos o Play 
# já possui o módulo em sua pasta interna, e no projeto é inserido apenas um 
# arquivo na pasta modulos (ex.: modules/pdf-0.9) contendo o caminho para a 
# pasta do módulo. Este caminho pode não estar coerente com as configurações do 
# servidor para o qual será executado o deploy. Assim, se esta configuração 
# estiver ativa, este arquivo será substituído por uma cópia da pasta do módulo 
# que ele referencia. Assim a release conterá a pasta do módulo com seus códigos 
# fonte.
config_backend_copy_modules_source_code=true


# @config_build(config_build_frontend, type: boolean, default: true)
# Indica se será efetuago o build do frontend do projeto. Configurado como false
# quando o projeto não possui frontend.
config_build_frontend=true

# @config_build(config_grunt_cmd, type: string)
# Configura qual o comando a ser executado para efetuar o build do frontend, 
# caso utilize grunt. Por padrão o comando executado é "grunt nome_do_ambiente".
config_grunt_cmd=""

# @config_build(config_gulp_cmd, type: string)
# Configura qual o comando a ser executado para efetuar o build do frontend, 
# caso utilize gulp. Por padrão o comando executado é "gulp nome_do_ambiente".
config_gulp_cmd=""

# @config_build(config_compile_frontend_cmd, type: string)
# Configura qual o comando a ser executado para efetuar o build do frontend. 
# Por padrão o script tenta executar o grunt ou gulp, de acordo com o projeto.
config_compile_frontend_cmd=""


### VERSÃO DA RELEASE 


# @config_release_version(config_require_version_name, type: boolean, default: true)
# Indica se o script deve perguntar ao usuário o nome/número da versão da 
# release a ser gerada.
config_require_version_name=true

# @config_release_version(config_use_last_tag_as_version_name, type: boolean, default: false)
# Indica se o nome da última tag local deve ser utilizada como nome/número da 
# versão da release a ser gerada. Esta configuração só será considerada se a 
# configuração "config_require_version_name" estiver inativa.
config_use_last_tag_as_version_name=false

# @config_release_version(config_use_branch_as_version_name, type: boolean, default: false)
# Indica se o nome da branch local deve ser utilizada como nome/número da 
# versão da release a ser gerada. Esta configuração só será considerada se a 
# configuração "config_require_version_name" estiver inativa.
config_use_branch_as_version_name=false

# @config_release_version(config_backend_version_property, type: string, default: "server.version")
# Propriedade a ser inserida no arquivo application.conf da release para 
# armazenar o nome/número da versão da release gerada. Por exemplo, se o valor 
# configurado for "server.version" e o nome da versão informado for "v1.2.2", 
# será inserida no appplication.conf a linha: 
#     server.version=v1.2.2
config_backend_version_property="server.version"

# @config_release_version(config_backend_update_property, type: string, default: "server.update")
# Propriedade a ser inserida no arquivo application.conf da release para 
# armazenar a data de geração da release. Por exemplo, se o valor configurado 
# for "server.update" será inserida uma linha no application.conf parecida com:
#    server.update=15/08/2017 11:07
config_backend_update_property="server.update"

# @config_release_version(config_backend_deploy_user_gitname_property, type: string, default: "deploy.user.gitName")
# Propriedade a ser inserida no arquivo application.conf da release para 
# armazenar o nome do git do usuário que gerou a release. Por exemplo, se o 
# valor configurado for "deploy.user.gitName" e o usuário que gerou a release
# tiver seu nome configurado no git (local) como "João Silva", será inserida 
# uma linha no application.conf parecida com:
#    deploy.user.gitName=João Silva
config_backend_deploy_user_gitname_property="deploy.user.gitName"

# @config_release_version(config_backend_deploy_user_gitemail_property, type: string, default: "deploy.user.gitEmail")
# Propriedade a ser inserida no arquivo application.conf da release para 
# armazenar o email do git do usuário que gerou a release. Por exemplo, se o 
# valor configurado for "deploy.user.gitEmail" e o usuário que gerou a release
# tiver seu email configurado no git (local) como "jsilva@ufla.br", será 
# inserida uma linha no application.conf parecida com:
#    deploy.user.gitEmail=jsilva@ufla.br
config_backend_deploy_user_gitemail_property="deploy.user.gitEmail"

# @config_release_version(config_backend_deploy_user_mac_address_property, type: string, default: "deploy.user.macAddress")
# Propriedade a ser inserida no arquivo application.conf da release para 
# armazenar o endereço mac da máquina onde a release foi gerada. Por exemplo, 
# se o valor configurado for "deploy.user.macAddress", será inserida uma 
# linha no application.conf parecida com:
#    deploy.user.macAddress=02:42:86:54:97:de
config_backend_deploy_user_mac_address_property="deploy.user.macAddress"


### SERVIDOR

# @config_server(config_update_server_app, type: boolean, default: false)
# Indica se o script deve atualizar uma aplicação em execução no servidor.
config_update_server_app=false

# @config_server(config_server_enable_sudo_cmds, type: boolean, default: true)
# Indica se é permitido executar comandos utilizando sudo no servidor. Em caso
# de valor "false", toas as outras configurações de servidor a respeito de
# utilizar ou não sudo serão desconsideradas.
config_server_enable_sudo_cmds=true

# @config_server(config_clean_app_folder_on_deploy, type: boolean, default: true)
# Indica se a pasta atual da aplicação será removida para ser substituída pela
# pasta da release (valor: true) ou se os arquivos da release serão apenas 
# copiados sobre os pré-existentes (valor: false), preservando intactos os que 
# não estiverem contidos na release.
config_clean_app_folder_on_deploy=true

# @config_server(config_clean_app_subfolders_on_deploy, type: array, default: ("app"))
# Array contendo as pastas a serem removidas antes dos arquivos da release serem
# copiados sobre os pré-existentes, não preservando nenhum arquivo antigo nestas 
# pastas.
# Obs.: configuração disponível apenas se config_clean_app_folder_on_deploy=false
config_clean_app_subfolders_on_deploy=("app")

# @config_server(config_clean_app_folder_as_sudo, type: boolean, default: true)
# Indica se, ao limpar as pastar ou subpastas da aplicação, deve-se executar os
# comandos como sudo. Ver também "config_clean_app_folder_on_deploy" e
# "config_clean_app_subfolders_on_deploy"
config_clean_app_folder_as_sudo=true

# @config_server(config_copy_release_to_server, type: boolean, default: true)
# Indica se a release gerada deve ser copiada para o servidor. Normalmente ativa
# para ambientes de produção e homologação, onde a versão será apenas copiada
# para algum local e será posteriormente efetuado o deploy manualmente.
config_copy_release_to_server=true

# @config_server(config_use_app_repository_server, type: boolean, default: false)
# Indica se a release gerada deve ser copiada primeiramente em um servidor
# repositório de releases para depois baixado a partir de outro servidor (de
# homologação, por exemplo).
config_use_app_repository_server=false

# @config_server(config_app_repository_server_ssh, type: string)
# Endereço utilizado para conectar ao servidor repositório de releases via ssh.
# Exemplo: "lemaf@java2.ti.lemaf.ufla.br"
# Obs.: configuração disponível apenas se config_use_app_repository_server=true
config_app_repository_server_ssh=""

# @config_server(config_app_repository_server_release_folder_path, type:string)
# Caminho no servidor repositório de releases para a pasta para a qual a release 
# compactada será copiada.
# Exemplo: "/home/lemaf/releases/releases_homolog"
# Obs.: configuração disponível apenas se config_use_app_repository_server=true
config_app_repository_server_release_folder_path=""

# @config_server(config_app_repository_server_download_release_url, type:string)
# URL de download da release localizada no servidor repositório. O nome do 
# arquivo da release será concatenado ao final da URL.
# Exemplo: "http://car.ti.lemaf.ufla.br/releases/"
# Obs.: configuração disponível apenas se config_use_app_repository_server=true
config_app_repository_server_download_release_url=""

# @config_server(config_remote_app_server_url, type:string)
# URL a ser utilizada pelo usuário para acessar um servidor remoto (que não 
# possa ser acessado por este script) para efetuar o deploy. Utilizado se 
# config_use_app_repository_server=true, para instruir o usuário sobre 
# procedimentos de deploy neste servidor.
# Exemplo: "http://www.servidordehomolgacao.com"
# Obs.: configuração disponível apenas se config_use_app_repository_server=true
config_remote_app_server_url=""

# @config_server(config_remote_app_server_extra_instructions, type:string)
# Texto extra a ser exibido ao usuário caso utilize o deploy em servidor remoto
# que não possa ser acessado por este script. Este texto é exibido ao final da
# execução do script e pode ser utilizado para adicionar instruções ao usuário.
# Obs.: configuração disponível apenas se config_use_app_repository_server=true
config_remote_app_server_extra_instructions=""

# @config_server(config_server_ssh, type: string)
# Endereço utilizado para conectar ao servidor via ssh.
# Exemplo: "lemaf@java2.ti.lemaf.ufla.br"
config_server_ssh=""

# @config_server(config_server_release_folder_path, type:string)
# Caminho no servidor para a pasta para a qual a release compactada será copiada.
# Exemplo: "/home/lemaf/releases/releases_homolog"
config_server_release_folder_path=""

# @config_server(config_server_app_path, type:string)
# Utilizada apenas quando config_update_server_app=true, esta 
# configuração armazena o caminho no servidor para a pasta onde está localizada
# a aplicação a ser atualizada. Deve conter o caminho completo até a pasta
# backend do projeto, incluindo a mesma.
# Exemplo: "/var/play/car/federal/analise/backend"
config_server_app_path=""

# @config_server(config_server_app_backup_folder_path, type:string)
# Utilizada apenas quando config_update_server_app=true, contém a pasta
# onde será armazenado um backup da aplicação ativa no servidor antes que a
# mesma seja atualizada. Caso esta configuração esteja vazia, o backup não será
# realizado.
# Exemplo: "/var/play/car/federal/analise/backups"
config_server_app_backup_folder_path=""

# @config_server(config_server_app_stop_cmd, type:string)
# Comando utilizado no servidor para parar a execução da aplicação. Caso não
# configurado será executado "service $config_project_name stop". Não deve ser
# inserido "sudo" no comando pois ele já será executado como root por padrão.
config_server_app_stop_cmd=""

# @config_server(config_server_app_start_cmd, type:string)
# Comando utilizado no servidor para iniciar a execução da aplicação. Caso não
# configurado será executado "service $config_project_name start". Não deve ser
# inserido "sudo" no comando pois ele já será executado como root por padrão.
config_server_app_start_cmd=""

# @config_server(config_server_force_kill_app_process, type: boolean, default: true)
# Ao parar a aplicação no servidor para atualizá-a, caso não consiga parar
# sua execução utilizando o comando configurado (config_server_app_stop_cmd),
# esta configuração indica se o script deve forçar esta finalização,
# utilizando "kill -9" para "matar" o processo.
config_server_force_kill_app_process=true

# @config_server(config_stop_start_app_as_sudo, type: boolean, default: true)
# Indica se, ao inicializar a aplicação, finalizar a aplicação ou matar o
# processo será utilizado "sudo".
config_stop_start_app_as_sudo=true

# @config_server(config_ssh_save_passwords, type: boolean, default: true)
# Indica se a senha utilizada para conectar no servidor via ssh deve ser
# armazenada na máquina local para evitar futuras solicitações da mesma
# senha.
config_ssh_save_passwords=true

# @config_server(config_server_available_variables, type: array)
# Array contendo os nomes das variáveis a serem disponibilizadas nos 
# procedimentos efetuados no servidor. Estas variáveis estarão disponíveis em 
# funções como "dp_before_start_app_on_server"
config_server_available_variables=""

# @config_server(config_server_app_folder_owner, type:string)
# Grupo e usuário (grupo:user) que deve ser proprietário da pasta e arquivos 
# da aplicação no servidor. Caso esta configuração não esteja vazia, o 
# grupo/usuário será configurado como dono da pasta e seus arquivos.
# Exemplo de configuração: config_server_app_folder_owner="root:root"
config_server_app_folder_owner=""


### PLAY


# @config_play(config_play_version, type:string)
# Número da versão do Play utilizado no projeto. Caso o script utilize o Play,
# por exemplo quando for precompilar o código, este número de versão será
# utilizado para encontrar o Play na máquina local. A busca será efetuada em
# pastas mais comumente utilizadas e, caso não encontrado, o script tentará
# inferir o último digito da versão. 
# Exemplo: Se configurado  "config_play_version=1.2.7", o Play será procurado
# em "/opt/play-1.2.7/play", "/opt/play/play-1.2.7/play", etc. Caso não seja
# encontrado, a busca será repetida com as versões 1.2.7.1, 1.2.7.2, 1.2.7.3 ...
# Caso nenhum valor seja atribuído, o script executará simplesmente o comando
# "play", o que não garante que seja a versão correta para a aplicação.
config_play_version=

#!/bin/bash

VERSION="1.0.0"

UTILS_SELF_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ -z "$ROOT_FOLDER" ]; then
	ROOT_FOLDER="$UTILS_SELF_DIR/../.."
	ROOT_FOLDER="$( cd "$ROOT_FOLDER" && pwd )"
fi

SCRIPT_NAME=`basename "$0"`
CLASS_ENCRYPTION_JAR="$UTILS_SELF_DIR/classEncryption.jar"
SAVED_INFO_FOLDER_PATH="$HOME/.play_shell_utils"

source "$UTILS_SELF_DIR/ui_utils.sh"
source "$UTILS_SELF_DIR/defaults.sh"
source "$UTILS_SELF_DIR/configs.sh"
source "$UTILS_SELF_DIR/play_utils.sh"
source "$UTILS_SELF_DIR/ssh_utils.sh"
source "$UTILS_SELF_DIR/deploy_utils.sh"
source "$UTILS_SELF_DIR/deploy_server_utils.sh"
source "$UTILS_SELF_DIR/doc_sources.sh"
source "$UTILS_SELF_DIR/git_utils.sh"
source "$UTILS_SELF_DIR/net_utils.sh"

# Exibe uma mensagem de erro no terminal, imprime no log (LOG_FILE)
# e finaliza o script (exit).
# Uso: error "Minha mensagem de erro"
error() {

	ui_show_error "$1"
	echo ""

	if [ ! -z "$LOG_FILE" ] && [ -f "$LOG_FILE" ]; then
		echo "------ LOG:"
		tail -n 20 $LOG_FILE
	fi

	exit 1
}


error_help() {

	ui_show_error "$1"
	ui_show_help

	exit 1
}


utils_create_saved_info_folder() {

	if [ ! -d  "$SAVED_INFO_FOLDER_PATH" ]; then

		mkdir $SAVED_INFO_FOLDER_PATH
		validate_cmd_result "Não foi possível criar a pasta \"$SAVED_INFO_FOLDER_PATH\""
	fi
}


utils_require_command() {

	command -v "$1" >/dev/null 2>&1 || { 
		error "O comando '$1' é necessário e não foi encontrado. Instale-o com seu gerenciador de pacotes e execute este script novamente."
	}
}


utils_show_script_title() {
	echo -e "\n\n\n"
	ui_print_line $color_green
	echo ""
	ui_print "  PLAY SHELL UTILS $VERSION - scripts utilitários para aplicações Play" $color_green

	if [ ! -z "$1" ]; then
		echo ""
		ui_print "  > $1" $color_green
	fi

	echo ""
	ui_print_line $color_green
	echo ""
}


utils_show_help() {
	echo "  Play-shell-utils é um projeto que busca oferecer diversas funções utilitárias
  de shell script, comumente utilizadas nos projetos PlayFramework para 
  automação de tarefas. Dentre os recursos oferecidos estão desde funções 
  utilizadas para exibir mensagens ou capturar entradas do usuário no terminal 
  até procedimentos mais complexos de deploy.

  Seguem as opções disponíveis até agora:
"
  ui_print "  --help" $color_green
  echo -e "
    Exibe este texto de ajuda.\n"

  ui_print "  --help-deploy" $color_green
  echo -e "
    Exibe a ajuda para a utilização dos procedimentos de deploy.\n"

  ui_print "  --help-configure-remote-server" $color_green
  echo -e "
    Exibe a ajuda para a configuração do servidor da aplicação para o deploy, 
    quando este não é acessível via ssh.\n"


  ui_print "  --list-ssh-saved-servers" $color_green
  echo -e "
    Lista os servidores para os quais o play-shell-utils armazenou localmente
    a senha para conexão via SSH. Estas senhas são utilizadas nos procedimentos
    de deploy, para que não seja solicitada ao usuário em todas as execuções.\n"

  ui_print "  --clean-ssh-passwords param_ssh_server(opcional)" $color_green
  echo -e "
    Limpa a senha de acesso SSH do(s) servidor(es). Se o parâmetro
    param_ssh_server for informado, será apagada localmente apenas a senha do
    servidor em questão, caso contrário serão apagadas as senhas de todos os
    servidores. Ver também a opção \"--list-ssh-saved-servers\".\n"
      
}


# Execution

if [ "$1" == "--help" ]; then

	utils_show_script_title "Help"
	utils_show_help
	exit 0

elif [ "$1" == "--help-deploy" ]; then

	utils_show_script_title "Help: deploy"
	dp_show_deploy_help
	exit 0

elif [ "$1" == "--help-configure-remote-server" ]; then

	utils_show_script_title "Help: configure remote server"
	dp_show_configure_remote_server_help
	exit 0

elif [ "$1" == "--clean-ssh-passwords" ]; then

	utils_show_script_title "Clean ssh passwords"
	ssh_clean_passwords $@
	exit 0

elif [ "$1" == "--list-ssh-saved-servers" ]; then

	utils_show_script_title "List ssh saved servers"
	ssh_list_saved_passwords_servers
	exit 0
fi

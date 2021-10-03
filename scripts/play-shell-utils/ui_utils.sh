color_default="\e[m"
color_black="\e[1;30m"
color_red="\e[1;31m"
color_green="\e[1;32m"
color_yellow="\e[1;33m"
color_blue="\e[1;34m"
color_cyan="\e[1;36m"
color_white="\e[1;37m"

ui_print_line () {

	local color=${1:-$color_default}
	local c=${2:-'-'}

	printf "$color%*s\n$color_default" "${COLUMNS:-$(tput cols)}" '' | tr ' ' "$c"
}


ui_print_center() {

	local COLUMNS=$(tput cols) 
	local title="$1" 
	local color=${2:-$color_default}

	printf "$color%*s\n$color_default" $(((${#title}+$COLUMNS)/2)) "$title"
}


ui_print() {

	local title="$1" 
	local color=${2:-$color_default}
	printf "$color%*s\n$color_default" 1 "$title"
}


ui_get_input() {

	local prompt=$1

	local cr=`echo $'\n.'`
	cr=${cr%.}

	read -p "$cr$config_msg_prefix $prompt" input_value
	echo "$input_value"
}


ui_get_password() {

	local prompt=$1

	local cr=`echo $'\n.'`
	cr=${cr%.}

	read -s -p "$cr$config_msg_prefix $prompt" input_value
	echo "$input_value"
}


# Exibe um título no terminal.
# Uso: show_title "Meu título"
ui_show_title() {

	echo ""
	echo ""
	echo ""
	ui_print_line $color_green
	echo ""
	ui_print "$1" $color_green
	echo ""
	ui_print_line $color_green
	echo ""
}


ui_show_help() {

	if [ ! -z "$1" ]; then

		echo ""
		echo "$config_msg_prefix $1"
	fi

	echo ""
	echo "$config_msg_prefix AJUDA:"
	echo "$HELP_TEXT"
	echo ""

	exit 1
}


ui_show_error() {

	echo ""
	ui_print_line $color_red
	echo ""
	ui_print "    ERRO: $1" $color_red
	echo ""
}


# Exibe uma mensagem no terminal com o prefixo configurado em config_msg_prefix.
# Uso: show_msg "Minha mensagem"
ui_show_msg() {
	
	local msg_color="$2"

	if [ -z "$msg_color" ]; then
		msg_color="$color_default"
	fi

	echo ""
	printf "${config_msg_prefix_color}$config_msg_prefix${msg_color} $1 ${color_default}\n"
}

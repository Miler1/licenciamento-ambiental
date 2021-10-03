
PLAY_CMD='play'

PLAY_PATH_OPTIONS=(
	"/opt/play-<version>/play"
	"/opt/play/play-<version>/play"
	"/opt/playframework/play-<version>/play")


play_configure_command() {

	if [ -z "$config_play_version" ]; then
		play_show_command
		return
	fi

	PLAY_CMD=''

	play_configure_command_to_version "$config_play_version"

	if [ -z "$PLAY_CMD" ]; then

		for ((i=1;i<=9;i++)); do 
			 play_configure_command_to_version "$config_play_version.$i"
		done

	fi

	if [ -z "$PLAY_CMD" ]; then
		play_error_not_found
	fi
}


play_configure_command_to_version() {

	if [ ! -z "$PLAY_CMD" ]; then
		return
	fi

	play_version=$1

	for path in "${PLAY_PATH_OPTIONS[@]}"; do

		real_path=${path//<version>/$play_version}

		if [ -f "$real_path" ]; then

			PLAY_CMD="$real_path"
			play_show_command
			return
		fi
	done
}


play_error_not_found() {

	ui_show_error "Não foi possível encontrar o comando 'play' compatível com a versão $config_play_version;"

	echo "Foram procurados os seguintes caminhos:"

	for path in "${PLAY_PATH_OPTIONS[@]}"; do
		echo '- '${path//<version>/$config_play_version}
	done

	echo ""
	echo "Também foram verificados caminhos para sub-versões como $config_play_version.1, $config_play_version.2 ..."
	echo ""
	echo "Observe se configuração da variável 'config_play_version' está correta."
	echo ""
	echo ""
	exit 1
}


play_show_command() {

	ui_show_msg "Comando do Playframework configurado: $PLAY_CMD "
}
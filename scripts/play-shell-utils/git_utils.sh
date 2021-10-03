
git_is_installed () {

	git --version &> /dev/null
	return $?
}

git_get_config() {

	local config_name="$1"
	local config_value=""

	if ! git_is_installed; then
		return 0
	fi

	local config_value="$(git config $config_name)"
	local result=$?
	
	if [ $result -ne 0 ]; then
		return 0
	fi

	echo "$config_value"
}

git_get_user_name() {

	git_get_config user.name
}

git_get_user_email() {

	git_get_config user.email
}
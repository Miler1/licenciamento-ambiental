config_play_version='1.5.0'
config_compile_frontend_cmd="gulp dist"
config_update_server_app=true
config_server_ssh="sysadmin@177.105.35.81"
config_server_release_folder_path="/home/sysadmin"
config_server_app_path="/var/play/amapa/licenciamento_ambiental/licenciamento_simplificado"
config_server_app_stop_cmd="systemctl stop licenciamento-simplificado-ap.service"
config_server_app_start_cmd="systemctl start licenciamento-simplificado-ap.service"
config_require_version_name=false
config_use_branch_as_version_name=true
config_backend_static_folders=("conf" "lib" "public" "modules")

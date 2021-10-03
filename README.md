# licensing-environment

[« Home](home) / Tecnologias Utilizadas

# Tecnologias Utilizadas

### Seguem abaixo as tecnologias utilizadas no sistema:

- Tecnologia Backend: 
    - Java **`1.8`**
    - [Play Framework](http://www.playframework.com) versão **`1.5.0`**
    
- Tecnologia Frontend: 
    - HTML, CSS, Javascript
    - [Less](http://lesscss.org/)
    - [AngularJS](http://angularjs.org/) versão **`1.6.2`**
    - [Pug](https://pugjs.org)
    - [Gulp](http://gulpjs.com/)
    - [Bower](http://bower.io/)
    
- Banco de dados
  - [PostgreSQL](http://www.postgresql.org/) (banco de dados) - versão **`9.5`** ou maior
  - [PostGis](http://postgis.net/) (extensão gis para o postgreSQL) - versão **`2.2`** ou maior
  
  

  [« Home](home) / Configurações do ambiente de desenvolvimento

# Configurações do ambiente de desenvolvimento

### 1 - Pré-requisitos

Instalar as dependências das [tecnologias utilizadas](tecnologias-utilizadas).

#### 1.1 - Instalando a JDK da Oracle

    sudo add-apt-repository ppa:webupd8team/java
    sudo apt-get update
    sudo apt-get install oracle-jdk8-installer

#### 1.2 - Instalando o PostgreSQL 

Instale o PostgreSQL pelo apt-get :

    sudo apt-get install postgresql

Inicialize o PostgreSQL com o comando:

    sudo service postgresql start

Caso houver alguma dependencia faltando, instale-a utilizando o apt-get install e re-execute o comando acima.

#### 1.3 - Instalando o PostGIS

Instale o PostGIS pelo apt-get :

    sudo apt-get install postgis

Pronto. Se tudo ocorreu bem, seu PostGIS foi instalado com sucesso.

#### 1.4 - Instalando o Node.js

Execute o seguinte comando no terminal para instalar o Curl:

    sudo apt-get install curl

Em seguida execute o comando a baixo: 

    curl -sL https://deb.nodesource.com/setup_6.x | sudo -E bash -

Assim que terminar o comando anterior execute o seguinte para instalar o Node.js:

    sudo apt-get install nodejs

#### 1.5 - Instalando o Play Framework

Baixe o Play 1.5.0 no site do Play Framework:

    https://www.playframework.com/download#older-versions

Em seguida extraia o play na pasta onde você fez o download.

Execute o comando a baixo para criar a pasta do play na pasta `opt`:

    sudo mkdir /opt/playframework

Agora devemos mover a pasta do play para a pasta que acabamos de criar, para isso execute o comando (lembre-se de substituir `pasta_onde_extraiu_o_play` pela pasta onde você extraiu o play):

    sudo mv `pasta_onde_extraiu_o_play`/play-1.5.0 /opt/playframework/

Rode o comando a seguir:

    perl -pi -e 's/\r\n$/\n/' /opt/playframework/play-1.5.0/play


Falta, agora, informar para o sistema onde o play está localizado, para isso executamos o comando:

    sudo update-alternatives --install /usr/bin/play play /opt/playframework/play-1.5.0/play 1

Pronto! Agora para verificar se está tudo certo, é só digitar o comando:

    play -version

Se aparecer a versão que você baixou tudo está certo.

#### 1.6 - Instalando o PGAdmin

Instale o PGAdmin pelo apt-get :

    sudo apt-get install pgadmin3

### 2- Configuração do Banco de Dados ###

#### 2.1 - Em algum database que não seja o licenciamento_ap:
```sql
DROP DATABASE licenciamento_ap;
DROP ROLE licenciamento_ap;

CREATE DATABASE licenciamento_ap
  WITH TEMPLATE template1;

CREATE ROLE licenciamento_ap LOGIN
  ENCRYPTED PASSWORD 'licenciamento_ap'
  SUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
```

#### 2.2 - No terminal (qualquer um)
```
{versao_postgres} = 9.5
{versao_postgis} = 2.2
``` 
##### Atenção: Os comandos abaixo podem alterar de acordo com seu ambiente local. As configurações acima são de exemplo.
```
sudo su postgres
psql -d licenciamento_ap -f /usr/share/postgresql/{versao_postgres}/contrib/postgis-{versao_postgis}/postgis.sql
psql -d licenciamento_ap -f /usr/share/postgresql/{versao_postgres}/contrib/postgis-{versao_postgis}/spatial_ref_sys.sql
exit
```

#### 2.3 - Dentro do licenciamento_ap

```sql
CREATE SCHEMA licenciamento;
CREATE SCHEMA correios;

ALTER SCHEMA licenciamento OWNER TO postgres;
ALTER SCHEMA correios OWNER TO postgres;

GRANT USAGE ON SCHEMA public TO licenciamento_ap;
GRANT USAGE ON SCHEMA licenciamento TO licenciamento_ap;
GRANT USAGE ON SCHEMA correios TO licenciamento_ap;

ALTER DEFAULT PRIVILEGES FOR USER licenciamento_ap IN SCHEMA licenciamento, correios
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO licenciamento_ap;

ALTER DEFAULT PRIVILEGES FOR USER licenciamento_ap IN SCHEMA licenciamento, correios
    GRANT SELECT, USAGE ON SEQUENCES TO licenciamento_ap;
```

#### 2.4 - No backend do Licenciamento

```
play deps
play evolutions
play evolutions:apply
```

#### 2.5 - No terminal (qualquer um)

ATENÇÃO! A seed dos correios demora (deixe executando mas já pode utilizar a aplicação, porém sem buscas de CEP)
```
psql -h localhost -p 5432 -d licenciamento_ap -U postgres -f /home/brunoperuzza/git/ap/licenciamento/licenciamento/backend/db/seeds/01_estado_the_geom.sql
psql -h localhost -p 5432 -d licenciamento_ap -U postgres -f /home/brunoperuzza/git/ap/licenciamento/licenciamento/backend/db/seeds/02_municpios_the_geom.sql
psql -h localhost -p 5432 -d licenciamento_ap -U postgres -f /home/brunoperuzza/git/ap/licenciamento/licenciamento/backend/db/seeds/03_inserts_correios.sql
```

### 3 - Configurando a aplicação

Baixe o projeto:

    git clone git@gitlab.ti.lemaf.ufla.br:amapa/licenciamento.git ou 
    git clone https://gitlab.ti.lemaf.ufla.br/amapa/licenciamento.git


Entre na pasta do backend, baixe as dependências, e execute as evolutions para o banco de desenvolvimento e teste:

    cd  licenciamento/backend
    play deps
    play evolutions:markApplied
    play evolutions:apply --%test

Volte à raiz do projeto e entre na pasta frontend. Depois execute os comandos abaixo para baixar dependências e configurar o frontend:

    npm install
    bower install
    gulp

### 4 - Criando pastas para execução do projeto

Para o correto funcionamento do sistema, deve-se criar a pasta 'licenciamento-ap' no home, para isso execute o seguintes comando:

    sudo mkdir /home/licenciamento-ap

Após a criação da pasta devemos dar permissão a mesma, execute o comando a seguir:

    sudo chmod -R 777 /home/licenciamento-ap

### 5 - Testando a aplicação

Para testar o sistema, execute o comando `play run` na pasta backend e acesse o endereço abaixo:

    http://localhost:9010

`Obs`: É necessário que a aplicação portal-segurança também esteja rodando.

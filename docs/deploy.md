# Deploy em Homolog/Produção

E aí meu jovem gafanhoto, tudo bom? Espero que não esteja a duas horas do combinado pro deploy, e esteja fazendo isso com bastante tempo de sobra :)

Não vou enrolar, pq a minha frase acima pode não ser sua situação, então vamos direto ao ponto.

### Gerando versão

Okay, se você está gerando para **homologação** talvez precise se preocupar primeiramente em gerar a **tag** da versão no Git.
Vamos lá, confere aí pra mim qual a última versão no seu repositório? 

```
git tag
```

A última versão deve ser a maior :)

Conversa com o seu time e verifica a necessidade de mudar a numeração, se são coisas simples, talvez seja necessário mudar apenas o último número, como em v1.1.1 para v1.1.2 ou um dos números anteriores para mudanças maiores.

```
git tag -a <numero-da-versao>
git push origin <numero-da-versao>
```

Estamos prontos para iniciar nossa aventura!<br>
Vá até a pasta raiz do projeto no seu computador pelo terminal, e caso ainda não tenha feito, digite

```
git submodule init
git submodule update
```

Agora basta entrar na pasta scripts em `raiz-do-projeto/scripts` e executar o script de deploy com `./deploy.sh <ambiente>`

> Nesse passo, normalmente as aplicações são configuradas para enviar a release para um outro servidor, com `lemaf@car.ti.lemaf.ufla.br` e a senha para esse scp é `id*jc)W7`.

> Vale lembrar também de anotar a pasta que o scp enviou a release, e tudo que estiver após o ".../releases_homolog/" deve ser inserido no link do wget abaixo

Por via das dúvidas, nesse momento faça um *ping* no servidor que o scp enviou a aplicação, só para guardarmos o IP.

```
ping car.ti.lemaf.ufla.br
```

> Caso sua aplicação não utilize o servidor acima, altere para o domínio que seu deploy está usando.

*output:*
```
PING car.ti.lemaf.ufla.br (177.105.35.45) 56(84) bytes of data.
64 bytes from 177.105.35.45 (177.105.35.45): icmp_seq=1 ttl=53 time=23.5 ms
```

> Guarde esse IP para utilizarmos no wget também. No caso, 177.105.35.45.

### Configurando o servidor

Antes de começar, você precisa saber que em algumas aplicações aqui, é necessário primeiro acessar um servidor onde fica configurado o nginx através de SSH para então acessar uma outra máquina, onde ficam configuradas as aplicações.

No caso dos ambientes do Amazonas, acesse o servidor do nginx através de ```<usuario>@homologacao.ipaam.lemaf.com.br``` (Sim, é .com.br), a senha na maioria das vezes é `L3m4f S3rv3r`.

Lá dentro você vai acessar o servidor da aplicação que você está fazendo deploy.

> **Máquina 192.168.100.15 - Amazonas**
> - Licenciamento
> - Análise do Licenciamento
> - Gestão de Demandas
> - SIG
> - Dashboard

> **Máquina 192.168.100.10 - Amazonas**
> - Carteira de Pesca
> - Fiscalização
> - Gestão de Empreendimentos
> - Gestão de Pagamentos
> - LDI Automatizado
> - LDI Site
> - SICAR
> - SID Site
> - SISFAP
> - Cadastro Unificado
> - Portal de Segurança

Acesse utilizando ssh, já dentro do servidor nginx
```
ssh <usuario>@192.168.100.[IP]
```
> Aqui novamente a senha deve ser L3m4f S3rv3r

Vamos para a pasta onde se encontra(m) o(s) arquivo(s) da aplicação com
```
cd /dados/var/<framework [spring | play]>/
```
e então devemos parar o serviço, com
```
sudo systemctl stop <nome do servico>.service
```

Okay, estamos no servidor e nossa aplicação já está parada, então vamos pegar nossa pasta da aplicação e renomeá-la com o padrão das antigas pastas de backup. Em alguns casos, pode ser feito com
```
sudo mv <aplicacao> <aplicacao>_bkp
```
> Aqui preste bastante atenção para manter os padrões do servidor, guardando as versões em backup conforme já é feito, nas suas devidas pastas. Seja flexível meu caro

Vamos trazer nosso arquivo *.zip* com a release.
```
sudo wget 177.105.35.45/releases/releases_homolog/<path que pedi pra você anotar no começo do tutorial>
```
> No wget acima você vai simplesmente adicionar o restante do caminho após o releases_homolog/ no caminho do scp exibido ao executar ./deploy.sh \<ambiente\>

Extraia com
```
sudo unzip <nome do arquivo .zip>
```

Renomeie a pasta `dist` para o nome da pasta da aplicação que você renomeou para `bkp`
```
sudo mv dist <aplicacao>
```

Suba novamente o serviço com
```
sudo systemctl start <servico>.service
```

### Produção?

Em alguns lugares como `releses_homolog` o path deve ser algo parecido com `releases_prod`, e após gerar a release, tu deve enviar para o pessoal da infra, eles normalmente sabem como proceder :)

# Tenha um ótimo dia!
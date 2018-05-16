# projetomodeloMinerium
* Importar "projetomodeloMinerium" do github.com
* Desconectar o projeto do github e remover do eclipse
* Trocar nome da pasta pelo sistema de arquivos
* Importar no eclipse e alterar o nome do projeto F2
* Utilizar Search > File para pequisar todos os locais onde o nome do projeto ("projetomodeloMinerium") é utilizado
* Alterar todas as referências para o novo nome
* Abrir o arquivo localizada em /scr/main/resources/aplicacao.properties e substituir o nome xxxx.ejbFacadeJndiLookupRemote2 para <nome do projeto sem a última letra>.ejbFacadeJndiLookupRemote2...
* Alterar o nome do datasource para o novo nome do projeto (servers > jboss > Filesets > Configuration File... > standalone.xml)
* Fechar e abrir o eclipse

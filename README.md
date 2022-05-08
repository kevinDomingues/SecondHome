# SecondHome App
## Configurar API

Como estamos a utilizar uma api em localhost nesta fase do desenvolvimento, é preciso modificar algumas coisas para poder ser usada nos vossos computadores.

- Aceder à linha de comandos e inserir "ipconfig", devem visualizar o vosso ip.
. Exemplo: 192.168.1.14

- Modificar o ficheiro network_security_config.xml que se encontra em "res/xml" e alterar o endereço ip que está lá para o vosso.
.Exemplo: <domain includeSubdomains="true">192.168.1.14</domain>

- Modificar o ficheiro ServiceBuilder que se encontra no package api e alterar o endereço que está lá pelo vosso, alterar apenas o ip e não a porta

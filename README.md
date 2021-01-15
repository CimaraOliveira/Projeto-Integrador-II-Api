# Projeto-Integrador-II-Api

#  Projeto Pagamento Web
**Projeto Integrador**


### Características

O  projeto proposto visou elaborar e desenvolver um software para o módulo de pagamento, o objetivo geral desse projeto é desenvolver uma Api de pagamento para os módulos Aéreo, Hospedagem e veículos consumirem. O sistema divide-se em duas aplicações distribuídas, uma web e outra api para o consumo de dados. O sistema foi desenvolvido utilizando a plataforma Java EE, JavaScript, API JPA para a persistência de dados, o framework de componentes Model-View-Controller, e o framework spring boot. Para o armazenamento das informações foi utilizado o banco de dados MySQL. Foi utilizado o serviço de hospedagem do Heroku para a hospedagem  da api.

### Apresentação 

Os usuários dos módulos Aéreo, Hospedagem e veículos  poderão se cadastrar no sistema com o nome, email e senha. Fazer login no sistema,  informar o valor a ser pago e a forma de pagamento cartão ou boleto. Caso a opção escolhida seja cartão, o sistema deve verificar se o número do cartão é válido, e só deve efetivar o pagamento em caso de validado. O usuário poderá visualizar as transações que já foram efetuadas por ele.

link para acesso a aplicação Web: https://projeto-pag-web.herokuapp.com/

link para documentação Swagger: https://projeto-pag-api.herokuapp.com/swagger-ui.html


### Descrição 

**1-** O Cliente poderá fazer login no sistema passando os dados por parâmetros, como email e senha.

**2-** Após fazer login o sistema gerará um token.

**3-** O Cliente com o token gerado poderá passar os dados da compra como o valor, data e o id do usuário. Na mesma requisição em Headers informar a origem e a autenticação token gerada ao fazer login.

**4-** Após passados os dados do requisito 3 acima gerará um link onde o usuário poderá copiar e colar no broswer para entar na tela de compra.


**Serviços realizados com O Postman**

Caminho para passar as informações no Postman: https://projeto-pag-api.herokuapp.com/


Podemos criar um novo usuário com a requisição post, passando por parâmetros nome email e senha.

Se os dados forem passados corretamente retornará ao status 201.

Se os dados forem passados incorretos retornará ao status de erro 500.

![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/PAGAE.png)


Login  utilizando Postman com a requisição Post,  passando por parâmetros email e senha. Mostrado como resposta os dados do usuário cadastrado e o token gerado após as informações dos dados.

Se os dados forem passados corretamente retornará ao status 200.

Se os dados forem passados incorretos retornará ao status de erro 404.


![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/LOGINn(1).jpeg)


Autenticação para gerar link da tela de compras em uma requisição Post, passando a origem de destino  e a autenticação token  no Headers. A origem de onde veio a requisição é obrigatória e a autenticação token, passada com a palavra  Bearer  na frente do token gerado.

Se os dados forem passados corretamente retornará ao status 200.

Se os dados forem passados incorretos retornará ao status de erro 404.

![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/GERARLINK.jpeg)


No mesmo método gerar link na requisição Post, informa por parâmetros o valor, data e o id do usuário que fez a autenticação.  Se os dados forem passados corretamente ao enviar a requisição ele retornará a uma resposta com o link da página de compra web.

Se os dados forem passados corretamente retornará ao status 200.

Se os dados forem passados incorretos retornará ao status de erro 404.


![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/DADOS.jpeg)



Após passar os dados corretamente o link é gerado, e pode ser copiado e colado no browser,  feito isso o usuário entrará na tela de compra onde recebemos o valor e informamos a quantidade desejada do que foi comprado e escolher a forma de pagamento desejada pelo usuário boleto ou cartão. 
A tela de compra poderá ser acessada através do link após o usuário enviar os dados necessários e fazer o consumo dos serviços de compra. Informando a quantidade de itens, a tela mostra o valor unitário e o valor total de acordo com a quantidade escolhida. E mostra a opção de escolha de pagamento via boleto ou cartão.


![](https://github.com/cleocardoso/PagamentoWeb/blob/main/IMAGENS/Compra.png)





**Tabela com a descrição dos Serviços do Módulo de Pagamento**

NOME | DESCRIÇÃO | TIPO | PARÂMETROS | STATUS DA RESPOSTA
--------- | ------ | ------- | ------ | --------
_Login_ | Usuário vai se logar para concluir o pagamento e escolher a forma de pagamento. | POST | Objeto JSON com dois parâmetros: email e senha | Retorna um objeto em formato JSON. | Retorna 200 caso as credenciais sejam válidas. Caso contrário retorna 404.
_Salvar Pagamento_ | Salvar pagamento do usuário. | POST | Objeto JSON com os parâmetros: valor, data, e id do usuário. Além disso, enviar o token autenticado no header da requisição e a origem. | Retorna um objeto em formato JSON. | Retorna 200 caso o token seja autenticado e válido. Caso contrário retorna 403.
_Salvar Usuário_ | Salvar usuario no banco de dados para fazer login |POST |Objeto JSON passando os parâmetros de nome, email e senha | Retorna um objeto em formato JSON. | Retorna status 201 se os dados forem passados corretamente, caso não exista no banco, e retorna status 500 ou se o usuário já existir no banco ou dados passados incorretos.






**Caminhos da api Passados no Postman:**
Requisição para salvar os dados.

POST    https://projeto-pag-api.herokuapp.com/api/usuarios/save   Salvar Usúario

POST    https://projeto-pag-api.herokuapp.com/api/usuarios/login    Login

POST    https://projeto-pag-api.herokuapp.com/api/compras/gerarLink  Gerar Link de acesso a tela de pagamento.



Desenvolvido por:

Cimara Oliveira          https://github.com/CimaraOliveira/

Cleonice Cardoso         Git: https://github.com/cleocardoso/

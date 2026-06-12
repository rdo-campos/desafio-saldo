<h1>BALANCE API</h1>

<h4>OBJETIVO:</h4>
Essa API é responsável por receber requisições do Autorizador para fazer atualização de saldo e para devolver consultas com o saldo mais atualizado do cliente.

A solução mantém uma projeção local dos saldos das contas, em Banco de Dados, garantindo a consistência das informações por meio da validação do timestamp da última transação recebida.
<br><br>
<h4>ARQUITETURA:</h4>
A aplicação foi desenvolvida utilizando:<br>
- Kotlin <br>
- Spring Boot<br>
- PostgreSQL<br>
- Docker<br>
- Gradle<br>
<br><br>

<h4>CAMADAS:</h4>
- Controller: exposição dos endpoints<br>
- Service: regras de negócio<br>
- Repository: acesso aos dados<br>
- Entity: representação das tabelas<br>
- DTO: objetos de entrada e saída<br>
<br><br>

<h4>ARQUITETURA ALVO:</h4>
O desenho (drawio) está com a arquitetura ideal, com filas, retry e DLQ.
Nessa versão do projeto, a implementação ainda não possui essa inteligencia de tratamento de eventos com erro.

![img_6.png](img_6.png)

![img_7.png](img_7.png)

<br><br>
<h4>COMO EXECUTAR:</h4>
1) Abrir o docker e executar o 'desafio-saldo' <br>
2) Subir o postgreSQL pelo gitbash (docker compose up -d postgres) <br>
3) Abrir o intellij e executar o BalanceApiApplication
<br>
<br>

<h4>ENDPOINTS:</h4>

1. Consulta de saldo: <br>
GET /balances/{accountid} <br> 
![img.png](img.png)
<br>

2. Processar evento: <br>
POST  /balances/events <br>
JSON: <br>
![img_1.png](img_1.png) <br>
Execução via gitbash:<br>  curl -X POST http://localhost:8080/balances/events -H "Content-Type: application/json" --data @evento.json

3) Contagem de registros no banco: <br>
GET account/count<br>


<br> <br>
<h4>REGRAS DE NEGÓCIO:</h4> 
- Eventos mais antigos não sobrescrevem eventos existentes mais recentes (regra feita por timestamp)<br>
- Contas inexistentes na base de saldo são criadas automaticamente ao receber um evento válido
<br><br>

<h4>OBSERVABILIDADE: </h4>
Spring Boot Actuator habilitado. <br>
Endpoints disponíveis:
- /actuator/health<br>
<img width="765" height="966" alt="img_3" src="https://github.com/user-attachments/assets/0cd6772e-c3be-42ad-8128-3a3a5d60a183" />
<br><br>
- /actuator/metrics <br>
<img width="905" height="1032" alt="img_4" src="https://github.com/user-attachments/assets/3fd68489-84bf-4ecf-8cc4-caa840705b0d" />

<br>
<br><br>

<h4>LOGS:</h4> Adicionamos no console logs para recebimento de eventos, criação de contas, atualização de saldos e rejeição de eventos com datas antigas
<br><br>
  <h4>TESTES:</h4> Implementados testes unitários para:<br>
1) Conta inexistente;<br>
2) Evento antigo não atualizar o saldo;<br>
3) Evento novo atualizar o saldo.<br>
<br><br>

<h4>MELHORIAS FUTURAS:</h4>
- Consumo direto via SQS<br>
- Retry<br>
- DLQ<br>
- Métricas<br>
- Reprocessar mensagens perdidas<br>

<br><br>
<h4>DECISÕES TÉCNICAS:</h4>
- Optamos por utilizar o POSTGRESQL ao invés de um banco NoSQL. Por ser um sistema crítico, e seguindo a referência (teorema CAP e ACID/BASE), um sistema de Contas deve ser Consistente e ter Tolerância - não podemos ter uma consitência eventual... nesse sentido, o Postgresql atende melhor ao que precisamos.<br>
- Por ter volume alto e ser sistema crítico, optamos por utilizar ECS ao invés de Lambda (possível problema de coldstart, embora possa ser contornado com algumas estratégias)<br>
- Utilizamos o timestamp para garantir que transações antigas liberadas pelo Autorizador não sobrescrevam o saldo correto do cliente <br>
- O projeto está utilizando o Hibernate, então as tabelas são criadas de acordo com o nosso projeto. No ambiente do banco, entendemos que essa forma não funcionaria - pois existem processo específicos de governança para criação/atualização de tabelas <br>
- Uso do Spring Boot Actuator para observabilidade<br>
- Separação do projeto em camadas: Controller, Service e Repository<br>
- Implementamos logs para rastreabilidade local. Para o ambiente de produção, o local e o volume dos logs precisará ser revisto.
<br><br>

<h4>OBSERVAÇÕES: </h4>
Embora o desenho da arquitetura considere o consumo via fila SQS, o projeto está adaptado para simular local os eventos chegando por POST.<br>
O arquivo dockercompose enviado no desafio, gera a fila CONTA-BANCARIA-CRIADA, que somente cria as contas - e não simula o Autorizador.<br>

A lógica de processamento está correta (validar e só aceitar atualizações de saldo com data recente), porém, para entrar em ambiente de produção, seria necessário adaptar/validar a leitura dos eventos corretamente.
<br><br>
![img_5.png](img_5.png)

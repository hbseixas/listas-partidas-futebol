# API para salvar partidas de futebol

Objetivo: desenvolver uma API para salvar partidas de futebol.

Requisitos:

- Java 17 ou 21
- SpringBoot
- Persistir os dados em um banco de dados relacional (MySQL, Postgres, SQLite, H2, HSQLDB, etc.)

![Spring Initializr](https://github.com/hbseixas/listas-partidas-futebol/assets/148393725/62b7502a-7a18-4cbc-a2d3-64846d152e81)


Operações básicas:

A aplicação deverá permitir...

- cadastrar uma partida, contendo no mínimo o nome dos clubes, o resultado da partida, a data e a hora da partida e o nome do estádio;

- atualizar os dados de uma partida;

- remover uma partida do cadastro;

Buscas:

A aplicação deverá permitir a busca por...

- partidas que terminaram em uma goleada (3 ou mais gols de diferença para um dos clubes);

- partidas que terminaram sem gols para nenhum dos clubes;

- todas as partidas de um clube específico, podendo filtrar as partidas onde este clube atuou como mandante ou como visitante;

- todas as partidas de um estádio específico;

Validações:

A aplicação não deverá permitir o cadastro ou a atualização...

- de uma partida antes das 8h ou após às 22h;

- de mais de uma partida em um mesmo estádio no mesmo dia;

- de mais de uma partida de um mesmo clube com menos de dois dias de intervalo;

- de uma partida sem conter o nome dos clubes, a data e a hora da partida e o nome do estádio;

- de uma partida com a data e a hora da partida no futuro;

- de uma partida sem conter o resultado, ou com valores negativos no resultado;


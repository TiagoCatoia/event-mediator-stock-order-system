# ADR 01. Adoção de Arquitetura Baseada em Eventos com Mediador Interno

```
Status: Aceita
```

## Contexto:
O projeto consiste em um Sistema de Pedidos e Controle de Estoque desenvolvido como aplicação acadêmica.
O sistema deve possuir capacidade de reagir a eventos internos como criação de pedido, baixa de estoque e atualização de status.
Também é necessário oferecer suporte a processamento assíncrono e desacoplado, garantir robustez e tolerância a falhas internas e 
possibilitar monitoramento completo do fluxo da aplicação por meio de observabilidade. Dado esse cenário, 
o sistema deve permitir evolução simples, separação clara de responsabilidades e facilidade para estender funcionalidades conforme o projeto cresce.

## Decisão:
A decisão é adotar uma Arquitetura Baseada em Eventos (Event-Driven Architecture) utilizando um mediador interno fornecido pelo Spring 
por meio de Application Events e Domain Events. O projeto também utilizará Circuit Breaking com Resilience4j para garantir proteção contra 
falhas em componentes internos que executam tarefas potencialmente demoradas, além de Observabilidade com Micrometer e Zipkin para 
logs estruturados e tracing distribuído.

## Consequências:
Os principais benefícios dessa decisão incluem o baixo acoplamento entre os módulos de domínio, comunicação simples baseada na publicação 
e assinatura de eventos, maior organização interna, rastreabilidade e depuração facilitadas devido à observabilidade e a possibilidade 
de evolução incremental do sistema. Por outro lado, existem desafios, como a curva de aprendizado maior em comparação com uma arquitetura 
totalmente síncrona, o fato de que a execução assíncrona pode gerar fluxos menos previsíveis caso o tracing não seja bem implementado e 
a necessidade de maior cuidado na gestão dos eventos, especialmente no que diz respeito à consistência e idempotência.

## Conformidade:
Para garantir alinhamento com essa decisão arquitetural, toda comunicação interna entre as partes deve ocorrer preferencialmente usando 
Domain Events, evitando-se chamadas diretas entre os serviços de Pedidos, Estoque e Rastreamento. Além disso, qualquer operação mais lenta 
ou sujeita a falhas deve ser protegida com Resilience4j, e a observabilidade deve ser implementada com Micrometer e Zipkin, assegurando logs e 
métricas padronizadas.
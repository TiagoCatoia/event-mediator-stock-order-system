# ADR-003  Uso de Spans para Observabilidade no OrderProcessor

```
Status: Aceito
```

## **Contexto**
Desde a ADR-001, foi definido que a arquitetura do sistema seguiria um modelo **event-driven** com forte apoio em **observabilidade**, incluindo tracing distribuído para permitir auditoria, rastreamento de eventos internos e diagnóstico de falhas.

Com a introdução do Circuit Breaker no fluxo de cancelamento de pedidos (ADR-002), tornou-se necessário garantir total visibilidade sobre seu comportamento em tempo de execução, especialmente porque o cancelamento envolve:

* comunicação com serviços internos (ex: estoque),
* reprocessamento via mensageria RabbitMQ,
* fallback com envio de e-mails,
* execução assíncrona e suscetível a falhas.

Para manter a conformidade arquitetural estabelecida na ADR-001 que exige rastreamento detalhado do fluxo completo da aplicação era fundamental instrumentar o `OrderProcessor` com spans que permitissem:

1. Identificar claramente quando o Circuit Breaker está **OPEN**, **CLOSED**.
2. Rastrear o motivo e o ponto exato onde cada fallback é disparado.
3. Monitorar o envio de e-mails.
4. Correlacionar tentativas de reprocessamento via RabbitMQ.
5. Expor metadados ricos (orderId, circuitBreakerState, tempo, erro) para ferramentas como Zipkin.

## Decisão
Implementamos **instrumentação com spans explícitos** utilizando `io.micrometer.tracing.Tracer` para melhorar a observabilidade do fluxo de cancelamento:

### 1. Criação de spans específicos para cada etapa crítica:
* `cancelOrderFallbackOpen`
* `cancelOrderFallbackClosed`
* `sendEmail`

### 2. Propagação explícita do estado do Circuit Breaker para o span do e-mail
O método `sendEmail()` agora recebe o estado (`"open"` ou `"closed"`) e adiciona tags ao span:

```java
emailSpan.tag("circuitBreakerState", circuitState);
```

### 3. Registro de metadados essenciais através de tags:
* `orderId`
* `recipient`
* `status`
* `subject`
* `circuitBreakerState`

### 4. Criação de spans independentes para e-mail
Isso permite enxergar no trace a seguinte estrutura:

```
sendEmail
    └── cancelOrderFallbackOpen
```

## Consequências
### Positivas
- **Visibilidade total do fluxo** – Cada etapa do cancelamento pode ser rastreada visualmente no Zipkin.
- **Diagnóstico mais rápido** – É possível identificar instantaneamente quando o Circuit Breaker abre, quando reprocessamentos ocorrem e quando o e-mail é enviado.
- **Correlação de eventos entre serviços** – Como os spans carregam o contexto, é possível ligar eventos no Order, Stock e Email Service.
- **Suporte a auditoria e alertas** – Sistemas de observabilidade podem acionar alertas, falhas repetidas em fallback ou envio de e-mails.

### Negativas
- **Mais código boilerplate** – Criar spans manuais (start, end, tags, error) adiciona verbosidade.
- **Dependência do Tracer** – O componente fica acoplado a um mecanismo de tracing (Micrometer Tracing).
- **Spans extras com grande volume de dados** – Em cenários de alto volume, a criação de spans extras aumenta o volume enviado ao backend de observabilidade.
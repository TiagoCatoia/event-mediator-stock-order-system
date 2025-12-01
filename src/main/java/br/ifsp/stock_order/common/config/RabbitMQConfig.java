package br.ifsp.stock_order.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //filas
    public static final String QUEUE_ORDER_CREATED = "q.order-created";
    public static final String QUEUE_STOCK_RESERVE = "q.stock-reserve";
    public static final String QUEUE_ORDER_CANCEL = "q.order-cancel";
    public static final String QUEUE_STOCK_FAILED = "q.stock-failed";

    //exchange
    public static final String EXCHANGE_ORDER = "ex.order";

    //routing keys
    public static final String RK_ORDER_CREATED = "order.created";
    public static final String RK_STOCK_RESERVE = "stock.reserve";
    public static final String RK_STOCK_FAILED = "stock.failed";
    public static final String RK_ORDER_CANCEL = "order.cancel";


    @Bean
    public Queue queueOrderCreated() {
        return new Queue(QUEUE_ORDER_CREATED, true);
    }

    @Bean
    public Queue queueStockReserve() {
        return new Queue(QUEUE_STOCK_RESERVE, true);
    }

    @Bean
    public Queue queueOrderCancel() {
        return new Queue(QUEUE_ORDER_CANCEL, true);
    }

    @Bean
    public Queue queueStockFailed() {
        return new Queue(QUEUE_STOCK_FAILED, true);
    }



    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(EXCHANGE_ORDER);
    }



    @Bean
    public Binding bindingOrderCreated() {
        return BindingBuilder.bind(queueOrderCreated())
                .to(orderExchange()).with(RK_ORDER_CREATED);
    }

    @Bean
    public Binding bindingStockReserve() {
        return BindingBuilder.bind(queueStockReserve())
                .to(orderExchange()).with(RK_STOCK_RESERVE);
    }

    @Bean
    public Binding bindingStockFailed() {
        return BindingBuilder.bind(queueStockFailed())
                .to(orderExchange()).with(RK_STOCK_FAILED);
    }

    @Bean
    public Binding bindingOrderCancel() {
        return BindingBuilder.bind(queueOrderCancel())
                .to(orderExchange()).with(RK_ORDER_CANCEL);
    }



    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}

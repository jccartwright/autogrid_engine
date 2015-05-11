package ngdc.next.autogrid

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.core.Queue

@SpringBootApplication
class Application {
    final static String TASK_QUEUE_NAME = 'autogrid_queue'
    final static String STATUS_QUEUE_NAME = 'status_queue'

    @Autowired
    RabbitTemplate rabbitTemplate

    @Bean
    Queue taskQueue() {
        new Queue(TASK_QUEUE_NAME, true)
    }

    @Bean
    Queue statusQueue() {
        new Queue(STATUS_QUEUE_NAME, true)
    }

    @Bean
    AutogridMessageHandler messageHandler() {
        return new AutogridMessageHandler(statusQueue(), rabbitTemplate);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(AutogridMessageHandler messageHandler) {
        return new MessageListenerAdapter(messageHandler, "handleMessage");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.connectionFactory = connectionFactory
        container.queueNames = [ TASK_QUEUE_NAME ]
        container.prefetchCount = 1
        container.messageListener = listenerAdapter
        return container
    }


    static void main(String[] args) {
        SpringApplication.run Application, args
    }
}

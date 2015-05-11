package ngdc.next.autogrid

import ngdc.next.AbstractEngineMessageHandler
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.core.Queue

class AutogridMessageHandler extends AbstractEngineMessageHandler{
    Queue statusQueue
    RabbitTemplate rabbitTemplate
    String engineName = 'autogrid'
    List requiredAttributes = [ 'directory', 'filterCriteria' ]

    AutogridMessageHandler(Queue statusQueue, RabbitTemplate rabbitTemplate) {
        this.statusQueue = statusQueue
        this.rabbitTemplate = rabbitTemplate
    }


    Map process ( Map payload ) {
        //TODO
        return [success:true, message:"processing complete"]
    }
}

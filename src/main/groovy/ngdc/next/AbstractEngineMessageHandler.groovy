package ngdc.next

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate

@Slf4j
abstract class AbstractEngineMessageHandler implements EngineMessageHandler {
    static jsonSlurper = new JsonSlurper()

    protected List getRequiredAttributes() {
        [ 'id' ]
    }

    void handleMessage( byte[] message ) {
        log.debug "received byte[]"
        handleMessage( new String(message) )
    }

    void handleMessage( String message ) {
        assert statusQueue != null
        assert rabbitTemplate != null
        println "received message:\n${message}"
        println "processing..."

        log.debug( "received message: $message" )

        def response, payload
        try {
            payload = jsonSlurper.parseText( message ) as Map

            def missingAttrs = requiredAttributes.findAll { payload[it] == null }
            if ( missingAttrs ) {
                log.error( "incoming message is missing required attributes ${missingAttrs}" )
                response = [ success: false, message: 'processing failed' ]
            }
            else {
                log.info "${engineName} engine starting processing message with id [${payload.id}]"
                def startTime = new Date()
                response = process( payload )
                def endTime = new Date()

                if ( !response.data ) response.data = [ : ]
                response.data.startTime = startTime.time
                response.data.endTime = endTime.time

                def elapsedSeconds = (endTime.time - startTime.time) / 1000
                log.info "${engineName} engine finished processing message with id [${payload.id}] and success [${response.success}] in [${elapsedSeconds}] seconds"
            }

            response.id = payload.id
            response.engine = engineName
            sendResponse( response )
        }
        catch( Exception e ) {
            log.error( 'processing failed', e )
            sendResponse( [ id: payload?.id, engine: engineName, success: false, message: 'processing failed' ] )
        }
    }

    protected void sendResponse( response ) {
        if ( rabbitTemplate == null ) {
            log.error( 'there is no rabbit template available to send a response message!!' )
            return
        }

        def message = new JsonBuilder( response ).toString()
        println "sending message ${message}"
        rabbitTemplate.convertAndSend( statusQueue.name, message )

        log.debug "Sent message: $message"
    }

}

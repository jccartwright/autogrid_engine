package ngdc.next

interface EngineMessageHandler {
    String getEngineName()

    void handleMessage(String message)

    void handleMessage(byte[] message)

    Map process( Map payload )
}

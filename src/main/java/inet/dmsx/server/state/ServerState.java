package inet.dmsx.server.state;

public interface ServerState {

    void handleRequest() throws IllegalStateServerException;
}

package inet.dmsx.server.state;

public interface ServerState {

    void handle() throws IllegalStateServerException;
}

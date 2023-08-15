package inet.dmsx.server.state;

public final class PauseState implements ServerState {

    @Override
    public void handleRequest() throws IllegalStateServerException {
        throw new IllegalStateServerException("Server is paused!");
    }
}

package servidor.modelo;

import lombok.Value;

import java.net.SocketAddress;

@Value
public class Player {
    private final String name;
    private final SocketAddress ip;
}

import java.util.List;

public interface Parser {
    boolean parse();
    List<Statement> obtenerSentencias();
}

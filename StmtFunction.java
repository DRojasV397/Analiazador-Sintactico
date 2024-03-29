import java.util.ArrayList;
import java.util.List;

public class StmtFunction extends Statement {
    final Token name;
    final List<Token> params;
    final StmtBlock body;

    StmtFunction(Token name, List<Token> params, StmtBlock body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }
    @Override
    public void exec(TablaSimbolos tablaSimbolos){
        List<Object> cuerpo = new ArrayList<>();
        cuerpo.add(params);
        cuerpo.add(body);
        if(!tablaSimbolos.existeIdentificador(name.lexema))
            tablaSimbolos.asignar(name.lexema, cuerpo);
    }
}

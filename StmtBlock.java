import java.util.List;

public class StmtBlock extends Statement{
    final List<Statement> statements;

    StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    void exec(TablaSimbolos tablaSimbolos) {
        TablaSimbolos tablaSimbolos2 = new TablaSimbolos(tablaSimbolos);
        for(int i = 0; i < statements.size(); i++){
            statements.get(i).exec(tablaSimbolos2);
        }
    }
}

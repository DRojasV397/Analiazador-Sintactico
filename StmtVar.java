public class StmtVar extends Statement {
    final Token name;
    final Expression initializer;

    StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    void exec(TablaSimbolos tablaSimbolos) {
        if(!tablaSimbolos.existeIdentificador(name.lexema))
            tablaSimbolos.asignar(name.lexema, initializer.solve(tablaSimbolos));
    }
}

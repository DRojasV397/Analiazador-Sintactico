public class StmtReturn extends Statement {
    final Expression value;

    StmtReturn(Expression value) {
        this.value = value;
    }
    @Override
    void exec(TablaSimbolos tablaSimbolos) {
        tablaSimbolos.asignar("return", value.solve(tablaSimbolos));
    }
}

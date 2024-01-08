public class StmtPrint extends Statement {
    final Expression expression;

    StmtPrint(Expression expression) {
        this.expression = expression;
    }

    @Override
    void exec(TablaSimbolos tablaSimbolos) {
        System.out.println(expression.solve(tablaSimbolos));
    }
}

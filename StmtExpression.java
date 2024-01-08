public class StmtExpression extends Statement {
    final Expression expression;

    StmtExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    void exec(TablaSimbolos tablaSimbolos) {
        expression.solve(tablaSimbolos);
    }
}

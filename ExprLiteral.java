class ExprLiteral extends Expression {
    final Object value;

    ExprLiteral(Object value) {
        this.value = value;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        return value;
    }
}

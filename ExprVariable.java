class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        return tablaSimbolos.obtener(name.lexema);
    }
}
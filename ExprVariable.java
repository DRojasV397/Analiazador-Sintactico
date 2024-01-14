class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name.lexema;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        return tablaSimbolos.obtener(name.lexema);
    }
}
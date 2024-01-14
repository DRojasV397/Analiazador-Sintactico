public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    Object solve(TablaSimbolos tablaSimbolos) {
        if(operator.tipo == TipoToken.MINUS)
            try {
                return (boolean) right.solve(tablaSimbolos);
            } catch (Exception e) {
                ASDR.error(operator.posicion, String.valueOf(operator.posicion),"El operando debe ser numerico");
                return null;
            }
        else
            try {
                return (boolean) right.solve(tablaSimbolos);
            } catch (Exception e) {
                ASDR.error(operator.posicion, String.valueOf(operator.posicion),"El operando debe ser booleano");
                return null;
            }
    }
}

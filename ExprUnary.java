package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
}

package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class ExprGet extends Expression{
    final Expression object;
    final Token name;

    ExprGet(Expression object, Token name) {
        this.object = object;
        this.name = name;
    }
}

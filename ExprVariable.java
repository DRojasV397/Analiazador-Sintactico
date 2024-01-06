package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }
}
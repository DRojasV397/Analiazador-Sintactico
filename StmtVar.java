package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class StmtVar extends Statement {
    final Token name;
    final Expression initializer;

    StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }
}

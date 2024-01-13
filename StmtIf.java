public class StmtIf extends Statement {
    final Expression condition;
    final Statement thenBranch;
    final Statement elseBranch;

    StmtIf(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    public void exec(TablaSimbolos tablaSimbolos){
        if ((boolean)condition.solve(tablaSimbolos)) {
            thenBranch.exec(tablaSimbolos);
        }
        else if (elseBranch!=null) {
            elseBranch.exec(tablaSimbolos);
        }
    }
}

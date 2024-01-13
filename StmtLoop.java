public class StmtLoop extends Statement {
    final Expression condition;
    final Statement body;

    StmtLoop(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }
    public void exec(TablaSimbolos tablaSimbolos){
        while((boolean)condition.solve(tablaSimbolos)){
            body.exec(tablaSimbolos);
        }
    }
}

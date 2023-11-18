import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;

public class ASDR implements Parser{
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private List<TipoToken> primeroEXPRESSION; //Se declara una lista en la que se incluiran los tokens que forman el conjunto primero de EXPRESSION
    private List<TipoToken> primeroSTATEMENT;

    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);

        //Se agregan los elementos del conjunto primero de EXPRESSION
        primeroEXPRESSION.add(TipoToken.BANG);
        primeroEXPRESSION.add(TipoToken.MINUS);
        primeroEXPRESSION.add(TipoToken.TRUE);
        primeroEXPRESSION.add(TipoToken.FALSE);
        primeroEXPRESSION.add(TipoToken.NULL);
        primeroEXPRESSION.add(TipoToken.NUMBER);
        primeroEXPRESSION.add(TipoToken.STRING);
        primeroEXPRESSION.add(TipoToken.IDENTIFIER);
        primeroEXPRESSION.add(TipoToken.LEFT_PAREN);

        //Se agregan los elementos del conjunto primero de STATEMENT
        primeroSTATEMENT.add(TipoToken.BANG);
        primeroSTATEMENT.add(TipoToken.MINUS);
        primeroSTATEMENT.add(TipoToken.TRUE);
        primeroSTATEMENT.add(TipoToken.FALSE);
        primeroSTATEMENT.add(TipoToken.NULL);
        primeroSTATEMENT.add(TipoToken.NUMBER);
        primeroSTATEMENT.add(TipoToken.STRING);
        primeroSTATEMENT.add(TipoToken.IDENTIFIER);
        primeroSTATEMENT.add(TipoToken.LEFT_PAREN);
        primeroSTATEMENT.add(TipoToken.FOR);
        primeroSTATEMENT.add(TipoToken.IF);
        primeroSTATEMENT.add(TipoToken.PRINT);
        primeroSTATEMENT.add(TipoToken.RETURN);
        primeroSTATEMENT.add(TipoToken.WHILE);
        primeroSTATEMENT.add(TipoToken.LEFT_BRACE);
    }

    @Override
    public boolean parse() {
        PROGRAM();
        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("La sintaxis es correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    // PROGRAM -> DECLARATION
    private void PROGRAM(){
            DECLARATION();
    }
    
    /************************************************************************
     *                            Declaraciones
    ************************************************************************/

    private void DECLARATION(){
        if(hayErrores) //Termina el 
            return;

        /*
         * Dentro de cada if se compara con los elementos del conjunto primero 
         * de cada uno de los no terminales
         */

        // DECLARATION -> FUN_DECL DECLARATION
        else if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            DECLARATION();
        }
        // DECLARATION -> VAR_DECL DECLARATION
        else if (preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
            DECLARATION();
        }
        // DECLARATION -> STATEMENT DECLARATION
        else if (primeroSTATEMENT.contains(preanalisis.tipo)){
            STATEMENT();
            DECLARATION();
        }

        /*
        * La producción DECLARATION -> Ɛ no se incluye en codigo, debido a esto, 
        * si el tipo de preanalisis no coincide con ninguno de los tipos de los if
        * No se conciderará como error.
        */

    }
    
    // FUN_DECL -> fun FUNCTION
    private void FUN_DECL(){
        if(hayErrores)
            return;
        
        /*
         * En esta función, a diferencia de la funcion anterior, primero 
         * se compara que el elemento del preanalisis sea FUN, ya que si 
         * no coincide se trata de un error. Las producciones de este 
         * no terminal no contemplan Ɛ, por lo que si se recibe un terminal
         * distinto será un error.
         */
        
        match(TipoToken.FUN);
        if(hayErrores){
            System.out.println("Se esperaba una FUNCION");
        }
        FUNCTION();
    }

    // VAR_DECL -> var id VAR_INIT ;
    private void VAR_DECL(){
        if(hayErrores)
            return;
        
        /*
         * En esta función primero se compara que el elemento del preanalisis 
         * sea VAR, ya que si no coincide se trata de un error. Las producciones 
         * de este no terminal no contemplan Ɛ, por lo que si se recibe un terminal
         * distinto será un error; es por ello que EL PRIMER IF-ELSE ES NECESARIO.
         * 
         * El if-else dentro del primero se usa para detallar el error. No es 
         * necesario seguir esta estructura, en seguida se muestra otra forma 
         * que se puede usar para detallar el error.
         */
        
        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            if(preanalisis.tipo == TipoToken.IDENTIFIER){
                match(TipoToken.IDENTIFIER);
                VAR_INIT();

                /*
                 * Esta es otra forma de manejar los errores.
                 * Despues de usar la función match() se verifica si hay errores, si
                 * los hay se muestra el mensaje de error y se usa "return".
                 * Usar "return" es necesario para evitar que  
                 */
                match(TipoToken.SEMICOLON);
                if(hayErrores){
                    System.out.println("Se esperaba ';'");
                    return;
                }
            }
            else{
                hayErrores = true;
                System.out.println("Se esperaba un IDENTIFICADOR luego de VAR.");
            }
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba una VARIABLE");
        }
    }

    // VAR_INIT -> = EXPRESSION | Ɛ
    private void VAR_INIT(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }

        /*
        * La producción DECLARATION -> Ɛ no se incluye en codigo, debido a esto, 
        * si el tipo de preanalisis no coincide con ninguno de los tipos de los if
        * No se conciderará como error.
        */
        
    }
    
    /************************************************************************
     *                              Sentencias
    ************************************************************************/


    /************************************************************************
     *                              Expresiones
    ************************************************************************/
    
    
    /************************************************************************
     *                                 Otras
    ************************************************************************/

    //FUNCTION -> id ( PARAMETERS_OPC ) BLOCK
    private void FUNCTION(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            if(hayErrores){
                System.out.println("Se esperaba un IDENTIFICADOR");
                return;
            }
            match(TipoToken.LEFT_PAREN);
            if(hayErrores){
                System.out.println("Se esperaba '(''");
                return;
            }
            PARAMETERS_OPC();
            match(TipoToken.RIGHT_PAREN);
            if (hayErrores) {
                System.out.println("Se esperaba ')''");
                return;
            }
            BLOCK();
        }
    }

    // PARAMETERS_OPC -> PARAMETERS | Ɛ
    private void PARAMETERS_OPC(){
        if(hayErrores)
            return;

        PARAMETERS();
    }

    // PARAMETERS -> id PARAMETERS_2
    private void PARAMETERS(){
        if(hayErrores)
            return;

        match(TipoToken.IDENTIFIER);
        if(hayErrores){
            System.out.println("Se esperaba un IDENTIFICADOR");
            return;
        }
        PARAMETERS_2();
    }

    //PARAMETERS_2 -> , id PARAMETERS_2 | Ɛ
    private void PARAMETERS_2(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            if (hayErrores) {
                System.out.println("Se esperaba un IDENTIFICADOR");
                return;
            }
            PARAMETERS_2();
        }
    }

    // ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | Ɛ
    private void ARGUMENTS_OPC(){
        if(hayErrores)
            return;
        
        /*
        
         * Se verifica si el tipo de preanalisis pertenece al conjunto primero de EXPRESSION, 
         * de lo contrario se tomara como la producción Ɛ.
         * Esto se podria hacer comparando de forma directa dentro del if el tipo de preanalisis 
         * con los elementos del conjunto primero de EXPRESSION pero de esta forma el codigo se 
         * vuelve mas legible.
         */
        if(primeroEXPRESSION.contains(preanalisis.tipo)){
            EXPRESSION();
            ARGUMENTS();
        }
    }

    // ARGUMENTS -> , EXPRESSION ARGUMENTS | Ɛ
    private void ARGUMENTS(){
        if(hayErrores)
            return;
        
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            EXPRESSION();
            ARGUMENTS();
        }
    }


    /************************************************************************
     *                                  Match
    ************************************************************************/
    private void match(TipoToken tt){
            if(preanalisis.tipo == tt){
                i++;
                preanalisis = tokens.get(i);
            }
            else{
                hayErrores = true;
            }
        }
}
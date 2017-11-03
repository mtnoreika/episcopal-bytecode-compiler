package Compiler;

import Compiler.Definitions.FunctionDef;
import Compiler.Expressions.Id;

import java.util.HashMap;

import static Compiler.Instruction.*;

public class Program {
    private Id id;
    private Expression expr;
    private FunctionDef[] queries;

    public Program(Id id, Expression expr) {
        this.id = id;
        this.expr = expr;
        this.queries = null;
    }

    public Program(Id id, Expression expr, FunctionDef[] queries) {
        this.id = id;
        this.expr = expr;
        this.queries = queries;
    }

    public void compile(Assembler assembler) {
        HashMap<String, Compilable> scope = new HashMap<>();

        assembler.addInstruction(".class public " + id.getName());
        assembler.addInstruction(".super java/lang/Object");
        assembler.addInstruction(INITMETHOD);
        assembler.addInstruction(MAINMETHOD);

        expr.compile(scope, assembler);

        if (queries != null) {
            for (FunctionDef query : queries) {
                query.compile(scope, assembler);
            }
        }

        assembler.addInstruction("Print:");
        assembler.addInstruction("getstatic java/lang/System/out Ljava/io/PrintStream;");
        assembler.addInstruction("swap");
        assembler.addInstruction("invokevirtual java/io/PrintStream/println(Ljava/lang/Object;)V");
        assembler.addInstruction(RETURN);
        assembler.addInstruction(ENDMETHOD);
    }

    public String getName() {
        return id.getName();
    }
}

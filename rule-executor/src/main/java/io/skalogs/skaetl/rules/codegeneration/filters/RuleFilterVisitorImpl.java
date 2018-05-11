package io.skalogs.skaetl.rules.codegeneration.filters;

import io.skalogs.skaetl.rules.RuleFilterBaseVisitor;
import io.skalogs.skaetl.rules.RuleFilterParser;
import io.skalogs.skaetl.rules.codegeneration.exceptions.RuleVisitorException;
import lombok.Getter;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

import static io.skalogs.skaetl.rules.codegeneration.RuleToJava.*;

@Getter
public class RuleFilterVisitorImpl extends RuleFilterBaseVisitor<String> {

    private String filter;

    @Override
    public String visitParse(RuleFilterParser.ParseContext ctx) {
        try {
            filter = visit(ctx.filter());
            return "filters";
        } catch (Exception e) {
            throw new RuleVisitorException(e);
        }
    }

    @Override
    public String visitTimeunit(RuleFilterParser.TimeunitContext ctx) {
        return timeunit(ctx.getText());
    }

    @Override
    public String visitTerminal(TerminalNode node) {
        return node.getText();
    }

    @Override
    public String visitFloatAtom(RuleFilterParser.FloatAtomContext ctx) {
        return ctx.getText() + "f";
    }

    @Override
    public String visitFieldname(RuleFilterParser.FieldnameContext ctx) {
        return "get(jsonValue,\"" + ctx.getText() + "\")";
    }

    protected String text(RuleNode node) {
        return node == null ? "" : node.getText();
    }

    @Override
    public String visitSubExpr(RuleFilterParser.SubExprContext ctx) {
        return "(" + visit(ctx.expr()) + ")";
    }

    @Override
    public String visitExponentExpr(RuleFilterParser.ExponentExprContext ctx) {
        return exp(visit(ctx.expr().get(0)), visit(ctx.expr().get(1)));
    }

    @Override
    public String visitHighPriorityOperationExpr(RuleFilterParser.HighPriorityOperationExprContext ctx) {
        String operation = ctx.HIGH_PRIORITY_OPERATION().getText();
        String expr1 = visit(ctx.expr(0));
        String expr2 = visit(ctx.expr(1));
        return highPriorityOperation(operation, expr1, expr2);
    }


    @Override
    public String visitLowPriorityOperationExpr(RuleFilterParser.LowPriorityOperationExprContext ctx) {
        String operation = ctx.LOW_PRIORITY_OPERATION().getText();
        String expr1 = visit(ctx.expr(0));
        String expr2 = visit(ctx.expr(1));
        return lowPriorityOperation(operation, expr1, expr2);
    }

    @Override
    public String visitComparisonExpr(RuleFilterParser.ComparisonExprContext ctx) {
        return comparisonMethod(ctx.COMPARISON_OPERATION().getText(), visit(ctx.expr(0)), visit(ctx.expr(1)));
    }


    @Override
    public String visitTimeCondition(RuleFilterParser.TimeConditionContext ctx) {
        return timeComparisonMethod(ctx.COMPARISON_OPERATION().getText(), visit(ctx.fieldname()), visit(ctx.INT()), visit(ctx.timeunit()));
    }

    @Override
    public String visitAndCondition(RuleFilterParser.AndConditionContext ctx) {
        return and(visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public String visitOrCondition(RuleFilterParser.OrConditionContext ctx) {
        return or(visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public String visitNotCondition(RuleFilterParser.NotConditionContext ctx) {
        return not(visit(ctx.expr()));
    }

    @Override
    public String visitIfCondition(RuleFilterParser.IfConditionContext ctx) {
        return ifCondition(visit(ctx.expr(0)), visit(ctx.expr(1)), visit(ctx.expr(2)));
    }

    @Override
    public String visitOneArgCondition(RuleFilterParser.OneArgConditionContext ctx) {
        String functionName = visit(ctx.functionname());
        return oneArgCondition(functionName, visit(ctx.fieldname()));
    }

    @Override
    public String visitVarArgCondition(RuleFilterParser.VarArgConditionContext ctx) {
        String functionName = visit(ctx.functionname());
        String notOperation = ctx.NOT_OPERATION() != null ? "!" : "";

        String fieldValue = visit(ctx.fieldname());
        String args = visit(ctx.expr(), ",", "", "");
        return notOperation + varArgCondition(functionName, fieldValue, args);
    }

    protected String visit(List<RuleFilterParser.ExprContext> exprs, String visitSeparators, String appendToVisitResultBegin, String appendToVisitResultEnd) {
        String args = "";
        for (RuleFilterParser.ExprContext expr : exprs) {
            if (!args.isEmpty()) {
                args += visitSeparators;
            }
            args += appendToVisitResultBegin + visit(expr) + appendToVisitResultEnd;
        }
        return args;
    }
}

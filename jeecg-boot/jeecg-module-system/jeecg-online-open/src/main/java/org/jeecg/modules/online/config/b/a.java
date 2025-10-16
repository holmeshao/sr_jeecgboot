/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.sf.jsqlparser.JSQLParserException
 *  net.sf.jsqlparser.expression.BinaryExpression
 *  net.sf.jsqlparser.expression.CaseExpression
 *  net.sf.jsqlparser.expression.CastExpression
 *  net.sf.jsqlparser.expression.DoubleValue
 *  net.sf.jsqlparser.expression.Expression
 *  net.sf.jsqlparser.expression.Function
 *  net.sf.jsqlparser.expression.LongValue
 *  net.sf.jsqlparser.expression.StringValue
 *  net.sf.jsqlparser.parser.CCJSqlParserManager
 *  net.sf.jsqlparser.schema.Column
 *  net.sf.jsqlparser.schema.Table
 *  net.sf.jsqlparser.statement.Statement
 *  net.sf.jsqlparser.statement.select.AllColumns
 *  net.sf.jsqlparser.statement.select.AllTableColumns
 *  net.sf.jsqlparser.statement.select.Join
 *  net.sf.jsqlparser.statement.select.PlainSelect
 *  net.sf.jsqlparser.statement.select.Select
 *  net.sf.jsqlparser.statement.select.SelectBody
 *  net.sf.jsqlparser.statement.select.SelectExpressionItem
 *  net.sf.jsqlparser.statement.select.SelectItem
 *  net.sf.jsqlparser.statement.select.SelectItemVisitor
 *  net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter
 *  net.sf.jsqlparser.statement.select.SetOperationList
 *  org.jeecg.common.util.security.AbstractQueryBlackListHandler
 *  org.jeecg.common.util.security.AbstractQueryBlackListHandler$QueryTable
 *  org.springframework.stereotype.Component
 */
package org.jeecg.modules.online.config.b;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.jeecg.common.util.security.AbstractQueryBlackListHandler;
import org.springframework.stereotype.Component;

@Component(value="onlReportQueryBlackListHandler")
public class a
extends AbstractQueryBlackListHandler {
    private static ThreadLocal<Map<String, AbstractQueryBlackListHandler.QueryTable>> a = new ThreadLocal();
    private static ThreadLocal<String> b = new ThreadLocal();

    private void b() {
        a.set(new HashMap(5));
        b.set(new String());
    }

    private void c() {
        a.remove();
        b.remove();
    }

    private void a(String string, AbstractQueryBlackListHandler.QueryTable queryTable) {
        a.get().put(string, queryTable);
    }

    private AbstractQueryBlackListHandler.QueryTable a(String string) {
        return a.get().get(string);
    }

    private void a(String string, String string2) {
        AbstractQueryBlackListHandler.QueryTable queryTable = a.get().get(string);
        queryTable.addField(string2);
    }

    private List<AbstractQueryBlackListHandler.QueryTable> getResult() {
        Map<String, AbstractQueryBlackListHandler.QueryTable> map = a.get();
        ArrayList<AbstractQueryBlackListHandler.QueryTable> arrayList = new ArrayList<AbstractQueryBlackListHandler.QueryTable>(map.values());
        this.c();
        return arrayList;
    }

    protected List<AbstractQueryBlackListHandler.QueryTable> getQueryTableInfo(String sql) {
        this.b();
        CCJSqlParserManager cCJSqlParserManager = new CCJSqlParserManager();
        try {
            Statement statement = cCJSqlParserManager.parse((Reader)new StringReader(sql));
            if (statement instanceof Select) {
                PlainSelect plainSelect;
                Select select = (Select)statement;
                SelectBody selectBody = select.getSelectBody();
                if (selectBody instanceof PlainSelect) {
                    plainSelect = (PlainSelect)selectBody;
                    this.a(plainSelect);
                    this.b(plainSelect);
                }
                if (selectBody instanceof SetOperationList) {
                    plainSelect = (SetOperationList)selectBody;
                    List list = plainSelect.getSelects();
                    for (int i2 = 0; i2 < list.size(); ++i2) {
                        SelectBody selectBody2 = (SelectBody)list.get(i2);
                        if (!(selectBody2 instanceof PlainSelect)) continue;
                        PlainSelect plainSelect2 = (PlainSelect)selectBody2;
                        this.a(plainSelect2);
                        this.b(plainSelect2);
                    }
                }
            }
            return this.getResult();
        }
        catch (JSQLParserException jSQLParserException) {
            jSQLParserException.printStackTrace();
            return null;
        }
    }

    private void a(PlainSelect plainSelect) {
        Table table = (Table)plainSelect.getFromItem();
        this.a(table);
        List list = plainSelect.getJoins();
        if (list != null) {
            for (Join join : list) {
                Table table2 = (Table)join.getRightItem();
                this.a(table2);
            }
        }
    }

    private void a(Table table) {
        String string = "";
        string = table.getAlias() != null ? table.getAlias().getName() : table.getName();
        if (b.get().length() == 0) {
            b.set(string);
        }
        this.a(string, new AbstractQueryBlackListHandler.QueryTable((AbstractQueryBlackListHandler)this, table.getName(), string));
    }

    private void b(PlainSelect plainSelect) {
        List list = plainSelect.getSelectItems();
        final String string = b.get();
        for (SelectItem selectItem : list) {
            selectItem.accept((SelectItemVisitor)new SelectItemVisitorAdapter(){

                public void visit(SelectExpressionItem item) {
                    Expression expression = item.getExpression();
                    if (expression instanceof Column) {
                        Column column = (Column)expression;
                        if (column.getTable() == null) {
                            String string4 = column.getColumnName();
                            a.this.a(string, string4);
                        } else {
                            String string5 = column.getTable().getName();
                            AbstractQueryBlackListHandler.QueryTable queryTable = null;
                            queryTable = string5 == null || "".equals(string5) ? a.this.a(string) : a.this.a(string5);
                            if (queryTable != null) {
                                queryTable.addField(column.getColumnName());
                            }
                        }
                    } else if (!a.this.b(expression)) {
                        String string6 = expression.toString();
                        boolean bl = false;
                        Set set = ((Map)a.get()).keySet();
                        for (String string2 : set) {
                            String string3 = string2 + ".";
                            if (string6.indexOf(string3) < 0) continue;
                            bl = true;
                            a.this.a(string2, string6);
                        }
                        if (!bl) {
                            a.this.a(string, string6);
                        }
                    }
                }

                public void visit(AllTableColumns columns) {
                    String string2 = null;
                    try {
                        string2 = columns.getTable().getName();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (string2 == null) {
                        string2 = string;
                    }
                    a.this.a(string2).setAll(true);
                }

                public void visit(AllColumns columns) {
                    if ("*".equals(columns.toString())) {
                        a.this.a(string).setAll(true);
                    }
                }
            });
        }
    }

    private boolean a(Expression expression) {
        if (expression != null) {
            return expression instanceof Function || expression instanceof BinaryExpression || expression instanceof CastExpression || expression instanceof CaseExpression;
        }
        return false;
    }

    private boolean b(Expression expression) {
        return expression instanceof StringValue || expression instanceof DoubleValue || expression instanceof LongValue;
    }
}


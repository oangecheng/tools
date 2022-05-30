package com.ustc.orange.annotations.compiler;

import javax.annotation.processing.Messager;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * Time: 2022/4/28
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
public class ZaxLogTranslator extends TreeTranslator {

  private TreeMaker mTreeMaker;
  private Name.Table mNames;
  private Messager mMessager;
  private String mTag;
  private JCTree.JCExpression mJCExpression;

  public ZaxLogTranslator(
      TreeMaker treeMaker,
      Name.Table names,
      Messager messager) {
    mTreeMaker = treeMaker;
    mNames = names;
    mMessager = messager;
  }

  void setTag(String tag) {
    mTag = tag;
    mJCExpression = mTreeMaker.Literal(tag);
  }


  @Override
  public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
    super.visitMethodDef(jcMethodDecl);
    JCTree.JCStatement statement = createJCStatement(jcMethodDecl);
    addLogToMethod(jcMethodDecl, statement);
  }


  private void addLogToMethod(JCTree.JCMethodDecl jcMethodDecl, JCTree.JCStatement logStatement) {
    ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
    List<JCTree.JCStatement> methodStatements = jcMethodDecl.getBody().getStatements();
    if (methodStatements.size() > 0) {
      JCTree.JCStatement firstStatement = methodStatements.get(0);
      // super(...) statement must before log statement in constructor
      if (firstStatement.toString().startsWith("super(")) {
        statements.append(firstStatement);
        statements.append(logStatement);
      } else {
        statements.append(logStatement);
        statements.append(firstStatement);
      }
    } else {
      statements.add(logStatement);
    }
    for (int i = 1; i < methodStatements.size(); i++) {
      statements.append(jcMethodDecl.getBody().getStatements().get(i));
    }

    JCTree.JCBlock body = mTreeMaker.Block(0, statements.toList());

    result = mTreeMaker.MethodDef(
        jcMethodDecl.getModifiers(),
        mNames.fromString(jcMethodDecl.getName().toString()),
        jcMethodDecl.restype,
        jcMethodDecl.getTypeParameters(),
        jcMethodDecl.getParameters(),
        jcMethodDecl.getThrows(),
        body,
        jcMethodDecl.defaultValue
    );
  }

  private JCTree.JCStatement createJCStatement(JCTree.JCMethodDecl methodDecl) {
    JCTree.JCFieldAccess log = mTreeMaker.Select(mTreeMaker.Select(
        mTreeMaker.Select(
            mTreeMaker.Ident(mNames.fromString("android")),
            mNames.fromString("util")
        ),
        mNames.fromString("Log")
    ), mNames.fromString("d"));

    JCTree.JCExpression expression = createJCExpression(methodDecl);
    JCTree.JCMethodInvocation invocation = mTreeMaker.Apply(List.nil(), log, List.of(mJCExpression, expression));
    return mTreeMaker.Exec(invocation);
  }

  private JCTree.JCExpression createJCExpression(JCTree.JCMethodDecl methodDecl) {
    JCTree.JCExpression expression = mTreeMaker.Literal(methodDecl.getName().toString() + "{");
    if (methodDecl.params.size() > 0) {
      boolean first = true;
      for (JCTree.JCVariableDecl decl : methodDecl.params) {
        if (!first) {
          expression = mTreeMaker.Binary(JCTree.Tag.PLUS, expression, mTreeMaker.Literal(","));
        }
        if (decl.sym == null) {
          continue;
        }
        first = false;
        JCTree.JCExpression paramName = mTreeMaker.Literal(decl.getName().toString() + "=");
        JCTree.JCExpression paramValue = mTreeMaker.Ident(decl);
        expression = mTreeMaker.Binary(
            JCTree.Tag.PLUS,
            expression,
            mTreeMaker.Binary(JCTree.Tag.PLUS, paramName, paramValue));
      }
    }
    return mTreeMaker.Binary(JCTree.Tag.PLUS, expression, mTreeMaker.Literal("}"));
  }
}

package com.ustc.orange.annotations.compiler;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import com.ustc.orange.annotation.ZaxLog;


/**
 * Time: 2022/4/28
 * Author: chengzhi@kuaishou.com
 *
 * Desc:
 */
@AutoService(Processor.class)
public class ZaxLogProcessor extends AbstractProcessor {

  private Trees mTrees;
  private ZaxLogTranslator mTranslator;
  private Messager mMessager;

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
    mTrees = Trees.instance(env);
    mMessager = env.getMessager();

    final Context context = ((JavacProcessingEnvironment) env).getContext();
    mTranslator = new ZaxLogTranslator(
        TreeMaker.instance(context), Names.instance(context).table, mMessager);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> set = new HashSet<>();
    set.add(ZaxLog.class.getCanonicalName());
    return set;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.RELEASE_8;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (!roundEnv.processingOver()) {
      Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ZaxLog.class);
      for (Element element : elements) {
        if (element.getKind() == ElementKind.CLASS) {
          JCTree jcTree = (JCTree) mTrees.getTree(element);
          if (jcTree != null) {
            mTranslator.setTag(element.getSimpleName().toString());
            mMessager
                .printMessage(Diagnostic.Kind.NOTE, "handlerElement:" + element.getSimpleName());
            jcTree.accept(mTranslator);
          }
        }
      }
    }

    return false;
  }
}

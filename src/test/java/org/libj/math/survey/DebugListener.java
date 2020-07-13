package org.libj.math.survey;

import java.util.HashSet;
import java.util.Set;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

public class DebugListener implements AgentBuilder.Listener {
  public static Set<String> ignored = new HashSet<>();
  public static Set<String> completed = new HashSet<>();

  @Override
  public void onDiscovery(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {
//    System.err.println("onDiscovery: " + typeName);
  }

  @Override
  public void onTransformation(final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module, final boolean loaded, final DynamicType dynamicType) {
//    System.err.println("onTransformation: " + typeDescription);
  }

  @Override
  public void onIgnored(final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {
//    System.err.println("onIgnored: " + typeDescription);
    ignored.add(typeDescription.getName());
  }

  @Override
  public void onError(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded, final Throwable throwable) {
//    System.err.println("onError: " + typeName);
    throwable.printStackTrace();
  }

  @Override
  public void onComplete(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {
//    System.err.println("onComplete: " + typeName);
    completed.add(typeName);
  }
}
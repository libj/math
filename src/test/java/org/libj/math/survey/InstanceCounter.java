package org.libj.math.survey;

import static net.bytebuddy.matcher.ElementMatchers.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Collections;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import net.bytebuddy.utility.JavaModule;

public class InstanceCounter {
  private static final int DEFAULT_SOCKET_BUFFER_SIZE = 65536;

  public static byte[] readBytes(final URL url) {
    try {
      try (final InputStream in = url.openStream()) {
        return readBytes(in);
      }
    }
    catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static byte[] readBytes(final InputStream in) throws IOException {
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream(DEFAULT_SOCKET_BUFFER_SIZE);
    final byte[] bytes = new byte[DEFAULT_SOCKET_BUFFER_SIZE];
    for (int len; (len = in.read(bytes)) != -1;)
      if (len != 0)
        buffer.write(bytes, 0, len);

    return buffer.toByteArray();
  }

  static void load() {
    ClassInjector.UsingUnsafe.ofBootLoader().injectRaw(Collections.singletonMap("org.libj.math.survey.BootstrapState", readBytes(InstanceCounter.class.getClassLoader().getResource("org/libj/math/survey/BootstrapState.class"))));
//    System.out.println(BootstrapState.class.getClassLoader());
    new AgentBuilder.Default()
      .ignore(none())
//      .disableClassFormatChanges()
//      .with(TypeProxy.)
      .with(RedefinitionStrategy.RETRANSFORMATION)
      .with(InitializationStrategy.NoOp.INSTANCE)
      .with(TypeStrategy.Default.REDEFINE)
//      .with(new DebugListener())
    .type(any())
      .transform(new Transformer() {
        @Override
        public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module) {
          return builder.visit(Advice.to(InstanceCounter.class).on(any()));
        }})
    .installOn(ByteBuddyAgent.install());
  }

  @Advice.OnMethodExit
  public static void exit() {
//    if (!BootstrapState.trace || BootstrapState.lock)
      return;

//    BootstrapState.lock = true;
//    System.out.println(">>> ");
//    BootstrapState.lock = false;
  }
}
/*
 * Copyright (C) 2014-2025 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.jetty;

import com.github.tomakehurst.wiremock.common.JettySettings;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.http.*;
import com.github.tomakehurst.wiremock.jetty12.Jetty12HttpServer;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyHttpServerFactory implements HttpServerFactory, DefaultFactory {

  private final JettySettings settings;

  public JettyHttpServerFactory() {
    this(JettySettings.Builder.aJettySettings().build());
  }

  public JettyHttpServerFactory(JettySettings settings) {
    this.settings = settings;
  }

  public JettySettings getSettings() {
    return settings;
  }

  @Override
  public HttpServer buildHttpServer(
      Options options,
      AdminRequestHandler adminRequestHandler,
      StubRequestHandler stubRequestHandler) {
    return new Jetty12HttpServer(
        options,
        adminRequestHandler,
        stubRequestHandler,
        settings,
        new QueuedThreadPool(options.containerThreads()));
  }
}

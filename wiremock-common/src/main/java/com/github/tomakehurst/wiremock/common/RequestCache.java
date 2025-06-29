/*
 * Copyright (C) 2020-2025 Thomas Akehurst
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
package com.github.tomakehurst.wiremock.common;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class RequestCache {

  private static RequestCache OFF =
      new RequestCache() {
        @Override
        public void put(Key key, Object value) {}

        @Override
        public <T> T get(Key key) {
          return null;
        }

        @Override
        public <T> T get(Key key, Supplier<T> supplier) {
          return supplier.get();
        }
      };

  private static final ThreadLocal<RequestCache> current = new ThreadLocal<>();

  public static RequestCache getCurrent() {
    RequestCache requestCache = current.get();
    if (requestCache == null) {
      requestCache = new RequestCache();
      current.set(requestCache);
    }

    return requestCache;
  }

  public static void onRequestEnd() {
    current.remove();
  }

  public static void disable() {
    current.set(OFF);
  }

  private final Map<Key, Object> cache = new HashMap<>();

  public void put(Key key, Object value) {
    cache.put(key, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(Key key) {
    return (T) cache.get(key);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(Key key, Supplier<T> supplier) {
    return (T) cache.computeIfAbsent(key, k -> supplier.get());
  }

  public static class Key {
    private final Class<?> forClass;
    private final List<?> elements;

    public static Key keyFor(Class<?> forClass, Object... elements) {
      return new Key(forClass, asList(elements));
    }

    private Key(Class<?> forClass, List<?> elements) {
      this.forClass = forClass;
      this.elements = elements;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("Key{");
      sb.append("forClass=").append(forClass);
      sb.append(", elements=").append(elements);
      sb.append('}');
      return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Key key = (Key) o;
      return forClass.equals(key.forClass) && elements.equals(key.elements);
    }

    @Override
    public int hashCode() {
      return Objects.hash(forClass, elements);
    }
  }
}

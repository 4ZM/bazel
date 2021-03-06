// Copyright 2017 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.rules.java;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Streams;
import com.google.devtools.build.lib.analysis.PackageSpecificationProvider;
import com.google.devtools.build.lib.analysis.TransitiveInfoProvider;
import com.google.devtools.build.lib.cmdline.Label;
import com.google.devtools.build.lib.concurrent.ThreadSafety.Immutable;
import java.util.List;

/** A provider for Java plugin configuration. */
@AutoValue
@Immutable
public abstract class JavaPluginConfigurationProvider implements TransitiveInfoProvider {

  /** Creates a {@link JavaPluginConfigurationProvider}. */
  public static JavaPluginConfigurationProvider create(
      List<PackageSpecificationProvider> packageSpecifications, JavaPluginInfoProvider plugin) {
    return new AutoValue_JavaPluginConfigurationProvider(packageSpecifications, plugin);
  }

  /** Package specifications for which the plugins should be enabled. */
  abstract List<PackageSpecificationProvider> packageSpecifications();

  /** The Java plugins described by this configuration. */
  abstract JavaPluginInfoProvider plugin();

  /**
   * Returns true if this plugin configuration matches the current label: that is, if the label's
   * package is contained by any of the {@link #packageSpecifications}.
   */
  public boolean matches(Label label) {
    return packageSpecifications()
        .stream()
        .flatMap(p -> Streams.stream(p.getPackageSpecifications()))
        .anyMatch(p -> p.containsPackage(label.getPackageIdentifier()));
  }
}

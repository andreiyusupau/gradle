/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.jvm.toolchain

class JavaToolchainDownloadUtil {

    public static final String NO_RESOLVER = null

    public static final String NO_TOOLCHAIN_MANAGEMENT = ""

    public static final String DEFAULT_TOOLCHAIN_MANAGEMENT = null

    public static final String DEFAULT_PLUGIN = null

    static String applyToolchainResolverPlugin(String resolverClass, String resolverCode) {
        applyToolchainResolverPlugin(resolverClass, resolverCode, DEFAULT_PLUGIN, DEFAULT_TOOLCHAIN_MANAGEMENT)
    }

    static String applyToolchainResolverPlugin(String resolverClass, String resolverCode, String pluginName) {
        applyToolchainResolverPlugin(resolverClass, resolverCode, pluginName, DEFAULT_TOOLCHAIN_MANAGEMENT)
    }

    static String applyToolchainResolverPlugin(String resolverClass, String resolverCode, String pluginName, String toolchainManagementCode) {
        if (pluginName == null) {
            pluginName = resolverClass + "Plugin"
        }
        """
            public abstract class ${pluginName} implements Plugin<Settings> {
                @Inject
                protected abstract JavaToolchainResolverRegistry getToolchainResolverRegistry();

                void apply(Settings settings) {
                    settings.getPlugins().apply("jvm-toolchain-management");

                    JavaToolchainResolverRegistry registry = getToolchainResolverRegistry();
                    registry.register(${resolverClass}.class);
                }
            }

            import java.util.Optional;
            import org.gradle.platform.BuildPlatform;

            ${(resolverCode == null) ? "" : """
                public abstract class ${resolverClass} implements JavaToolchainResolver {
                    @Override
                    public Optional<JavaToolchainDownload> resolve(JavaToolchainRequest request) {
                        ${resolverCode}
                    }
                }
            """}

            apply plugin: ${pluginName}

            ${(toolchainManagementCode != null) ? toolchainManagementCode : """
                toolchainManagement {
                    jvm {
                        javaRepositories {
                            repository('custom') {
                                resolverClass = ${resolverClass}
                            }
                        }
                    }
                }
            """}
        """
    }

    static String singleUrlResolverCode(String uri) {
        """
            URI uri = URI.create("$uri");
            return Optional.of(JavaToolchainDownload.fromUri(uri));
        """
    }

    static String noUrlResolverCode() {
        """return Optional.empty();"""
    }

}

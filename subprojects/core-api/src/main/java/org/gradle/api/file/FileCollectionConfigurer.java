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

package org.gradle.api.file;

import org.gradle.api.Incubating;
import org.gradle.api.provider.ConfigurableValue;

/**
 * The configuration protocol for incremental updates to ConfigurableFileCollections.
 *
 * @since 8.7
 */
@Incubating
public interface FileCollectionConfigurer extends ConfigurableValue.Configurer {
    /**
     * Adds a set of source paths to this collection. The given paths are evaluated as per {@link org.gradle.api.Project#files(Object...)}.
     *
     * @param paths The files to add.
     * @return this
     *
     * @since 8.7
     */
    FileCollectionConfigurer from(Object... paths);
}

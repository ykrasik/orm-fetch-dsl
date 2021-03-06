/******************************************************************************
 * Copyright (C) 2015 Yevgeny Krasik                                          *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 * http://www.apache.org/licenses/LICENSE-2.0                                 *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/

package com.github.ykrasik.fetch.node;

import com.github.ykrasik.fetch.DescriptorRepository;

import java.util.List;

/**
 * A {@link FetchNode} that is a reference to another {@link FetchDescriptor}.
 * The reference will be resolved lazily, on first access.
 * Must keep a reference to the {@link DescriptorRepository} until the reference is resolved. After resolving,
 * the reference to the {@link DescriptorRepository} will be discarded, to allow garbage collection if necessary.
 *
 * @author Yevgeny Krasik
 */
public class FetchDescriptorRef implements FetchNode {
    private final String column;
    private final String refName;

    private DescriptorRepository repository;

    private volatile FetchDescriptor ref;

    public FetchDescriptorRef(String column, String refName, DescriptorRepository repository) {
        this.column = column;
        this.refName = refName;
        this.repository = repository;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public List<FetchNode> getChildren() {
        return getRef().getChildren();
    }

    private FetchDescriptor getRef() {
        // Double checked locking.
        if (ref == null) {
            synchronized (this) {
                if (ref == null) {
                    ref = repository.getDescriptor(refName);
                    repository = null;      // Release the reference.
                }
            }
        }
        return ref;
    }

    @Override
    public String toString() {
        return "FetchDescriptorRef{column = '" + column + "', refName = '" + refName + "'}";
    }
}

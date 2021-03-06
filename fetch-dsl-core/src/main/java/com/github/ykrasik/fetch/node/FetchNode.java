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

import java.util.List;

/**
 * A node in a fetch tree.
 * Contains the name of the column to be fetched, and any child nodes to be fetched.
 *
 * @author Yevgeny Krasik
 */
public interface FetchNode {
    /**
     * @return Column name.
     */
    String getColumn();

    /**
     * @return Child nodes.
     */
    List<FetchNode> getChildren();
}

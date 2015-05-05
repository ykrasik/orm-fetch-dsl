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

package com.github.ykrasik.fetch

/**
 * The voodoo that makes a call to an unknown method 'descriptor' in a groovy script make a FetchDescriptor out of it.
 *
 * @author Yevgeny Krasik
 */
class FetchDslBinding extends Binding {
    FetchDslBinding(FetchDslBuilder builder) {
        this.setProperty(FetchDsl.DESCRIPTOR, { name, closure ->
            closure.delegate = delegate
            builder."${FetchDsl.DESCRIPTOR}"(name, closure)
        })
    }
}
package com.cedarsoftware.util.convert;

import java.time.Duration;
import java.util.Map;

import com.cedarsoftware.util.CompactLinkedMap;

/**
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         <a href="http://www.apache.org/licenses/LICENSE-2.0">License</a>
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public final class DurationConversions {

    private DurationConversions() {}

    static Map toMap(Object from, Converter converter, ConverterOptions options) {
        long sec = ((Duration) from).getSeconds();
        long nanos = ((Duration) from).getNano();
        Map<String, Object> target = new CompactLinkedMap<>();
        target.put("seconds", sec);
        target.put("nanos", nanos);
        return target;
    }
}
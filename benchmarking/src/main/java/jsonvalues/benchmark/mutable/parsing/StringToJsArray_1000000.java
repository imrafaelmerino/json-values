/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jsonvalues.benchmark.mutable.parsing;

import com.fasterxml.jackson.databind.JsonNode;
import jsonvalues.*;
import jsonvalues.benchmark.JacksonObjectMapper;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;

import static jsonvalues.benchmark.Data.ARR_1000000;

public class StringToJsArray_1000000
{
    private static final String array = ARR_1000000.get();

    @Benchmark
    public JsonNode jackson() throws IOException
    {
        return JacksonObjectMapper.get().readTree(array
                                                 );
    }


    @Benchmark
    public JsArray java_array_list() throws MalformedJson
    {

        return JsArray.parse(array
                                        )
               ;
    }




}

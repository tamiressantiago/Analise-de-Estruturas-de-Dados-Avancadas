/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sample;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import reader.GeneralFileReader;

@State(Scope.Thread)
@Fork(value = 5)
@Warmup(iterations = 5, time=5)
@Measurement(iterations = 10, time=5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)

public class TreeMapBenchmark{
    private List<Integer> values;
    private TreeMap<Integer,Integer> treeMap;
    private TreeMap<Integer,Integer> treeMapCheia;

    @Setup(Level.Iteration) //Feito a cada iteração
    public void setup() {
        values = GeneralFileReader.readValues("data/dados_com_repeticoes_ordenados.csv");
        treeMap = new TreeMap<>();
        treeMapCheia = new TreeMap<>();

	    for (int value : values) {
            treeMapCheia.put(value,value);
        }
        System.gc();
    }

    @Benchmark
    public void baselineAccess(Blackhole blackhole) {
        blackhole.consume(treeMap.isEmpty());
    }

    @Benchmark
    public void treeMapInsertAll(Blackhole blackhole) {
        for (int value : values) {
            blackhole.consume(treeMap.put(value, value));
        }
    }

    @Benchmark
    public void treeMapInsertOneElement(Blackhole blackhole){
        Random random = new Random();
        int random_index = random.nextInt(100000000);
        blackhole.consume(treeMapCheia.put(random_index, random_index));
    }

    //Procura os valores no treemap pela chave;
    @Benchmark
    public void treeMapSearchAllValue(Blackhole blackhole) {
    // valores antes da busca
        for (int value : values) {
            blackhole.consume(treeMapCheia.containsKey(value));
        }
    }

    @Benchmark
    public void treeMapSearchOneElement(Blackhole blackhole){
        Random random = new Random();
        int random_index = random.nextInt(100000000);
        blackhole.consume(treeMapCheia.containsKey(random_index));
    }

    //Removendo pela chave;
    @Benchmark
    public void treeMapRemoveAllByKey(Blackhole blackhole) {
        for (int value : values) {
            blackhole.consume(treeMapCheia.remove(value));
        }
    }

    @Benchmark
    public void treeMapRemoveOneElement(Blackhole blackhole){
        Random random = new Random();
        int random_index = random.nextInt(100000000);
        blackhole.consume(treeMapCheia.remove(random_index));
    }

}

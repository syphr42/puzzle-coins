/**
 * Copyright 2018 Gregory Moyer and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.syphr.puzzle.coins.strategy;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.reflections.Reflections;
import org.syphr.puzzle.coins.DataLoader;
import org.syphr.puzzle.coins.MonitoredScale;
import org.syphr.puzzle.coins.Scenario;
import org.syphr.puzzle.coins.Strategy;

import com.google.common.reflect.Reflection;

class StrategyTest
{
    @TestFactory
    public Stream<DynamicNode> testAllStrategies() throws IOException
    {
        List<Scenario> scenarios = DataLoader.defaultScenarios();

        Reflections r = new Reflections(Reflection.getPackageName(StrategyTest.class));
        return r.getSubTypesOf(Strategy.class)
                .stream()
                .map(sClass -> createStrategy(sClass))
                .map(strategy -> dynamicContainer(strategy.getClass().getSimpleName(),
                                                  scenarios.stream().map(scenario ->
                                                  {
                                                      return dynamicTest(scenario.getDescription(),
                                                                         () -> testScenario(strategy,
                                                                                            scenario));
                                                  })));
    }

    void testScenario(Strategy strategy, Scenario scenario)
    {
        MonitoredScale scale = new MonitoredScale();
        assertEquals(scenario.getUniqueCoinId(),
                     strategy.findUnique(scenario.getCoins(), scale).getId());
    }

    Strategy createStrategy(Class<? extends Strategy> sClass)
    {
        try
        {
            return sClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}

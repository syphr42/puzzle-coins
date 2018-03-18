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
package org.syphr.puzzle.coins;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Runner
{
    private final Strategy strategy;

    public Runner(Strategy strategy)
    {
        this.strategy = strategy;
    }

    public void run(MonitoredScale scale, List<Scenario> scenarios)
    {
        for (Scenario scenario : scenarios)
        {
            System.out.println("Testing scenario: " + scenario.getDescription());
            Coin uniqueCoin = strategy.findUnique(scenario.getCoins(), scale);

            System.out.println("Strategy made " + scale.getWeighings() + " weighings.");

            if (uniqueCoin == null)
            {
                System.out.println("Failed to find a unique coin.");
            }
            else if (uniqueCoin.getId() != scenario.getUniqueCoinId())
            {
                System.out.println("Identified the wrong coin as unique (strategy returned coin "
                                   + uniqueCoin.getId()
                                   + " while scenario expected coin "
                                   + scenario.getUniqueCoinId()
                                   + ").");
            }
            else
            {
                System.out.println("Correctly identified coin "
                                   + uniqueCoin.getId()
                                   + " as unique.");
            }

            scale.reset();
        }
    }

    public static void main(String[] args) throws InstantiationException,
                                           IllegalAccessException,
                                           ClassNotFoundException,
                                           IOException
    {
        if (args.length < 1)
        {
            System.out.println("Strategy class name is required.");
            System.exit(1);
        }

        Strategy strategy = (Strategy)Class.forName(args[0]).newInstance();
        new Runner(strategy).run(new MonitoredScale(), defaultScenarios());
    }

    private static List<Scenario> defaultScenarios() throws IOException
    {
        try (Reader r = new InputStreamReader(Runner.class.getResourceAsStream("/org/syphr/puzzle/coins/12coins.json")))
        {
            Type type = new TypeToken<List<Scenario>>()
            {}.getType();
            return new Gson().fromJson(r, type);
        }
    }
}
